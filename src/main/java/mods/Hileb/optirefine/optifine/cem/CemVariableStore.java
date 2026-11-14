package mods.Hileb.optirefine.optifine.cem;

/** A small open-addressed primitive table, allocated only when an entity writes a variable. */
public final class CemVariableStore {
    private String[] names = new String[8];
    private float[] values = new float[8];
    private int size;

    private int find(String name) {
        int slot = name.hashCode() & (names.length - 1);
        while (names[slot] != null && !names[slot].equals(name)) {
            slot = (slot + 1) & (names.length - 1);
        }
        return slot;
    }

    public float get(String name) {
        return values[find(name)];
    }

    public void set(String name, float value) {
        int slot = find(name);
        if (names[slot] == null) {
            if ((size + 1) * 2 >= names.length) {
                grow();
                slot = find(name);
            }
            names[slot] = name;
            ++size;
        }
        values[slot] = value;
    }

    private void grow() {
        String[] previousNames = names;
        float[] previousValues = values;
        names = new String[previousNames.length * 2];
        values = new float[names.length];
        for (int i = 0; i < previousNames.length; ++i) {
            if (previousNames[i] != null) {
                int slot = find(previousNames[i]);
                names[slot] = previousNames[i];
                values[slot] = previousValues[i];
            }
        }
    }
}
