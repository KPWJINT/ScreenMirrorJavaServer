package screen_mirror;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {

    private Server server;

    @FXML
    private Button buttonChangeServerState;

    public void changeServerStateHandler(ActionEvent event)
    {
        if(server != null && server.isActive())
        {
            server.stopServer();
            buttonChangeServerState.setText("Start server");
        }else {
            server = new Server();
            server.setDaemon(true);
            server.start();
            buttonChangeServerState.setText("Stop server");
        }
    }


}
