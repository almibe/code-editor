package org.almibe.codearea;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class AceCodeArea implements CodeArea {
    private final WebView webView;
    private final WebEngine webEngine;
    private final List<CodeArea.InitializerListener> initializerListeners = new ArrayList<>();
    
    public AceCodeArea() {
        webView = new WebView();        
        webEngine = webView.getEngine();
    }

    @Override
    public void init() {
        final String html = AceCodeArea.class.getResource("html/editor.html").toExternalForm();
        webEngine.load(html);                
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if(newValue == Worker.State.SUCCEEDED) {
                   fireIntializationEvents();
                }
            }
        });
    }

    @Override
    public void addInitializationListener(CodeArea.InitializerListener listener) {
        initializerListeners.add(listener);
    }
    
    private void fireIntializationEvents() {
        for(CodeArea.InitializerListener listener : initializerListeners) {
            listener.onInitialized();
        }
    }
    
    public Node getNode() {
        return this.webView;
    }
    
    public JSObject fetchEditor() {
        Object editor = webEngine.executeScript("editor;");
        if(editor instanceof JSObject) {
            return (JSObject) editor;
        }
        throw new IllegalStateException("CodeArea not loaded.");
    }
    
    public JSObject fetchSession() {
        Object temp = webEngine.executeScript("editor.session;");
        if(temp instanceof JSObject) {
            return (JSObject) temp;
        }
        throw new IllegalStateException("CodeArea not loaded.");
    }
    
    public void setValue(String value) {
        fetchEditor().call("setValue", value);
    }
    
    public String getValue() {
        return (String) fetchEditor().call("getValue"); //TODO add check
    }
    
    public void setMode(String mode) {
        fetchSession().call("setMode", mode);
    }
    
    public String getMode() {
        return (String) fetchSession().eval("this.getMode().$id;");
    }

    @Override
    public StringProperty contentProperty() {
        throw new UnsupportedOperationException("contentProperty is not implemented");
    }

    @Override
    public BooleanProperty readOnlyProperty() {
        throw new UnsupportedOperationException("readOnly is not implemented");
    }

    @Override
    public StringProperty modeProperty() {
        throw new UnsupportedOperationException("modeProperty is not implemented");
    }

    @Override
    public StringProperty themeProperty() {
        throw new UnsupportedOperationException("themeProperty is not implemented");
    }
}