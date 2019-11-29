package vad.testmav.src;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private String outputFileName;
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

                    TextField name = new TextField("NewExcelFile");
                    name.setLayoutX(135);
                    name.setLayoutY(60);
                    root.getChildren().add(name);

                    Label nameFile = new Label("Write the name of file:");
                    nameFile.setLayoutX(10);
                    nameFile.setLayoutY(63);
                    nameFile.setLabelFor(name);
                    root.getChildren().add(nameFile);

                    Button acceptName = new Button("Accept name");
                    acceptName.setLayoutX(300);
                    acceptName.setLayoutY(60);
                    root.getChildren().add(acceptName);
                    acceptName.setOnAction((event1) -> {
                        outputFileName = name.getText() + ".xlsx";
                        Button accept = new Button("Start");
                        Button refresh = new Button("Refresh");

                        accept.setLayoutX(10);
                        accept.setLayoutY(90);
                        accept.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                compare = new Compare(fileNames, outputFileName);
                                compare.process();
                                root.getChildren().add(new Text(10, 150, "Finish"));
                            }
                        });

                        refresh.setLayoutX(70);
                        refresh.setLayoutY(90);
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
                    });
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


