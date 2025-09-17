package pl.sages.java.dopcalculator.expr.constants;

import pl.sages.java.dopcalculator.expr.Expr;

public record LConst(long a) implements Expr.Const {

    @Override
    public double toDouble() {
        return a;
    }
}