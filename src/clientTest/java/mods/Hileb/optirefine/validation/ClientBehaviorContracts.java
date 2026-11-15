package mods.Hileb.optirefine.validation;

import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.optifine.gui.GuiChatOF;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.system.Platform;
import org.lwjgl.system.windows.User32;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/** Native GUI, local-command, notification and debug-key contracts. */
public final class ClientBehaviorContracts {
    private ClientBehaviorContracts() {}

    public static void verify(Minecraft mc) throws Exception {
        GuiScreen previous = mc.currentScreen;
        List<String> history = new ArrayList<>(mc.ingameGUI.getChatGUI().getSentMessages());
        try {
            notifications(mc);
            chat(mc);
            debugKeys(mc);
        } finally {
            mc.ingameGUI.getChatGUI().getSentMessages().clear();
            mc.ingameGUI.getChatGUI().getSentMessages().addAll(history);
            mc.displayGuiScreen(previous);
        }
    }

    private static void frame(Minecraft mc) throws Exception {
        Method method = EntityRenderer.class.getDeclaredMethod("frameInit");
        method.setAccessible(true);
        method.invoke(mc.entityRenderer);
    }

    @SuppressWarnings("unchecked")
    private static void notifications(Minecraft mc) throws Exception {
        String release = Config.getNewRelease();
        boolean javaNotice = Config.isNotify64BitJava();
        GuiNewChat chat = mc.ingameGUI.getChatGUI();
        List<ChatLine> lines = (List<ChatLine>) field(GuiNewChat.class, "chatLines", "field_146252_h").get(chat);
        List<ChatLine> drawn = (List<ChatLine>) field(GuiNewChat.class, "drawnChatLines", "field_146253_i").get(chat);
        List<ChatLine> savedLines = new ArrayList<>(lines), savedDrawn = new ArrayList<>(drawn);
        try {
            Config.setNewRelease("CONTRACT_RELEASE");
            Config.setNotify64BitJava(true);
            frame(mc);
            String versionText = I18n.format("of.message.newVersion", "\u00a7nHD Ultra CONTRACT_RELEASE\u00a7r");
            String javaText = I18n.format("of.message.java64Bit");
            if (versionText.equals("of.message.newVersion") || javaText.equals("of.message.java64Bit")) {
                throw new AssertionError("G5 notification translations are missing");
            }
            long versions = lines.stream().filter(line -> line.getChatComponent().getUnformattedText().equals(versionText)).count();
            long javaMessages = lines.stream().filter(line -> line.getChatComponent().getUnformattedText().equals(javaText)).count();
            if (versions != 1 || javaMessages != 1 || Config.getNewRelease() != null || Config.isNotify64BitJava()) {
                throw new AssertionError("G5 release/64-bit notifications did not consume their one-shot flags");
            }
            ChatLine version = lines.stream().filter(line -> line.getChatComponent().getUnformattedText().equals(versionText)).findFirst().orElseThrow();
            ClickEvent click = version.getChatComponent().getStyle().getClickEvent();
            if (click == null || click.getAction() != ClickEvent.Action.OPEN_URL
                    || !click.getValue().equals("https://optifine.net/downloads")) {
                throw new AssertionError("Release notification lost its download link");
            }
            int count = lines.size();
            frame(mc);
            if (lines.size() != count) throw new AssertionError("One-shot notifications repeated on the next frame");
        } finally {
            lines.clear(); lines.addAll(savedLines);
            drawn.clear(); drawn.addAll(savedDrawn);
            Config.setNewRelease(release);
            Config.setNotify64BitJava(javaNotice);
        }
    }

