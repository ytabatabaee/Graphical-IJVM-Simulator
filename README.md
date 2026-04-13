# Graphical IJVM Simulator

A JavaFX-based educational simulator for a simplified IJVM-like architecture.  
The project visualizes key CPU datapath and control signals while executing IJVM assembly programs step-by-step.

## Features

- Graphical view of CPU components (ALU, buses, registers, control signals, memory, and cache)
- Step-by-step execution of IJVM instructions
- Program loader for `.txt` assembly files
- Live views for:
  - Program listing
  - Stack
  - Local variables
  - Constant pool
  - Register values and control signals
- Cache simulation with selectable eviction policy:
  - FIFO
  - LRU
  - MRU
  - Random
  - LIP
- Cache metrics (hits, misses, hit rate), throughput, and utilization indicators

## Supported Instructions

The assembler/parser in this project supports:

- `NOP`
- `BIPUSH <byte>`
- `ILOAD <index>`
- `ISTORE <index>`
- `IADD`
- `ISUB`
- `GOTO <offset>`
- `IFEQ <offset>`
- `IFLT <offset>`
- `IF_ICMPEQ <offset>`
- `IINC <index> <const>`

## Project Structure

```text
src/
  main/
    java/              # Java source files
    assets/
      images/          # UI assets
      styles/          # UI style snippets
  examples/            # Sample IJVM programs (.txt)
```

## Requirements

- Java:
  - JDK 8 (includes JavaFX), or
  - JDK 11+ with OpenJFX installed and configured
- JavaFX runtime/module setup as needed by your JDK distribution

## Run

From the repository root:

```bash
javac src/main/java/*.java
java -cp src/main/java GUI
```

If your JDK requires explicit JavaFX modules, use `--module-path` and `--add-modules javafx.controls,javafx.graphics` for both `javac` and `java`.

## Using the Simulator

1. Launch the app.
2. Click **Input Code** and select a program from `src/examples/` (or your own `.txt` file in the same format).
3. Use **Step** to execute micro-operations/instructions progressively.
4. Use **Reset** to clear processor/memory state.
5. Optionally change cache eviction mode from the dropdown.

## Sample Programs

Example inputs are available in:

- `src/examples/test1.txt`
- `src/examples/test2.txt`
- `src/examples/test3.txt`
- `src/examples/test4.txt`
- `src/examples/test5.txt`
