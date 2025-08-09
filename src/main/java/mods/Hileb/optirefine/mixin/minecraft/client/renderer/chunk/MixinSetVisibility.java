package mods.Hileb.optirefine.mixin.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.*;

@Mixin(SetVisibility.class)
public abstract class MixinSetVisibility {

    @Shadow @Final
    private static int COUNT_FACES;

    @Unique
    private long optiRefine$bits;

    @Unique
    public void setVisible(EnumFacing facing, EnumFacing facing2, boolean p_178619_3_) {
        this.optiRefine$setBit(facing.ordinal() + facing2.ordinal() * COUNT_FACES, p_178619_3_);
        this.optiRefine$setBit(facing2.ordinal() + facing.ordinal() * COUNT_FACES, p_178619_3_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setAllVisible(boolean visible) {
        if (visible) {
            this.optiRefine$bits = -1L;
        } else {
            this.optiRefine$bits = 0L;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isVisible(EnumFacing facing, EnumFacing facing2) {
        return this.optiRefine$getBit(facing.ordinal() + facing2.ordinal() * COUNT_FACES);
    }

    @Unique
    private boolean optiRefine$getBit(int i) {
        return (this.optiRefine$bits & 1L << i) != 0L;
    }

    @Unique
    private void optiRefine$setBit(int i, boolean on) {
        if (on) {
            this.optiRefine$setBit(i);
        } else {
            this.optiRefine$clearBit(i);
        }
    }

    @Unique
    private void optiRefine$setBit(int i) {
        this.optiRefine$bits |= 1L << i;
    }

    @Unique
    private void optiRefine$clearBit(int i) {
        this.optiRefine$bits &= ~(1L << i);
    }
}
