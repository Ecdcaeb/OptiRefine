package mods.Hileb.optirefine.core;

import mods.Hileb.optirefine.OptiRefine;
import mods.Hileb.optirefine.library.fmlmodhacker.MetaDataDecoder;
import net.minecraftforge.fml.common.ModMetadata;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/** Metadata loading is independent of transformer and Mixin registration. */
public final class ModMetadataLoader {
    private ModMetadataLoader() {}

    public static ModMetadata load(File source) {
        if (source == null) {
            OptiRefineLog.log.warn("OptiRefine source is unavailable; using fallback metadata");
            return fallback();
        }
        try {
            ModMetadata metadata = null;
            if (source.isDirectory()) {
                try (InputStream input = Files.newInputStream(source.toPath().resolve("mcmod.info"))) {
                    metadata = MetaDataDecoder.decodeMcModInfo(input).get("optirefine");
                }
            } else if (source.isFile()) {
                try (JarFile jar = new JarFile(source)) {
                    JarEntry entry = jar.getJarEntry("mcmod.info");
                    if (entry != null) {
                        try (InputStream input = jar.getInputStream(entry)) {
                            metadata = MetaDataDecoder.decodeMcModInfo(input).get("optirefine");
                        }
                    }
                }
            }
            if (metadata != null) return metadata;
            OptiRefineLog.log.warn("No OptiRefine metadata found in {}", source);
        } catch (IOException | RuntimeException exception) {
            OptiRefineLog.log.error("Cannot read OptiRefine metadata from " + source, exception);
        }
        return fallback();
    }

    private static ModMetadata fallback() {
        ModMetadata metadata = new ModMetadata();
        metadata.modId = "optirefine";
        metadata.name = OptiRefine.NAME;
        metadata.authorList.add("Hileb");
        metadata.version = "Unknown";
        return metadata;
    }
}
