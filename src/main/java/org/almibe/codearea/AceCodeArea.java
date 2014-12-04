package org.almibe.codearea;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Worker;
import javafx.scene.Parent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class AceCodeArea implements CodeArea {
    private final WebView webView;
    private final WebEngine webEngine;
    private final ReadOnlyBooleanWrapper isInitializedProperty = new ReadOnlyBooleanWrapper(false);
    
    public AceCodeArea() {
        webView = new WebView();        
        webEngine = webView.getEngine();
    }

    @Override
    public void init() {
        //final String html = AceCodeArea.class.getResource("html/editor.html").toExternalForm();
        //final String html = AceCodeArea.class.getResource("html/codemirror-4.8/mode/groovy/index.html").toExternalForm();
        final String html = AceCodeArea.class.getResource("html/codemirror-4.8/demo/vim.html").toExternalForm();
        webEngine.load(html);                
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) ->  {
            if(newValue == Worker.State.SUCCEEDED) {
               isInitializedProperty.setValue(true);
            }
        });
    }

    @Override
    public ReadOnlyBooleanProperty isInitializedProperty() {
        return isInitializedProperty.getReadOnlyProperty();
    }

    @Override
    public Parent getWidget() {
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