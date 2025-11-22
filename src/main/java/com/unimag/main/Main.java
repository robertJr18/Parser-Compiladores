package com.unimag.main;

import com.unimag.eval.Evaluator;
import com.unimag.lexer.Lexer;
import com.unimag.lexer.Token;
import com.unimag.parser.Parser;
import com.unimag.parser.astNodes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Programa principal - Parser y Evaluador de Expresiones Trigonométricas
 *
 * CARACTERÍSTICAS:
 * - Tokenización de expresiones matemáticas
 * - Parser recursivo descendente con gramática LL(1)
 * - Evaluación de expresiones con funciones trigonométricas
 * - Soporte para variables y constantes (pi, e)
 * - Manejo robusto de errores
 *
 * PRECEDENCIA (de mayor a menor):
 * 1. Funciones: sin(), cos(), tan()
 * 2. Potencia: ^ (asociatividad DERECHA)
 * 3. Negación unaria: -
 * 4. Multiplicación/División: *, /
 * 5. Suma/Resta: +, -
 *
 * EJEMPLOS:
 * - 3 + 4 * 2        → 11
 * - -2^2             → -4
 * - 2^3^2            → 512
 * - sin(pi/2)        → 1.0
 * - x*2+y (x=3,y=4)  → 10
 */
public class Main {
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static int totalTests = 0;


    private static final String BANNER = """
            ╔════════════════════════════════════════════════════════════╗
            ║   PARSER Y EVALUADOR DE EXPRESIONES TRIGONOMÉTRICAS        ║
            ║                                                            ║
            ║   Operadores: +, -, *, /, ^                                ║
            ║   Funciones:  sin, cos, tan                                ║
            ║   Constantes: pi, e                                        ║
            ║   Variables:  x, y, z, etc.                                ║
            ║                                                            ║
            ║   Comandos:   'exit' para salir                            ║
            ║               'test' para ejecutar pruebas                 ║
            ╚════════════════════════════════════════════════════════════╝
            """;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Mostrar banner
        System.out.println(BANNER);

