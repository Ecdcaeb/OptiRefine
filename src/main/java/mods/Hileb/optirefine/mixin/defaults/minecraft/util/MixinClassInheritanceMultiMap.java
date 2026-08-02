package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import mods.Hileb.optirefine.library.common.utils.Caster;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.optifine.util.IteratorCache;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
@Mixin(ClassInheritanceMultiMap.class)
public abstract class MixinClassInheritanceMultiMap<T> extends AbstractSet<T> {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
// [AUDIT-OK] OF-added field empty (in OF, not in baseline; public in OF)
    public boolean empty;

    @Shadow @Final
// [AUDIT-OK] baseline member values exists in target class
    private List<T> values;

    @Inject(method = "<init>", at = @At("RETURN"))
// [AUDIT-OK] empty-flag refresh on <init> matches OF (empty = values.isEmpty())
    public void init$checkEmpty(Class<?> p_i45909_1_, CallbackInfo ci){
        this.empty = this.values.isEmpty();
    }

    @Inject(method = "add", at = @At("RETURN"))
// [AUDIT-OK] empty-flag refresh on add matches OF; CIR correct for non-void add
    public void add$checkEmpty(Object p_add_1_, CallbackInfoReturnable<Boolean> cir){
        this.empty = this.values.isEmpty();
    }

    @Inject(method = "addForClass", at = @At("RETURN"))
// [AUDIT-OK] empty-flag refresh on addForClass matches OF
    public void addForClass$checkEmpty(Object p_181743_1_, Class<?> p_181743_2_, CallbackInfo ci){
        this.empty = this.values.isEmpty();
    }

    @Inject(method = "remove", at = @At("RETURN"))
// [AUDIT-OK] empty-flag refresh on remove matches OF; CIR correct for non-void remove
    public void remove$checkEmpty(Object p_add_1_, CallbackInfoReturnable<Boolean> cir){
        this.empty = this.values.isEmpty();
    }

    @Override
// [AUDIT-OK] isEmpty override matches OF (returns empty); target does not declare isEmpty -> plain merge is fine
    public boolean isEmpty() {
        return this.empty;
    }

    /**
     * @author Hileb
     * @reason so
     */
    @Overwrite
    @Nonnull
// [AUDIT-OK] @Overwrite matches OF iterator (emptyIterator / IteratorCache.getReadOnly); @Overwrite required - target declares iterator
    public Iterator<T> iterator() {
        return this.values.isEmpty() ? Collections.emptyIterator() : Caster.cast(IteratorCache.getReadOnly(this.values));
    }

}
