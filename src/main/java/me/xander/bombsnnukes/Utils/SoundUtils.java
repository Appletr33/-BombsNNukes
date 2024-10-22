package me.xander.bombsnnukes.Utils;

import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class SoundUtils {

    public static void sendSoundToPlayer(ServerPlayerEntity player, SoundEvent sound, BlockPos pos, SoundCategory category, float volume, float pitch) {
        // Create the sound effect packet
        SPlaySoundEffectPacket packet = new SPlaySoundEffectPacket(
                sound,           // The sound event
                category,        // Sound category (e.g., BLOCKS, WEATHER)
                pos.getX(),      // X position
                pos.getY(),      // Y position
                pos.getZ(),      // Z position
                volume,          // Volume
                pitch            // Pitch
        );

        // Send the packet to the player
        player.connection.send(packet);
    }
}