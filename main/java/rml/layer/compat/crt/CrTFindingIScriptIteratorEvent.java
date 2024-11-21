package rml.layer.compat.crt;

import crafttweaker.runtime.IScriptIterator;
import crafttweaker.runtime.IScriptProvider;
import rml.jrx.announces.PublicAPI;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Iterator;
import java.util.List;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/19 22:54
 **/
@PublicAPI
public class CrTFindingIScriptIteratorEvent extends Event {
    @PublicAPI public final List<IScriptIterator> scriptIterators;
    @PublicAPI public CrTFindingIScriptIteratorEvent(List<IScriptIterator> list){
        scriptIterators = list;
    }
    @PublicAPI public void load(IScriptProvider provider){
        Iterator<IScriptIterator> iterator = provider.getScripts();
        while (iterator.hasNext()){
            scriptIterators.add(iterator.next());
        }
    }
    @Override
    public boolean isCancelable() {
        return false;
    }
}
