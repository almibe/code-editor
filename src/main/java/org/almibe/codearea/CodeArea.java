package org.almibe.codearea;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class CodeArea {
    private WebView webView;
    private WebEngine webEngine;
    
    public CodeArea() {
        super();
    }
    
    public void init() {
        final String html = CodeArea.class.getResource("editor.html").toExternalForm();
        webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.load(html);
    }
    
    public WebView getWebView() {
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
}
