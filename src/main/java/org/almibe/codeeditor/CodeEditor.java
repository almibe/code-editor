package org.almibe.codeeditor;

import javafx.scene.Parent;
import netscape.javascript.JSObject;

import java.net.URI;

public interface CodeEditor {
    String getContent();
    void setContent(String newContent);
    boolean isEditorInitialized();
    void init(URI indexPage, Runnable... runAfterLoading);
    Parent getWidget();
    boolean isReadOnly();
    void setReadOnly(boolean readOnly);
    String getMode();
    void setMode(String mode);
    void includeJSModules(String[] modules, Runnable runnable);
    JSObject fetchRequireJSObject();
    JSObject fetchEditor();
    String getTheme();
    void setTheme(String theme, String... cssFile);
    void setTheme(String theme);
    void runWhenReady(Runnable runnable);
}
