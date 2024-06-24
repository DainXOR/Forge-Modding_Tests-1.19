package net.dain.testsmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import org.jetbrains.annotations.Nullable;

public class CoreBlock extends Block {
    private final float destroyNormalizer;
    private float destroyProgress = 0;
    private boolean isBeingMined = false;

    public CoreBlock(Properties properties) {
        super(properties);
        destroyNormalizer = 1 / defaultDestroyTime();
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide()) {
            if(hand == InteractionHand.MAIN_HAND && player.isShiftKeyDown()) {
                player.sendSystemMessage(Component.literal("[Core]: Life remaining: " + (defaultDestroyTime() - destroyProgress)));
                return super.use(state, level, pos, player, hand, hitResult);
            }
            for (var interaction : InteractionHand.values()) {
                player.sendSystemMessage(Component.literal("Interaction: " + interaction.name()));
            }
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter blockGetter, BlockPos pos) {
        float mineSpeed = super.getDestroyProgress(state, player, blockGetter, pos);
        destroyProgress += mineSpeed;
        //boolean  player.

        if(mineSpeed == 0){
            player.sendSystemMessage(Component.literal("Not breaking!"));
        }

        for (var interaction : InteractionHand.values()) {
            //player.sendSystemMessage(Component.literal("Interaction: " + interaction.name()));
        }

        if(destroyProgress >= defaultDestroyTime()){
            player.level.destroyBlock(pos, false, player, 3);
        }

        BlockHitResult
        return isBeingMined? mineSpeed : destroyProgress;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        float normalized = destroyProgress * destroyNormalizer;
        return (int) (normalized * 15);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean isSignalSource(BlockState p_60571_) {
        return true;
    }

    @Override
    public int getSignal(BlockState p_60483_, BlockGetter p_60484_, BlockPos p_60485_, Direction p_60486_) {
        return 15;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {

        double d0 = (double)pos.getX() + 0.5D + (randomSource.nextDouble() - 0.5D) * 0.2D;
        double d1 = (double)pos.getY() + 0.7D + (randomSource.nextDouble() - 0.5D) * 0.2D;
        double d2 = (double)pos.getZ() + 0.5D + (randomSource.nextDouble() - 0.5D) * 0.2D;
        level.addParticle(ParticleTypes.CRIT, d0, d1, d2, 0.0D, 0.0D, 0.0D);

    }
}
