package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import mods.Hileb.optirefine.library.common.utils.Checked;
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

@Checked
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

/*
--- net/minecraft/util/ClassInheritanceMultiMap.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/util/ClassInheritanceMultiMap.java	Tue Aug 19 14:59:58 2025
@@ -7,36 +7,43 @@
 import java.util.AbstractSet;
 import java.util.Collections;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;
+import java.util.concurrent.ConcurrentHashMap;
+import net.optifine.util.IteratorCache;

 public class ClassInheritanceMultiMap<T> extends AbstractSet<T> {
-   private static final Set<Class<?>> ALL_KNOWN = Sets.newHashSet();
+   private static final Set<Class<?>> ALL_KNOWN = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Map<Class<?>, List<T>> map = Maps.newHashMap();
    private final Set<Class<?>> knownKeys = Sets.newIdentityHashSet();
    private final Class<T> baseClass;
    private final List<T> values = Lists.newArrayList();
+   public boolean empty;

    public ClassInheritanceMultiMap(Class<T> var1) {
       this.baseClass = var1;
       this.knownKeys.add(var1);
       this.map.put(var1, this.values);

       for (Class var3 : ALL_KNOWN) {
          this.createLookup(var3);
       }
+
+      this.empty = this.values.size() == 0;
    }

    protected void createLookup(Class<?> var1) {
       ALL_KNOWN.add(var1);
+      int var2 = this.values.size();

-      for (Object var3 : this.values) {
-         if (var1.isAssignableFrom(var3.getClass())) {
-            this.addForClass((T)var3, var1);
+      for (int var3 = 0; var3 < var2; var3++) {
+         Object var4 = this.values.get(var3);
+         if (var1.isAssignableFrom(var4.getClass())) {
+            this.addForClass((T)var4, var1);
          }
       }

       this.knownKeys.add(var1);
    }

@@ -56,22 +63,25 @@
       for (Class var3 : this.knownKeys) {
          if (var3.isAssignableFrom(var1.getClass())) {
             this.addForClass((T)var1, var3);
          }
       }

+      this.empty = this.values.size() == 0;
       return true;
    }

    private void addForClass(T var1, Class<?> var2) {
       List var3 = this.map.get(var2);
       if (var3 == null) {
          this.map.put(var2, Lists.newArrayList(new Object[]{var1}));
       } else {
          var3.add(var1);
       }
+
+      this.empty = this.values.size() == 0;
    }

    public boolean remove(Object var1) {
       Object var2 = var1;
       boolean var3 = false;

@@ -81,12 +91,13 @@
             if (var6 != null && var6.remove(var2)) {
                var3 = true;
             }
          }
       }

+      this.empty = this.values.size() == 0;
       return var3;
    }

    public boolean contains(Object var1) {
       return Iterators.contains(this.getByClass(var1.getClass()).iterator(), var1);
    }
@@ -103,13 +114,17 @@
             }
          }
       };
    }

    public Iterator<T> iterator() {
-      return (Iterator<T>)(this.values.isEmpty() ? Collections.emptyIterator() : Iterators.unmodifiableIterator(this.values.iterator()));
+      return this.values.isEmpty() ? Collections.emptyIterator() : IteratorCache.getReadOnly(this.values);
    }

    public int size() {
       return this.values.size();
+   }
+
+   public boolean isEmpty() {
+      return this.empty;
    }
 }
 */
