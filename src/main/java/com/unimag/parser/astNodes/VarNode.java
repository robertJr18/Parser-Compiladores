package com.unimag.parser.astNodes;

import java.util.Map;

public class VarNode extends Node {
    String identifier;

    public VarNode(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public double evaluate(Map<String, Double> env) throws Exception {
        if (!env.containsKey(identifier)) throw new Exception("Variable " + identifier + " not found");
        return env.get(identifier);
    }

    public String getIdentifier() {
        return identifier;
    }
}
