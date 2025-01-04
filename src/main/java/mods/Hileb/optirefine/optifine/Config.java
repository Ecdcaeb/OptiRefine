package mods.Hileb.optirefine.optifine;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.optifine.config.GlVersion;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;

/**
 * Redirect to "Config"
 */
public class Config {
    public static final String OF_NAME = "OptiFine";
    public static final String MC_VERSION = "1.12.2";
    public static final String OF_EDITION = "HD_U";
    public static final String OF_RELEASE = "G5";
    public static final String VERSION = "OptiFine_1.12.2_HD_U_G5";
    public static String openGlVersion = null;
    public static String openGlRenderer = null;
    public static String openGlVendor = null;
    public static String[] openGlExtensions = null;
    public static GlVersion glVersion = null;
    public static GlVersion glslVersion = null;
    public static int minecraftVersionInt = -1;
    public static boolean fancyFogAvailable = false;
    public static boolean occlusionAvailable = false;
    public static boolean zoomMode = false;
    public static boolean zoomSmoothCamera = false;
    public static boolean waterOpacityChanged = false;
    public static final Float DEF_ALPHA_FUNC_LEVEL = 0.1F;
    public static final boolean logDetail = false;

    private Config() {
    }

    public native static String getVersion();

    public native static String getVersionDebug() ;

    public native static void initGameSettings(GameSettings settings) ;

    public native static void initDisplay() ;

    public native static void checkInitialized() ;

    public native static String getBuild() ;

    public native static boolean isFancyFogAvailable() ;

    public native static boolean isOcclusionAvailable() ;

    public native static int getMinecraftVersionInt() ;

    public native static String getOpenGlVersionString() ;

    public native static GlVersion getGlVersion() ;

    public native static GlVersion getGlslVersion() ;

    public native static GlVersion parseGlVersion(String versionString, GlVersion def) ;

    public native static String[] getOpenGlExtensions() ;

    public native static void updateThreadPriorities() ;

    public native static boolean isMinecraftThread() ;

    public native static boolean isMipmaps() ;

    public native static int getMipmapLevels() ;

    public native static int getMipmapType() ;

    public native static boolean isUseAlphaFunc() ;

    public native static float getAlphaFuncLevel() ;

    public native static boolean isFogFancy() ;

    public native static boolean isFogFast() ;

    public native static boolean isFogOff() ;

    public native static boolean isFogOn() ;

    public native static float getFogStart() ;

    public native static void detail(String s) ;

    public native static void dbg(String s) ;

    public native static void warn(String s) ;

    public native static void error(String s) ;

    public native static void log(String s) ;

    public native static int getUpdatesPerFrame() ;

    public native static boolean isDynamicUpdates() ;

    public native static boolean isRainFancy() ;

    public native static boolean isRainOff() ;

    public native static boolean isCloudsFancy() ;

    public native static boolean isCloudsOff() ;

    public native static void updateTexturePackClouds() ;

    public native static ModelManager getModelManager() ;

    public native static boolean isTreesFancy() ;

    public native static boolean isTreesSmart() ;

    public native static boolean isCullFacesLeaves() ;

    public native static boolean isDroppedItemsFancy() ;

    public native static int limit(int val, int min, int max) ;

    public native static float limit(float val, float min, float max) ;

    public native static double limit(double val, double min, double max) ;

    public native static float limitTo1(float val) ;

    public native static boolean isAnimatedWater() ;

    public native static boolean isGeneratedWater() ;

    public native static boolean isAnimatedPortal() ;

    public native static boolean isAnimatedLava() ;

    public native static boolean isGeneratedLava() ;

    public native static boolean isAnimatedFire() ;

    public native static boolean isAnimatedRedstone();

    public native static boolean isAnimatedExplosion() ;

    public native static boolean isAnimatedFlame() ;

    public native static boolean isAnimatedSmoke() ;

    public native static boolean isVoidParticles() ;

