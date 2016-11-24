package asteriods.asteriods;

import asteriods.elements.Asteriod;
import asteriods.rockengine.CollisionDetector;
import asteriods.rockengine.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

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

        root.getChildren().add(a);
        root.getChildren().add(b);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();

        a.setOrigin(new Point(0.0, 0.0));
        a.setEndPoint(new Point(300.0, 600.0));
        a.setCurrentPosition(new Point(0.0, 0.0));
        a.moveFromOrigin();
        a.setSpeed(32.0);
        a.calculateSpeedVector();

        b.setOrigin(new Point(100.0, 200.0));
        b.setEndPoint(new Point(600.0, 600.0));
        b.setCurrentPosition(new Point(100.0, 200.0));
        b.moveFromOrigin();
        b.setSpeed(32.0);
        b.calculateSpeedVector();

        Timeline timeline = new Timeline();
        Duration time = new Duration(100);

        KeyValue keyValuea = new KeyValue(a.translateXProperty(), 0.0);
        KeyValue keyValuea2 = new KeyValue(a.translateXProperty(), 0.0);
        KeyFrame keyFrame = new KeyFrame(time, keyValuea, keyValuea2);

        KeyValue keyValueb = new KeyValue(b.translateXProperty(), 100.0);
        KeyValue keyValueb2 = new KeyValue(b.translateYProperty(), 200.0);
        KeyFrame keyFrameb = new KeyFrame(time, keyValueb, keyValueb2);

        timeline.getKeyFrames().add(keyFrame);
        timeline.getKeyFrames().add(keyFrameb);
        timeline.setCycleCount(1);
        timeline.play();
        timeline.setOnFinished((ActionEvent event) -> {
            timeline.getKeyFrames().clear();
            Point pa = a.getNextPoint();
            a.updateAsteriodPosition(pa);

            Point pb = b.getNextPoint();
            b.updateAsteriodPosition(pb);

            CollisionDetector col = new CollisionDetector(600, 600);
            col.addElement(a);
            col.addElement(b);
//            Set<Integer> indexOfElementsCrashed = col.getCrashedElements();
//            List<Integer> indexes = new ArrayList<>(indexOfElementsCrashed);

            timeline.getKeyFrames().add(new KeyFrame(time, new KeyValue(a.translateXProperty(), pa.getX())));
            timeline.getKeyFrames().add(new KeyFrame(time, new KeyValue(a.translateYProperty(), pa.getY())));
            timeline.getKeyFrames().add(new KeyFrame(time, new KeyValue(b.translateXProperty(), pb.getX())));
            timeline.getKeyFrames().add(new KeyFrame(time, new KeyValue(b.translateYProperty(), pb.getY())));
            //if (indexes.isEmpty()) {
                timeline.play();
            //}
        });

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

}
