package rml.jrx.utils;

import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/5/25 16:44
 **/

@PublicAPI
@EarlyClass
public class IteratorHelper {
    public static <T> Collection<T> getAll(Iterator<T> iterator){
        LinkedList<T> list = new LinkedList<>();
        while (iterator.hasNext()){
            list.add(iterator.next());
        }
        return list;
    }
    public static <T> Stream<T> stream(Iterator<T> iterator){
        Stream.Builder<T> builder = Stream.builder();
        while (iterator.hasNext()){
            builder.add(iterator.next());
        }
        return builder.build();
    }
}
