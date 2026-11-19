package mods.Hileb.optirefine.validation;

import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.client.GameSettingsOptionOF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ScreenShotHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/** Captures real creative inventory frames across item-lighting changes. */
final class CreativeTooltipContracts extends GuiContainerCreative implements AutoCloseable {
    private static final String[] CAPTURES = {"away", "hover", "away-again"};
    private static final int HOVER = 1;
    private final Minecraft client;
    private final GuiScreen previousScreen;
    private final boolean previousFastRender, previousSky, previousHideGui, previousDebug, previousDaylight;
    private final int previousClouds;
    private final long previousTime;
    private final float previousPitch, previousInterpolatedPitch;
    private final float previousRain, previousThunder;
    private final BufferedImage[] images = new BufferedImage[CAPTURES.length];
    private final ItemStack[] tooltips = new ItemStack[CAPTURES.length];
    private Slot stoneSlot;
    private ItemStack renderedTooltip = ItemStack.EMPTY;
    private int captures, frames;

    CreativeTooltipContracts(Minecraft client) {
        super(client.player);
        this.client = client;
        previousScreen = client.currentScreen;
        previousFastRender = Config.isFastRender();
        previousSky = Config.isSkyEnabled();
        previousHideGui = client.gameSettings.hideGUI;
        previousDebug = client.gameSettings.showDebugInfo;
        previousClouds = client.gameSettings.clouds;
        previousTime = client.world.getWorldTime();
        previousDaylight = client.world.getGameRules().getBoolean("doDaylightCycle");
        previousPitch = client.player.rotationPitch;
        previousInterpolatedPitch = client.player.prevRotationPitch;
        previousRain = client.world.getRainStrength(1.0F);
        previousThunder = client.world.getThunderStrength(1.0F);
    }

    void open() {
        if (!previousFastRender) client.gameSettings.setOptionValue(GameSettingsOptionOF.FAST_RENDER, 1);
        if (!previousSky) client.gameSettings.setOptionValue(GameSettingsOptionOF.SKY, 1);
        if (!Config.isFastRender() || !Config.isSkyEnabled() || OpenGlHelper.isFramebufferEnabled()) {
            throw new AssertionError("Creative tooltip contract requires Fast Render, sky and a direct back buffer");
        }
        client.gameSettings.hideGUI = true;
        client.gameSettings.showDebugInfo = false;
        client.gameSettings.clouds = 0;
        client.world.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
        client.world.setWorldTime(6000L);
        client.world.setRainStrength(0.0F);
        client.world.setThunderStrength(0.0F);
        client.player.rotationPitch = client.player.prevRotationPitch = -35.0F;
        client.displayGuiScreen(this);
        stoneSlot = inventorySlots.inventorySlots.stream().filter(slot -> isStone(slot.getStack())).findFirst()
                .orElseThrow(() -> new AssertionError("Stone is missing from the creative inventory"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean hover = captures == HOVER;
        renderedTooltip = ItemStack.EMPTY;
        super.drawScreen(hover ? guiLeft + stoneSlot.xPos + 8 : 0, hover ? guiTop + stoneSlot.yPos + 8 : 0, partialTicks);
        // The tooltip can affect the next frame's sky, so wait through two frame presentations.
        if (captures < images.length && ++frames == 3) {
            images[captures] = ScreenShotHelper.createScreenshot(client.displayWidth, client.displayHeight, client.getFramebuffer());
            tooltips[captures] = renderedTooltip;
            captures++;
            frames = 0;
        }
    }

    @Override
    protected void renderToolTip(ItemStack stack, int x, int y) {
        super.renderToolTip(stack, x, y);
        renderedTooltip = stack;
    }

    boolean isComplete() {
        return captures == images.length;
    }

    String verify(Path output) throws IOException {
        Path destination = Files.createDirectories(output.resolve("creative-tooltip"));
        for (int index = 0; index < CAPTURES.length; index++) {
            ImageIO.write(images[index], "png", destination.resolve(CAPTURES[index] + ".png").toFile());
        }
        if (!tooltips[0].isEmpty() || !isStone(tooltips[HOVER]) || !tooltips[2].isEmpty()) {
            throw new AssertionError("Expected only a Stone tooltip in the middle creative inventory frame");
        }
        double away = skyLuminance(images[0]);
        double hover = skyLuminance(images[HOVER]);
        double awayAgain = skyLuminance(images[2]);
        String evidence = String.format(Locale.ROOT, "Creative tooltip with Fast Render and hidden HUD, sky luminance %.3f / %.3f / %.3f",
                away, hover, awayAgain);
        if (away < 10.0 || Math.abs(hover - away) > 2.0 || Math.abs(awayAgain - away) > 2.0) {
            throw new AssertionError(evidence);
        }
        return evidence;
    }

    private static boolean isStone(ItemStack stack) {
        return stack.getItem() == Item.getItemFromBlock(Blocks.STONE) && stack.getMetadata() == 0;
    }

    private static double skyLuminance(BufferedImage image) {
        int left = (5 * image.getWidth() + 99) / 100;
        int right = (15 * image.getWidth() + 99) / 100;
        int top = (8 * image.getHeight() + 99) / 100;
        int bottom = (18 * image.getHeight() + 99) / 100;
        double sum = 0;
        for (int y = top; y < bottom; y++) {
            for (int x = left; x < right; x++) {
                int pixel = image.getRGB(x, y);
                sum += 0.2126 * (pixel >> 16 & 255) + 0.7152 * (pixel >> 8 & 255) + 0.0722 * (pixel & 255);
            }
        }
        return sum / ((right - left) * (bottom - top));
    }

    @Override
    public void close() {
        if (Config.isFastRender() != previousFastRender) client.gameSettings.setOptionValue(GameSettingsOptionOF.FAST_RENDER, 1);
        if (Config.isSkyEnabled() != previousSky) client.gameSettings.setOptionValue(GameSettingsOptionOF.SKY, 1);
        client.gameSettings.hideGUI = previousHideGui;
        client.gameSettings.showDebugInfo = previousDebug;
        client.gameSettings.clouds = previousClouds;
        client.world.setWorldTime(previousTime);
        client.world.getGameRules().setOrCreateGameRule("doDaylightCycle", Boolean.toString(previousDaylight));
        client.world.setRainStrength(previousRain);
        client.world.setThunderStrength(previousThunder);
        client.player.rotationPitch = previousPitch;
        client.player.prevRotationPitch = previousInterpolatedPitch;
        client.displayGuiScreen(previousScreen);
    }
}
