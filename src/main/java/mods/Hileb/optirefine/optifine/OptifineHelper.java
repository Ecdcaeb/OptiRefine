package mods.Hileb.optirefine.optifine;



import optifine.OptiFineClassTransformer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class OptifineHelper {
    private static final OptiFineClassTransformer optifineClassTransformer = new OptiFineClassTransformer();
    public static InputStream getOptifineResource(String path){
        byte[] d = optifineClassTransformer.getOptiFineResource(path);
        if (d == null) return null;
        else return new ByteArrayInputStream(d);
    }
}
