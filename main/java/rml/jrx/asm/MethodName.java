package rml.jrx.asm;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PrivateAPI;
import rml.jrx.announces.PublicAPI;

import java.util.Objects;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/14 23:16
 **/
@EarlyClass
@PublicAPI
public class MethodName {
    @PublicAPI public String srgClass;@PublicAPI public String notchClass;
    @PublicAPI public String mcpName;@PublicAPI public String mcpDesc;
    @PublicAPI public String srgName;@PublicAPI public String srgDesc;
    @PublicAPI public String notchName;@PublicAPI public String notchDesc;

    @PrivateAPI MethodName(){}

    @PublicAPI public static Builder of(){return new Builder();}

    @PublicAPI public boolean is(MethodNode mn){return is(mn.name,mn.desc);}
    @PublicAPI public boolean isClass(String clazz){
        return Objects.equals(srgClass, clazz) || Objects.equals(notchClass, clazz);
    }

    @PublicAPI public boolean is(String name,String desc){
        return Objects.equals(srgName, name) || (Objects.equals(mcpName, name) && Objects.equals(mcpDesc, desc)) || (Objects.equals(notchName, name) && Objects.equals(notchDesc, desc));
    }

    @PublicAPI public boolean is(MethodInsnNode methodInsnNode){
        return this.is(methodInsnNode.name, methodInsnNode.desc) && isClass(methodInsnNode.owner);
    }

    @EarlyClass
    @PublicAPI
    public static class Builder{
        @PrivateAPI MethodName methodName;
        @PublicAPI public Builder(){
            methodName=new MethodName();
            methodName.notchName = null;
            methodName.mcpName = null;
            methodName.srgName = null;
            methodName.notchDesc = null;
            methodName.mcpDesc = null;
            methodName.srgDesc = null;
            methodName.notchClass = null;
            methodName.srgClass = null;
        }

        @PublicAPI public Builder mcp(String name, String desc){
            methodName.mcpName = name;
            methodName.mcpDesc = desc;
            return this;
        }

        @PublicAPI public Builder srg(String name, String desc){
            methodName.srgName = name;
            methodName.srgDesc = desc;
            return this;
        }

        @PublicAPI public Builder notch(String name, String desc){
            methodName.notchName = name;
            methodName.notchDesc = desc;
            return this;
        }

        @PublicAPI public Builder classes(String srg, String notch){
            methodName.srgClass = srg;
            methodName.notchClass = notch;
            return this;
        }
        @PublicAPI public MethodName build(){
            return methodName;
        }
    }
}
