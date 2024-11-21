package rml.layer.compat.crt;

import crafttweaker.runtime.IScriptIterator;
import crafttweaker.runtime.IScriptProvider;
import rml.jrx.announces.PrivateAPI;
import net.minecraftforge.common.MinecraftForge;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/19 22:40
 **/
@PrivateAPI
public class EventScriptProvider implements IScriptProvider {
    @PrivateAPI public IScriptProvider proxy;
    @PrivateAPI public EventScriptProvider(IScriptProvider provider){
        proxy=provider;
    }
    @PrivateAPI
    @Override
    public Iterator<IScriptIterator> getScripts() {
        return this.combine().iterator();
    }
    @PrivateAPI public List<IScriptIterator> combine(){
        LinkedList<IScriptIterator> list = new LinkedList<>();

        Iterator<IScriptIterator> iterator = proxy.getScripts();
        while (iterator.hasNext()){
            list.add(iterator.next());
        }
        MinecraftForge.EVENT_BUS.post(new CrTFindingIScriptIteratorEvent(list));
        return list;
    }
}
