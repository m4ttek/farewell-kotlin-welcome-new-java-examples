package pl.sages.java.dopcalculator.expr;

import pl.sages.java.dopcalculator.expr.binaryops.Add;
import pl.sages.java.dopcalculator.expr.binaryops.Div;
import pl.sages.java.dopcalculator.expr.binaryops.Mul;
import pl.sages.java.dopcalculator.expr.binaryops.Sub;
import pl.sages.java.dopcalculator.expr.constants.Constants;
import pl.sages.java.dopcalculator.expr.constants.DConst;
import pl.sages.java.dopcalculator.expr.constants.LConst;
import pl.sages.java.dopcalculator.utils.Either;

import java.util.function.BiFunction;

import static pl.sages.java.dopcalculator.expr.Expr.*;
import static pl.sages.java.dopcalculator.expr.constants.Constants.*;
import static pl.sages.java.dopcalculator.utils.Either.left;
import static pl.sages.java.dopcalculator.utils.Either.right;

public sealed interface Expr {

    // ADT

    sealed interface Const extends Expr permits LConst, DConst, Constants {
        double toDouble();
    }

    sealed interface BinaryOperators extends Expr permits Add, Sub, Mul, Div {
        Expr l();
        Expr r();
    }

    // DSL

    static Const $(long l) { return simplify(new LConst(l)); }
    static Const $(double d) { return simplify(new DConst(d)); }

    static BinaryOperators add(Expr l, Expr r) { return new Add(l, r); }
    static BinaryOperators sub(Expr l, Expr r) { return new Sub(l, r); }
    static BinaryOperators mul(Expr l, Expr r) { return new Mul(l, r); }
    static BinaryOperators div(Expr l, Expr r) { return new Div(l, r); }

    // Operations

    static Either<Throwable, Const> calculate(Expr expr) {
        Either<Throwable, Const> result = switch (expr) {
            case Add(LConst(long lLong), LConst(long rLong)) -> right($(lLong + rLong));
            case Add(Const l, Const r)                       -> right($(l.toDouble() + r.toDouble()));
            case Add(Expr l, Expr r)                         -> nestCalculate(l, r, Add::new);

            case Sub(LConst(long lLong), LConst(long rLong)) -> right($(lLong - rLong));
            case Sub(Const l, Const r)                       -> right($(l.toDouble() - r.toDouble()));
            case Sub(Expr l, Expr r)                         -> nestCalculate(l, r, Sub::new);

            case Mul(Const l, Const r)                       -> right($(l.toDouble() * r.toDouble()));
            case Mul(Expr l, Expr r)                         -> nestCalculate(l, r, Mul::new);

            case Div(_, Const r) when r.toDouble() == .0d    -> left(new ArithmeticException("Pamiętaj cholero, nie dziel przez zero"));
            case Div(Const l, Const r)                       -> right($(l.toDouble() / r.toDouble()));
            case Div(Expr l, Expr r)                         -> nestCalculate(l, r, Div::new);

            case Const c                                     -> right(c);
        };
        return result.mapRight(Expr::simplify);
    }

    static Either<Throwable, Const> nestCalculate(Expr l, Expr r, BiFunction<Expr, Expr, Expr> biOperator) {
        return switch (calculate(l)) {
            case Either.Left<Throwable, Const> err -> err;
            case Either.Right(Const calculatedLeft) -> switch (calculate(r)) {
                case Either.Left<Throwable, Const> err -> err;
                case Either.Right(Const calculatedRight) ->
                        calculate(biOperator.apply(calculatedLeft, calculatedRight));
            };
        };
    }

    static Const simplify(Const constant) {
        return switch (constant) {
            case Constants _ -> constant;
            case LConst(long num) -> switch (num) {
                case 0L -> ZERO;
                case 1L -> ONE;
                case 2L -> TWO;
                case 3L -> THREE;
                default -> constant;
            };
            case DConst(double num) -> switch (num) {
                case 0.d -> ZERO;
                case 1.d -> ONE;
                case 2.d -> TWO;
                case 3.d -> THREE;
                case long l -> new LConst(l);
                default -> constant;
            };
        };
    }
}

interface Tester {
    static void main() {
        var divExpr = div(Expr.mul(add(ZERO, ONE), sub(ZERO, $(.3d))), TWO);
        IO.println(calculate(divExpr));
        IO.println(calculate(div(divExpr, $(0))));
    }
}
