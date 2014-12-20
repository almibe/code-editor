package org.almibe.codeeditor;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.concurrent.Worker;
import javafx.scene.Parent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.net.URI;

public class CodeMirrorEditor implements CodeEditor {
    private final WebView webView;
    private final WebEngine webEngine;
    private final ReadOnlyBooleanWrapper editorInitializedProperty = new ReadOnlyBooleanWrapper(false);

    public CodeMirrorEditor() {
        webView = new WebView();        
        webEngine = webView.getEngine();
    }

    @Override
    public void init(URI indexPage) {
        webEngine.load(indexPage.toString());
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) ->  {
            if(newValue == Worker.State.SUCCEEDED) {
                JSObject window = fetchWindow();
                window.call("editorLoaded", new EditorLoadedCallback());
            }
        });
    }

    //because of JavaFX/WebKit access policy this callback has to be a public inner class and can't be a lambda or anonymous inner class
    public class EditorLoadedCallback implements Runnable {
        @Override
        public void run() {
            editorInitializedProperty.setValue(true);
        }
    }

    @Override
    public String getContent() {
        return (String) fetchEditor().call("getValue");
    }

    @Override
    public void setContent(String newContent) {
        fetchEditor().call("setValue", newContent);
    }

    @Override
    public ReadOnlyBooleanProperty editorInitializedProperty() {
        return editorInitializedProperty.getReadOnlyProperty();
    }

    @Override
    public Parent getWidget() {
        return this.webView;
    }

    @Override
    public boolean isReadOnly() {
        return (Boolean) fetchEditor().call("getOption","readOnly");
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        fetchEditor().call("setOption", "readOnly", readOnly);
    }

    @Override
    public JSObject fetchEditor() {
        Object editor = webEngine.executeScript("require('codeeditor').codeMirror;");
        if(editor instanceof JSObject) {
            return (JSObject) editor;
        }
        throw new IllegalStateException("CodeMirror not loaded.");
    }

    @Override
    public String getMode() {
        return (String) fetchEditor().call("getOption", "mode");
    }

    @Override
    public void setMode(String mode) {
        webEngine.executeScript("require('codeeditor').setMode('" + mode + "')");
    }

    @Override
    public void includeJSModules(String[] modules, Runnable runnable) {
        //TODO test this
        fetchCodeEditorObject().call("importJSModules", modules, runnable);
    }

    @Override
    public JSObject fetchRequireJSObject() {
        return (JSObject) webEngine.executeScript("require();");
    }

    @Override
    public String getTheme() {
        return (String) fetchEditor().call("getOption", "theme");
    }

    @Override
    public void setTheme(String theme) {
        webEngine.executeScript("require('codeeditor').setTheme('" + theme + "')");
    }

    private JSObject fetchCodeEditorObject() {
        return (JSObject) webEngine.executeScript("require('codeeditor');");
    }

    private JSObject fetchWindow() { return (JSObject) webEngine.executeScript("window;"); }
}