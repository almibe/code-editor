package org.almibe.codearea;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;

public interface CodeArea {
    StringProperty contentProperty();
    ReadOnlyBooleanProperty isInitializedProperty();
    void init();
    Parent getWidget();
    boolean isReadOnly();
    void setReadOnly(boolean readOnly);
    String getMode();
    void setMode(String mode);
    String getTheme();
    void setTheme(String theme);
}