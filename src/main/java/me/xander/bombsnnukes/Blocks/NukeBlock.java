package me.xander.bombsnnukes.Blocks;

import me.xander.bombsnnukes.Entities.PrimedNukeEntity;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

// NukeBlock class
public class NukeBlock extends Block {
    public NukeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (world.hasNeighborSignal(pos)) {
                explode(world, pos);
                world.removeBlock(pos, false);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (world.hasNeighborSignal(pos)) {
            explode(world, pos);
            world.removeBlock(pos, false);
        }
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            world.removeBlock(pos, false);
            explode(world, pos);
            itemstack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            return ActionResultType.sidedSuccess(world.isClientSide);
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    private static void explode(World world, BlockPos pos) {
        if (!world.isClientSide()) {
            PrimedNukeEntity primedNuke = new PrimedNukeEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, null);
            world.addFreshEntity(primedNuke);
            world.playSound(null, primedNuke.getX(), primedNuke.getY(), primedNuke.getZ(), SoundEvents.TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);

            // Send an update packet to clients
            if (world instanceof ServerWorld) {
                ((ServerWorld) world).getChunkSource().broadcastAndSend(primedNuke, new SSpawnObjectPacket(primedNuke));
            }
        }
    }
}