        // Modo interactivo
        while (true) {
            try {
                System.out.print("Expresión> ");
                String input = scanner.nextLine().trim();

                // Comandos especiales
                if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("salir")) {
                    System.out.println("\n¡Hasta luego!");
                    break;
                }


                if (input.equalsIgnoreCase("test") || input.equalsIgnoreCase("pruebas")) {
                    System.out.println("\nEjecutando casos de prueba...");
                    System.out.println("╔══════════════════════════════════════════════════════════╗");
                    System.out.println("║   SUITE DE PRUEBAS - PARSER DE EXPRESIONES TRIG.        ║");
                    System.out.println("╚══════════════════════════════════════════════════════════╝\n");

                    // CASOS CORRECTOS
                    System.out.println("═══════════════════════════════════════════════════════════");
                    System.out.println("CASOS CORRECTOS (15 pruebas)");
                    System.out.println("═══════════════════════════════════════════════════════════\n");

                    // 1. Precedencia básica
                    testExpression("3 + 4 * 2", null, 11.0, "Precedencia: multiplicación antes que suma");

                    // 2. Paréntesis
                    testExpression("(3 + 4) * 2", null, 14.0, "Paréntesis cambian precedencia");

                    // 3. Negación unaria con potencia
                    testExpression("-2^2", null, -4.0, "Negación unaria: -(2^2) = -4");

                    // 4. Potencia asociativa derecha
                    testExpression("2^3^2", null, 512.0, "Potencia derecha: 2^(3^2) = 512");

                    // 5. Función sin con constante pi
                    testExpression("sin(pi/2)", null, 1.0, "sin(π/2) = 1.0", 0.0001);

                    // 6. Suma de funciones trigonométricas
                    testExpression("cos(0) + sin(pi)", null, 1.0, "cos(0) + sin(π) ≈ 1.0", 0.0001);

                    // 7. Variable con operación
                    Map<String, Double> vars1 = new HashMap<>();
                    vars1.put("x", 3.0);
                    vars1.put("y", 4.0);
                    testExpression("x*2+y", vars1, 10.0, "Variables: x*2+y con x=3, y=4");

                    // 8. Números decimales
                    testExpression("3.5 * 2.0 + .5", null, 7.5, "Números decimales: 3.5 * 2.0 + 0.5");

                    // 9. Función tan
                    testExpression("tan(pi/4)", null, 1.0, "tan(π/4) = 1.0", 0.0001);

                    // 10. Raíz cuadrada con potencia
                    testExpression("2^(1/2)", null, 1.41421356, "Raíz cuadrada: 2^(1/2)", 0.00001);

                    // 11. Identidad trigonométrica
                    Map<String, Double> vars2 = new HashMap<>();
                    vars2.put("x", 0.5);
                    testExpression("cos(x)^2 + sin(x)^2", vars2, 1.0, "Identidad: cos²(x) + sin²(x) = 1", 0.0001);

                    // 12. Composición de funciones
                    Map<String, Double> vars3 = new HashMap<>();
                    vars3.put("x", 0.0);
                    testExpression("sin(cos(x))", vars3, 0.8414709848, "sin(cos(0)) ≈ 0.8414", 0.0001);

                    // 13. Doble negación
                    testExpression("-(-5)", null, 5.0, "Doble negación: -(-5) = 5");

                    // 14. Constante e
                    testExpression("e^1", null, Math.E, "Constante e: e^1 ≈ 2.71828", 0.00001);

                    // 15. Múltiplo de pi
                    testExpression("2*pi", null, 2*Math.PI, "2*π ≈ 6.28318", 0.00001);

                    // CASOS ERRÓNEOS
                    System.out.println("\n═══════════════════════════════════════════════════════════");
                    System.out.println("             CASOS ERRÓNEOS (15 pruebas)");
                    System.out.println("═══════════════════════════════════════════════════════════\n");

                    // 16. Error sintáctico: operador sin operando
                    testExpressionError("3+*4", "Error sintáctico: operador sin operando");

                    // 17. Error léxico: múltiples puntos
                    testExpressionError("5..3", "Error léxico: número mal formado");

                    // 18. Error: paréntesis sin cerrar
                    testExpressionError("(3+4", "Error sintáctico: paréntesis sin cerrar");

                    // 19. Error: paréntesis extra
                    testExpressionError("3+4)", "Error sintáctico: paréntesis extra");

                    // 20. Error: expresión vacía
                    testExpressionError("", "Error: expresión vacía");

                    // 21. Error: división por cero
                    testDivisionByZero("1/0", "Error de ejecución: división por cero");

                    // 23. Error: carácter inválido
                    testExpressionError("3@4", "Error léxico: carácter inválido");

                    // 24. Error: función sin paréntesis
                    testExpressionError("sin1", "Error sintáctico: función sin paréntesis");

                    // 25. Error: múltiples operadores
                    testExpressionError("3++4", "Error sintáctico: múltiples operadores");


                    continue;
                }

                if (input.isEmpty()) {
                    continue;
                }

                // Procesar expresión
                processExpression(input, scanner);

            } catch (Exception e) {
                System.err.println("\n " + e.getMessage() + "\n");
            }
        }

        scanner.close();
    }

    private static void processExpression(String input, Scanner scanner) throws Exception {
        // 1. TOKENIZACIÓN
        System.out.println("\n--- FASE 1: TOKENIZACIÓN ---");
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        int size = tokens.size() - 1;
        System.out.println("Tokens generados: " + size);
        for (Token token : tokens) {
            if (token.type().name().equals("EOF")) continue;
            System.out.println("  " + token);
        }

        // 2. PARSING
        System.out.println("\n--- FASE 2: ANÁLISIS SINTÁCTICO ---");
        Parser parser = new Parser(tokens);
        Node ast = parser.parse();
        System.out.println("✓ AST construido correctamente");
        
        // Visualizar AST
        printAST(ast);

        // 3. EVALUACIÓN
        System.out.println("\n--- FASE 3: EVALUACIÓN ---");
        Evaluator evaluator = new Evaluator(ast);

        // Recolectar y pedir variables
        evaluator.requestVariableValues(scanner);

        // Evaluar
        double result = evaluator.evaluate();

        // Mostrar resultado
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.printf("     RESULTADO: %-31.10f ║%n", result);
        System.out.println("  ╚══════════════════════════════════════════════╝\n");
    }

    private static void testExpression(String expression, Map<String, Double> variables,
                                       double expected, String description) {
        testExpression(expression, variables, expected, description, 0.000001);
    }


    private static void testExpression(String expression, Map<String, Double> variables,
                                       double expected, String description, double tolerance) {

        System.out.printf("  Expresión: %s%n", expression);

        try {
            Lexer lexer = new Lexer(expression);
            Parser parser = new Parser(lexer.tokenize());
            Node ast = parser.parse();

            Evaluator evaluator = new Evaluator(ast);
            if (variables != null) {
                evaluator.setVariables(variables);
            }

            double result = evaluator.evaluate();

            if (Math.abs(result - expected) < tolerance) {
                System.out.printf("   Resultado: %.10f (esperado: %.10f)%n%n", result, expected);
                testsPassed++;
            } else {
                System.out.printf("    Resultado: %.10f (esperado: %.10f)%n%n", result, expected);
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.printf("    Error inesperado: %s%n%n", e.getMessage());
            testsFailed++;
        }
    }

    private static void testExpressionError(String expression, String description) {

        System.out.printf("  Expresión: %s%n", expression);

        try {
            Lexer lexer = new Lexer(expression);
            Parser parser = new Parser(lexer.tokenize());
            Node ast = parser.parse();

            Evaluator evaluator = new Evaluator(ast);
            double result = evaluator.evaluate();

            System.out.printf("    Se esperaba error pero se obtuvo: %.10f%n%n", result);

        } catch (Exception e) {
            System.out.printf("    Error capturado correctamente: %s%n%n", e.getMessage());

        }
    }

    private static void testDivisionByZero(String expression, String description) {

        System.out.printf("  Expresión: %s%n", expression);

        try {
            Lexer lexer = new Lexer(expression);
            Parser parser = new Parser(lexer.tokenize());
            Node ast = parser.parse();

            Evaluator evaluator = new Evaluator(ast);
            double result = evaluator.evaluate();

            System.out.printf("    Se esperaba ArithmeticException pero se obtuvo: %.10f%n%n", result);

        } catch (ArithmeticException e) {
            System.out.printf("   Error capturado: %s%n%n", e.getMessage());

        } catch (Exception e) {
            System.out.printf("    Error incorrecto: %s%n%n", e.getMessage());

        }
    }


    // HELPERS PARA VISUALIZACIÓN DEL AST

    /**
     * Imprime el AST en forma de árbol vertical
     */
    private static void printAST(Node ast) {
        System.out.println("\n--- VISUALIZACIÓN DEL AST ---");
        System.out.println();
        printTreeVertical(ast, 0);
        System.out.println();
    }

    /**
     * Imprime el árbol de forma vertical con la raíz arriba
     */
    private static void printTreeVertical(Node node, int depth) {
        if (node == null) return;

        String nodeStr = getNodeString(node);
        int indent = depth * 4;

        // Imprimir el nodo actual centrado
        printCentered(nodeStr, indent);

        // Procesar hijos según el tipo de nodo
        if (node instanceof BinaryNode binaryNode) {
            Node left = binaryNode.getLeft();
            Node right = binaryNode.getRight();

            // Imprimir líneas conectoras
            printBinaryConnectors(indent, nodeStr.length());

            // Imprimir hijos lado a lado
            printBinaryChildren(left, right, depth + 1);
        }
        else if (node instanceof UnaryNode unaryNode) {
            // Imprimir línea vertical para hijo único
            printSpaces(indent + nodeStr.length() / 2);
            System.out.println("│");
            printTreeVertical(unaryNode.getExpression(), depth + 1);
        }
        else if (node instanceof FunctionNode functionNode) {
            // Imprimir línea vertical para argumento
            printSpaces(indent + nodeStr.length() / 2);
            System.out.println("│");
            printTreeVertical(functionNode.getArgument(), depth + 1);
        }
    }

    /**
     * Imprime dos hijos en paralelo (para nodos binarios)
     */
    private static void printBinaryChildren(Node left, Node right, int depth) {
        List<String> leftLines = getTreeLines(left, depth);
        List<String> rightLines = getTreeLines(right, depth);

        int maxLines = Math.max(leftLines.size(), rightLines.size());
        int leftWidth = getMaxWidth(leftLines);

        for (int i = 0; i < maxLines; i++) {
            String leftLine = i < leftLines.size() ? leftLines.get(i) : "";
            String rightLine = i < rightLines.size() ? rightLines.get(i) : "";

            // Espaciar apropiadamente
            System.out.print(leftLine);
            printSpaces(leftWidth - getVisibleLength(leftLine) + 8);
            System.out.println(rightLine);
        }
    }

    /**
     * Obtiene todas las líneas del árbol como lista de strings
     */
    private static List<String> getTreeLines(Node node, int depth) {
        List<String> lines = new ArrayList<>();
        if (node == null) return lines;

        String nodeStr = getNodeString(node);
        int indent = depth * 4;
        
        lines.add(repeat(" ", indent) + nodeStr);

        if (node instanceof BinaryNode binaryNode) {
            List<String> leftLines = getTreeLines(binaryNode.getLeft(), depth + 1);
            List<String> rightLines = getTreeLines(binaryNode.getRight(), depth + 1);
            
            // Agregar conectores
            String connector = repeat(" ", indent) + repeat(" ", nodeStr.length() / 2) + "/" + repeat(" ", nodeStr.length() / 2) + "\\";
            lines.add(connector);
            
            // Combinar líneas de hijos
            int maxLines = Math.max(leftLines.size(), rightLines.size());
            int leftWidth = getMaxWidth(leftLines);
            
            for (int i = 0; i < maxLines; i++) {
                String leftLine = i < leftLines.size() ? leftLines.get(i) : "";
                String rightLine = i < rightLines.size() ? rightLines.get(i) : "";
                lines.add(leftLine + repeat(" ", leftWidth - getVisibleLength(leftLine) + 4) + rightLine);
            }
        }
        else if (node instanceof UnaryNode unaryNode) {
            lines.add(repeat(" ", indent + nodeStr.length() / 2) + "│");
            lines.addAll(getTreeLines(unaryNode.getExpression(), depth + 1));
        }
        else if (node instanceof FunctionNode functionNode) {
            lines.add(repeat(" ", indent + nodeStr.length() / 2) + "│");
            lines.addAll(getTreeLines(functionNode.getArgument(), depth + 1));
        }

        return lines;
    }

    /**
     * Imprime conectores para nodos binarios
     */
    private static void printBinaryConnectors(int indent, int nodeWidth) {
        int center = indent + nodeWidth / 2;
        printSpaces(center);
        System.out.println("/" + repeat(" ", nodeWidth - 1) + "\\");
    }

    /**
     * Imprime texto centrado con indentación
     */
    private static void printCentered(String text, int indent) {
        printSpaces(indent);
        System.out.println(text);
    }

    /**
     * Imprime espacios
     */
    private static void printSpaces(int count) {
        if (count > 0) {
            System.out.print(repeat(" ", count));
        }
    }

    /**
     * Obtiene el ancho máximo de una lista de líneas
     */
    private static int getMaxWidth(List<String> lines) {
        int max = 0;
        for (String line : lines) {
            int len = getVisibleLength(line);
            if (len > max) max = len;
        }
        return max;
    }

    /**
     * Obtiene la longitud visible de una cadena
     */
    private static int getVisibleLength(String str) {
        return str.length();
    }

    /**
     * Obtiene la representación en string de un nodo
     */
    private static String getNodeString(Node node) {
        if (node instanceof NumberNode numberNode) {
            return formatNumber(numberNode.getValue());
        }
        else if (node instanceof VarNode varNode) {
            return varNode.getIdentifier();
        }
        else if (node instanceof BinaryNode binaryNode) {
            return "[" + binaryNode.getOperator() + "]";
        }
        else if (node instanceof UnaryNode unaryNode) {
            return "[" + unaryNode.getOperator() + "]";
        }
        else if (node instanceof FunctionNode functionNode) {
            return functionNode.getName() + "()";
        }
        return "[?]";
    }

    /**
     * Formatea un número para mostrarlo de manera legible
     */
    private static String formatNumber(double value) {
        // Si es un número entero, mostrarlo sin decimales
        if (value == Math.floor(value)) {
            return String.format("%.0f", value);
        }
        // Si es PI o E, mostrarlo con su símbolo
        if (Math.abs(value - Math.PI) < 0.000001) {
            return "π";
        }
        if (Math.abs(value - Math.E) < 0.000001) {
            return "e";
        }
        // Número decimal normal
        return String.valueOf(value);
    }

    /**
     * Repite un string n veces
     */
    private static String repeat(String str, int count) {
        if (count <= 0) return "";
        return str.repeat(count);
    }

}