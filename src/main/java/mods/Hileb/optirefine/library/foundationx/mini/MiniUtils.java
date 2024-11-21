package mods.Hileb.optirefine.library.foundationx.mini;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.tree.AbstractInsnNode.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class MiniUtils {
	
	public static String describe(Iterable<? extends AbstractInsnNode> list) {
		StringBuilder sb = new StringBuilder();
		describe(sb, list);
		return sb.toString();
	}
	
	public static String describe(AbstractInsnNode[] arr) {
		return describe(Arrays.asList(arr));
	}

	public static String describe(AbstractInsnNode a) {
		StringBuilder sb = new StringBuilder();
		describeInner(sb, a, null);
		return sb.toString();
	}
	
	public static void describe(StringBuilder sb, Iterable<? extends AbstractInsnNode> list) {
		describe(sb, list, "");
	}
	
	public static void describe(StringBuilder sb, AbstractInsnNode[] arr) {
		describe(sb, Arrays.asList(arr), "");
	}

	public static void describe(StringBuilder sb, AbstractInsnNode a) {
		describeInner(sb, a, null);
	}
	
	public static void describe(StringBuilder sb, Iterable<? extends AbstractInsnNode> list, String indent) {
		Map<LabelNode, Integer> labelIds = new IdentityHashMap<>();
		boolean first = true;
		for (AbstractInsnNode n : list) {
			if (first) {
				first = false;
			} else {
				sb.append(",\n");
				sb.append(indent);
			}
			describeInner(sb, n, labelIds);
		}
	}
	
	public static void describe(StringBuilder sb, AbstractInsnNode[] arr, String indent) {
		describe(sb, Arrays.asList(arr), indent);
	}
	
	public static void describeInner(StringBuilder sb, AbstractInsnNode a, Map<LabelNode, Integer> labelIds) {
		if (a instanceof LabelNode) {
			if (labelIds == null) {
				sb.append("L").append(Integer.toHexString(System.identityHashCode(a)));
				return;
			}
			sb.append("L").append(labelIds.computeIfAbsent((LabelNode) a, (ln) -> labelIds.size()));
			return;
		} else if (a instanceof LineNumberNode) {
			sb.append("// line ").append(((LineNumberNode) a).line);
			return;
		} else if (a instanceof FrameNode) {
			sb.append("// frame: ");
			switch (((FrameNode)a).type) {
				case F_NEW:
					sb.append("new");
					break;
				case F_FULL:
					sb.append("full");
					break;
				case F_APPEND:
					sb.append("append");
					break;
				case F_CHOP:
					sb.append("chop");
					break;
				case F_SAME:
					sb.append("same");
					break;
			}
			return;
		}
		sb.append(getMnemonic(a.getOpcode()));
		sb.append("(");
		switch (a.getType()) {
			case INSN: {
				break;
			}
			case INT_INSN: {
				IntInsnNode n = (IntInsnNode)a;
				sb.append(n.operand);
				break;
			}
			case VAR_INSN: {
				VarInsnNode n = (VarInsnNode)a;
				sb.append(n.var);
				break;
			}
			case TYPE_INSN: {
				TypeInsnNode n = (TypeInsnNode)a;
				appendString(sb, n.desc);
				break;
			}
			case FIELD_INSN: {
				FieldInsnNode n = (FieldInsnNode)a;
				appendString(sb, n.owner).append(", ");
				appendString(sb, n.name).append(", ");
				appendString(sb, n.desc);
				break;
			}
			case METHOD_INSN: {
				MethodInsnNode n = (MethodInsnNode)a;
				appendString(sb, n.owner).append(", ");
				appendString(sb, n.name).append(", ");
				appendString(sb, n.desc);
				break;
			}
			case JUMP_INSN: {
				JumpInsnNode n = (JumpInsnNode)a;
				if (n.label == null) {
					sb.append("*");
				} else {
					describeInner(sb, n.label, labelIds);
				}
				break;
			}
			case LDC_INSN: {
				LdcInsnNode n = (LdcInsnNode)a;
				if (n.cst instanceof String) {
					appendString(sb, (String)n.cst);
				} else if (n.cst instanceof Integer) {
					sb.append(((Integer)n.cst).intValue());
				} else if (n.cst instanceof Long) {
					sb.append(((Long)n.cst).longValue());
					sb.append("L");
				} else if (n.cst instanceof Float) {
					sb.append(((Float)n.cst).floatValue());
					sb.append("f");
				} else if (n.cst instanceof Double) {
					sb.append(((Double)n.cst).doubleValue());
					sb.append("D");
				} else if (n.cst instanceof Type) {
					sb.append("/* type ");
					sb.append(n.cst);
					sb.append(" */");
				}
				break;
			}
			case IINC_INSN: {
				IincInsnNode n = (IincInsnNode)a;
				sb.append(n.var).append(", ");
				sb.append(n.incr);
				break;
			}
			case MULTIANEWARRAY_INSN: {
				MultiANewArrayInsnNode n = (MultiANewArrayInsnNode)a;
				appendString(sb, n.desc).append(", ");
				sb.append(n.dims);
				break;
			}
			case INVOKE_DYNAMIC_INSN:
			case TABLESWITCH_INSN:
			case LOOKUPSWITCH_INSN: {
				sb.append("...");
				break;
			}
			default: {
				sb.append("???????");
				break;
			}
		}
		sb.append(")");
	}

	private static StringBuilder appendString(StringBuilder sb, String str) {
		sb.append("\"");
		sb.append(str.replace("\\", "\\\\").replace("\"", "\\\""));
		sb.append("\"");
		return sb;
	}

	public static String remapType(String type) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapType(type);
	}
	public static String remapField(String owner, String name, String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(owner, name, desc);
	}
	public static String remapMethod(String owner, String name, String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc);
	}
	public static String remapMethodDesc(String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
	}
	public static String remapFieldDesc(String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapDesc(desc);
	}
	
	public static String getMnemonic(int opcode) {
        return switch (opcode) {
            case NOP -> "NOP";
            case ACONST_NULL -> "ACONST_NULL";
            case ICONST_M1 -> "ICONST_M1";
            case ICONST_0 -> "ICONST_0";
            case ICONST_1 -> "ICONST_1";
            case ICONST_2 -> "ICONST_2";
            case ICONST_3 -> "ICONST_3";
            case ICONST_4 -> "ICONST_4";
            case ICONST_5 -> "ICONST_5";
            case LCONST_0 -> "LCONST_0";
            case LCONST_1 -> "LCONST_1";
            case FCONST_0 -> "FCONST_0";
            case FCONST_1 -> "FCONST_1";
            case FCONST_2 -> "FCONST_2";
            case DCONST_0 -> "DCONST_0";
            case DCONST_1 -> "DCONST_1";
            case BIPUSH -> "BIPUSH";
            case SIPUSH -> "SIPUSH";
            case LDC -> "LDC";
            case ILOAD -> "ILOAD";
            case LLOAD -> "LLOAD";
            case FLOAD -> "FLOAD";
            case DLOAD -> "DLOAD";
            case ALOAD -> "ALOAD";
            case IALOAD -> "IALOAD";
            case LALOAD -> "LALOAD";
            case FALOAD -> "FALOAD";
            case DALOAD -> "DALOAD";
            case AALOAD -> "AALOAD";
            case BALOAD -> "BALOAD";
            case CALOAD -> "CALOAD";
            case SALOAD -> "SALOAD";
            case ISTORE -> "ISTORE";
            case LSTORE -> "LSTORE";
            case FSTORE -> "FSTORE";
            case DSTORE -> "DSTORE";
            case ASTORE -> "ASTORE";
            case IASTORE -> "IASTORE";
            case LASTORE -> "LASTORE";
            case FASTORE -> "FASTORE";
            case DASTORE -> "DASTORE";
            case AASTORE -> "AASTORE";
            case BASTORE -> "BASTORE";
            case CASTORE -> "CASTORE";
            case SASTORE -> "SASTORE";
            case POP -> "POP";
            case POP2 -> "POP2";
            case DUP -> "DUP";
            case DUP_X1 -> "DUP_X1";
            case DUP_X2 -> "DUP_X2";
            case DUP2 -> "DUP2";
            case DUP2_X1 -> "DUP2_X1";
            case DUP2_X2 -> "DUP2_X2";
            case SWAP -> "SWAP";
            case IADD -> "IADD";
            case LADD -> "LADD";
            case FADD -> "FADD";
            case DADD -> "DADD";
            case ISUB -> "ISUB";
            case LSUB -> "LSUB";
            case FSUB -> "FSUB";
            case DSUB -> "DSUB";
            case IMUL -> "IMUL";
            case LMUL -> "LMUL";
            case FMUL -> "FMUL";
            case DMUL -> "DMUL";
            case IDIV -> "IDIV";
            case LDIV -> "LDIV";
            case FDIV -> "FDIV";
            case DDIV -> "DDIV";
            case IREM -> "IREM";
            case LREM -> "LREM";
            case FREM -> "FREM";
            case DREM -> "DREM";
            case INEG -> "INEG";
            case LNEG -> "LNEG";
            case FNEG -> "FNEG";
            case DNEG -> "DNEG";
            case ISHL -> "ISHL";
            case LSHL -> "LSHL";
            case ISHR -> "ISHR";
            case LSHR -> "LSHR";
            case IUSHR -> "IUSHR";
            case LUSHR -> "LUSHR";
            case IAND -> "IAND";
            case LAND -> "LAND";
            case IOR -> "IOR";
            case LOR -> "LOR";
            case IXOR -> "IXOR";
            case LXOR -> "LXOR";
            case IINC -> "IINC";
            case I2L -> "I2L";
            case I2F -> "I2F";
            case I2D -> "I2D";
            case L2I -> "L2I";
            case L2F -> "L2F";
            case L2D -> "L2D";
            case F2I -> "F2I";
            case F2L -> "F2L";
            case F2D -> "F2D";
            case D2I -> "D2I";
            case D2L -> "D2L";
            case D2F -> "D2F";
            case I2B -> "I2B";
            case I2C -> "I2C";
            case I2S -> "I2S";
            case LCMP -> "LCMP";
            case FCMPL -> "FCMPL";
            case FCMPG -> "FCMPG";
            case DCMPL -> "DCMPL";
            case DCMPG -> "DCMPG";
            case IFEQ -> "IFEQ";
            case IFNE -> "IFNE";
            case IFLT -> "IFLT";
            case IFGE -> "IFGE";
            case IFGT -> "IFGT";
            case IFLE -> "IFLE";
            case IF_ICMPEQ -> "IF_ICMPEQ";
            case IF_ICMPNE -> "IF_ICMPNE";
            case IF_ICMPLT -> "IF_ICMPLT";
            case IF_ICMPGE -> "IF_ICMPGE";
            case IF_ICMPGT -> "IF_ICMPGT";
            case IF_ICMPLE -> "IF_ICMPLE";
            case IF_ACMPEQ -> "IF_ACMPEQ";
            case IF_ACMPNE -> "IF_ACMPNE";
            case GOTO -> "GOTO";
            case JSR -> "JSR";
            case RET -> "RET";
            case TABLESWITCH -> "TABLESWITCH";
            case LOOKUPSWITCH -> "LOOKUPSWITCH";
            case IRETURN -> "IRETURN";
            case LRETURN -> "LRETURN";
            case FRETURN -> "FRETURN";
            case DRETURN -> "DRETURN";
            case ARETURN -> "ARETURN";
            case RETURN -> "RETURN";
            case GETSTATIC -> "GETSTATIC";
            case PUTSTATIC -> "PUTSTATIC";
            case GETFIELD -> "GETFIELD";
            case PUTFIELD -> "PUTFIELD";
            case INVOKEVIRTUAL -> "INVOKEVIRTUAL";
            case INVOKESPECIAL -> "INVOKESPECIAL";
            case INVOKESTATIC -> "INVOKESTATIC";
            case INVOKEINTERFACE -> "INVOKEINTERFACE";
            case INVOKEDYNAMIC -> "INVOKEDYNAMIC";
            case NEW -> "NEW";
            case NEWARRAY -> "NEWARRAY";
            case ANEWARRAY -> "ANEWARRAY";
            case ARRAYLENGTH -> "ARRAYLENGTH";
            case ATHROW -> "ATHROW";
            case CHECKCAST -> "CHECKCAST";
            case INSTANCEOF -> "INSTANCEOF";
            case MONITORENTER -> "MONITORENTER";
            case MONITOREXIT -> "MONITOREXIT";
            case MULTIANEWARRAY -> "MULTIANEWARRAY";
            case IFNULL -> "IFNULL";
            case IFNONNULL -> "IFNONNULL";
            default -> "UNK_" + Integer.toHexString(opcode);
        };
	}
	
}
