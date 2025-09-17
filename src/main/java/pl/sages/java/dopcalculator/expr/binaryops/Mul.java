package pl.sages.java.dopcalculator.expr.binaryops;

import pl.sages.java.dopcalculator.expr.Expr;

public record Mul(Expr l, Expr r) implements Expr.BinaryOperators {
}