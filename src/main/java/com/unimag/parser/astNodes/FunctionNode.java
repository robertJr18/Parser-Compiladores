package com.unimag.parser.astNodes;

import java.util.Map;

public class FunctionNode extends Node {
    String name;
    Node body;

    public FunctionNode(String name, Node body) {
        this.name = name;
        this.body = body;
    }
    @Override
    public double evaluate(Map<String, Double> env) throws Exception {
        var arg = body.evaluate(env);
        switch (name) {
            case "sin","sen": return Math.sin(arg);
            case "cos": return Math.cos(arg);
            case "tan": return Math.tan(arg);
            default: throw new Exception("Invalid function name");
        }
    }

    public String getName() {
        return name;
    }

    public Node getBody() {
        return body;
    }
}
