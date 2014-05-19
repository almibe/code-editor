package org.almibe.codearea.testfx;

import javafx.scene.Parent;
import org.almibe.codearea.CodeArea;
import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

public class UITests extends GuiTest {
    @Override
    public Parent getRootNode() {
        return new CodeArea();
    }
    
    @Test
    public void dummyTest() {
        Assert.assertEquals(true,true);
    }
}
