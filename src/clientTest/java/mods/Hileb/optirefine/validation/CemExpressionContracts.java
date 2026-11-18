package mods.Hileb.optirefine.validation;

import mods.Hileb.optirefine.optifine.cem.CemRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.passive.EntityWolf;
import net.optifine.entity.model.CustomModelRegistry;
import net.optifine.entity.model.CustomModelRenderer;
import net.optifine.entity.model.ModelAdapter;
import net.optifine.entity.model.anim.ModelResolver;
import net.optifine.expr.ExpressionParser;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

import java.util.ArrayList;
import java.util.List;

final class CemExpressionContracts {
    private CemExpressionContracts() {}

    static void verify(Minecraft mc) throws ParseException {
        ModelResolver wolfModel = resolver("wolf");
        ExpressionParser parser = new ExpressionParser(wolfModel);
        IExpressionBool anyWolfState = parser.parseBool("is_aggressive || is_hurt || is_glowing || is_sitting || is_tamed");
        IExpressionBool allWolfStates = parser.parseBool("is_aggressive && is_hurt && is_glowing && is_sitting && is_tamed");
        IExpressionBool aggressive = parser.parseBool("is_aggressive");
        IExpressionBool inGui = parser.parseBool("is_in_gui");
        IExpressionBool onShoulder = parser.parseBool("is_on_shoulder");
        EntityWolf wolf = new EntityWolf(mc.world);
        EntityVex vex = new EntityVex(mc.world);
        require(!anyWolfState.eval() && !inGui.eval() && !onShoulder.eval(), "CEM state leaked outside entity rendering");
        CemRenderContext.push(wolf, mc.world, 0.25F);
        try {
            require(!anyWolfState.eval(), "CEM reported a native predicate for a calm, untamed wolf");
            wolf.setAngry(true);
            wolf.hurtTime = 4;
            wolf.setGlowing(true);
            wolf.setTamed(true);
            wolf.setSitting(true);
            require(allWolfStates.eval(), "CEM predicates did not follow the wolf's native state");
            CemRenderContext.push(vex, mc.world, 0.5F);
            try {
                require(!anyWolfState.eval(), "Nested CEM evaluation reused the enclosing wolf");
                vex.setCharging(true);
                require(aggressive.eval(), "CEM aggression missed a charging Vex's client state");
                vex.setCharging(false);
                vex.setAttackTarget(wolf);
                require(aggressive.eval(), "CEM aggression missed the entity's native attack target");
            } finally {
                CemRenderContext.pop();
            }
            require(allWolfStates.eval(), "Nested CEM evaluation lost the enclosing wolf");
            verifyGui(mc, wolf, inGui);
            require(!inGui.eval() && !onShoulder.eval() && CemRenderContext.entity() == wolf,
                    "Inventory preview did not restore the enclosing CEM context");
        } finally {
            CemRenderContext.pop();
        }

        ModelResolver chestModel = resolver("chest");
        for (String name : List.of("is_aggressive", "is_glowing", "is_hurt", "is_in_gui", "is_on_shoulder", "is_sitting", "is_tamed")) {
            require(chestModel.getExpression(name) == null, "Block entity resolved entity predicate " + name);
        }
        require(chestModel.getExpression("frame_counter") != null && chestModel.getExpression("day_time") != null,
                "Block entity lost shared render parameters");
        verifyPrintFunctions(parser);
    }

    static IExpressionBool shoulderExpression() throws ParseException {
        return new ExpressionParser(resolver("parrot")).parseBool("is_on_shoulder");
    }

    private static ModelResolver resolver(String name) {
        ModelAdapter adapter = CustomModelRegistry.getModelAdapter(name);
        return new ModelResolver(adapter, adapter.makeModel(), new CustomModelRenderer[0]);
    }

    private static void verifyGui(Minecraft mc, EntityWolf wolf, IExpressionBool inGui) {
        RenderManager manager = mc.getRenderManager();
        GuiWolfRenderer probe = new GuiWolfRenderer(manager, inGui);
        Render<? extends Entity> previous = manager.entityRenderMap.put(EntityWolf.class, probe);
        try {
            GuiInventory.drawEntityOnScreen(0, 0, 16, 0, 0, wolf);
            require(probe.renders == 1 && probe.inGui, "Inventory preview did not set is_in_gui for the wolf");
            manager.renderEntity(wolf, 0, 0, 0, 0, 0, true);
            require(probe.renders == 2 && !probe.inGui, "Ordinary render inherited the inventory preview's GUI state");
        } finally {
            manager.entityRenderMap.put(EntityWolf.class, previous);
        }
    }

    private static void verifyPrintFunctions(ExpressionParser parser) throws ParseException {
        List<String> messages = new ArrayList<>();
        AbstractAppender appender = new AbstractAppender("CemPrintContract", null, null, true, Property.EMPTY_ARRAY) {
            @Override
            public void append(LogEvent event) {
                messages.add(event.getMessage().getFormattedMessage());
            }
        };
        Logger logger = (Logger) LogManager.getLogger("OptiRefine/CEM");
        appender.start();
        logger.addAppender(appender);
        try {
            require(parser.parseFloat("1 + print(701, 1, lerp(0.5, 2, 6))").eval() == 5F, "print changed its float value");
            require(parser.parseBool("printb(702, 1, ifb(true, true, false)) && true").eval(), "printb changed its boolean value");
            require(parser.parseFloat("print(703, 2, 8)").eval() == 8F, "print's value depended on its frame interval");
            require(!parser.parseBool("printb(704, 0, false)").eval(), "printb's value depended on its frame interval");
            List<String> expected = new ArrayList<>(List.of("CEM print [701] = 4.0", "CEM printb [702] = true"));
            if ((CemRenderContext.frameCounter() & 1) == 0) {
                expected.add("CEM print [703] = 8.0");
            }
            require(messages.equals(expected), "CEM print functions logged unexpected IDs, values, or cadence: " + messages);
        } finally {
            logger.removeAppender(appender);
            appender.stop();
        }
        reject(parser, "print(1, 2)");
        reject(parser, "print(1, true, 2)");
        reject(parser, "print(1, 2, true)");
        reject(parser, "printb(true, 2, true)");
        reject(parser, "printb(1, 2, 3)");
    }

    private static void reject(ExpressionParser parser, String expression) {
        try {
            parser.parse(expression);
            throw new AssertionError("CEM accepted an invalid expression: " + expression);
        } catch (ParseException expected) {
            // The native parser validates arity and expression types at parse time.
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static final class GuiWolfRenderer extends RenderWolf {
        private final IExpressionBool expression;
        private int renders;
        private boolean inGui;

        private GuiWolfRenderer(RenderManager manager, IExpressionBool expression) {
            super(manager);
            this.expression = expression;
        }

        @Override
        protected void renderModel(EntityWolf wolf, float limbSwing, float limbSwingAmount, float ageInTicks,
                                   float netHeadYaw, float headPitch, float scaleFactor) {
            require(CemRenderContext.entity() == wolf, "Wolf model was rendered with the wrong CEM entity");
            ++renders;
            inGui = expression.eval();
            super.renderModel(wolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        }
    }
}
