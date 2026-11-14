package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import mods.Hileb.optirefine.optifine.cem.CemVariableStore;
import mods.Hileb.optirefine.optifine.cem.EntityCemVariables;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class MixinEntityCemVariables implements EntityCemVariables {
    @Unique private CemVariableStore optiRefine$cemValues;
    @Unique private int optiRefine$cemGeneration;

    @Override
    public CemVariableStore optiRefine$cemVariables(boolean create) {
        int generation = CemRenderContext.resourceGeneration();
        if (optiRefine$cemGeneration != generation) {
            optiRefine$cemValues = null;
            optiRefine$cemGeneration = generation;
        }
        if (create && optiRefine$cemValues == null) {
            optiRefine$cemValues = new CemVariableStore();
        }
        return optiRefine$cemValues;
    }
}
