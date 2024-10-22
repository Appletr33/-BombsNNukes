package me.xander.bombsnnukes.Entities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.prism.RenderTarget;
import me.xander.bombsnnukes.Destruction.ChunkClearer;
import me.xander.bombsnnukes.Renderer.WhiteQuadRenderer;
import me.xander.bombsnnukes.Utils.DelayUtility;
import me.xander.bombsnnukes.init.EntityInit;
import me.xander.bombsnnukes.init.SoundsInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

import static me.xander.bombsnnukes.Utils.SoundUtils.sendSoundToPlayer;

// PrimedNukeEntity class
public class PrimedNukeEntity extends TNTEntity {
    private static final DataParameter<Integer> FUSE = EntityDataManager.defineId(PrimedNukeEntity.class, DataSerializers.INT);
    private static final int DEFAULT_FUSE = 100; // 5 seconds
    private World world;

    public PrimedNukeEntity(EntityType<? extends PrimedNukeEntity> type, World world) {
        super(type, world);
    }

    public PrimedNukeEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(EntityInit.PRIMED_NUKE.get(), world);
        this.setPos(x, y, z);
        double d0 = world.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(DEFAULT_FUSE);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.world = world;

        try {
            Field livingEntity = TNTEntity.class.getDeclaredField("owner");
            livingEntity.setAccessible(true);  // Bypass the private modifier
            livingEntity.set(this, igniter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FUSE, DEFAULT_FUSE);
    }

    @Override
    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }

        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            if (!this.level.isClientSide) {
                this.explode();
            }
            else {
                ApplyVisualExplosionEffects();
            }

            this.remove();
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level.isClientSide) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private void ApplyVisualExplosionEffects()
    {
        WhiteQuadRenderer.ApplyBlindingEffect(10);
    }

    @Override
    protected void explode() {

        playExplosionSound();
        //this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 10.0F, Explosion.Mode.NONE);

        for (int x = -5; x < 5; x++)
            for (int z = -5; z < 5; z++)
            {
                ChunkClearer.clearAndSyncChunk(world, new ChunkPos(new BlockPos(this.getX() + x * 16, this.getY(), this.getZ() + z * 16)));
            }
    }

    private void playExplosionSound()
    {
        if (!world.isClientSide()) {
            ServerWorld serverWorld = (ServerWorld) this.level;

            BlockPos blockPos = new BlockPos(this.position());

            // Define the maximum range for the sound (e.g., 100 blocks)
            double maxRange = 500.0D;

            // Get all players within the range of the explosion
            AxisAlignedBB boundingBox = new AxisAlignedBB(blockPos).inflate(maxRange);
            List<ServerPlayerEntity> players = serverWorld.getEntitiesOfClass(ServerPlayerEntity.class, boundingBox);

            for (ServerPlayerEntity player : players) {
                // Calculate squared distance between player and explosion
                double distanceSquared = player.distanceToSqr(this.position());
                // Calculate volume based on the distance (linear falloff)
                double distance = Math.sqrt(distanceSquared);
                float volume = (float) (1.0D - distance  / maxRange);

                float soundTravelTime = (float) distance / 100;


                System.out.println("Playing at Volume: " + volume);
                if (volume > 0.0F) {
                    // Play the sound at the player's location, adjusting volume

                    DelayUtility.delayFunction(() -> {
                        sendSoundToPlayer(player, SoundsInit.SOUND_NUCLEAR_EXPLOSION.get(), blockPos, SoundCategory.MASTER, volume, 1.0F);
                    }, soundTravelTime);
                    System.out.println("PLAYING SOUND EFFECT");
                }
            }
        }
    }

    public int getFuse() {
        return this.entityData.get(FUSE);
    }

    public void setFuse(int fuse) {
        this.entityData.set(FUSE, fuse);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}