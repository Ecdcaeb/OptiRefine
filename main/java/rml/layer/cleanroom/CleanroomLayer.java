package rml.layer.cleanroom;

import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/8/2 18:44
 **/
public class CleanroomLayer {
    public static final ArtifactVersion CURRENT_JAVA = new DefaultArtifactVersion(System.getenv().get("java.specification.version"));
    public static final ArtifactVersion JAVA_8 = new DefaultArtifactVersion("1.8");
    public static boolean isRunningOnCleanroom(){
        return CURRENT_JAVA.compareTo(JAVA_8) > 0; // Only Cleanroom could run on higher java version.
    }
}
