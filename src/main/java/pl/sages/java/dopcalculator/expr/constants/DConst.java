package pl.sages.java.dopcalculator.expr.constants;

import pl.sages.java.dopcalculator.expr.Expr;

public record DConst(double a) implements Expr.Const {

    @Override
    public double toDouble() {
        return a;
    }
}