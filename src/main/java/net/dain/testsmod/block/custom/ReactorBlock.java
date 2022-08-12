package net.dain.testsmod.block.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;


public class ReactorBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;

    private int TIME_UNTIL_COOL = 200;
    private static final int COOL_TIME = 100;
    private static int COOL_RATE = 1;
    public static final BooleanProperty COOLED = BooleanProperty.create("cooled");
    public static final BooleanProperty ENTITY_ON = BooleanProperty.create("entity_over");

    public static final int MIN_POWER = 2;
    public static final int MAX_POWER = 15;
    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    private int TIME_UNTIL_USE = 100;
    private static final int ADD_USE_TIME = 20;
    private static final int MIN_USAGE_STEP = 1;
    private static int USAGE_STEP = 1;
    public static final int MAX_USAGE = 12;
    public static final IntegerProperty USAGE = IntegerProperty.create("usage", 0, 24);

    private Entity entityOnTop = null;

    public ReactorBlock(BlockBehaviour.Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false)
                .setValue(UNSTABLE, false)
                .setValue(COOLED, false)
                .setValue(ENTITY_ON, false)

                .setValue(POWER, MIN_POWER)
                .setValue(USAGE, 0));
    }
/*
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }
 */

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, UNSTABLE, COOLED, ENTITY_ON);
        builder.add(POWER, USAGE);
    }
