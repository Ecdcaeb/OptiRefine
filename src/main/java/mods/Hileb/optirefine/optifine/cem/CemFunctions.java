package mods.Hileb.optirefine.optifine.cem;

import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.IExpressionFloat;
import net.optifine.expr.ParseException;

/** Additions confirmed against J9 FunctionType; G5 retains all existing operators/functions. */
public final class CemFunctions {
    private CemFunctions() {}

    public static boolean handles(String name) {
        return "lerp".equals(name) || "ifb".equals(name) || "random".equals(name)
                || "day_time".equals(name) || "day_count".equals(name);
    }

    public static boolean permitsBare(String name) {
        return "random".equals(name) || "day_time".equals(name) || "day_count".equals(name);
    }

    public static IExpression create(String name, IExpression[] args) throws ParseException {
        if ("ifb".equals(name)) {
            if (args.length < 3 || (args.length & 1) == 0) {
                throw new ParseException("Invalid number of arguments, function: ifb, expected condition/value pairs and an else value");
            }
            for (int i = 0; i < args.length; ++i) {
                requireType(name, args, i, ExpressionType.BOOL);
            }
            return new IfBool(args);
        }
        if ("lerp".equals(name)) {
            requireCount(name, args, 3);
            for (int i = 0; i < 3; ++i) {
                requireType(name, args, i, ExpressionType.FLOAT);
            }
            return new Lerp(args);
        }
        if ("random".equals(name)) {
            if (args.length > 1) {
                throw new ParseException("Invalid number of arguments, function: random, expected zero or one");
            }
            if (args.length == 1) {
                requireType(name, args, 0, ExpressionType.FLOAT);
                return new SeededRandom((IExpressionFloat) args[0]);
            }
            return UnseededRandom.INSTANCE;
        }
        requireCount(name, args, 0);
        if ("day_time".equals(name)) {
            return CemRenderParameter.DAY_TIME;
        }
        if ("day_count".equals(name)) {
            return CemRenderParameter.DAY_COUNT;
        }
        throw new ParseException("Unknown function: " + name);
    }

    private static void requireCount(String name, IExpression[] args, int count) throws ParseException {
        if (args.length != count) {
            throw new ParseException("Invalid number of arguments, function: " + name + ", expected: " + count);
        }
    }

    private static void requireType(String name, IExpression[] args, int index, ExpressionType type) throws ParseException {
        if (args[index].getExpressionType() != type) {
            throw new ParseException("Invalid argument type, function: " + name + ", index: " + index + ", expected: " + type);
        }
    }

    public static float seededRandom(float seed) {
        int value = Float.floatToIntBits(seed);
        value = (value ^ 61) ^ (value >> 16);
        value += value << 3;
        value ^= value >> 4;
        value *= 668265261;
        value ^= value >> 15;
        return (float) Math.abs(value) / 2.1474836E9F;
    }

    private static final class Lerp implements IExpressionFloat {
        private final IExpressionFloat k;
        private final IExpressionFloat x;
        private final IExpressionFloat y;

        private Lerp(IExpression[] args) {
            k = (IExpressionFloat) args[0];
            x = (IExpressionFloat) args[1];
            y = (IExpressionFloat) args[2];
        }

        @Override
        public float eval() {
            float factor = k.eval();
            float from = x.eval();
            float to = y.eval();
            return from + factor * (to - from);
        }
    }

    private static final class IfBool implements IExpressionBool {
        private final IExpression[] args;

        private IfBool(IExpression[] args) {
            this.args = args;
        }

        @Override
        public boolean eval() {
            for (int i = 0; i < args.length - 1; i += 2) {
                if (((IExpressionBool) args[i]).eval()) {
                    return ((IExpressionBool) args[i + 1]).eval();
                }
            }
            return ((IExpressionBool) args[args.length - 1]).eval();
        }
    }

    private static final class SeededRandom implements IExpressionFloat {
        private final IExpressionFloat seed;

        private SeededRandom(IExpressionFloat seed) {
            this.seed = seed;
        }

        @Override
        public float eval() {
            return seededRandom(seed.eval());
        }
    }

    private enum UnseededRandom implements IExpressionFloat {
        INSTANCE;

        @Override
        public float eval() {
            return (float) Math.random();
        }
    }
}
