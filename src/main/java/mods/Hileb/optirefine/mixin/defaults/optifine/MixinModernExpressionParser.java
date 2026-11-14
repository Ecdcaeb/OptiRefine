package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.cem.CemFunctions;
import net.optifine.expr.IExpression;
import net.optifine.expr.ParseException;
import net.optifine.expr.Token;
import net.optifine.expr.TokenType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Deque;

@Mixin(targets = "net.optifine.expr.ExpressionParser", remap = false)
public abstract class MixinModernExpressionParser {
    @Shadow
    private static native Deque<Token> getGroup(Deque<Token> tokens, TokenType end, boolean required) throws ParseException;

    @Shadow
    private native IExpression[] parseExpressions(Deque<Token> tokens) throws ParseException;

    @Inject(method = "parseExpression", at = @At("HEAD"), cancellable = true)
    private void optiRefine$modernFunction(Deque<Token> tokens, CallbackInfoReturnable<IExpression> cir) throws ParseException {
        Token token = tokens.peek();
        if (token == null || token.getType() != TokenType.IDENTIFIER || !CemFunctions.handles(token.getText())) {
            return;
        }
        String name = tokens.remove().getText();
        Token next = tokens.peek();
        IExpression[] arguments;
        if (next != null && next.getType() == TokenType.BRACKET_OPEN) {
            tokens.remove();
            arguments = parseExpressions(getGroup(tokens, TokenType.BRACKET_CLOSE, true));
        } else {
            if (!CemFunctions.permitsBare(name)) {
                throw new ParseException("Missing arguments: " + name);
            }
            arguments = new IExpression[0];
        }
        cir.setReturnValue(CemFunctions.create(name, arguments));
    }
}
