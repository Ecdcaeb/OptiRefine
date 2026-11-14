package mods.Hileb.optirefine.core;

import net.minecraftforge.fml.common.ModMetadata;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class ModMetadataLoaderTest {
    @TempDir Path directory;

    @Test void directoryMetadataSelectsTheOptirefineEntry() throws Exception {
        Files.writeString(directory.resolve("mcmod.info"), "[{\"modid\":\"other\",\"name\":\"Other\"},{\"modid\":\"optirefine\",\"name\":\"Fixture\",\"version\":\"3.2.1\",\"authorList\":[\"Author\"]}]");
        ModMetadata metadata = ModMetadataLoader.load(directory.toFile());
        assertEquals("Fixture", metadata.name);
        assertEquals("3.2.1", metadata.version);
        assertEquals(java.util.List.of("Author"), metadata.authorList);
    }

    @Test void jarMetadataIsReadAndItsFileHandleIsReleased() throws Exception {
        Path jar = directory.resolve("mod.jar");
        try (JarOutputStream output = new JarOutputStream(Files.newOutputStream(jar))) {
            output.putNextEntry(new JarEntry("mcmod.info"));
            output.write("[{\"modid\":\"optirefine\",\"name\":\"Jar Fixture\",\"version\":\"2\"}]".getBytes(StandardCharsets.UTF_8));
            output.closeEntry();
        }
        assertEquals("Jar Fixture", ModMetadataLoader.load(jar.toFile()).name);
        Files.delete(jar);
    }

    @Test void missingAndMalformedMetadataKeepTheModIdentifiable() throws Exception {
        ModMetadata missing = ModMetadataLoader.load(directory.toFile());
        Files.writeString(directory.resolve("mcmod.info"), "{not-json");
        ModMetadata malformed = ModMetadataLoader.load(directory.toFile());
        for (ModMetadata metadata : java.util.List.of(missing, malformed, ModMetadataLoader.load(null))) {
            assertEquals("optirefine", metadata.modId);
            assertEquals("OptiRefine", metadata.name);
            assertEquals("Unknown", metadata.version);
        }
    }
}
