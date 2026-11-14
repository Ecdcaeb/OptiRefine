package mods.Hileb.optirefine.core.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Validated, ordered view of the built-in mixin module manifest. */
public final class MixinModulePlan {
    private static final String REQUIRED_MODULE = "g5-base";

    private final List<Block> blocks;
    private final Map<String, Module> modules;

    private MixinModulePlan(List<Block> blocks, Map<String, Module> modules) {
        this.blocks = List.copyOf(blocks);
        this.modules = Map.copyOf(modules);
    }

    public static MixinModulePlan loadDefault() {
        InputStream stream = MixinModulePlan.class.getResourceAsStream("/optirefine.modules.json");
        if (stream == null) throw new IllegalStateException("Missing /optirefine.modules.json");
        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            return parse(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read /optirefine.modules.json", e);
        }
    }

    public static MixinModulePlan parse(Reader reader) {
        try {
            JsonElement rootElement = JsonParser.parseReader(reader);
            if (!rootElement.isJsonObject()) throw invalid("root must be an object");
            JsonObject root = rootElement.getAsJsonObject();
            JsonArray modules = requiredArray(root, "modules");
            JsonArray rawBlocks = requiredArray(root, "blocks");
            Map<String, Module> byId = new HashMap<>();
            for (JsonElement element : modules) {
                JsonObject item = object(element, "module");
                String id = string(item, "id");
                String side = string(item, "side");
                if (!side.equals("both") && !side.equals("client") && !side.equals("server")) throw invalid("invalid side for module " + id);
                List<String> requires = strings(requiredArray(item, "requires"), "dependency");
                if (byId.putIfAbsent(id, new Module(id, side, requires)) != null) throw invalid("duplicate module " + id);
            }
            Module required = byId.get(REQUIRED_MODULE);
            if (required == null || !required.side.equals("both")) throw invalid("required module g5-base must exist on both sides");
            for (Module module : byId.values()) {
                Set<String> dependencies = new HashSet<>();
                for (String dependency : module.requires) {
                    if (!dependencies.add(dependency)) throw invalid("duplicate dependency " + dependency + " in " + module.id);
                    Module target = byId.get(dependency);
                    if (target == null) throw invalid("unknown dependency " + dependency + " in " + module.id);
                }
            }
            checkCycles(byId);
            List<Block> blocks = new ArrayList<>(rawBlocks.size());
            Set<String> seen = new HashSet<>();
            boolean hasBase = false;
            boolean hasCommonBase = false;
            for (JsonElement element : rawBlocks) {
                JsonObject item = object(element, "block");
                String module = string(item, "module");
                Module definition = byId.get(module);
                if (definition == null) throw invalid("unknown module " + module + " in block");
                String side = string(item, "side");
                if (!side.equals("common") && !side.equals("client") && !side.equals("server")) throw invalid("invalid block side " + side);
                if ((side.equals("common") && !definition.side.equals("both")) || (side.equals("client") && definition.side.equals("server")) || (side.equals("server") && definition.side.equals("client"))) throw invalid("side-incompatible block for module " + module);
                List<String> mixins = strings(requiredArray(item, "mixins"), "mixin");
                for (String mixin : mixins) if (!seen.add(mixin)) throw invalid("duplicate mixin " + mixin);
                if (module.equals(REQUIRED_MODULE)) hasBase = true;
                if (module.equals(REQUIRED_MODULE) && side.equals("common")) hasCommonBase = true;
                blocks.add(new Block(module, side, List.copyOf(mixins)));
            }
            if (!hasBase) throw invalid("required module g5-base has no mixin block");
            if (!hasCommonBase) throw invalid("required module g5-base must include a common block");
            return new MixinModulePlan(blocks, byId);
        } catch (IllegalStateException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid mixin module manifest", e);
        }
    }

    public List<String> mixins(boolean client) {
        validateSide(client);
        List<String> result = new ArrayList<>();
        for (Block block : blocks) if (block.side.equals("common")) result.addAll(block.mixins);
        String side = client ? "client" : "server";
        for (Block block : blocks) if (block.side.equals(side)) result.addAll(block.mixins);
        return List.copyOf(result);
    }

    void validateSide(boolean client) {
        Set<String> selected = new HashSet<>();
        String side = client ? "client" : "server";
        for (Module module : modules.values()) {
            if (module.side.equals("both") || module.side.equals(side)) selected.add(module.id);
        }
        for (String id : selected) for (String dependency : modules.get(id).requires) {
            if (!selected.contains(dependency)) throw invalid("selected module " + id + " requires unavailable " + side + " module " + dependency);
        }
    }

    private static void checkCycles(Map<String, Module> modules) {
        Set<String> done = new HashSet<>(), active = new HashSet<>();
        for (String id : modules.keySet()) visit(id, modules, done, active);
    }

    private static void visit(String id, Map<String, Module> modules, Set<String> done, Set<String> active) {
        if (done.contains(id)) return;
        if (!active.add(id)) throw invalid("dependency cycle involving " + id);
        for (String dependency : modules.get(id).requires) visit(dependency, modules, done, active);
        active.remove(id);
        done.add(id);
    }

    private static JsonArray requiredArray(JsonObject object, String key) {
        JsonElement value = object.get(key);
        if (value == null || !value.isJsonArray()) throw invalid("'" + key + "' must be an array");
        return value.getAsJsonArray();
    }

    private static JsonObject object(JsonElement value, String name) {
        if (!value.isJsonObject()) throw invalid(name + " must be an object");
        return value.getAsJsonObject();
    }

    private static String string(JsonObject object, String key) {
        JsonElement value = object.get(key);
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString() || value.getAsString().isBlank()) throw invalid("'" + key + "' must be a non-empty string");
        return value.getAsString();
    }

    private static List<String> strings(JsonArray array, String name) {
        List<String> result = new ArrayList<>(array.size());
        for (JsonElement value : array) {
            if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString() || value.getAsString().isBlank()) throw invalid(name + " entries must be non-empty strings");
            result.add(value.getAsString());
        }
        return result;
    }

    private static IllegalArgumentException invalid(String message) { return new IllegalArgumentException("Invalid mixin module manifest: " + message); }
    private record Module(String id, String side, List<String> requires) { }
    private record Block(String module, String side, List<String> mixins) { }
}
