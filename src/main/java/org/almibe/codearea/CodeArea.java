package org.almibe.codearea;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;

public interface CodeArea {
    StringProperty contentProperty();
    BooleanProperty readOnlyProperty();
    StringProperty modeProperty();
    StringProperty themeProperty();
    void init();
    void addInitializationListener(InitializerListener listener);
    Parent getWidget();

    public interface InitializerListener {
        void onInitialized();
    }
}