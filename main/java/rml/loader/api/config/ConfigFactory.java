package rml.loader.api.config;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.IOException;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/5/12 10:28
 *
 * A demo Config Factory using .cfg.
 **/
public class ConfigFactory {
    public static final File canonicalConfigDir;

    static {
        try {
            canonicalConfigDir = new File(Launch.minecraftHome, "config").getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException("RML could not launch ConfigFactory! Something wrong when accessing canonicalConfigDir!", e);
        }
    }

    public static Configuration addConfig(String name, String owner, byte[] define){
        File file = new File(canonicalConfigDir, name + ".cfg");
        Configuration configuration = new Configuration(file); // create
        ConfigPatcher.Cfg.process(configuration, define, Configuration.DEFAULT_ENCODING); // define
        ConfigPatcher.transformConfigRedefalut(configuration, file.getName()); // run redefault
        configuration.load(); // load from file
        ConfigPatcher.transformConfigOverride(configuration, file.getName()); //run overrides
        configuration.save(); // save it
        return configuration;
    }
}
