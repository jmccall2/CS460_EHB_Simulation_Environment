package GUI.ButtonPanel;

import GUI.*;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ButtonPanel implements GUIPanel
{
    private BorderPane _buttonPanel;
    private EHBButton _ehbButton;
    public ButtonPanel()
    {
        _buttonPanel = new BorderPane();
        _ehbButton = new EHBButton();
        _buttonPanel.setCenter(_ehbButton.getEHBButton());
        Text t = new Text (10, 20, "EHB Button");
        t.setFont(Font.font ("Verdana", 35));
        t.setFill(Color.RED);
        _buttonPanel.setTop(t);
        _buttonPanel.setAlignment(t,Pos.BOTTOM_CENTER);
        Image tmp = new Image(getClass().getResource("/Resources/img/metal.jpg").toExternalForm(),250,250,true,true);
        BackgroundImage myBI= new BackgroundImage(tmp,BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        _buttonPanel.setBackground(new Background(myBI));
    }

    public BorderPane getPanel()
    {
        return _buttonPanel;
    }

    public EHBButton getEHBButton()
    {
        return _ehbButton;
    }




}
