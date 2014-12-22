package org.almibe.codeeditor;

import javafx.concurrent.Worker;
import javafx.scene.Parent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.net.URI;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class CodeMirrorEditor implements CodeEditor {
    private final WebView webView;
    private final WebEngine webEngine;
    private boolean isEditorInitialized = false; //TODO make atomic var?
    private final Queue<Runnable> queue = new LinkedBlockingQueue<>();
    private final EditorLoadedCallback editorLoadedCallback = new EditorLoadedCallback();

    public CodeMirrorEditor() {
        webView = new WebView();        
        webEngine = webView.getEngine();
    }

    public void init(URI editorUri, Runnable... runAfterLoading) {
        queue.addAll(Arrays.asList(runAfterLoading));
        webEngine.load(editorUri.toString());
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) ->  {
            if(newValue == Worker.State.SUCCEEDED) {
                JSObject window = fetchWindow();
                window.call("editorLoaded", editorLoadedCallback);
            }
        });
    }

    //because of JavaFX/WebKit access policy this callback has to be a public inner class and can't be a lambda or anonymous inner class
    public class EditorLoadedCallback implements Runnable {
        @Override
        public void run() {
            isEditorInitialized = true;
            for(Runnable runnable : queue) {
                runnable.run();
            }
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
    public boolean isEditorInitialized() {
        return isEditorInitialized;
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

    @Override
    public void runWhenReady(Runnable runnable) {
        if(isEditorInitialized) {
            runnable.run();
        } else {
            queue.add(runnable);
        }
    }

    private JSObject fetchCodeEditorObject() {
        return (JSObject) webEngine.executeScript("require('codeeditor');");
    }

    private JSObject fetchWindow() { return (JSObject) webEngine.executeScript("window;"); }
}