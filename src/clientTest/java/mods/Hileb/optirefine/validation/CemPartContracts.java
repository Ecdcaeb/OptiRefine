package mods.Hileb.optirefine.validation;

import com.google.gson.JsonObject;
import mods.Hileb.optirefine.optifine.cem.CemHandAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.optifine.entity.model.CustomEntityModel;
import net.optifine.entity.model.CustomEntityModelParser;
import net.optifine.entity.model.CustomEntityModels;
import net.optifine.entity.model.CustomEntityRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.entity.model.ModelAdapterZombie;
import net.optifine.player.PlayerItemParser;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import java.util.Arrays;

final class CemPartContracts {
    private static final String DOMAIN = "optirefine_client_contracts";
    private static final float MODEL_SCALE = 1.0F / 16.0F;

    private CemPartContracts() {}

    static void verify() throws Exception {
        JsonObject jem = CustomEntityModelParser.loadJson(new ResourceLocation(DOMAIN, "cem/parts_probe.jem"));
        CustomEntityRenderer definition = CustomEntityModelParser.parseEntityRender(jem, "optifine/cem/zombie.jem");
        Method makeRenderer = CustomEntityModels.class.getDeclaredMethod("makeEntityRender", ModelAdapter.class, CustomEntityRenderer.class);
        makeRenderer.setAccessible(true);
        RenderLivingBase<?> renderer = (RenderLivingBase<?>) makeRenderer.invoke(null, new ModelAdapterZombie(), definition);
        require(renderer != null, "G5 could not initialize the JEM boolean animations");
        ModelBiped model = (ModelBiped) renderer.getMainModel();
        model.isChild = false;
        ModelRenderer body = model.bipedBody;
        ModelRenderer root = definition.getCustomModelRenderers()[0].getModelRenderer();
        require(body.childModels.contains(root), "G5 did not attach the parsed JPM to the native model");
        require(root.childModels != null && root.childModels.size() == 1, "External JPM submodel was not loaded");
        ModelRenderer child = root.childModels.getFirst();
        verifyGeometry(root, child);
        verifyVisibilityAndAttachments(renderer, model, body, root, child);
    }

    private static void verifyGeometry(ModelRenderer root, ModelRenderer child) throws Exception {
        JsonObject scalar = CustomEntityModelParser.loadJson(new ResourceLocation(DOMAIN, "cem/parts_body.jpm"));
        scalar.getAsJsonArray("boxes").get(0).getAsJsonObject().remove("sizesAdd");
        scalar.getAsJsonObject("submodel").getAsJsonArray("boxes").get(0).getAsJsonObject().remove("sizesAdd");
        ModelRenderer reference = PlayerItemParser.parseModelRenderer(scalar, new CustomEntityModel(), new int[]{64,64}, "optifine/cem");
        ModelBox offset = root.cubeList.getFirst();
        ModelBox faces = child.cubeList.getFirst();
        checkBounds(offset, -10, 4.5, -15, -2, 10.5, -3);
        checkBounds(faces, -0.5, 0, 0.5, 0.5, 5, 7.5);
        checkBounds(child.cubeList.get(1), 0.75, 1.75, 2.75, 3.25, 5.25, 7.25);
        compareUvs(offset, reference.cubeList.getFirst());
        compareUvs(faces, reference.childModels.getFirst().cubeList.getFirst());
        require(root.mirror && ModelRenderer.class.getField("mirrorV").getBoolean(root), "JPM mirror flags changed");
        int visibleFaces = 0;
        boolean down = false;
        boolean front = false;
        for (TexturedQuad quad : quads(faces)) {
            if (quad == null) continue;
            ++visibleFaces;
            down |= Arrays.stream(quad.vertexPositions).allMatch(vertex -> vertex.vector3D.y == 5);
            front |= Arrays.stream(quad.vertexPositions).allMatch(vertex -> vertex.vector3D.z == 0.5);
        }
        require(visibleFaces == 2 && down && front, "Sparse UVs must render only the down and front faces");
        require(quads(offset)[0].vertexPositions[0].vector3D.x == -10, "Mirrored box winding or side changed");
    }

    private static void verifyVisibilityAndAttachments(RenderLivingBase<?> renderer, ModelBiped model, ModelRenderer body,
                                                        ModelRenderer root, ModelRenderer child) throws Exception {
        Method displayList = ModelRenderer.class.getMethod("getDisplayList");
        int matrixMode = GL11.glGetInteger(GL11.GL_MATRIX_MODE);
        float pointSize = GL11.glGetFloat(GL11.GL_POINT_SIZE);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.pushMatrix();
        try {
            GlStateManager.loadIdentity();
            body.rotationPointX = 1;
            body.rotationPointY = 1;
            body.render(MODEL_SCALE);
            require(child.rotationPointZ == 8 && displayList(displayList, root) != 0 && displayList(displayList, child) == 0,
                    "visible_boxes must suppress only the target's own geometry");
            body.rotationPointY = 0;
            body.render(MODEL_SCALE);
            require(displayList(displayList, child) != 0, "A hidden parent box suppressed descendant geometry");

            labelDisplayList(displayList(displayList, root), 2);
            labelDisplayList(displayList(displayList, child), 3);
            expectVisibleGeometry(body, false, 3);
            expectVisibleGeometry(body, true, 3);
            body.rotationPointY = 1;
            expectVisibleGeometry(body, false, 2);
            expectVisibleGeometry(body, true, 2);
            body.rotationPointX = 0;
            expectVisibleGeometry(body, false, 1);
            require(child.rotationPointZ == 4, "The JEM did not read whole-branch visibility as a boolean");
            expectVisibleGeometry(body, true, 1);
            require(!CemHandAttachments.apply(model, EnumHandSide.LEFT, MODEL_SCALE), "A hidden branch supplied a handheld anchor");

            body.rotationPointX = 1;
            body.rotationPointY = 0;
            expectVisibleGeometry(body, false, 3);
            verifyAnchor(model, EnumHandSide.RIGHT, -3, 7, -10);
            verifyAnchor(model, EnumHandSide.LEFT, -1, 13, 18);
            verifyHandLayer(renderer, body);
        } finally {
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(matrixMode);
            GL11.glPointSize(pointSize);
            for (ModelRenderer part : new ModelRenderer[]{body, root, child}) {
                int list = displayList(displayList, part);
                if (list != 0) GLAllocation.deleteDisplayLists(list);
            }
        }
    }

