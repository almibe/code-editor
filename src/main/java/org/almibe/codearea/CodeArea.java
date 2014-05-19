package org.almibe.codearea;


import javafx.scene.Parent;
import javafx.scene.web.WebView;

public class CodeArea extends Parent {
    public CodeArea() {
        super();

        final String html = CodeArea.class.getResource("editor.html").toExternalForm();
        final java.net.URI uri = java.nio.file.Paths.get(html).toAbsolutePath().toUri();
        final WebView root = new WebView();
        root.getEngine().load(html);
        System.out.println(html);
        this.getChildren().add(root);
    }
}
