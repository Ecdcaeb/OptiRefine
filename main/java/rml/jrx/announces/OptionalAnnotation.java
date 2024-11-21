package rml.jrx.announces;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import rml.layer.cleanroom.LaunchClassLoaderUtil;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/8/14 11:53
 **/
@PublicAPI
@EarlyClass
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value={CONSTRUCTOR, FIELD, METHOD, TYPE})
public @interface OptionalAnnotation {
    Class<?>[] value();

    class Handler{
        public static int handle(ClassNode classNode){
            handle(classNode.visibleAnnotations);
            classNode.methods.forEach(mn -> handle(mn.visibleAnnotations));
            classNode.fields.forEach(mn -> handle(mn.visibleAnnotations));
            return ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS;
        }

        public static void handle(List<AnnotationNode> anns){
            if (anns != null){
                Set<String> set = anns.stream().filter(node -> "Lrml/jrx/announces/OptionalAnnotation;".equals(node.desc))
                        .flatMap(
                                (Function<AnnotationNode, Stream<String>>)
                                        annotationNode -> annotationNode.values.stream().map(Type.class::cast).map(Type::getInternalName))
                        .filter(s -> LaunchClassLoaderUtil.getClassBytes(s) == null)
                        .map(s -> "L" + s + ";")
                        .collect(Collectors.toSet());

                anns.removeIf(annotationNode -> set.contains(annotationNode.desc));
            }
        }
    }
}
