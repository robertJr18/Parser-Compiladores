package com.unimag.parser.astNodes;

import java.util.Map;

public class UnaryNode extends Node {

    final char operator;
    Node expression;

    public UnaryNode(Node expr) {
        this.operator = '-';
        this.expression = expr;
    }
    @Override
    public double evaluate(Map<String, Double> env) throws Exception {
        return -expression.evaluate(env);
    }

    public Node getExpression() {
        return expression;
    }
}
