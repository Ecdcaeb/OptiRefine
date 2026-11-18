package mods.Hileb.optirefine.optifine.cem;

import net.minecraft.client.model.ModelRenderer;
import net.optifine.expr.IExpressionBool;

public record CemPartVisibility(ModelRenderer part, boolean boxesOnly) implements IExpressionBool {
    public interface Part {
        boolean optiRefine$areBoxesVisible();
        void optiRefine$setBoxesVisible(boolean visible);
        boolean optiRefine$isPartVisible();
        void optiRefine$setPartVisible(boolean visible);
        void optiRefine$updateHiddenBranch();
    }

    public static boolean matches(String name) {
        int separator = name.indexOf('.');
        return separator > 0 && separator == name.lastIndexOf('.') && !CemEntityVariable.isVariable(name)
                && (name.endsWith(".visible") || name.endsWith(".visible_boxes"));
    }

    @Override
    public boolean eval() {
        Part state = (Part) part;
        return boxesOnly ? state.optiRefine$areBoxesVisible() : state.optiRefine$isPartVisible();
    }

    public void set(boolean visible) {
        Part state = (Part) part;
        if (boxesOnly) {
            state.optiRefine$setBoxesVisible(visible);
        } else {
            state.optiRefine$setPartVisible(visible);
        }
    }
}
