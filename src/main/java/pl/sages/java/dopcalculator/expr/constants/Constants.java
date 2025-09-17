package pl.sages.java.dopcalculator.expr.constants;

import pl.sages.java.dopcalculator.expr.Expr;

public enum Constants implements Expr.Const {
    ZERO, ONE, TWO, THREE;

    public double toDouble() {
        return ordinal();
    }
}