/*
 * Mini - an ASM-based class transformer reminiscent of MalisisCore and Mixin
 * 
 * The MIT License
 *
 * Copyright (c) 2017-2021 Una Thompson (unascribed) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mods.Hileb.optirefine.library.foundationx.mini;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mods.Hileb.optirefine.library.foundationx.ASMHelper;
import mods.Hileb.optirefine.library.foundationx.TransformerHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import mods.Hileb.optirefine.library.foundationx.mini.annotation.Patch;

public abstract class MiniTransformer implements TransformerHelper.TargetedASMTransformer, ASMHelper {

	public static final Logger LOGGER = LogManager.getLogger(MiniTransformer.class);

	@FunctionalInterface
	public interface PatchMethod {
		boolean patch(PatchContext ctx) throws Throwable;
	}
	
	private final String classTargetName;
	private final Map<String, List<PatchMethod>> methods = new HashMap<>();
	private final Set<String> requiredMethods = new HashSet<>();

	public static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("foundation.dump", "false"));

	public MiniTransformer() {
		Patch.Class classAnn = getClass().getAnnotation(Patch.Class.class);
		String className = classAnn.value().replace('.', '/');
		classTargetName = classAnn.value();
		if (!className.equals(classTargetName)) {
			LOGGER.debug("Retargeted {} from {} to {}", getClass().getSimpleName(), className, classTargetName);
		}
		for (final Method method : getClass().getMethods()) {
			final String name = method.getName();
			final boolean isStatic = Modifier.isStatic(method.getModifiers());

			for (final Patch.Method a : method.getAnnotationsByType(Patch.Method.class)) {
				final String methodSign = a.value();
				final int index = methodSign.indexOf('(');
				final String methodName = methodSign.substring(0, index -1);
				final String methodDesc = methodSign.substring(index, methodSign.length() -1);
				String desc = MiniUtils.remapMethod(className, methodName, methodDesc) + MiniUtils.remapMethodDesc(methodDesc);

				if (DEBUG) {
					if (!methodSign.equals(desc)) {
						logForDeobfuscation(name, methodSign, desc);
					}
				}
				if (!methods.containsKey(desc)) {
					methods.put(desc, new ArrayList<>());
				}

				final boolean frames = method.getAnnotation(Patch.Method.AffectsControlFlow.class) != null;

				if (isStatic) {
					methods.get(desc).add(new PatchMethod() {
						@Override
						public boolean patch(PatchContext ctx) throws Throwable {
							try {
								method.invoke(null, ctx);
							} catch (InvocationTargetException e) {
								throw e.getCause();
							}
							return frames;
						}
						@Override
						public String toString() {
							return name;
						}
					});
				} else {
					methods.get(desc).add(new PatchMethod() {
						@Override
						public boolean patch(PatchContext ctx) throws Throwable {
							try {
								method.invoke(MiniTransformer.this, ctx);
							} catch (InvocationTargetException e) {
								throw e.getCause();
							}
							return frames;
						}
						@Override
						public String toString() {
							return name;
						}
					});
				}

				if (method.getAnnotation(Patch.Method.Optional.class) == null) {
					requiredMethods.add(desc);
				}

			}
		}

		for (final Field f : getClass().getFields()) {
			final String name = f.getName();
			final boolean isStatic = Modifier.isStatic(f.getModifiers());

			for (final Patch.Method a : f.getAnnotationsByType(Patch.Method.class)) {
				final String methodSign = a.value();
				final int index = methodSign.indexOf('(');
				final String methodName = methodSign.substring(0, index -1);
				final String methodDesc = methodSign.substring(index, methodSign.length() -1);
				String desc = MiniUtils.remapMethod(className, methodName, methodDesc) + MiniUtils.remapMethodDesc(methodDesc);

				if (DEBUG) {
					if (!methodSign.equals(desc)) {
						logForDeobfuscation(name, methodSign, desc);
					}
				}
				if (!methods.containsKey(desc)) {
					methods.put(desc, new ArrayList<>());
				}

				final boolean frames = f.getAnnotation(Patch.Method.AffectsControlFlow.class) != null;

				if (isStatic) {
                    try {
						final PatchMethod patchMethod = (PatchMethod) f.get(null);
						methods.get(desc).add(new PatchMethod() {
							@Override
							public boolean patch(PatchContext ctx) throws Throwable {
								return patchMethod.patch(ctx);
							}
							@Override
							public String toString() {
								return name;
							}
						});
                    } catch (IllegalAccessException | ClassCastException e) {
						throw new Error("Failed to load patch", e);
                    }
                } else {
					try {
						final PatchMethod patchMethod = (PatchMethod) f.get(MiniTransformer.this);
						methods.get(desc).add(new PatchMethod() {
							@Override
							public boolean patch(PatchContext ctx) throws Throwable {
								return patchMethod.patch(ctx);
							}
							@Override
							public String toString() {
								return name;
							}
						});
					} catch (IllegalAccessException | ClassCastException e) {
						throw new Error("Failed to load patch", e);
					}
				}

				if (f.getAnnotation(Patch.Method.Optional.class) == null) {
					requiredMethods.add(desc);
				}

			}
		}
	}
	private void logForDeobfuscation(String name, String className, String desc){
		LOGGER.debug("Retargeted {}.{} from {} to {}", getClass().getSimpleName(), name, className, desc);
	}
	public String getClassTargetName() {
		return classTargetName;
	}
	
	/**
	 * Override point to perform modifications to a class that Mini's method patching is not sufficient
	 * for. This can be used to add entirely new methods, add interfaces, add fields, etc.
	 * <p>
	 * <b>Mini and NilGradle will not be aware of what you are doing in this method, and therefore
	 * <i>no remapping will occur</i>!</b>
	 * @return {@code true} if frames need to be computed
	 */
	protected boolean modifyClassStructure(ClassNode clazz) {
		return false;
	}
	
	public String[] getTargets() {
		return new String[]{classTargetName};
	}

	@Override
	public int transform(ClassNode clazz) {
		boolean frames = modifyClassStructure(clazz);
		
		List<String> foundMethods = new ArrayList<>();
		Set<String> requiredsNotSeen = new HashSet<>(requiredMethods.size());
		requiredsNotSeen.addAll(requiredMethods);
		
		for (MethodNode mn : clazz.methods) {
			String name = mn.name + mn.desc;
			foundMethods.add(name);
			List<PatchMethod> li = methods.get(name);
			if (li != null) {
				for (PatchMethod pm : li) {
					try {
						PatchContext ctx = new PatchContext(mn);
						frames |= pm.patch(ctx);
						ctx.finish();
					} catch (Throwable t) {
						throw new Error("Failed to patch "+ classTargetName +"."+mn.name+mn.desc+ " via "+pm, t);
					}
					LOGGER.debug("[{}] Successfully transformed {}.{}{} via {}", getClass().getName(), classTargetName, mn.name, mn.desc, pm);
				}
			}
			requiredsNotSeen.remove(name);
		}
		
		if (!requiredsNotSeen.isEmpty()) {
			String msgS = makeSuchMethodNotFoundsString(requiredsNotSeen, foundMethods);
			throw new Error(msgS);
		}
		
		return frames ? ClassWriter.COMPUTE_MAXS : ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
	}

	private String makeSuchMethodNotFoundsString(Set<String> requiredsNotSeen, List<String> foundMethods) {
		StringBuilder msg = new StringBuilder();
		msg.append(requiredsNotSeen.size());
		msg.append(" required method");
		msg.append(requiredsNotSeen.size() == 1 ? " was" : "s were");
		msg.append(" not found while patching ");
		msg.append(classTargetName);
		msg.append("!");
		for (String name : requiredsNotSeen) {
			msg.append(" ");
			msg.append(name);
			msg.append(",");
		}
		msg.deleteCharAt(msg.length()-1);
		msg.append("\nThe following methods were found:");
		for (String name : foundMethods) {
			msg.append(" ");
			msg.append(name);
			msg.append(",");
		}
		msg.deleteCharAt(msg.length()-1);
        return msg.toString();
	}

}
