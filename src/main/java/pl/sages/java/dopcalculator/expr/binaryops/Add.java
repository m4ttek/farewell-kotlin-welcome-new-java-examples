package pl.sages.java.dopcalculator.expr.binaryops;

import pl.sages.java.dopcalculator.expr.Expr;

public record Add(Expr l, Expr r) implements Expr.BinaryOperators {
}