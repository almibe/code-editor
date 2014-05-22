package org.almibe.codearea;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class CodeArea {
    private JSObject editor;
    private JSObject session;
    private WebView webView;
    private WebEngine webEngine;
    
    public CodeArea() {
        super();
    }
    
    public void init() {
        final String html = CodeArea.class.getResource("editor.html").toExternalForm();
        final java.net.URI uri = java.nio.file.Paths.get(html).toAbsolutePath().toUri();
        
        webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.load(html);
        
    }
    
    public WebView getWebView() {
        return this.webView;
    }
    
    public JSObject fetchEditor() {
        if(editor == null) {
            Object temp = webEngine.executeScript("editor;");
            if(temp instanceof JSObject) {
                editor = (JSObject) temp;
            }
        }
        return editor;
    }
    
    public JSObject fetchSession() {
        if(session == null) {
            Object temp = webEngine.executeScript("editor.session;");
            if(temp instanceof JSObject) {
                session = (JSObject) temp;
            }
        }
        return session;
    }
    
    public void setValue(String value) {
        if(fetchEditor() != null) {
            editor.call("setValue", value);
        }
    }
    
    public String getValue() {
        if(fetchEditor() != null) {
            return (String) editor.call("getValue"); //TODO add check
        } else {
            return null;
        }
    }
    
    public void setMode(String mode) {
        if(fetchSession() != null) {
            session.call("setMode", mode);
        }
    }
    
    public String getMode() {
        if(fetchSession() != null) {
            return (String) session.eval("this.getMode().$id;");
        } else {
            return null;
        }
    }
}
