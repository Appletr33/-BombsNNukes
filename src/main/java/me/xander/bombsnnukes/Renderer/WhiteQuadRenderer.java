package me.xander.bombsnnukes.Renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class WhiteQuadRenderer {
    private static final Minecraft mc = Minecraft.getInstance();

    private static int it = 0;
    private static boolean blind = false;
    private static float fadeTime = 0f;
    private static float remainingFadeTime = 0f;
    private static long lastFrameTime;

    public static void ApplyBlindingEffect(float time)
    {
        blind = true;
        fadeTime = time;
        remainingFadeTime = fadeTime;
        lastFrameTime = System.currentTimeMillis();
    }

    public static void renderWhiteQuad() {
        // Save the current OpenGL state
        if (!blind)
            return;

        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;
        float deltaTimeSeconds = deltaTime / 1000.0F;

        remainingFadeTime -= deltaTimeSeconds;

        if (remainingFadeTime <= 0)
        {
            blind = false;
        }

        float alpha = 1.0F;
        if (remainingFadeTime < fadeTime / 2)
        {
            alpha = remainingFadeTime / (fadeTime / 2);
        }
        if (alpha < 0.0F) {
            alpha = 0.0F;
        }


        RenderSystem.pushMatrix();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Set up orthographic projection
        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.loadIdentity();
        RenderSystem.ortho(0.0D, mc.getWindow().getWidth(), mc.getWindow().getHeight(), 0.0D, 1000.0D, 3000.0D);
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        RenderSystem.loadIdentity();
        RenderSystem.translatef(0.0F, 0.0F, -2000.0F);

        // Set color to white
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);

        // Render the quad
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(0, mc.getWindow().getHeight());
        GL11.glVertex2f(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        GL11.glVertex2f(mc.getWindow().getWidth(), 0);
        GL11.glEnd();

        // Restore the OpenGL state
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();

        System.out.println("Called " + ++it);
    }
}
