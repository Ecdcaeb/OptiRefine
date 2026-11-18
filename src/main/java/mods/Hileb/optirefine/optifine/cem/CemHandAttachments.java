package mods.Hileb.optirefine.optifine.cem;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;

public record CemHandAttachments(Vec3d left, Vec3d right) {
    public interface Part {
        void optiRefine$setHandAttachments(CemHandAttachments attachments);
        void optiRefine$setAttachmentParent(Part parent);
        void optiRefine$markHandAttachments();
        boolean optiRefine$isAttachmentRoot();
        boolean optiRefine$applyHandAttachment(EnumHandSide side, float scale);
        void optiRefine$applyAttachmentTransforms(float scale);
    }

    public Vec3d forHand(EnumHandSide side) {
        return side == EnumHandSide.LEFT ? left : right;
    }

    public static boolean apply(ModelBase model, EnumHandSide side, float scale) {
        for (int i = 0; i < model.boxList.size(); i++) {
            ModelRenderer renderer = model.boxList.get(i);
            Part part = (Part) renderer;
            if (part.optiRefine$isAttachmentRoot() && part.optiRefine$applyHandAttachment(side, scale)) {
                return true;
            }
        }
        return false;
    }
}