    public native static boolean isWaterParticles() ;

    public native static boolean isRainSplash() ;

    public native static boolean isPortalParticles() ;

    public native static boolean isPotionParticles() ;

    public native static boolean isFireworkParticles() ;

    public native static float getAmbientOcclusionLevel() ;

    public native static String listToString(List list) ;

    public native static String listToString(List list, String separator) ;

    public native static String arrayToString(Object[] arr) ;

    public native static String arrayToString(Object[] arr, String separator) ;

    public native static String arrayToString(int[] arr) ;

    public native static String arrayToString(int[] arr, String separator) ;

    public native static String arrayToString(float[] arr) ;

    public native static String arrayToString(float[] arr, String separator) ;

    public native static Minecraft getMinecraft() ;

    public native static TextureManager getTextureManager() ;

    public native static IResourceManager getResourceManager() ;

    public native static InputStream getResourceStream(ResourceLocation location) throws IOException ;

    public native static InputStream getResourceStream(IResourceManager resourceManager, ResourceLocation location) throws IOException ;

    public native static boolean hasResource(ResourceLocation location);

    public native static boolean hasResource(IResourceManager resourceManager, ResourceLocation location) ;

    public native static IResourcePack[] getResourcePacks() ;

    public native static String getResourcePackNames() ;

    public native static DefaultResourcePack getDefaultResourcePack() ;

    public native static boolean isFromDefaultResourcePack(ResourceLocation loc) ;

    public native static IResourcePack getDefiningResourcePack(ResourceLocation location) ;

    public native static RenderGlobal getRenderGlobal();

    public native static boolean isBetterGrass();
    public native static boolean isBetterGrassFancy();

    public native static boolean isWeatherEnabled() ;
    public native static boolean isSkyEnabled() ;
    public native static boolean isSunMoonEnabled() ;
    public native static boolean isSunTexture() ;

    public native static boolean isMoonTexture() ;

    public native static boolean isVignetteEnabled() ;

    public native static boolean isStarsEnabled() ;

    public native static void sleep(long ms) ;

    public native static boolean isTimeDayOnly() ;

    public native static boolean isTimeDefault();

    public native static boolean isTimeNightOnly();

    public native static boolean isClearWater() ;

    public native static int getAnisotropicFilterLevel() ;

    public native static boolean isAnisotropicFiltering() ;

    public native static int getAntialiasingLevel() ;

    public native static boolean isAntialiasing() ;

    public native static boolean isAntialiasingConfigured();

    public native static boolean isMultiTexture() ;

    public native static boolean between(int val, int min, int max) ;

    public native static boolean between(float val, float min, float max) ;

    public native static boolean isDrippingWaterLava() ;

    public native static boolean isBetterSnow() ;
    public native static Dimension getFullscreenDimension();

    public native static int parseInt(String str, int defVal) ;

    public native static float parseFloat(String str, float defVal) ;

    public native static boolean parseBoolean(String str, boolean defVal) ;

    public native static Boolean parseBoolean(String str, Boolean defVal);

    public native static String[] tokenize(String str, String delim) ;

    public native static DisplayMode getDesktopDisplayMode();

    public native static DisplayMode[] getDisplayModes() ;

    public native static DisplayMode getLargestDisplayMode() ;

    public native static String[] getDisplayModeNames() ;

    public native static DisplayMode getDisplayMode(Dimension dim) throws LWJGLException ;

    public native static boolean isAnimatedTerrain() ;
    public native static boolean isAnimatedTextures() ;

    public native static boolean isSwampColors();

    public native static boolean isRandomEntities();

    public native static void checkGlError(String loc) ;
    public native static boolean isSmoothBiomes() ;

    public native static boolean isCustomColors() ;

    public native static boolean isCustomSky() ;

    public native static boolean isCustomFonts() ;

    public native static boolean isShowCapes() ;

