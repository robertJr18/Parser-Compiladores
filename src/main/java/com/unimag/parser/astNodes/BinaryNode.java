package com.unimag.parser.astNodes;

import java.util.Map;

public class BinaryNode extends Node{

    char operator;
    Node left;
    Node right;

    public BinaryNode(char operator, Node left, Node right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate(Map<String, Double> env) throws Exception {
        var l = left.evaluate(env);
        var r = right.evaluate(env);

        switch (operator) {
            case '+' -> {
                return  l + r;
            }
            case '-' -> {
                return  l - r;
            }
            case '*' -> {
                return  l * r;
            }
            case '/' -> {
                if (r == 0) throw new ArithmeticException("Cannot divide by zero");
                return  l / r;
            }
            case '^' -> {
                return  Math.pow(left.evaluate(env), right.evaluate(env));
            }
            default -> throw new RuntimeException("Unknown operator " + operator);
        }
    }

    public char getOperator() {
        return operator;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }
}
