package rml.loader;

import com.google.common.eventbus.EventBus;
import rml.jrx.announces.PrivateAPI;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.InjectedModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/3 10:19
 **/
@PrivateAPI
public class RMLModContainer extends InjectedModContainer {
    public final Logger LOGGER;
    public RMLModContainer(ModMetadata metadata,File source) {
        super(new DummyModContainer(metadata), source);
        LOGGER = LogManager.getLogger(metadata.modId);
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }

    @Override
    public Class<?> getCustomResourcePackClass()
    {
        try
        {
            return getSource().isDirectory() ? Class.forName("net.minecraftforge.fml.client.FMLFolderResourcePack", true, getClass().getClassLoader()) : Class.forName("net.minecraftforge.fml.client.FMLFileResourcePack", true, getClass().getClassLoader());
        }
        catch (ClassNotFoundException e)
        {
            return null;
        }
    }
}