    private static void chat(Minecraft mc) throws Exception {
        GuiChat vanilla = new GuiChat("/reloadS");
        mc.displayGuiScreen(vanilla);
        frame(mc);
        if (!(mc.currentScreen instanceof GuiChatOF)) throw new AssertionError("Vanilla chat was not replaced");
        GuiChatOF chat = (GuiChatOF) mc.currentScreen;
        GuiTextField input = (GuiTextField) field(GuiChat.class, "inputField", "field_146415_a").get(chat);
        if (!input.getText().equals("/reloadS")) throw new AssertionError("Chat replacement discarded typed text");
        TabCompleter completer = (TabCompleter) field(GuiChat.class, "tabCompleter", "field_184096_i").get(chat);
        completer.complete();
        chat.setCompletions(new String[0]);
        if (!input.getText().equals("/reloadShaders")) throw new AssertionError("Local shader command completion failed");
        input.setText("/reloadC");
        completer.resetDidComplete();
        completer.complete();
        chat.setCompletions(new String[0]);
        if (!input.getText().equals("/reloadChunks")) throw new AssertionError("Local chunk command completion failed");

        CommandEvents events = new CommandEvents();
        MinecraftForge.EVENT_BUS.register(events);
        try {
            Object oldFrustum = field(RenderGlobal.class, "viewFrustum", "field_175008_n").get(mc.renderGlobal);
            chat.sendChatMessage("/reloadChunks");
            Object newFrustum = field(RenderGlobal.class, "viewFrustum", "field_175008_n").get(mc.renderGlobal);
            if (oldFrustum == newFrustum || newFrustum == null) throw new AssertionError("Local chunk command did not reload the actual renderers");
            chat.sendChatMessage("/reloadShaders");
            List<String> sent = mc.ingameGUI.getChatGUI().getSentMessages();
            if (!sent.contains("/reloadShaders") || !sent.contains("/reloadChunks") || events.count != 0) {
                throw new AssertionError("Local commands escaped into ordinary chat or missed sent history");
            }
            chat.sendChatMessage("/help");
            if (events.count != 1) throw new AssertionError("Ordinary chat command stopped reaching the normal client pipeline");
        } finally {
            MinecraftForge.EVENT_BUS.unregister(events);
        }
        GuiChat subclass = new ThirdPartyChat();
        mc.displayGuiScreen(subclass);
        frame(mc);
        if (mc.currentScreen != subclass) throw new AssertionError("Third-party chat subclass was replaced");
    }

    private static void debugKeys(Minecraft mc) throws Exception {
        Method check = EntityRenderer.class.getDeclaredMethod("checkLoadVisibleChunks", Entity.class,
                float.class, ICamera.class, boolean.class);
        check.setAccessible(true);
        Field pending = field(EntityRenderer.class, "loadVisibleChunks", "loadVisibleChunks");
        Field action = field(Minecraft.class, "actionKeyF3", "field_184129_aV");
        Field updates = field(RenderChunk.class, "renderChunksUpdated", "field_178592_a");
        Field configured = field(GameSettings.class, "ofChunkUpdates", "ofChunkUpdates");
        Field lazy = field(GameSettings.class, "ofLazyChunkLoading", "ofLazyChunkLoading");
        boolean previousAction = action.getBoolean(mc), previousPending = pending.getBoolean(mc.entityRenderer);
        int previousUpdates = updates.getInt(null), configuredUpdates = configured.getInt(mc.gameSettings);
        boolean lazyLoading = lazy.getBoolean(mc.gameSettings);
        int advancementsKey = mc.gameSettings.keyBindAdvancements.getKeyCode();
        boolean debug = mc.gameSettings.showDebugInfo;
        Frustum camera = new Frustum();
        camera.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
        Robot keyboard = Platform.get() == Platform.WINDOWS ? null : new Robot();
        try {
            pending.setBoolean(mc.entityRenderer, false);
            action.setBoolean(mc, false);
            mc.displayGuiScreen(new GuiChat());
            focusWindow();
            keyInput(keyboard, KeyEvent.VK_F3, GLFW.GLFW_KEY_F3, true);
            keyInput(keyboard, KeyEvent.VK_L, GLFW.GLFW_KEY_L, true);
            keyState(true);
            check.invoke(mc.entityRenderer, mc.player, 0F, camera, false);
            if (pending.getBoolean(mc.entityRenderer) || action.getBoolean(mc)) {
                throw new AssertionError("F3+L activated through an unrelated open GUI");
            }
            mc.gameSettings.keyBindAdvancements.setKeyCode(Keyboard.KEY_L);
            mc.displayGuiScreen(new GuiScreenAdvancements(mc.player.connection.getAdvancementManager()));
            check.invoke(mc.entityRenderer, mc.player, 0F, camera, false);
            if (mc.currentScreen != null || !pending.getBoolean(mc.entityRenderer) || !action.getBoolean(mc)) {
                throw new AssertionError("F3+L did not close advancements, schedule visible chunks and consume F3");
            }
            keyInput(keyboard, KeyEvent.VK_L, GLFW.GLFW_KEY_L, false);
            keyInput(keyboard, KeyEvent.VK_F3, GLFW.GLFW_KEY_F3, false);
            keyState(false);
            Method nativeKeys;
            try {
                nativeKeys = Minecraft.class.getDeclaredMethod("runTickKeyboard");
            } catch (NoSuchMethodException ignored) {
                nativeKeys = Minecraft.class.getDeclaredMethod("func_184118_az");
            }
            nativeKeys.setAccessible(true);
            nativeKeys.invoke(mc);
            if (action.getBoolean(mc) || mc.gameSettings.showDebugInfo != debug) {
                throw new AssertionError("Releasing F3 after visible-chunk loading toggled the debug overlay");
            }
            // Include pre-existing per-second updates while the loader performs actual terrain work.
            updates.setInt(null, 17);
            check.invoke(mc.entityRenderer, mc.player, 0F, camera, false);
            if (pending.getBoolean(mc.entityRenderer) || updates.getInt(null) != 0
                    || configured.getInt(mc.gameSettings) != configuredUpdates
                    || lazy.getBoolean(mc.gameSettings) != lazyLoading
                    || mc.gameSettings.showDebugInfo != debug) {
                throw new AssertionError("Visible-chunk loading lost its counter/settings/debug-state contract");
            }
        } finally {
            keyInput(keyboard, KeyEvent.VK_L, GLFW.GLFW_KEY_L, false);
            keyInput(keyboard, KeyEvent.VK_F3, GLFW.GLFW_KEY_F3, false);
            keyState(false);
            while (Keyboard.next()) {
                // Consume any release events left by a failed scenario before restoring the F3 flag.
            }
            mc.gameSettings.keyBindAdvancements.setKeyCode(advancementsKey);
            action.setBoolean(mc, previousAction);
            pending.setBoolean(mc.entityRenderer, previousPending);
            updates.setInt(null, previousUpdates);
            mc.ingameGUI.getChatGUI().deleteChatLine(201435902);
        }
    }

