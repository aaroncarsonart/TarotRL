package com.aaroncarsonart.tarotrl.util;

import com.aaroncarsonart.tarotrl.exception.TarotRLException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Tracery {

    public static String loadResourceFile(String path) {
        try {
            ClassLoader classLoader = Tracery.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new TarotRLException(e);
        }
    }

    private static String loadTraceryLibrary() {
        return loadResourceFile("js/tracery.node.js");
    }

    private static String loadTraceryGrammar() {
        return loadResourceFile("grammar/example.json");
    }


    public static void main(String args[]) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");

//        String tracery = loadResourceFile("js/tracery.node.js");
        String tracery = loadResourceFile("js/tracery.js");
        engine.eval(tracery);

        String grammar = loadTraceryGrammar();
        engine.eval("var example = " + grammar + ";");

        engine.eval("var grammar = tracery.createGrammar(example);");
        engine.eval("var myTitle = grammar.flatten(\"#origin#\");");
        engine.eval("print(myTitle);");


//        engine.eval("var x = 10;");
//        engine.eval("var y = 20;");
//        engine.eval("var z = x + y;");
//        engine.eval("print (z);");


    }


}

