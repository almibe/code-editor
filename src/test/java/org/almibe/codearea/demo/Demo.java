package org.almibe.codearea.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.almibe.codearea.CodeMirrorArea;
import org.almibe.codearea.CodeArea;

public class Demo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        CodeArea codeArea = new CodeMirrorArea();
        Scene scene = new Scene(codeArea.getWidget());
        primaryStage.setScene(scene);
        codeArea.init();
        primaryStage.show();
    }

    public static void main(String[] args) {
        Demo.launch();
    }
}
