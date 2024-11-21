package rml.layer.compat.groovyscripts;

import groovy.lang.GroovyClassLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/8/21 12:52
 **/
public class NamedScript {
    private ResourceLocation name;
    private byte[] file;
    private Class<?> clazz;
    public NamedScript(ResourceLocation name, byte[] file){
        this.name = name;
        this.file = file;
    }

    public ResourceLocation getName() {
        return name;
    }

    public byte[] getFile() {
        return file;
    }

    public Class<?> compile(String name, GroovyClassLoader groovyClassLoader){
        if (clazz != null){
            return clazz;
        }else return clazz = groovyClassLoader.parseClass(new String(this.getFile()), name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamedScript)) return false;
        NamedScript that = (NamedScript) o;
        return Objects.equals(name, that.name) && Arrays.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(file);
        return result;
    }
}
