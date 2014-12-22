package org.almibe.codeeditor.demo;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.almibe.codeeditor.CodeMirrorEditor;
import org.almibe.codeeditor.CodeEditor;

import java.nio.file.Paths;

public class Demo extends Application {
    private final CodeEditor codeEditor = new CodeMirrorEditor();
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(codeEditor.getWidget());
        VBox controls = new VBox();
        controls.getChildren().addAll(readOnlyControls(), modeControls(), themeControls(), contentControls());
        borderPane.setBottom(controls);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        codeEditor.init(Paths.get("src/test/resources/html/editor.html").toUri());
        //codeEditor.setReadOnly(true); <-- this will crash
        codeEditor.runWhenReady(() -> codeEditor.setReadOnly(true));
        codeEditor.runWhenReady(() -> codeEditor.setReadOnly(false));
        codeEditor.runWhenReady(() -> codeEditor.setContent("Test content?!?@!?@!?@!?@!?"));
        primaryStage.show();
    }

    private Parent readOnlyControls() {
        HBox box = new HBox();
        Button setReadOnly = new Button("Set Read Only");
        Button unsetReadOnly = new Button("Set Editable");
        setReadOnly.setOnAction(event -> codeEditor.setReadOnly(true));
        unsetReadOnly.setOnAction(event -> codeEditor.setReadOnly(false));
        box.getChildren().addAll(setReadOnly, unsetReadOnly);
        return box;
    }

    private Parent modeControls() {
        HBox box = new HBox();
        TextField modeInput = new TextField();
        Button button = new Button("Set Mode");
        box.getChildren().addAll(modeInput, button);
        button.setOnAction(event -> codeEditor.setMode(modeInput.getText()));
        return box;
    }

    private Parent themeControls() {
        HBox box = new HBox();
        TextField themeInput = new TextField();
        Button button = new Button("Set Theme");
        button.setOnAction(event -> codeEditor.setTheme(themeInput.getText()));
        box.getChildren().addAll(themeInput, button);
        return box;
    }

    private Parent contentControls() {
        HBox box = new HBox();
        TextArea content = new TextArea();
        Button setButton = new Button("Set Content");
        Button getButton = new Button("Get Content");
        setButton.setOnAction(event -> {
            codeEditor.setContent(content.getText());});
        getButton.setOnAction(event -> {content.setText(codeEditor.getContent());});
        box.getChildren().addAll(content, setButton, getButton);
        return box;
    }

    public static void main(String[] args) {
        Demo.launch();
    }
}
