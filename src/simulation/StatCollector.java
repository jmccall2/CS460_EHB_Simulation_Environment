package simulation;

import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.chart.XYChart;

import interfaces.SpeedInterface;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.Pulsar;


/**
 * Class that collects statistics when the simulation is running to be
 * displayed on the stats panel.
 */
class StatCollector {


    private List<Double> _deltaXValues = Arrays.asList(1.0,10.0,30.0,60.0);
    // Data is gathered on speed, pressure and jerk.
    private Map<Double, LinkedList<Double>> _deltaXSpeedData = new HashMap<>();
    private Map<Double, LinkedList<Double>> _deltaXPressureData = new HashMap<>();
    private Map<Double, LinkedList<Double>> _deltaXJerkAverages = new HashMap<>();
    // Jerk changes happen quickly so data points are averaged meaning
    // we must buffer data between time intervals.
    private LinkedList<Double> _jerkBuffer = new LinkedList<>();
    private double _pressure = 0.0;
    private Helper _helper = new Helper();
    private double MS_TO_MPH = 2.23694;
    private double _jerkInterval = 1.0;

    /**
     * Initialize data structures used for storing gathered stats and spawn
     * pulsars to collect the stats.
     */
     StatCollector() {
        Engine.getMessagePump().signalInterest(SimGlobals.SET_PRESSURE, _helper);
        Engine.getMessagePump().signalInterest(SimGlobals.JERK, _helper);
        // Initialize data containers.
        for (double deltaX : _deltaXValues) {
            _deltaXPressureData.put(deltaX, new LinkedList<>());
            _deltaXSpeedData.put(deltaX, new LinkedList<>());
        }
        _deltaXJerkAverages.put(_jerkInterval, new LinkedList<>());

        // Generate Pulsars to get data from the engine.
        new Pulsar(1.0, () -> _updateData(1.0)).start();
        new Pulsar(10.0, () -> _updateData(10.0)).start();

    }

    // Data update method called by Pulsar engine callbacks.
    private void _updateData(double deltaX)
    {
        // Only allow 20 data points max on a graph to reduce clutter.
        if(_deltaXSpeedData.get(deltaX).size() >= 20) _deltaXSpeedData.get(deltaX).pop();
        if(_deltaXPressureData.get(deltaX).size() >= 20) _deltaXPressureData.get(deltaX).pop();
        // Average all jerk data to form this pulses data point.
        if(deltaX == _jerkInterval) {
            if (_deltaXJerkAverages.get(deltaX).size() >= 20) _deltaXJerkAverages.get(deltaX).pop();
            int nJerkPoints = _jerkBuffer.size();
            if(nJerkPoints > 0) {
                double jerkAvg = _jerkBuffer.stream().mapToDouble(Double::doubleValue).sum() / nJerkPoints;
                _deltaXJerkAverages.get(deltaX).add(jerkAvg);
                _jerkBuffer.clear();
            }
        }
        _deltaXSpeedData.get(deltaX).add(SpeedInterface.getSpeed());
        _deltaXPressureData.get(deltaX).add(_pressure); // Not available through the interfaces.
    }

    /**
     * Build XYChart.Series with relevant data since a request
     * is being made.
     *
     * @param deltaX for grahp
     * @param gt GraphTypes
     * @return XYChart.Series data to be plotted on Line chart.
     */
    XYChart.Series mapToSeries(double deltaX, GraphTypes gt)
    {
        LinkedList<Double> rawData;
        switch (gt)
        {
            case SPEED_VS_TIME:
                rawData = _deltaXSpeedData.get(deltaX).stream().map(s -> s*MS_TO_MPH).collect(Collectors.toCollection(LinkedList::new));
                break;
            case PRESSURE_VS_TIME:
                rawData = _deltaXPressureData.get(deltaX);
                break;
            case JERK_AVG_VS_TIME:
                rawData = _deltaXJerkAverages.get(deltaX);
                break;
            default:
                    rawData = new LinkedList<>();
                    break;
        }
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i < rawData.size(); i++) series.getData().add(new XYChart.Data((i+1)*deltaX+"", rawData.get(i)));
        return series;
    }

    /**
     *
     * @return DeltaX values.
     */
    List<Double> getDeltaXValues() { return _deltaXValues; }

    /**
     * Inner class to handle messages about pressure and jerk relayed from the engine.
     */
    class Helper implements MessageHandler
    {
        @Override
        public void handleMessage(Message message)
        {
            switch (message.getMessageName())
            {
                case SimGlobals.SET_PRESSURE:
                    _pressure = (Double) message.getMessageData();
                    break;
                case SimGlobals.JERK:
                    _jerkBuffer.push((Double) message.getMessageData());
                    break;
            }
        }
    }

}
