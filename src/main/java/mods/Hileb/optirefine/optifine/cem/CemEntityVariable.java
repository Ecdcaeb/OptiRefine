package mods.Hileb.optirefine.optifine.cem;

import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.IExpressionFloat;

/** Compiled variable expressions hold names, never entity instances. */
public abstract class CemEntityVariable implements IExpression {
    protected final String name;

    private CemEntityVariable(String name) {
        this.name = name;
    }

    public static boolean isVariable(String name) {
        return name.startsWith("var.") || name.startsWith("varb.");
    }

    public static CemEntityVariable resolve(String name) {
        if (name.startsWith("var.") && name.length() > 4) {
            return new FloatVariable(name);
        }
        if (name.startsWith("varb.") && name.length() > 5) {
            return new BoolVariable(name);
        }
        return null;
    }

    public abstract void assign(IExpression expression);

    private static final class FloatVariable extends CemEntityVariable implements IExpressionFloat {
        private FloatVariable(String name) {
            super(name);
        }

        @Override
        public float eval() {
            CemVariableStore values = CemRenderContext.variables(false);
            return values == null ? 0 : values.get(name);
        }

        @Override
        public void assign(IExpression expression) {
            float value = ((IExpressionFloat) expression).eval();
            CemVariableStore values = CemRenderContext.variables(true);
            if (values != null) {
                values.set(name, value);
            }
        }
    }

    private static final class BoolVariable extends CemEntityVariable implements IExpressionBool {
        private BoolVariable(String name) {
            super(name);
        }

        @Override
        public boolean eval() {
            CemVariableStore values = CemRenderContext.variables(false);
            return values != null && values.get(name) != 0;
        }

        @Override
        public void assign(IExpression expression) {
            boolean value = ((IExpressionBool) expression).eval();
            CemVariableStore values = CemRenderContext.variables(true);
            if (values != null) {
                values.set(name, value ? 1 : 0);
            }
        }
    }
}
