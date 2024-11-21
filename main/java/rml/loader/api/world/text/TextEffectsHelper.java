package rml.loader.api.world.text;

import rml.jrx.announces.PrivateAPI;
import rml.jrx.announces.RewriteWhenCleanroom;
import rml.loader.api.event.client.gui.HandleComponentEvent;
import rml.jrx.reflection.jvm.ReflectionHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/6/8 11:30
 **/

//TODO if https://github.com/CleanroomMC/Cleanroom/pull/107 merged, we need to rewrite this to adapt Cleanroom.
@RewriteWhenCleanroom
public class TextEffectsHelper {
    private TextEffectsHelper(){}
    public static Map<String, HoverEvent.Action> hoverMapping = ReflectionHelper.getPrivateValue(HoverEvent.Action.class, null, "NAME_MAPPING", "field_150690_d");
    public static Map<String, ClickEvent.Action> clickMapping = ReflectionHelper.getPrivateValue(ClickEvent.Action.class, null, "NAME_MAPPING", "field_150679_e");

    public static ClickEventHandler click(String nameIn, boolean allowedInChatIn){
        return new ClickEventHandler(nameIn, allowedInChatIn);
    }

    public static HoverEventHandler hover(String nameIn, boolean allowedInChatIn){
        return new HoverEventHandler(nameIn, allowedInChatIn);
    }
    public static class ClickEventHandler{
        public BiConsumer<ClickEventHandler, HandleComponentEvent.Click> handler = null;
        public ClickEvent.Action action;

        public ClickEventHandler(String nameIn, boolean allowedInChatIn){
            this.action =  EnumHelper.addEnum(ClickEvent.Action.class, nameIn, new Class<?>[]{String.class, boolean.class}, nameIn, allowedInChatIn);
            clickMapping.put(nameIn, this.action);
        }

        public ClickEvent.Action getAction() {
            return action;
        }

        public ClickEvent makeEvent(String value){
            return new ClickEvent(action, value);
        }

        public ClickEventHandler eventHandler(BiConsumer<ClickEventHandler, HandleComponentEvent.Click> handler){
            this.handler = handler;
            MinecraftForge.EVENT_BUS.register(this);
            return this;
        }

        @PrivateAPI
        @SubscribeEvent
        public void handle(HandleComponentEvent.Click event){
            handler.accept(this, event);
        }

        public boolean is(ClickEvent clickEvent){
            return clickEvent.getAction() == this.action;
        }

        public boolean is(HandleComponentEvent.Click clickEvent){
            return clickEvent.getClickEvent().getAction() == this.action;
        }
    }

    public static class HoverEventHandler{
        public BiConsumer<HoverEventHandler, HandleComponentEvent.Hover> handler = null;
        public HoverEvent.Action action;

        public HoverEventHandler(String nameIn, boolean allowedInChatIn){
            this.action =  EnumHelper.addEnum(HoverEvent.Action.class, nameIn, new Class<?>[]{String.class, boolean.class}, nameIn, allowedInChatIn);
            hoverMapping.put(nameIn, this.action);
        }

        public HoverEvent.Action getAction() {
            return action;
        }

        public HoverEvent makeEvent(ITextComponent value){
            return new HoverEvent(action, value);
        }


        public HoverEventHandler eventHandler(BiConsumer<HoverEventHandler, HandleComponentEvent.Hover> handler){
            this.handler = handler;
            MinecraftForge.EVENT_BUS.register(this);
            return this;
        }

        @PrivateAPI
        @SubscribeEvent
        public void handle(HandleComponentEvent.Hover event){
            handler.accept(this, event);
        }

        public boolean is(HoverEvent event){
            return event.getAction() == this.action;
        }

        public boolean is(HandleComponentEvent.Hover event){
            return event.getHoverEvent().getAction() == this.action;
        }
    }
}
