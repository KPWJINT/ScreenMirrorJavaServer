package screen_mirror;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by anna on 2017-06-21.
 */
public class WindowStage extends Stage {
    private Pane pane;
    public WindowStage(String layoutFXML, String windowIcon){
        try {
            layoutFXML = FXMLLoader.load(getClass().getResource(layoutFXML));
            FXMLLoader loader=new FXMLLoader(getClass().getResource("layout_main.fxml"));
            Pane window = (Pane) loader.load();
            Controller controller=(Controller) loader.getController();
        //    layoutFXML.setPadding(new Insets(22,6,6,6));
            StackPane stack =  new StackPane();
          //  getIcons().add((new Image( DODAC IKONKE OKNA)))
            Scene scene=new Scene(stack);
            scene.setFill(null);
            setScene(scene);
            setTitle("Screen Mirror");
            giveControllerAccess(controller);
            initStyle(StageStyle.TRANSPARENT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void giveControllerAccess(Controller controller){
      //  controller.setStage(this);
    }
    public Pane getPane(){
        return pane;
    }


}
