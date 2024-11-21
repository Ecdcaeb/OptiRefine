package rml.loader.api.event.client.gui;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ModMenuInfoEvent extends Event {
    public ModContainer mod;
    public Object info;
    public List<ITextComponent> textComponents;
    public GuiModList guiModList;

    public ModMenuInfoEvent(Object info, GuiModList modList, ModContainer mod, List<ITextComponent> textComponents){
        this.info = info;
        this.guiModList = modList;
        this.mod = mod;
        this.textComponents = textComponents;
    }

    public ModContainer getMod() {
        return mod;
    }

    public List<ITextComponent> getTextComponents() {
        return textComponents;
    }

    public GuiModList getGuiModList() {
        return guiModList;
    }

    public Object getInfo() {
        return info;
    }

    public static List<ITextComponent> post(List<ITextComponent> textComponentList, Object info, GuiModList modList, ModContainer mod){
        ModMenuInfoEvent event = new ModMenuInfoEvent(info, modList, mod, textComponentList);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getTextComponents();
    }

}