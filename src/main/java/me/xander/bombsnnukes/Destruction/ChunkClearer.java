package me.xander.bombsnnukes.Destruction;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.network.play.server.SUpdateLightPacket;
import net.minecraft.network.play.server.SChunkDataPacket;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class ChunkClearer {
    public static void clearAndSyncChunk(World world, ChunkPos chunkPos) {
        if (!(world instanceof ServerWorld)) {
            return;
        }
        ServerWorld serverWorld = (ServerWorld) world;
        Chunk chunk = serverWorld.getChunk(chunkPos.x, chunkPos.z);

        // Clear the chunk
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 2; y < world.getHeight(); y++) {
                    chunk.setBlockState(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), false);
                }
            }
        }

        // Mark chunk for saving and client update
        chunk.setUnsaved(true);
        for (ChunkSection section : chunk.getSections()) {
            if (section != null) {
                section.recalcBlockCounts();
            }
        }

        BlockPos minPos = new BlockPos(chunkPos.x << 4, 0, chunkPos.z << 4);
        BlockPos maxPos = minPos.offset(15, 255, 15); // Chunk size is 16x16x256

        // Send chunk data to clients
        SUpdateLightPacket lightPacket = new SUpdateLightPacket(chunkPos, serverWorld.getChunkSource().getLightEngine(), true);
        SChunkDataPacket chunkPacket = new SChunkDataPacket(chunk, 65535);

        ServerChunkProvider chunkProvider = serverWorld.getChunkSource();
        chunkProvider.chunkMap.getPlayers(chunkPos, false).forEach(player -> {
            player.connection.send(lightPacket);
            player.connection.send(chunkPacket);
        });

        // Mark chunk for saving
        chunkProvider.save(true);
    }
}
