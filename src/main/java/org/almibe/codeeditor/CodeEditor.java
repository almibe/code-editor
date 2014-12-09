package org.almibe.codeeditor;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Parent;
import netscape.javascript.JSObject;

public interface CodeEditor {
    String getContent();
    void setContent(String newContent);
    ReadOnlyBooleanProperty isInitializedProperty();
    void init();
    Parent getWidget();
    boolean isReadOnly();
    void setReadOnly(boolean readOnly);
    String getMode();
    void setMode(String mode);
    void includeJSModules(String[] modules, Runnable runnable);
    JSObject fetchRequireJSObject();
    JSObject fetchEditor();
    String getTheme();
    void setTheme(String theme);
}