package vad.testmav.src;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import vad.testmav.src.component.Compare;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App extends Application {

    private List<String> fileNames = new ArrayList<>();
    private Compare compare;

    @Override
    public void start(Stage primaryStage) {
        final Group root = new Group();
        Scene scene = new Scene(root, 551, 400);
        scene.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });

        // Dropping over surface
        scene.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    for (File file : db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        fileNames.add(filePath);
                    }
                }
                event.setDropCompleted(success);
                event.consume();

                if (fileNames.size() == 1)
                    root.getChildren().add(new Text(10, 30, "Your first file is: " + fileNames.get(0)));

                if (fileNames.size() == 2) {
                    root.getChildren().add(new Text(10, 50, "Your second file is: " + fileNames.get(1)));

                    Button accept = new Button("Accept");
                    Button refresh = new Button("Refresh");

                    accept.setLayoutX(10);
                    accept.setLayoutY(60);
                    accept.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            root.getChildren().add(new Text(10, 120, "Start process"));
                            compare = new Compare(fileNames);
                            root.getChildren().add(new Text(10, 150, "Finish"));
                        }
                    });

                    refresh.setLayoutX(100);
                    refresh.setLayoutY(60);
                    refresh.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            fileNames = new ArrayList<>();
                            root.getChildren().clear();
                            root.getChildren().add(new Text(10, 10, "Drop file into the area"));
                        }
                    });

                    root.getChildren().add(accept);
                    root.getChildren().add(refresh);
                }


            }
        });

        root.getChildren().add(new Text(10, 10, "Drop file into the area"));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}


