package org.almibe.codearea;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

public interface CodeArea {
    StringProperty contentProperty();
    BooleanProperty readOnlyProperty();
    StringProperty modeProperty();
    StringProperty themeProperty();
    void init();
    void addInitializationListener(InitializerListener listener);

    public interface InitializerListener {
        void onInitialized();
    }
}