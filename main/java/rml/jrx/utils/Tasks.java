package rml.jrx.utils;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;

import java.util.Map;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/3/31 9:24
 *
 * A task list. Used for check if the tasks completed.
 **/
@PublicAPI
@EarlyClass
public class Tasks {
    public Object2BooleanArrayMap<String> tasks;
    public Tasks(String... tasks){
        this.tasks = new Object2BooleanArrayMap<>(tasks, new boolean[tasks.length]);
    }

    public void complete(String s){
        if (tasks.containsKey(s)){
            this.tasks.put(s, true);
        }else throw new IllegalArgumentException("progress must be defined ahead!");
    }

    public boolean isCompleted(){
        for (boolean b : this.tasks.values()){
            if (!b) return false;
        }
        return true;
    }

    public boolean isCompleted(String value){
        return this.tasks.containsKey(value) && this.tasks.getBoolean(value);
    }

    public String[] getCompleted(){
        return this.tasks.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).distinct().toArray(String[]::new);
    }

    public String[] getFails(){
        return this.tasks.entrySet().stream().filter((entry)->!entry.getValue()).map(Map.Entry::getKey).distinct().toArray(String[]::new);
    }

    public String getCompletedString(){
        return combineStringArray(this.getCompleted());
    }

    public String getFailsString(){
        return combineStringArray(this.getFails());
    }

    public static String combineStringArray(String[] strings){
        StringBuilder builder = new StringBuilder();
        for(int i = 0, size = strings.length - 1; i < size; i++){
            builder.append(strings[i]).append(", ");
        }
        builder.append(strings[strings.length - 1]);
        return builder.toString();
    }

    public void throwError(){
        throw new IllegalStateException("Have not completed tasks :" + getFailsString() + " ; " + getCompletedString());
    }
}
