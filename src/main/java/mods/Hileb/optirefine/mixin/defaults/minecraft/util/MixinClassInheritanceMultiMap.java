package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean empty;

    @Shadow @Final
    private List<T> values;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void inject_empty0(Class<?> p_i45909_1_, CallbackInfo ci){
        this.empty = this.values.isEmpty();
    }

    @Inject(method = "add", at = @At("RETURN"))
    public void inject_empty1(Object p_add_1_, CallbackInfoReturnable<Boolean> cir){
        this.empty = this.values.isEmpty();
    }

    @Inject(method = "addForClass", at = @At("RETURN"))
    public void inject_empty2(Object p_181743_1_, Class<?> p_181743_2_, CallbackInfo ci){
        this.empty = this.values.isEmpty();
    }

    @Inject(method = "remove", at = @At("RETURN"))
    public void inject_empty3(Object p_add_1_, CallbackInfoReturnable<Boolean> cir){
        this.empty = this.values.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return this.empty;
    }

    /**
     * @author Hileb
     * @reason so
     */
    @Overwrite
    @Nonnull
    public Iterator<T> iterator() {
        return this.values.isEmpty() ? Collections.emptyIterator() : castIterator(IteratorCache.getReadOnly(this.values));
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @AccessibleOperation()
    private static<E, V> Iterator<E> castIterator(Iterator<V> i) {
        throw new AbstractMethodError();
    }

}
