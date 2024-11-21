package rml.loader.api.event.early;


import rml.loader.api.RMLBus;
import rml.loader.core.GlobalTransformer;
import org.objectweb.asm.tree.ClassNode;

import java.util.function.ToIntFunction;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/4/13 10:46
 **/
public class RMLAddTransformerEvent {
    public Object transformer;
    public String target;
    public RMLAddTransformerEvent(String target, ToIntFunction<ClassNode> transformer){
        this.transformer = transformer;
        this.target = target;
    }
    public RMLAddTransformerEvent(GlobalTransformer globalTransformer){
        this.transformer = globalTransformer;
        this.target = null;
    }
    public static void post(GlobalTransformer transformer){
        RMLBus.BUS.post(new RMLAddTransformerEvent(transformer));
    }
    public static void post(String target, ToIntFunction<ClassNode> transformer){
        RMLBus.BUS.post(new RMLAddTransformerEvent(target, transformer));
    }
}
