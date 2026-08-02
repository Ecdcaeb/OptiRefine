package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;


import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.*;
@Mixin(SetVisibility.class)
public abstract class MixinSetVisibility {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @Shadow @Final
    // [AUDIT-OK] baseline static final COUNT_FACES (deobf)
    private static int COUNT_FACES;

    // [AUDIT-OK] OF-added field (OF 'private long bits') — renamed to avoid shadow collision; vanilla BitSet field stays unused
    private long optiRefine$bits;

    /**
     * @author
     * @reason
     */
    @Overwrite
    // [AUDIT-OK] @Overwrite (name-collision route, AGENT.md §4) — long-bitset semantics identical to OF SetVisibility; setManyVisible/toString still function via the overwritten methods
    public void setVisible(EnumFacing facing, EnumFacing facing2, boolean p_178619_3_) {
        this.optiRefine$setBit(facing.ordinal() + facing2.ordinal() * COUNT_FACES, p_178619_3_);
        this.optiRefine$setBit(facing2.ordinal() + facing.ordinal() * COUNT_FACES, p_178619_3_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    // [AUDIT-OK] all-bits set/clear matches OF:30-35 (vanilla set 0..63 of BitSet == 64 bits)
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
    // [AUDIT-OK] bit check matches OF
    public boolean isVisible(EnumFacing facing, EnumFacing facing2) {
        return this.optiRefine$getBit(facing.ordinal() + facing2.ordinal() * COUNT_FACES);
    }

    @Unique
    private boolean optiRefine$getBit(int i) {
        return (this.optiRefine$bits & 1L << i) != 0L;
    }

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
