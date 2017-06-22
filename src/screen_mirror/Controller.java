package screen_mirror;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller {

    private Server server;

    @FXML
    private Button buttonChangeServerState;
    @FXML
    private Label labelMain;
    @FXML
    private Label labelButtonDescription;


    public void changeServerStateHandler(ActionEvent event)
    {
        if(server == null)
        {
            server = new Server();
            server.setDaemon(true);
            server.start();

            labelMain.setText("Server is running");
            labelButtonDescription.setText("Press button to stop the server");
            buttonChangeServerState.setText("Stop");
        }else if(server.isActive())
        {
            server.stopServer();

            labelMain.setText("Server paused");
            labelButtonDescription.setText("Press button to resume the server");
            buttonChangeServerState.setText("Start");
        }else
        {
            server.resumeServer();

            labelMain.setText("Server is running");
            labelButtonDescription.setText("Press button to stop the server");
            buttonChangeServerState.setText("Stop");
        }
    }


}
