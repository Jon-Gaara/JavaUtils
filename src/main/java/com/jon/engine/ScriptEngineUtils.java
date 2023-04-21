package com.jon.engine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptEngineUtils {

    public ScriptEngine getScriptEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        // ScriptEngine engine = manager.getEngineByExtension("js");
        // ScriptEngine engine = manager.getEngineByMimeType("text/javascript");
        if (engine == null) {
            throw new RuntimeException("找不到SciptEngine语言执行引擎");
        }
        return engine;
    }

    public static void main(String[] args) throws ScriptException {
        ScriptEngineUtils util = new ScriptEngineUtils();
        ScriptEngine engine = util.getScriptEngine();
        engine.eval("print('12312312');");
    }
}