    private static void verifyHandLayer(RenderLivingBase<?> renderer, ModelRenderer body) {
        final class HandLayer extends LayerHeldItem {
            int vanillaHands;

            HandLayer() {
                super(renderer);
            }

            @Override
            protected void translateToHand(EnumHandSide side) {
                vanillaHands++;
                super.translateToHand(side);
            }
        }
        HandLayer layer = new HandLayer();
        EntityZombie zombie = new EntityZombie(Minecraft.getMinecraft().world);
        zombie.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.STICK));
        zombie.setHeldItem(EnumHand.OFF_HAND, new ItemStack(Items.BONE));
        layer.doRenderLayer(zombie, 0, 0, 0, 0, 0, 0, MODEL_SCALE);
        require(layer.vanillaHands == 0, "Native held item layer ignored the custom attachment anchors");
        body.rotationPointX = 0;
        body.render(MODEL_SCALE);
        layer.doRenderLayer(zombie, 0, 0, 0, 0, 0, 0, MODEL_SCALE);
        require(layer.vanillaHands == 2, "Native held item layer did not restore vanilla hand placement");
    }

    private static void verifyAnchor(ModelBiped model, EnumHandSide side, float x, float y, float z) {
        GlStateManager.pushMatrix();
        try {
            require(CemHandAttachments.apply(model, side, MODEL_SCALE), "Missing " + side + " JPM anchor");
            FloatBuffer transform = BufferUtils.createFloatBuffer(16);
            GL11C.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, transform);
            require(Math.abs(transform.get(12) - x * MODEL_SCALE) < 0.00001F
                    && Math.abs(transform.get(13) - y * MODEL_SCALE) < 0.00001F
                    && Math.abs(transform.get(14) - z * MODEL_SCALE) < 0.00001F,
                    "Incorrect animated " + side + " anchor transform");
        } finally {
            GlStateManager.popMatrix();
        }
    }

    private static void expectVisibleGeometry(ModelRenderer part, boolean withRotation, float pointSize) {
        GL11.glPointSize(1);
        if (withRotation) part.renderWithRotation(MODEL_SCALE);
        else part.render(MODEL_SCALE);
        require(GL11.glGetFloat(GL11.GL_POINT_SIZE) == pointSize, "Incorrect CEM display list executed");
    }

    private static int displayList(Method getter, ModelRenderer part) throws Exception {
        return (int) getter.invoke(part);
    }

    private static void labelDisplayList(int list, float pointSize) {
        GL11.glNewList(list, GL11.GL_COMPILE);
        GL11.glPointSize(pointSize);
        GL11.glEndList();
    }

    private static TexturedQuad[] quads(ModelBox box) throws IllegalAccessException {
        return (TexturedQuad[]) QUADS.get(box);
    }

    private static final Field QUADS = quadField();

    private static Field quadField() {
        for (Field field : ModelBox.class.getDeclaredFields()) {
            if (field.getType() == TexturedQuad[].class) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new AssertionError("Missing ModelBox quad data");
    }

    private static void compareUvs(ModelBox actual, ModelBox reference) throws IllegalAccessException {
        TexturedQuad[] expanded = quads(actual);
        TexturedQuad[] scalar = quads(reference);
        for (int face = 0; face < scalar.length; face++) {
            require((scalar[face] == null) == (expanded[face] == null), "Box inflation changed available UV faces");
            if (scalar[face] == null) continue;
            for (int i = 0; i < scalar[face].vertexPositions.length; i++) {
                PositionTextureVertex a = expanded[face].vertexPositions[i];
                PositionTextureVertex b = scalar[face].vertexPositions[i];
                require(a.texturePositionX == b.texturePositionX && a.texturePositionY == b.texturePositionY,
                        "Box inflation changed G5 UV coordinates or winding");
            }
        }
    }

    private static void checkBounds(ModelBox box, double x0, double y0, double z0, double x1, double y1, double z1)
            throws IllegalAccessException {
        double minX = Double.POSITIVE_INFINITY, minY = minX, minZ = minX;
        double maxX = Double.NEGATIVE_INFINITY, maxY = maxX, maxZ = maxX;
        for (TexturedQuad quad : quads(box)) {
            if (quad == null) continue;
            for (PositionTextureVertex vertex : quad.vertexPositions) {
                Vec3d p = vertex.vector3D;
                minX = Math.min(minX, p.x);
                minY = Math.min(minY, p.y);
                minZ = Math.min(minZ, p.z);
                maxX = Math.max(maxX, p.x);
                maxY = Math.max(maxY, p.y);
                maxZ = Math.max(maxZ, p.z);
            }
        }
        require(minX == x0 && minY == y0 && minZ == z0 && maxX == x1 && maxY == y1 && maxZ == z1,
                "Unexpected rendered box bounds " + new Vec3d(minX, minY, minZ) + " to " + new Vec3d(maxX, maxY, maxZ));
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
