package net.dain.testsmod.block.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

import javax.swing.text.StyledEditorKit;


public class ReactorBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;

    private int TIME_UNTIL_COOL = 200;
    private static final int COOL_TIME = 100;
    private static int COOL_RATE = 1;
    public static final BooleanProperty COOLED = BooleanProperty.create("cooled");
    public static final BooleanProperty ENTITY_ON = BooleanProperty.create("entity_over");

    public static final int MIN_LUMINOSITY = 0;
    public static final int MAX_LUMINOSITY = 15;
    public static final IntegerProperty LUMINOSITY = BlockStateProperties.POWER;

    private int TIME_UNTIL_USE = 100;
    private static final int ADD_USE_TIME = 20;
    private static int USAGE_STEP = 1;
    public static final int MAX_USAGE = 12;
    public static final IntegerProperty USAGE = IntegerProperty.create("usage", 0, 24);

    public ReactorBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
        this.registerDefaultState(this.stateDefinition.any().setValue(UNSTABLE, false));
        this.registerDefaultState(this.stateDefinition.any().setValue(COOLED, false));
        this.registerDefaultState(this.stateDefinition.any().setValue(ENTITY_ON, false));

        this.registerDefaultState(this.stateDefinition.any().setValue(LUMINOSITY, 2));
        //this.registerDefaultState(this.stateDefinition.any().setValue(USAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, UNSTABLE, COOLED, ENTITY_ON);
        builder.add(LUMINOSITY, USAGE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {

        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND){
            if(state.getValue(UNSTABLE)) {
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

                if(distanceToLimit(state) > 200) {
                    player.sendSystemMessage(
                            Component.literal("[Reactor] Time until overcharge: " + (distanceToLimit(state) / 20) + " seconds")
                                    .withStyle(ChatFormatting.YELLOW));
                }else{
                    player.sendSystemMessage(
                            Component.literal("[Reactor] Time until overcharge: " + (distanceToLimit(state) / 20) + " seconds")
                                    .withStyle(ChatFormatting.RED));
                }

            }else {
                player.sendSystemMessage(
                        Component.literal("[Reactor] State: Normal")
                        .withStyle(ChatFormatting.GREEN));

            }
        }


        return super.use(state, level, blockPos, player, hand, blockHitResult);

    }



    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(LUMINOSITY);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        super.animateTick(state, level, pos, randomSource);

        setCooled(level, pos, state, Boolean.FALSE);
        setUnstable(level, pos, state, Boolean.FALSE);
        setLuminosity(level, pos, state, 4);

        if(isWaterClose(level, pos, state)){
            setCooled(level, pos, state, Boolean.TRUE);
        }

        if(!state.getValue(ENTITY_ON) && state.getValue(USAGE) > 0 && TIME_UNTIL_COOL == 0){
            cool(level, pos, state);
            TIME_UNTIL_COOL = COOL_TIME;

        } else if (state.getValue(ENTITY_ON) && TIME_UNTIL_USE == 0) {
            addUsage(level, pos, state);
            TIME_UNTIL_USE = ADD_USE_TIME;
        }

        if(!state.getValue(ENTITY_ON) && state.getValue(USAGE) == 0){
            setUnstable(level, pos, state, Boolean.FALSE);
        }else if(state.getValue(ENTITY_ON) && state.getValue(USAGE) >= MAX_USAGE){
            setUnstable(level, pos, state, Boolean.TRUE);
        }

        if(state.getValue(ENTITY_ON)){
            setLuminosity(level, pos, state, USAGE_STEP + 6);
        }else {
            addLuminosity(level, pos, state, - 1);
        }

        setEntityOn(level, pos, state, Boolean.FALSE);

        setUsageStep(1);

        TIME_UNTIL_COOL -= 1;
        TIME_UNTIL_USE -= 1;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity){

        if(level.isClientSide())
            return;

        setUsageStep(USAGE_STEP + 1);

        if(entity instanceof LivingEntity livingEntity){
            setEntityOn(level, pos, state, true);
            addLuminosity(level, pos, state, 2);
            setUsageStep(USAGE_STEP + 1);

            if((distanceToLimit(state)) >= 100 && !state.getValue(UNSTABLE)) {

                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 19, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 5, 2, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5, 1, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 2, 0, true, false, false));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20, 9, true, false, false));
                //livingEntity.addEffect();

                if (livingEntity.isShiftKeyDown()) {
                    addLuminosity(level, pos, state, 3);
                    setUsageStep(USAGE_STEP + 2);
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 5, 5, true, false, false));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 1, true, false, false));
                }
            }

            if(livingEntity instanceof Player player) { //  && usageTime % 200 == 0
                player.sendSystemMessage(Component.literal("Time left: " + distanceToLimit(state)));
            }

            if(distanceToLimit(state) < 0){
                setUsageStep(USAGE_STEP - 1);
                limiter(level, pos, state, livingEntity);
                setUnstable(level, pos, state, true);
            }
        }

    }

    private void limiter(Level level, BlockPos blockPos, BlockState state, LivingEntity livingEntity){

        setLuminosity(level, blockPos, state, 10);
        int effectMultiplier = ((state.getValue(USAGE) - MAX_USAGE) / 200);

        livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100 + (effectMultiplier * 20), effectMultiplier));
        livingEntity.hurt(DamageSource.MAGIC, 2.0f + effectMultiplier);

        switch (effectMultiplier){
            default: if(effectMultiplier < 2) {break;}
            case 8: {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40 + effectMultiplier, 1));
                addLuminosity(level, blockPos, state, 1);
            }
            case 3: {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 1200 + effectMultiplier, -1 * (effectMultiplier)));
                addLuminosity(level, blockPos, state, 1);
            }
            case 1: {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * effectMultiplier, effectMultiplier));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20 * effectMultiplier, effectMultiplier));
                addLuminosity(level, blockPos, state, 1);
            }
        }

    }

    private int distanceToLimit(BlockState state){
        return MAX_USAGE - state.getValue(USAGE);
    }
    private boolean isWaterClose(Level level, BlockPos blockPos, BlockState state){
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

    private void setLuminosity(Level level, BlockPos blockPos, BlockState state, int value){
        if(value < 16 && value > 3)
            level.setBlock(blockPos, state.setValue(LUMINOSITY, value), 3);
    }
    private void addLuminosity(Level level, BlockPos blockPos, BlockState state, int value){
        if(state.getValue(LUMINOSITY) == MAX_LUMINOSITY)
            return;
        else if(state.getValue(LUMINOSITY) + value <= MAX_LUMINOSITY && state.getValue(LUMINOSITY) + value >= 4)
            level.setBlock(blockPos, state.setValue(LUMINOSITY, state.getValue(LUMINOSITY) + value), 3);
        else
            level.setBlock(blockPos, state.setValue(LUMINOSITY, MAX_LUMINOSITY), 3);
    }
}