    private static void keyInput(Robot keyboard, int virtualKey, int glfwKey, boolean down) {
        if (Platform.get() == Platform.WINDOWS) {
            long parameters = 1L | ((long) GLFW.glfwGetKeyScancode(glfwKey) << 16);
            if (!down) parameters |= 0xC0000000L;
            if (!User32.PostMessage(null, GLFWNativeWin32.glfwGetWin32Window(Display.getWindow()),
                    down ? User32.WM_KEYDOWN : User32.WM_KEYUP, virtualKey, parameters)) {
                throw new AssertionError("Posting a native contract key failed");
            }
        } else if (down) keyboard.keyPress(virtualKey);
        else keyboard.keyRelease(virtualKey);
    }

    private static void focusWindow() throws Exception {
        long window = Display.getWindow();
        org.lwjgl.glfw.GLFW.glfwRestoreWindow(window);
        org.lwjgl.glfw.GLFW.glfwShowWindow(window);
        org.lwjgl.glfw.GLFW.glfwFocusWindow(window);
        long deadline = System.nanoTime() + 2_000_000_000L;
        do {
            Display.processMessages();
            if (Display.isActive()) return;
            Thread.sleep(10);
        } while (System.nanoTime() < deadline);
        throw new AssertionError("Native contract window did not acquire keyboard focus");
    }

    private static void keyState(boolean down) throws InterruptedException {
        long deadline = System.nanoTime() + 2_000_000_000L;
        do {
            Display.processMessages();
            Keyboard.poll();
            if (Keyboard.isKeyDown(Keyboard.KEY_F3) == down && Keyboard.isKeyDown(Keyboard.KEY_L) == down) return;
            Thread.sleep(10);
        } while (System.nanoTime() < deadline);
        throw new AssertionError("Native keyboard did not receive F3/L " + (down ? "press" : "release")
                + ": focused=" + Display.isActive() + ", F3=" + Keyboard.isKeyDown(Keyboard.KEY_F3)
                + ", L=" + Keyboard.isKeyDown(Keyboard.KEY_L));
    }

    private static Field field(Class<?> owner, String mcp, String srg) throws NoSuchFieldException {
        Field field;
        try {
            field = owner.getDeclaredField(mcp);
        } catch (NoSuchFieldException ignored) {
            field = owner.getDeclaredField(srg);
        }
        field.setAccessible(true);
        return field;
    }

    public static final class CommandEvents {
        int count;
        @SubscribeEvent public void sent(ClientChatEvent event) { ++count; }
    }

    private static final class ThirdPartyChat extends GuiChat {}
}
