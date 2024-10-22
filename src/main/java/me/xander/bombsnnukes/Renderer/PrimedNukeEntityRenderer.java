package me.xander.bombsnnukes.Renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.xander.bombsnnukes.BombsnNukes;
import me.xander.bombsnnukes.Entities.PrimedNukeEntity;
import me.xander.bombsnnukes.Models.Entities.PrimedNukeEntityModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTRenderer;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class PrimedNukeEntityRenderer extends TNTRenderer {
    private static final ResourceLocation NUKE_TEXTURE = new ResourceLocation(BombsnNukes.MOD_ID, "textures/entity/primed_nuke.png");
    private final PrimedNukeEntityModel<PrimedNukeEntity> nukeModel;

    public PrimedNukeEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.nukeModel = new PrimedNukeEntityModel<>();
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(TNTEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.0D, 0.0D);

        // Add circular rotation
        float rotationSpeed = 2.0F; // Adjust this value to change rotation speed
        float rotationAngle = (entity.tickCount + partialTicks) * rotationSpeed;
        float radius = 0.25F; // Adjust this value to change the circle's radius

        // Calculate the X and Z offsets for circular motion
        float xOffset = MathHelper.sin(rotationAngle) * radius;
        float zOffset = MathHelper.cos(rotationAngle) * radius;

        float scale = 1.0f;
        if ((float)entity.getFuse() - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)entity.getFuse() - partialTicks + 1.0F) / 10.0F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            scale = 1.0F + f * 0.3F;
            scale *= scale * scale;
            matrixStack.scale(scale, scale, scale);
        }

        // Apply the circular motion
        matrixStack.translate(scale * xOffset, 0, scale * zOffset);

        // Rotate the entity around its Y-axis
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotationAngle));

        this.nukeModel.renderToBuffer(matrixStack, buffer.getBuffer(this.nukeModel.renderType(this.getTextureLocation(entity))), packedLight, 0, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(TNTEntity entity) {
        return NUKE_TEXTURE;
    }
}