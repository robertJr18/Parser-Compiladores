package com.unimag.parser.astNodes;

import java.util.Map;

public abstract class Node {
    public  abstract double evaluate(Map<String,Double> env) throws Exception;
}
