package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {

    @FXML
    private Button buttonChangeServerState;

    public void changeServerStateHandler(ActionEvent event)
    {
        Thread thread = new Thread(new Server());
        thread.start();
    }
}
