package rml.jrx.utils.file;

import com.google.common.io.ByteSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rml.jrx.announces.PublicAPI;
import rml.loader.api.mods.ContainerHolder;
import rml.loader.api.mods.module.Module;
import rml.loader.core.RMLFMLLoadingPlugin;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/14 23:23
 **/
@PublicAPI
public class FileHelper {
    @PublicAPI
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void findAssets(ContainerHolder containerHolder, Module module, ModFileConsumer consumer){
        if (module.moduleType.isFile){
            findFile(containerHolder, module, "assets/" + containerHolder.getContainer().getModId() + "/" + module.location, consumer);
        }else {
            findAssets(containerHolder, module, module.location, consumer);
        }
    }

    public static void findAssets(ContainerHolder containerHolder, @Nullable Module module, String base, ModFileConsumer consumer){
        findFiles(containerHolder, module, "assets/" + containerHolder.getContainer().getModId() + "/" + base, consumer);
    }

    public static void findFiles(ContainerHolder containerHolder, @Nullable Module module, String base, ModFileConsumer consumer)
    {
        final ModContainer mod = containerHolder.getContainer();

        if ("minecraft".equals(mod.getModId()))
        {
            return;
        }
        File source = mod.getSource();

        FileSystem fs = null;

        try
        {
            Path root = null;

            if (source.isFile())
            {
                try
                {
                    fs = FileSystems.newFileSystem(source.toPath(), (ClassLoader) null);
                    root = fs.getPath("/" + base);
                }
                catch (IOException e)
                {
                    RMLFMLLoadingPlugin.LOGGER.error("Error loading FileSystem from jar: ", e);
                    return;
                }
            }
            else if (source.isDirectory())
            {
                root = source.toPath().resolve(base);
            }

            if (root == null || !Files.exists(root))
                return;


            if (consumer != null)
            {
                Iterator<Path> itr;
                try
                {
                    itr = Files.walk(root).filter(Files::isReadable).iterator();
                }
                catch (IOException e)
                {
                    RMLFMLLoadingPlugin.LOGGER.error("Error iterating filesystem for: {}", mod.getModId(), e);
                    return;
                }

                while (itr.hasNext())
                {
                    consumer.accept(containerHolder, module, root, itr.next());
                }
            }
        }
        finally
        {
            IOUtils.closeQuietly(fs);
        }
    }

    @PublicAPI
    public static void findFile(ContainerHolder containerHolder, Module module, String base, ModFileConsumer processor) throws InvalidPathException
    {
        ModContainer mod = containerHolder.container;
        File source = mod.getSource();
        if ("minecraft".equals(mod.getModId()))
        {
            return;
        }
        if (source.isFile())
        {
            try
            {
                FileSystem fs = FileSystems.newFileSystem(source.toPath(), (ClassLoader) null);
                Path path;
                try{
                    path = fs.getPath("/" + base);
                }catch (InvalidPathException e){
                    return;
                }
                if (Files.isReadable(path)) processor.accept(containerHolder, module, path, path);
                IOUtils.closeQuietly(fs);
            }
            catch (IOException e) {
                RMLFMLLoadingPlugin.LOGGER.error("Error loading FileSystem from jar: ", e);
            }
        }
        else if (source.isDirectory())
        {
            Path path;
            try{
                path = source.toPath().resolve(base);
            } catch (InvalidPathException e){
                return;
            }
            if (Files.isReadable(path)) processor.accept(containerHolder, module, path, path);
        }
    }

    @PublicAPI
    public static byte[] findFile(ModContainer containerHolder, String base) {
        File source = containerHolder.getSource();
        if ("minecraft".equals(containerHolder.getModId()))
        {
            return null;
        }
        if (source.isFile())
        {
            try
            {
                FileSystem fs = FileSystems.newFileSystem(source.toPath(), (ClassLoader) null);
                Path path;
                byte[] toReturn;
                try{
                    path = fs.getPath("/" + base);
                    if (Files.isReadable(path)) toReturn = getBytes(path);
                    else return null;
                }catch (InvalidPathException e){
                    toReturn = null;
                }
                IOUtils.closeQuietly(fs);
                return toReturn;
            }
            catch (IOException e) {
                RMLFMLLoadingPlugin.LOGGER.error("Error loading FileSystem from jar: ", e);
            }
        }
        else if (source.isDirectory())
        {
            Path path;
            try{
                path = source.toPath().resolve(base);
                if (Files.isReadable(path)) return getBytes(path);
                else return null;
            } catch (InvalidPathException | IOException e){
                return null;
            }
        }
        return null;
    }

    @PublicAPI
    public static CharArrayReader getCachedFile(byte[] path) throws IOException {
        return new CharArrayReader(IOUtils.toCharArray(ByteSource.wrap(path).openBufferedStream(), StandardCharsets.UTF_8));
    }

    @PublicAPI
    public static CharArrayReader getCachedFile(Path path) throws IOException {
        return new CharArrayReader(IOUtils.toCharArray(Files.newBufferedReader(path)));
    }

    @PublicAPI
    public static ByteSource getByteSource(Path path, Charset charset) throws IOException {
        return ByteSource.wrap(IOUtils.toByteArray(Files.newBufferedReader(path), charset));
    }

    @PublicAPI
    public static ByteSource getByteSource(Path path) throws IOException {
        return ByteSource.wrap(IOUtils.toByteArray(Files.newBufferedReader(path), StandardCharsets.UTF_8));
    }

    @PublicAPI
    public static byte[] getBytes(Path path, Charset charset) throws IOException {
        return IOUtils.toByteArray(Files.newBufferedReader(path), charset);
    }

    @PublicAPI
    public static byte[] getBytes(Path path) throws IOException {
        return getBytes(path, StandardCharsets.UTF_8);
    }

    @FunctionalInterface
    public interface ModFileConsumer{
        void accept(ContainerHolder containerHolder, @Nullable Module module, Path root, Path file);
    }
}
