package mods.Hileb.optirefine;

import mods.Hileb.optirefine.library.fmlmodhacker.MetaDataDecoder;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class OptiRefine {
    public static final String NAME = "OptiRefine";
    public static final ModMetadata MOD_METADATA = MetaDataDecoder.decodeMcModInfo(OptiRefine.class.getResourceAsStream("mcmod.info")).get("optirefine");
    public static final Logger LOGGER = LogManager.getLogger(NAME);
}