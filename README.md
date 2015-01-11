CodeEditor
==========

JavaFX wrapper for CodeMirror

Check out src/test/java/org/almibe/codeeditor/demo/Demo.java for an example of how to use this project as it currently exists.  I've pretty much stopped work on this project after discovering how buggy WebView is when trying to integrate JS libraries like CodeMirror and AceEditor as regular JavaFX widgets, particularly when two-way communication is required with multiple WebViews concurrently.

I still think this work is a good demonstration of using WebView with existing JS libraries and it works well if you restrict yourself to only calling JS from Java via executeScript, never trying to call Java from JS, and only using returned values from executeScript("") calls if they are not JSObjects.

If you are curious my current work on this problem is in my repo code-text-area and it focuses on using a fork of OpenJDK to supply various support for coding efforts.
