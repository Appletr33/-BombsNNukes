package me.xander.bombsnnukes.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.xander.bombsnnukes.Renderer.WhiteQuadRenderer;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "renderLevel",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/profiler/IProfiler;popPush(Ljava/lang/String;)V",
                    args = "ldc=hand",
                    shift = At.Shift.BEFORE))
    private void injectWhiteQuadRender(float p_228378_1_, long p_228378_2_, MatrixStack p_228378_4_, CallbackInfo ci) {
        WhiteQuadRenderer.renderWhiteQuad();
    }
}
