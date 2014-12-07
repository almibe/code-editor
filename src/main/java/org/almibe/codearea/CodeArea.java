package org.almibe.codearea;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import netscape.javascript.JSObject;

import java.util.List;

public interface CodeArea {
    StringProperty contentProperty();
    ReadOnlyBooleanProperty isInitializedProperty();
    void init();
    Parent getWidget();
    boolean isReadOnly();
    void setReadOnly(boolean readOnly);
    String getMode();
    void setModeByName(String mode);
    void setModeByMIMEType(String mimeType);
    List<String> getAvailableMIMETypes();
    List<String> getAvailableModes();
    void includeJSFile(String filePath, Runnable runnable);
    JSObject fetchEditor();
    String getTheme();
    void setTheme(String theme);
}