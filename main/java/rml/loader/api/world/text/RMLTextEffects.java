package rml.loader.api.world.text;

import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rml.jrx.reflection.jvm.FieldAccessor;
import rml.jrx.reflection.jvm.MethodAccessor;
import rml.jrx.reflection.jvm.ReflectionHelper;

import java.util.ArrayList;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/4/1 13:36
 **/

@SideOnly(Side.CLIENT)
public class RMLTextEffects {
    /**
     * @Project ResourceModLoader
     * @Author Hileb
     * @Date 2024/3/31 10:20
     **/
    @SideOnly(Side.CLIENT)
    public static class ChangeModClickAction {
        public static final FieldAccessor<ArrayList<ModContainer>, GuiModList> mods = ReflectionHelper.getFieldAccessor(GuiModList.class, "mods");
        public static final MethodAccessor<Void, GuiModList> selectModIndex = ReflectionHelper.getMethodAccessor(GuiModList.class, "selectModIndex", null, new Class<?>[]{int.class});

        public static final TextEffectsHelper.ClickEventHandler CHANGE_MOD = TextEffectsHelper.click("rml_change_mod", false)
                .eventHandler((clickEventHandler, event) -> {
                    if (clickEventHandler.is(event) && !event.isHandled()) {
                        if (event.getGuiScreen() instanceof GuiModList) {
                            GuiModList guiModList = (GuiModList) event.getGuiScreen();
                            String modid = event.getClickEvent().getValue();
                            ArrayList<ModContainer> modList = mods.get(guiModList);
                            for(int i = 0, size = modList.size(); i < size; i++){
                                if (modid.equals(modList.get(i).getModId())){
                                    selectModIndex.invoke(guiModList, i);
                                    event.setHandled(true);
                                    return;
                                }
                            }
                        }
                    }
                });
    }
}
