package mods.Hileb.optirefine.core.transformer;

import mods.Hileb.optirefine.library.foundationx.mini.MiniTransformer;
import mods.Hileb.optirefine.library.foundationx.mini.PatchContext;
import mods.Hileb.optirefine.library.foundationx.mini.annotation.Patch;
import org.objectweb.asm.tree.LabelNode;

import java.io.*;
import java.util.*;
import java.net.*;

import static mods.Hileb.optirefine.library.foundationx.ASMHelper.*;


@SuppressWarnings("unused") //ASM invoked
@Patch.Class("optifine.OptiFineClassTransformer")
public class OptifineTransformerTransformer extends MiniTransformer{

    @Patch.Method("transform(Ljava/lang/String;Ljava/lang/String;[B)[B")
    @Patch.Method.AffectsControlFlow
    public void patch$transform(PatchContext context){
        LabelNode labelNode = new LabelNode();
        context.jumpToStart();
        context.add(
                GETSTATIC("mods/Hileb/optirefine/core/OptiRefineBlackboard", "CLASSES", "Ljava/util/HashSet;"),
                ALOAD(2),
                INVOKEINTERFACE("java/util/Set", "contains", "(Ljava/lang/Object;)Z"),
                IFNE(labelNode),
                ALOAD(3),
                ARETURN(),
                labelNode
        );
    }

    @Patch.Method("<init>")
    public void patch$init(PatchContext context){
        PatchContext.SearchResult result = context.search(
            INVOKEVIRTUAL("java/net/URL", "toURI", "()Ljava/net/URI;")
        );
        while (result.isSuccessful()) {
            result.jumpBefore();
            result.erase();
            context.add(
                INVOKESTATIC("mods/Hileb/optirefine/core/transformer/OptifineTransformerTransformer", "url2uri", "(Ljava/net/URL;)Ljava/net/URI;")
            );
            result.next();
        }
    }

    public static URI url2uri(URL url) throws IOException, URISyntaxException {
        URLConnection connection = url.openConnection();
        if (connection instanceof JarURLConnection jarURLConnection) {
            return jarURLConnection.getJarFileURL().toURI();
        } else  {
            return url.toURI();
        }
    }


}
