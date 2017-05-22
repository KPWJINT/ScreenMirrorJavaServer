package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;


import java.awt.*;
import java.awt.image.BufferedImage;

public class Controller {

    private Server server;

    @FXML
    private Button buttonChangeServerState;
    @FXML
    private ImageView screenshotView;

    public void changeServerStateHandler(ActionEvent event)
    {
        if(server == null)
        {
            server = new Server();
            server.setDaemon(true);
            server.start();
            buttonChangeServerState.setText("Stop server");
        }else if(server.isActive())
        {
            server.stopServer();
            buttonChangeServerState.setText("Start server");
        }else
        {
            server.resumeServer();
            buttonChangeServerState.setText("Stop server");
        }
    }

    public void makeScreenshot(ActionEvent event)
    {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage captureImage = null;
        try {
            captureImage = new Robot().createScreenCapture(screenRect);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        WritableImage FXCaptureImage = null;

        FXCaptureImage = SwingFXUtils.toFXImage(captureImage, FXCaptureImage);

        screenshotView.setImage(FXCaptureImage);
    }
}
