package mods.Hileb.optirefine.core;

import mods.Hileb.optirefine.core.transformer.ConfigFacadeMapper;
import mods.Hileb.optirefine.core.transformer.OptiRefineConfigTransformer;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.nio.file.Path;
import java.util.jar.JarFile;

class RuntimeArtifactContractTest {
    @Test void packagedClassesRespectBootstrapAndRuntimeFacadeBoundaries() throws Exception {
        Path artifact = Path.of(System.getProperty("optirefine.contract.artifact"));
        OptiRefineConfigTransformer transformer = new OptiRefineConfigTransformer();
        try (JarFile jar = new JarFile(artifact.toFile())) {
            var entries = jar.entries();
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                String name = entry.getName();
                if (!name.startsWith("mods/Hileb/optirefine/") || !name.endsWith(".class")
                        || name.equals("mods/Hileb/optirefine/optifine/Config.class")) continue;
                byte[] bytes;
                try (var input = jar.getInputStream(entry)) { bytes = input.readAllBytes(); }
                if (name.startsWith("mods/Hileb/optirefine/core/") || name.startsWith("mods/Hileb/optirefine/library/")) {
                    ConfigFacadeMapper.verify(read(bytes));
                } else {
                    String type = name.substring(0, name.length() - 6).replace('/', '.');
                    ConfigFacadeMapper.verify(read(transformer.transform(type, type, bytes)));
                }
            }
        }
    }

    private static ClassNode read(byte[] bytes) {
        ClassNode node = new ClassNode(); new ClassReader(bytes).accept(node, 0); return node;
    }
}
