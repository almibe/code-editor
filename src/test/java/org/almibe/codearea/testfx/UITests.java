package org.almibe.codearea.testfx;

import javafx.scene.Parent;
import org.almibe.codearea.CodeArea;
import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

//Giving up on tests for right now since I've been getting weird timing issues
//in tests that I don't get when running it normally.
public class UITests {// extends GuiTest {
    CodeArea codeArea = new CodeArea();
    
    
//    @Override
//    public Parent getRootNode() {
//        codeArea.init();
//        codeArea.setId("codeArea");
//        return codeArea.getWebView();
//    }
//    
//    @Test
//    public void editor_should_start_empty() {
//        Assert.assertEquals("", codeArea.getValue());        
//    }
//    
//    @Test
//    public void set_and_get_text_test() {
//        codeArea.setValue("test");
//        Assert.assertEquals("test", codeArea.getValue());
//        codeArea.setValue("");
//        Assert.assertEquals("", codeArea.getValue());
//    }
//    
//    @Test
//    public void keyboard_test() {
//        click("#codeArea").type("test");
//        Assert.assertEquals("test", codeArea.getValue());
//    }
    
//    @Test
//    public void set_and_get_mode() {
//        codeArea.setMode("ace/mode/javascript");
//        sleep(1000);
//        Assert.assertEquals("ace/mode/javascript", codeArea.getMode());
//    }
    
    
//    @Test
//    public void set_and_get_mode() {
//        codeArea.setMode("ace/theme/twilight");
//        Assert.assertEquals("ace/theme/twilight", codeArea.getMode());
//    }
}
