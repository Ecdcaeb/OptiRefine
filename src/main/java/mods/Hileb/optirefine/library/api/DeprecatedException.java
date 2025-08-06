package mods.Hileb.optirefine.library.api;

@SuppressWarnings("unused")
public class DeprecatedException extends Exception {
    public DeprecatedException(String clazz, String symbol){
        super("Using an deprecated api : " + clazz + "#" + symbol);
    }

    public DeprecatedException(String clazz, String symbol, String forRemoval){
        super("Using an deprecated api : " + clazz + "#" + symbol + " , it will be removed at " + forRemoval);
    }
    public DeprecatedException(String clazz, String symbol, String forRemoval, String update){
        super("Using an deprecated api : " + clazz + "#" + symbol + " , it will be removed at " + forRemoval + ". And you need to update it :" + update);
    }
}
