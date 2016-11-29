package asteriods.asteriods;

import asteriods.elements.Asteriod;
import asteriods.rockengine.CollisionDetector;
import asteriods.rockengine.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        VBox root = new VBox();
        Scene scene = new Scene(root, 600, 600);

        Asteriod a = new Asteriod();
        a.setManaged(false);

        Asteriod b = new Asteriod();
        b.setManaged(false);
        
        Asteriod c = new Asteriod();
        c.setManaged(false);

        root.getChildren().add(a);
        root.getChildren().add(b);
        root.getChildren().add(c);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
        
        a.setRandomPath();
        b.setRandomPath();
        c.setRandomPath();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()-> {
                    a.updatePosition();
                    b.updatePosition();
                    c.updatePosition();
                    
                    CollisionDetector collisionDetector = new CollisionDetector(600, 600);
                    collisionDetector.addElement(a);
                    collisionDetector.addElement(b);
                    collisionDetector.addElement(c);
                    
                    Set<Integer> index = collisionDetector.getCrashedElements();
                    List<Integer> indexOfCrashedElements = new ArrayList<>(index);
                    if (!indexOfCrashedElements.isEmpty()){
                        timer.cancel();
                    }
                });
            }
        }, 50, 50);

    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void stop(){
        System.exit(0);
    }
}
