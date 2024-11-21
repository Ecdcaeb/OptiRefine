package rml.loader.core;

import org.objectweb.asm.tree.ClassNode;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/3/13 12:53
 **/
public abstract class GlobalTransformer {
    public abstract boolean isTarget(ClassNode cn);
    public abstract int apply(ClassNode cn);
}
