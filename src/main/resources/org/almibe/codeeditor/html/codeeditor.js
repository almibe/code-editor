define(["codemirror-4.8/lib/codemirror", "codemirror-4.8/addon/display/fullscreen", "codemirror-4.8/addon/selection/active-line",
            "codemirror-4.8/addon/mode/loadmode", "codemirror-4.8/mode/meta"], function(CodeMirror) {
    var codeMirror = CodeMirror(document.body);
    codeMirror.setOption("lineNumbers", true);
    codeMirror.setOption("fullScreen", true);
    codeMirror.setOption("styleActiveLine", true);
    CodeMirror.modeURL = "codemirror-4.8/mode/%N/%N.js";

    //modified from demo/loadmode.html
    function changeMode(val) {
        var m, mode, spec;
        if (/\//.test(val)) {
          var info = CodeMirror.findModeByMIME(val);
          if (info) {
            mode = info.mode;
            spec = val;
          }
        } else {
          mode = spec = val;
        }
        if (mode) {
          codeMirror.setOption("mode", spec);
          CodeMirror.autoLoadMode(codeMirror, mode);
        }
    }

    return {
        "codeMirror": codeMirror,
        setTheme: function(theme) {
            if (theme == 'default') {
              codeMirror.setOption("theme", 'default');
              return;
            }
            var sheets = document.styleSheets;
            var exists = false;
            for (var sheet in sheets) {
              if (sheet && sheet.href && sheet.href.endsWith("/" + theme + ".css")) {
                exists = true;
                break;
              }
            }
            if (!exists) {
              var link = document.createElement("link");
              link.rel = "stylesheet";
              link.type = "text/css";
              link.href = "codemirror-4.8/theme/" + theme + ".css";
              document.getElementsByTagName("head")[0].appendChild(link);
            }
            codeMirror.setOption("theme", theme);
        },

        importJSModules: function(modules, callback) {
            require(modules, function() {
                callback.run();
            });
        },

        setMode: function(modeName) {
            var mimeModes = Object.keys(CodeMirror.mimeModes);
            var modeNames = Object.keys(CodeMirror.modes);
            var allModeNames = mimeModes.concat(modeNames);
            if (allModeNames.indexOf(modeName) >= 0) {
              codeMirror.setOption("mode", modeName);
            } else {
              changeMode(modeName);
            }
        }
    }
});