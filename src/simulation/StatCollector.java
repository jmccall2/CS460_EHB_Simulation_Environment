package simulation;

import interfaces.SpeedInterface;
import javafx.scene.chart.XYChart;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.Pulsar;

import java.util.*;

public class StatCollector {

    private List<Double> _deltaXValues = Arrays.asList(1.0,10.0,30.0,60.0);
    private Map<Double, LinkedList<Double>> _deltaXSpeedData = new HashMap<>();
    private Map<Double, LinkedList<Double>> _deltaXPressureData = new HashMap<>();
    private double _pressure = 0.0;
    private Helper _helper = new Helper();
    private double MS_TO_MPH = 2.23694;

     StatCollector() {
        Engine.getMessagePump().signalInterest(SimGlobals.SET_PRESSURE, _helper);
        // Initialize data containers.
        for (double deltaX : _deltaXValues) {
            _deltaXPressureData.put(deltaX, new LinkedList<>());
            _deltaXSpeedData.put(deltaX, new LinkedList<>());
        }

        System.out.print(_deltaXSpeedData.get(1.0).size());

        // Generate Pulsars to get data from the engine.
        new Pulsar(1.0, () -> _updateData(1.0)).start();
        new Pulsar(10.0, () -> _updateData(10.0)).start();
        new Pulsar(30.0, () -> _updateData(30.0)).start();
        new Pulsar(60.0, () -> _updateData(60.0)).start();

    }

    private void _updateData(double deltaX)
    {
        System.out.println("Here: " + deltaX);
        if(_deltaXSpeedData.get(deltaX).size() >= 20) _deltaXSpeedData.get(deltaX).pop();
        if(_deltaXPressureData.get(deltaX).size() >= 20) _deltaXPressureData.get(deltaX).pop();
        _deltaXSpeedData.get(deltaX).add(SpeedInterface.getSpeed());
        _deltaXPressureData.get(deltaX).add(_pressure); // Not available through the interfaces.
    }

    public XYChart.Series mapToSeries(double deltaX, GraphTypes gt)
    {
        LinkedList<Double> rawData = gt == GraphTypes.SPEED_VS_TIME ? _deltaXSpeedData.get(deltaX) : _deltaXPressureData.get(deltaX);
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i < rawData.size(); i++) series.getData().add(new XYChart.Data((i+1)*deltaX+"", rawData.get(i)*MS_TO_MPH));
        return series;
    }

    public List<Double> getDeltaXValues() { return _deltaXValues; }

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

            }
        }
    }

}