/*
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof )
        }

        super.onRemove(state, level, pos, state1, isMoving);
    }
 */

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {

        if (level.isClientSide())
            return super.use(state, level, blockPos, player, hand, blockHitResult);

        if (hand == InteractionHand.MAIN_HAND) {
            if (player.isShiftKeyDown()) {
                player.sendSystemMessage(Component.literal("[Reactor] Active: " + state.getValue(POWERED)));
                player.sendSystemMessage(Component.literal("[Reactor] Stable: " + !state.getValue(UNSTABLE)));
                player.sendSystemMessage(Component.literal("[Reactor] Cooled: " + state.getValue(COOLED)));
                player.sendSystemMessage(Component.literal("[Reactor] Entity Over: " + state.getValue(ENTITY_ON)));

                player.sendSystemMessage(Component.literal("[Reactor] Luminosity: " + state.getValue(POWER)));
                player.sendSystemMessage(Component.literal("[Reactor] Usage: " + state.getValue(USAGE)));

            } else {
                if (state.getValue(UNSTABLE)) {
                    player.sendSystemMessage(
                            Component.literal("[Reactor] State: Unstable")
                                    .withStyle(ChatFormatting.RED));
                    player.sendSystemMessage(
                            Component.literal("[Reactor] Time to cool-down: " + (state.getValue(USAGE) / 20) + " seconds")
                                    .withStyle(ChatFormatting.AQUA));

                } else if (!state.getValue(UNSTABLE) && state.getValue(ENTITY_ON)) {
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


        return super.use(state, level, blockPos, player, hand, blockHitResult);

    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return state.getValue(POWER);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {

        if (level.isClientSide())
            return;

        setEntityOn(level, pos, state, true);
        storeEntity(entity);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        super.animateTick(state, level, pos, randomSource);

        setCooled(level, pos, state, Boolean.FALSE);
        setPower(level, pos, state, MIN_POWER);
        setUsageStep(MIN_USAGE_STEP);

        affectEntity(level, pos, state, entityOnTop);
        discardEntity();

        if (haveWaterClose(level, pos, state)) {
            setCooled(level, pos, state, Boolean.TRUE);
        }

        if (state.getValue(USAGE) >= MAX_USAGE) {
            setUnstable(level, pos, state, Boolean.TRUE);
        } else if (state.getValue(USAGE) == 0) {
            setUnstable(level, pos, state, Boolean.FALSE);
        }

        if (state.getValue(USAGE) > 0 && TIME_UNTIL_COOL == 0) {
            cool(level, pos, state);
            TIME_UNTIL_COOL = COOL_TIME;
        }

        if (state.getValue(ENTITY_ON)) {
            setEntityOn(level, pos, state, Boolean.FALSE);

            if (TIME_UNTIL_USE == 0) {
                addUsage(level, pos, state);
                TIME_UNTIL_USE = ADD_USE_TIME;
            }

        } else if (TIME_UNTIL_USE == 0) {
            addPower(level, pos, state, -1);
        }

        TIME_UNTIL_COOL -= 1;
        TIME_UNTIL_USE -= 1;
    }
/*
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return super.getTicker(p_153212_, p_153213_, p_153214_);
    }
 */

    private void affectEntity(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entityOnTop == null)
            return;

        setUsageStep(USAGE_STEP + 1);

        if (entity instanceof LivingEntity livingEntity) {
            setEntityOn(level, pos, state, true);
            addPower(level, pos, state, 2);
            setUsageStep(USAGE_STEP + 1);

            if ((distanceToLimit(state)) >= 100 && !state.getValue(UNSTABLE)) {

                ((LivingEntity) entity).setSpeed(0.5f);
                //livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 9, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 5, 2, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5, 1, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 2, 0, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20, 9, true, false, false));
                //livingEntity.addEffect();

                if (livingEntity.isShiftKeyDown()) {
                    addPower(level, pos, state, 3);
                    setUsageStep(USAGE_STEP + 2);
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 5, 5, true, false, false));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 1, true, false, false));
                }
            }

            if (livingEntity instanceof Player player) { //  && usageTime % 200 == 0
                player.sendSystemMessage(Component.literal("Time left: " + distanceToLimit(state)));
            }

            if (state.getValue(UNSTABLE)) {
                overloadEffect(level, pos, state, livingEntity);
            }
        }
    }
    private void overloadEffect(Level level, BlockPos blockPos, BlockState state, LivingEntity livingEntity) {

        // setPower(level, blockPos, state, 10);
        int effectMultiplier = ((state.getValue(USAGE) - MAX_USAGE) / 200);

        livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100 + (effectMultiplier * 20), effectMultiplier));
        livingEntity.hurt(DamageSource.MAGIC, 2.0f + effectMultiplier);

        switch (effectMultiplier) {
            default:
                if (effectMultiplier < 2) {
                    break;
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
        return MAX_USAGE - state.getValue(USAGE);
    }
    private boolean haveWaterClose(Level level, BlockPos blockPos, BlockState state){
        return false;
    }


    private void setEntityOn(Level level, BlockPos blockPos, BlockState state, boolean isOn){
        level.setBlock(blockPos, state.setValue(ENTITY_ON, isOn), 3);
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
        if(state.getValue(USAGE) - COOL_RATE > 0)
            level.setBlock(pos, state.setValue(USAGE, state.getValue(USAGE) - COOL_RATE), 3);
        else
            level.setBlock(pos, state.setValue(USAGE, 0), 3);
    }

    private void setUsageStep(int newStep){
        if(newStep > 0) {
            USAGE_STEP = newStep;
        }else {
            USAGE_STEP = 1;
        }
    }
    private void setUsage(Level level, BlockPos blockPos, BlockState state, int value){
        level.setBlock(blockPos, state.setValue(USAGE, value), 3);
    }
    private void addUsage(Level level, BlockPos blockPos, BlockState state){
        if(state.getValue(USAGE) + USAGE_STEP < MAX_USAGE)
            level.setBlock(blockPos, state.setValue(USAGE, state.getValue(USAGE) + USAGE_STEP), 3);

        else
            level.setBlock(blockPos, state.setValue(USAGE, MAX_USAGE), 3);
    }

    private void setPower(Level level, BlockPos blockPos, BlockState state, int value){
        if(value < 16 && value > 3)
            level.setBlock(blockPos, state.setValue(POWER, value), 3);
    }
    private void addPower(Level level, BlockPos blockPos, BlockState state, int value){
        if(state.getValue(POWER) == MAX_POWER)
            return;
        else if(state.getValue(POWER) + value <= MAX_POWER && state.getValue(POWER) + value >= MIN_POWER)
            level.setBlock(blockPos, state.setValue(POWER, state.getValue(POWER) + value), 3);
        else if(state.getValue(POWER) + value >= MAX_POWER)
            level.setBlock(blockPos, state.setValue(POWER, MAX_POWER), 3);
        else
            level.setBlock(blockPos, state.setValue(POWER, MIN_POWER), 3);

    }

    private void storeEntity(Entity entity){
        entityOnTop = entity;
    }
    private void discardEntity(){
        entityOnTop = null;
    }

}
