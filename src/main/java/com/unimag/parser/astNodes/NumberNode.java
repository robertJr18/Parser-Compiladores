package com.unimag.parser.astNodes;

import java.util.Map;

public class NumberNode extends Node {

    double value;
    public NumberNode(double value) {
        this.value = value;
    }
    @Override
    public double evaluate(Map<String, Double> env) throws Exception {
        return value;
    }

    public double getValue() {
        return value;
    }
}
