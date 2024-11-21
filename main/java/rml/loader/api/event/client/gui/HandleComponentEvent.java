package rml.loader.api.event.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import rml.jrx.announces.ASMInvoke;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/3/31 9:16
 **/
public class HandleComponentEvent extends Event {
    public ITextComponent textComponent;
    public GuiScreen guiScreen;

    public GuiScreen getGuiScreen() {
        return guiScreen;
    }

    public ITextComponent getTextComponent() {
        return textComponent;
    }

    public HandleComponentEvent(ITextComponent textComponent, GuiScreen guiScreen){
        this.textComponent = textComponent;
        this.guiScreen = guiScreen;
    }

    @ASMInvoke
    @SuppressWarnings("unused")
    public static void postHover(ITextComponent textComponent, GuiScreen guiScreen, int x, int y){
        Hover event = new Hover(textComponent, guiScreen, x, y);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @ASMInvoke
    @SuppressWarnings("unused")
    public static boolean postClick(ITextComponent textComponent, GuiScreen guiScreen){
        Click event = new Click(false, textComponent, guiScreen);
        MinecraftForge.EVENT_BUS.post(event);
        return event.isHandled();
    }

    public static class Click extends HandleComponentEvent{
        public boolean isHandled;
        public Click(boolean isHandled, ITextComponent textComponent, GuiScreen guiScreen) {
            super(textComponent, guiScreen);
            this.isHandled = isHandled;
        }

        public ClickEvent getClickEvent() {
            return getTextComponent().getStyle().getClickEvent();
        }

        public boolean isHandled() {
            return isHandled;
        }

        public void setHandled(boolean handled) {
            isHandled = handled;
        }
    }

    public static class Hover extends HandleComponentEvent{
        public int x;
        public int y;
        public  Hover(ITextComponent textComponent, GuiScreen guiScreen, int x, int y) {
            super(textComponent, guiScreen);
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public HoverEvent getHoverEvent(){
            return getTextComponent().getStyle().getHoverEvent();
        }
    }
}
