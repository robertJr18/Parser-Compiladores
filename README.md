# Parser y Evaluador de Expresiones Trigonométricas

## 📋 Descripción

Sistema completo de análisis léxico, sintáctico y evaluación de expresiones matemáticas con funciones trigonométricas, implementado en Java con interfaz gráfica para visualización del AST.

**Proyecto de Compiladores - Universidad del Magdalena**

## 👥 Autores

- Robert Gonzalez
- Esteban Puello
- Jose Rodriguez

## ✨ Características

### Operadores y Funciones
- **Operadores**: `+`, `-`, `*`, `/`, `^` (potencia)
- **Funciones**: `sin()`, `cos()`, `tan()`
- **Constantes**: `pi` (π), `e`
- **Variables**: `x`, `y`, `z`, etc.
- **Números**: enteros, decimales (incluyendo `.5`)
- **Negación unaria**: `-x`
- **Paréntesis**: `()`

### Precedencia de Operadores
1. Funciones trigonométricas (sin, cos, tan)
2. Potencia `^` (asociatividad derecha)
3. Negación unaria `-`
4. Multiplicación/División `*`, `/`
5. Suma/Resta `+`, `-`

**Ejemplos:**
- `2^3^2` → `512` (se evalúa como `2^(3^2)`)
- `-2^2` → `-4` (se evalúa como `-(2^2)`)
- `3+4*2` → `11` (se evalúa como `3+(4*2)`)

## 🏗️ Gramática LL(1)

```
E  → T E'
E' → + T E' | - T E' | ε

T  → U T'
T' → * U T' | / U T' | ε

U  → - U | F

F  → P F'
F' → ^ U F' | ε

P  → NUM | VAR | PI | E
   | sin(E) | cos(E) | tan(E)
   | (E)
```

**Donde:**
- `E` = Expresión
- `T` = Término
- `U` = Unario
- `F` = Factor
- `P` = Primario

## 🖥️ Interfaz Gráfica

El proyecto incluye una GUI desarrollada con **Java Swing** que permite:

### Funcionalidades de la GUI
- ✅ Campo de entrada para expresiones
- ✅ **Visualización gráfica del AST** con nodos de colores
- ✅ Consola de salida estilo terminal (fondo oscuro)
- ✅ Tokenización visible paso a paso
- ✅ Detección automática de variables
- ✅ Diálogos para ingresar valores de variables
- ✅ Mensajes de error detallados

### Colores de Nodos en el AST
- 🔵 **Azul**: Números
- 🟢 **Verde**: Variables
- 🟠 **Naranja**: Operadores binarios (+, -, *, /, ^)
- 🔴 **Rojo**: Operador unario (-)
- 🟣 **Púrpura**: Funciones trigonométricas

### Captura de la GUI
La interfaz se divide en:
- **Panel izquierdo**: Visualización del árbol AST
- **Panel derecho**: Consola con output de las fases
- **Panel superior**: Campo de entrada y botones

## 📁 Estructura del Proyecto

```
src/main/java/com/unimag/
├── lexer/
│   ├── Lexer.java          # Analizador léxico
│   ├── Token.java          # Definición de token
│   └── TokenType.java      # Tipos de tokens
├── parser/
│   ├── Parser.java         # Analizador sintáctico (recursivo descendente)
│   └── astNodes/           # Nodos del AST
│       ├── Node.java       # Clase base abstracta
│       ├── NumberNode.java
│       ├── VarNode.java
│       ├── BinaryNode.java
│       ├── UnaryNode.java
│       └── FunctionNode.java
├── eval/
│   └── Evaluator.java      # Evaluador de expresiones
├── gui/
│   └── ParserGUI.java      # Interfaz gráfica
└── main/
    └── Main.java           # Modo consola
```

## 🚀 Ejecución

### Requisitos
- Java 17 o superior
- Maven (opcional)

### Modo Interfaz Gráfica (Recomendado)

```bash
# Compilar
javac -d target/classes -sourcepath src/main/java \
    src/main/java/com/unimag/gui/ParserGUI.java

# Ejecutar
java -cp target/classes com.unimag.gui.ParserGUI
```

### Modo Consola

```bash
# Compilar
javac -d target/classes -sourcepath src/main/java \
    src/main/java/com/unimag/main/Main.java

# Ejecutar
java -cp target/classes com.unimag.main.Main
```

### Con Maven

```bash
# Compilar
mvn clean compile

# Ejecutar GUI
mvn exec:java -Dexec.mainClass="com.unimag.gui.ParserGUI"

# Ejecutar Consola
mvn exec:java -Dexec.mainClass="com.unimag.main.Main"
```

### Desde IntelliJ IDEA

1. Abrir `ParserGUI.java` o `Main.java`
2. Click derecho → **Run**
3. O presionar el botón ▶️ verde

## 📝 Ejemplos de Uso

### Expresiones Simples
```
3 + 4 * 2           → 11.0
(3 + 4) * 2         → 14.0
-2^2                → -4.0
2^3^2               → 512.0
```

### Con Funciones Trigonométricas
```
sin(pi/2)           → 1.0
cos(0)              → 1.0
tan(pi/4)           → 1.0
sin(pi/6)           → 0.5
```

### Con Variables
```
x*2+y               → (pide valores de x e y)
cos(x)^2 + sin(x)^2 → 1.0 (identidad trigonométrica)
```

### Expresiones Complejas
```
sin(x) + 3 * cos(y)
2^(1/2)             → √2 ≈ 1.414
e^1                 → 2.718...
2*pi                → 6.283...
```

## 🔍 Fases del Compilador

### 1. Análisis Léxico (Lexer)
- Tokenización de la entrada
- Reconocimiento de palabras clave
- Identificación de números, operadores y símbolos
- Manejo de espacios en blanco

### 2. Análisis Sintáctico (Parser)
- Parser recursivo descendente
- Construcción del AST (Abstract Syntax Tree)
- Validación de la gramática
- Respeto de precedencia y asociatividad

### 3. Evaluación (Evaluator)
- Recorrido del AST (post-order)
- Evaluación de expresiones
- Manejo de variables con entorno
- Cálculo de resultado numérico

## 🧪 Casos de Prueba

El proyecto incluye 30 casos de prueba automatizados:
- 15 casos correctos (expresiones válidas)
- 15 casos erróneos (validación de errores)

```bash
# Ejecutar pruebas
java -cp target/classes com.unimag.tests.TestRunner
```

## ⚠️ Manejo de Errores

El sistema detecta y reporta:

- **Errores léxicos**: caracteres inválidos, números mal formados
- **Errores sintácticos**: expresiones mal estructuradas, paréntesis sin cerrar
- **Errores semánticos**: variables no definidas
- **Errores de ejecución**: división por cero

Cada error incluye:
- Posición del error
- Descripción clara del problema
- Token o elemento problemático

## 📚 Conceptos de Compiladores Implementados

- ✅ Análisis Léxico
- ✅ Análisis Sintáctico (Parser Recursivo Descendente)
- ✅ Gramática Libre de Contexto (LL(1))
- ✅ Árbol de Sintaxis Abstracta (AST)
- ✅ Evaluación de Expresiones
- ✅ Manejo de Precedencia y Asociatividad
- ✅ Detección y Reporte de Errores

## 📖 Notas Importantes

- Las funciones trigonométricas trabajan en **RADIANES**
- Para convertir grados a radianes: `grados × π / 180`
- Ejemplo: `sin(90° × π/180)` = `sin(pi/2)` = 1.0

## 📄 Licencia

Proyecto académico de código abierto para fines educativos.

---

**Universidad del Magdalena - 2025**