    public native static boolean isConnectedTextures() ;

    public native static boolean isNaturalTextures() ;

    public native static boolean isEmissiveTextures() ;

    public native static boolean isConnectedTexturesFancy() ;

    public native static boolean isFastRender();

    public native static boolean isTranslucentBlocksFancy();

    public native static boolean isShaders() ;

    public native static String[] readLines(File file) throws IOException;

    public native static String[] readLines(InputStream is) throws IOException ;

    public native static String readFile(File file) throws IOException ;

    public native static String readInputStream(InputStream in) throws IOException;

    public native static String readInputStream(InputStream in, String encoding) throws IOException ;

    public native static byte[] readAll(InputStream is) throws IOException ;

    public native static GameSettings getGameSettings() ;

    public native static String getNewRelease() ;

    public native static void setNewRelease(String newRelease) ;

    public native static int compareRelease(String rel1, String rel2) ;

    public native static int intHash(int x) ;

    public native static int getRandom(BlockPos blockPos, int face) ;

    public native static int getAvailableProcessors() ;

    public native static void updateAvailableProcessors();

    public native static boolean isSingleProcessor() ;

    public native static boolean isSmoothWorld() ;
    public native static boolean isLazyChunkLoading() ;
    public native static boolean isDynamicFov();
    public native static boolean isAlternateBlocks();

    public native static int getChunkViewDistance() ;

    public native static boolean equals(Object o1, Object o2);

    public native static boolean equalsOne(Object a, Object[] bs) ;

    public native static boolean equalsOne(int val, int[] vals) ;

    public native static boolean isSameOne(Object a, Object[] bs) ;

    public native static String normalize(String s) ;

    public native static void checkDisplaySettings() ;

    public native static void checkDisplayMode();

    public native static void updateFramebufferSize() ;

    public native static Object[] addObjectToArray(Object[] arr, Object obj);

    public native static Object[] addObjectToArray(Object[] arr, Object obj, int index) ;

    public native static Object[] addObjectsToArray(Object[] arr, Object[] objs);

    public native static Object[] removeObjectFromArray(Object[] arr, Object obj) ;

    public native static Object[] collectionToArray(Collection coll, Class elementClass) ;

    public native static boolean isCustomItems() ;

    public native static void drawFps();

    public native static int getFpsMin() ;

    public native static int getBitsOs() ;

    public native static int getBitsJre();

    public native static boolean isNotify64BitJava() ;

    public native static void setNotify64BitJava(boolean flag) ;

    public native static boolean isConnectedModels();

    public native static void showGuiMessage(String line1, String line2) ;

    public native static int[] addIntToArray(int[] intArray, int intValue);

    public native static int[] addIntsToArray(int[] intArray, int[] copyFrom) ;

    public native static DynamicTexture getMojangLogoTexture(DynamicTexture texDefault);

    public native static void writeFile(File file, String str) throws IOException ;

    public native static TextureMap getTextureMap() ;

    public native static boolean isDynamicLights();

    public native static boolean isDynamicLightsFast();

    public native static boolean isDynamicHandLight();

    public native static boolean isCustomEntityModels() ;

    public native static boolean isCustomGuis() ;

    public native static int getScreenshotSize() ;

    public native static int[] toPrimitive(Integer[] arr) ;

    public native static boolean isRenderRegions() ;

    public native static boolean isVbo();

    public native static boolean isSmoothFps();

    public native static boolean openWebLink(URI uri) ;

    public native static boolean isShowGlErrors() ;

    public native static String arrayToString(boolean[] arr, String separator) ;

    public native static boolean isIntegratedServerRunning() ;

    public native static IntBuffer createDirectIntBuffer(int capacity) ;

    public native static String getGlErrorString(int err);

    public native static boolean isTrue(Boolean val);

    public native static boolean isQuadsToTriangles();

    public native static void checkNull(Object obj, String msg) throws NullPointerException;
}
