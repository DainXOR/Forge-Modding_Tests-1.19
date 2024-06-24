package net.dain.testsmod.block.custom;

import net.dain.testsmod.block.entity.ModBlockEntities;
import net.dain.testsmod.block.entity.ReactorBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;


public class ReactorBlock extends BaseEntityBlock {
    //public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;

    private int COOL_RATE = 1;
    public static final BooleanProperty COOLED = BooleanProperty.create("cooled");

    public static final int MIN_POWER = 2;
    public static final int MAX_POWER = 15;
    public static final IntegerProperty POWER = BlockStateProperties.POWER;


    private static final int MIN_USAGE_STEP = 1;
    private int USAGE_STEP = 1;
    public final int TOP_USAGE;
    public final int MAX_USAGE;
    public int USAGE = 0;

    private boolean entity_on = false;

    // private static final VoxelShape SHAPE = ;

    public ReactorBlock(BlockBehaviour.Properties properties) {
        this(properties, 1200, 2400);
    }
    public  ReactorBlock(BlockBehaviour.Properties properties, int topUsage, int maxUsage){
        super(properties);
        this.TOP_USAGE = topUsage;
        this.MAX_USAGE = maxUsage;

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false)
                .setValue(UNSTABLE, false)
                .setValue(COOLED, false)

                .setValue(POWER, MIN_POWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        //builder.add(FACING);
        builder.add(POWERED, UNSTABLE, COOLED);
        builder.add(POWER);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {

        if(!level.isClientSide()){
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof ReactorBlockEntity){
                if(hand == InteractionHand.MAIN_HAND && player.isShiftKeyDown()){
                    generateStatusMessage(player, state);
                }
                else {
                    NetworkHooks.openScreen((ServerPlayer) player, (ReactorBlockEntity) entity, blockPos);
                }
            }else {
                throw new IllegalStateException("The container provider is missing");
            }
        }

        if (hand == InteractionHand.MAIN_HAND) {
            if (player.isShiftKeyDown()) {
                player.sendSystemMessage(Component.literal("[Reactor] Active: " + state.getValue(POWERED)));
                player.sendSystemMessage(Component.literal("[Reactor] Stable: " + !state.getValue(UNSTABLE)));
                player.sendSystemMessage(Component.literal("[Reactor] Cooled: " + state.getValue(COOLED)));

                player.sendSystemMessage(Component.literal("[Reactor] Power output: " + state.getValue(POWER)));
                player.sendSystemMessage(Component.literal("[Reactor] Usage: " + USAGE));

            } else {
                generateStatusMessage(player, state);
            }
        }


        return InteractionResult.sidedSuccess(level.isClientSide());

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorBlockEntity(pos, state, TOP_USAGE, MAX_USAGE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.REACTOR_BLOCK_ENTITY.get(), ReactorBlockEntity::tick);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return state.getValue(POWERED)? state.getValue(POWER) : 0;
    }

    @Override
    public int getSignal(BlockState p_60483_, BlockGetter p_60484_, BlockPos p_60485_, Direction p_60486_) {
        return 0;
    }

