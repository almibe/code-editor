package org.almibe.codearea.demo;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.almibe.codearea.CodeMirrorArea;
import org.almibe.codearea.CodeArea;

public class Demo extends Application {
    private final CodeArea codeArea = new CodeMirrorArea();
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(codeArea.getWidget());
        VBox controls = new VBox();
        controls.getChildren().addAll(readOnlyControls(), modeControls(), mimeControls(), themeControls(), contentControls());
        borderPane.setBottom(controls);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        codeArea.init();
        primaryStage.show();
    }

    private Parent readOnlyControls() {
        HBox box = new HBox();
        Button setReadOnly = new Button("Set Read Only");
        Button unsetReadOnly = new Button("Set Editable");
        setReadOnly.setOnAction(event -> codeArea.setReadOnly(true));
        unsetReadOnly.setOnAction(event -> codeArea.setReadOnly(false));
        box.getChildren().addAll(setReadOnly, unsetReadOnly);
        return box;
    }

    private Parent mimeControls() {
        HBox box = new HBox();
        TextField modeInput = new TextField();
        Button button = new Button("Set MIME Type");
        box.getChildren().addAll(modeInput, button);
        button.setOnAction(event -> codeArea.setModeByMIMEType(modeInput.getText()));
        return box;
    }

    private Parent modeControls() {
        HBox box = new HBox();
        TextField modeInput = new TextField();
        Button button = new Button("Set Mode");
        box.getChildren().addAll(modeInput, button);
        button.setOnAction(event -> codeArea.setModeByName(modeInput.getText()));
        return box;
    }


    private Parent themeControls() {
        HBox box = new HBox();
        TextField themeInput = new TextField();
        Button button = new Button("Set Theme");
        button.setOnAction(event -> codeArea.setTheme(themeInput.getText()));
        box.getChildren().addAll(themeInput, button);
        return box;
    }

    private Parent contentControls() {
        HBox box = new HBox();
        TextArea content = new TextArea();
        content.editableProperty().setValue(false);
        //content.textProperty().bind(codeArea.contentProperty());
        box.getChildren().addAll(content);
        return box;
    }

    public static void main(String[] args) {
        Demo.launch();
    }
}
