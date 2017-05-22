package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {

    private Server server;

    @FXML
    private Button buttonChangeServerState;

    public void changeServerStateHandler(ActionEvent event)
    {
        if(server == null)
        {
            server = new Server();
            server.start();
        }
    }
}