    protected boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) {
        return level.hasSignal(pos.below(), Direction.DOWN);
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return false;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {

        if (!level.isClientSide() && state.getValue(POWERED)) {

            setUsageStep(MIN_USAGE_STEP);
            affectEntity(level, pos, state, entity);
            addUsage(level, pos, state);

            if (USAGE >= TOP_USAGE) {
                setUnstable(level, pos, state, Boolean.TRUE);
            }

        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {

        setCooled(level, pos, state, Boolean.FALSE);
        entity_on = false;

        int coolers = hasCoolersAround(level, pos, state);
        if (coolers > 0) {
            setCoolRate(coolers);
            setCooled(level, pos, state, Boolean.TRUE);
        }

        if (USAGE == 0) {
            setUnstable(level, pos, state, Boolean.FALSE);
        }else if (USAGE > 0) {
            cool(level, pos, state);
        }

        super.tick(state, level, pos, randomSource);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (state.getValue(POWERED)) {
            double d0 = (double)pos.getX() + 0.5D + (randomSource.nextDouble() - 0.5D) * 0.2D;
            double d1 = (double)pos.getY() + 0.7D + (randomSource.nextDouble() - 0.5D) * 0.2D;
            double d2 = (double)pos.getZ() + 0.5D + (randomSource.nextDouble() - 0.5D) * 0.2D;
            level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    private void affectEntity(Level level, BlockPos pos, BlockState state, Entity entity) {

        addUsageStep(1);

        if (entity instanceof LivingEntity livingEntity) {
            entity_on = true;
            addPower(level, pos, state, 2);
            addUsageStep(1);

            if ((distanceToLimit(state)) > 0) {

                // livingEntity.setSpeed(0);
                //livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 9, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 5, 2, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5, 1, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 2, 0, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20, 9, true, false, false));
                //livingEntity.addEffect();

                if (livingEntity.isShiftKeyDown()) {
                    addPower(level, pos, state, 3);
                    addUsageStep(2);
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 5, 5, true, false, false));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 1, true, false, false));
                }
            }

            if (livingEntity instanceof Player player) {
                player.sendSystemMessage(Component.literal("Time left: " + distanceToLimit(state)));
            }
            if (state.getValue(UNSTABLE)) {
                overloadEffect(level, pos, state, livingEntity);
            }
        }
    }
    private void overloadEffect(Level level, BlockPos blockPos, BlockState state, LivingEntity livingEntity) {

        addPower(level, blockPos, state, 5);
        int effectMultiplier = (USAGE - TOP_USAGE) / 100;

        livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100 + (effectMultiplier * 20), effectMultiplier));
        livingEntity.hurt(DamageSource.MAGIC, 1.0f + (effectMultiplier / 4f));

        switch (effectMultiplier) {
            default:
                if (effectMultiplier < 2) {
                    break;
                }
            case 12: {
                EntityType.TNT.spawn((ServerLevel) level, null, null, blockPos, MobSpawnType.EVENT, true, true);
                EntityType.TNT.spawn((ServerLevel) level, null, null, blockPos, MobSpawnType.EVENT, true, true);
                EntityType.TNT.spawn((ServerLevel) level, null, null, blockPos, MobSpawnType.EVENT, true, true);
                level.destroyBlock(blockPos, true);
                livingEntity.kill();
            }
            case 8: {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40 + effectMultiplier, 1));
                addPower(level, blockPos, state, 1);
            }
            case 3: {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 1200 + effectMultiplier, -1 * (effectMultiplier)));
                addPower(level, blockPos, state, 1);
            }
            case 1: {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * effectMultiplier, effectMultiplier));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20 * effectMultiplier, effectMultiplier));
                addPower(level, blockPos, state, 1);
            }
        }

    }


    private int distanceToLimit(BlockState state){
        return MAX_USAGE - USAGE;
    }
    private int hasCoolersAround(Level level, BlockPos blockPos, BlockState state){
        // level.getBlockStates()
        return 0;
    }


    private void setUnstable(Level level, BlockPos blockPos, BlockState state, boolean isUnstable){
        level.setBlock(blockPos, state.setValue(UNSTABLE, isUnstable), 3);
    }

    private void setCoolRate(int newRate){
        if(newRate > 0) {
            COOL_RATE = newRate;
        }
    }
    private void setCooled(Level level, BlockPos blockPos, BlockState state, boolean isCooled){
        level.setBlock(blockPos, state.setValue(COOLED, isCooled), 3);
        COOL_RATE = 1 + (isCooled? 1 : 0);

    }
    private void cool(Level level, BlockPos pos, BlockState state){
        if(USAGE - COOL_RATE > 0)
            USAGE -= COOL_RATE;
        else
            USAGE = 0;
    }

    private void setUsageStep(int newStep){
        newStep = Math.max(newStep, 0);
        USAGE_STEP = Math.min(newStep, 15);
    }
    public int getUsageStep() {
        return USAGE_STEP;
    }
    public void addUsageStep(int add){
        setUsageStep(getUsageStep() + add);
    }

    private void setUsage(Level level, BlockPos blockPos, BlockState state, int value){
        value = Math.max(value, 0);
        USAGE = Math.min(value, MAX_USAGE);
    }
    private void addUsage(Level level, BlockPos blockPos, BlockState state){
        setUsage(level, blockPos, state, USAGE + USAGE_STEP);
    }

    private void setPower(Level level, BlockPos blockPos, BlockState state, int value){
        if(value <= MAX_POWER && value >= MIN_POWER)
            level.setBlock(blockPos, state.setValue(POWER, value), 3);
    }
    private void addPower(Level level, BlockPos blockPos, BlockState state, int value){
        setPower(level, blockPos, state, state.getValue(POWER) + value);
    }

    private void generateStatusMessage(Player player, BlockState state){
        if (state.getValue(UNSTABLE)) {
            player.sendSystemMessage(
                    Component.literal("[Reactor] State: Unstable")
                            .withStyle(ChatFormatting.RED));
            player.sendSystemMessage(
                    Component.literal("[Reactor] Time to cool-down: " + (USAGE / 20) + " seconds")
                            .withStyle(ChatFormatting.AQUA));

        } else if (!state.getValue(UNSTABLE) && entity_on) {
            player.sendSystemMessage(
                    Component.literal("[Reactor] State: Stable")
                            .withStyle(ChatFormatting.BLUE));

            if (distanceToLimit(state) > 200) {
                player.sendSystemMessage(
                        Component.literal("[Reactor] Time until overcharge: " + (distanceToLimit(state) / 20) + " seconds")
                                .withStyle(ChatFormatting.YELLOW));
            } else {
                player.sendSystemMessage(
                        Component.literal("[Reactor] Time until overcharge: " + (distanceToLimit(state) / 20) + " seconds")
                                .withStyle(ChatFormatting.RED));
            }

        } else {
            player.sendSystemMessage(
                    Component.literal("[Reactor] State: Normal")
                            .withStyle(ChatFormatting.GREEN));

        }
    }
}
