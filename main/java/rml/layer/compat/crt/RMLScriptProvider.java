package rml.layer.compat.crt;

import crafttweaker.runtime.IScriptIterator;
import crafttweaker.runtime.IScriptProvider;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/3/16 20:49
 **/
public class RMLScriptProvider implements IScriptProvider {
    private final List<CustomScript> scripts;

    public RMLScriptProvider() {
        scripts = new ArrayList<>();
    }

    public void add(String name, byte[] content) {
        scripts.add(new CustomScript(name, content));
    }

    @Override
    public Iterator<IScriptIterator> getScripts() {
        return scripts.stream().map(CustomScript::iterator).collect(Collectors.toSet()).iterator();
    }

    private static class CustomScript {

        private final String name;
        private final byte[] content;

        public CustomScript(String name, byte[] content) {
            this.name = name;
            this.content = content;
        }

        public IScriptIterator iterator(){
            return new ScriptIteratorSingle(name, content);
        }
    }

    static class ScriptIteratorSingle implements IScriptIterator {

        private final byte[] file;
        private final String name;
        private boolean first = true;

        ScriptIteratorSingle(String name, byte[] file) {
            this.file = file;
            this.name = name;
        }

        @Override
        public String getGroupName() {
            return name;
        }

        @Override
        public boolean next() {
            if(first) {
                first = false;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public InputStream open() throws IOException {
            return new BufferedInputStream(new ByteArrayInputStream(file));
        }

        @Override
        public IScriptIterator copyCurrent() {
            return new ScriptIteratorSingle(name, file);
        }
    }
}
