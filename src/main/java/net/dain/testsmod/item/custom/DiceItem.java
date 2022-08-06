package net.dain.testsmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiceItem extends Item {
    public DiceItem(Properties properties, int slots, int diceTier) {
        super(properties);

        rolls = 0;
        upgradeable = false;

        tier = diceTier;
        normalRollMax = 10;

        effectSlotsCount = slots;
        effectSlots = new MobEffect[effectSlotsCount];

        setToolTipTexts();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand){
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                setNormalRollMax(100);
            } else if (!level.isClientSide() && hand == InteractionHand.OFF_HAND) {
                addEffectToSlot(MobEffects.HUNGER, 0);
                addEffectToSlot(MobEffects.ABSORPTION, 1);
                addEffectToSlot(MobEffects.DAMAGE_BOOST, 2);
                addEffectToSlot(MobEffects.DAMAGE_RESISTANCE, 3);
                addEffectToSlot(MobEffects.FIRE_RESISTANCE, 4);
                addEffectToSlot(MobEffects.JUMP, 5);

            }

            return super.use(level, player, hand);
        }

        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND){
            outputRandomNumber(player, normalRollMax);
            player.getCooldowns().addCooldown(this, 20);
        }

        else if(!level.isClientSide() && hand == InteractionHand.OFF_HAND){
            final int selectedSlot = getRandomNumber(effectSlotsCount);
            player.getCooldowns().addCooldown(this, 200);

            //player.sendSystemMessage(Component.literal("Effect: " + (selectedSlot + 1))); // effectSlots[selectedSlot]


            final MobEffect potionEffect = effectSlots[selectedSlot];

            if( potionEffect != null) {
                rolls += 1;
                player.addEffect(new MobEffectInstance(potionEffect, 400));
                player.sendSystemMessage(Component.literal("Effect acquired: " + potionEffect.getDisplayName().getString()));
                player.sendSystemMessage(Component.literal("Your roll count: " + rolls));
            }
            if(!upgradeable && rolls >= 20) {
                upgradeable = true;
                player.sendSystemMessage(Component.literal("Your dice is now upgradeable!"));
                player.playSound(new SoundEvent(new ResourceLocation("")));

            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> componentList, TooltipFlag tooltipFlag) {
        if(Screen.hasControlDown()){
            componentList.add(Component.literal(". .").withStyle(ChatFormatting.OBFUSCATED).withStyle(ChatFormatting.RED));

        } else if (Screen.hasShiftDown()) {
            componentList.add(Component.literal("Hmmmm").withStyle(ChatFormatting.DARK_PURPLE));
            componentList.add(Component.literal(". .").withStyle(ChatFormatting.STRIKETHROUGH));
        }else {
            componentList.add(Component.literal("Texto de prueba").withStyle(ChatFormatting.AQUA));
        }
        super.appendHoverText(itemStack, level, componentList, tooltipFlag);
    }

    public boolean addEffectToSlot(MobEffect effect, int numberSlot){
        if(numberSlot >= effectSlotsCount || numberSlot < 0)
            return false;

        effectSlots[numberSlot] = effect;
        return true;
    }

    public void setNormalRollMax(int value){
        normalRollMax = value - 1;
    }

    private void setToolTipTexts(){
        switch (tier) {
            case 1 -> {
                tooltipTexts = new MutableComponent[1];
                tooltipTexts[0] = Component.literal("Interesting dice...");
            }
            case 2 -> {
                tooltipTexts = new MutableComponent[2];
                tooltipTexts[0] = Component.literal("The power of the brewing stand...\n\nHold 'Shift' for more information");
                tooltipTexts[1] = Component.literal("Press 'Shift' and right click to access an interesting menu...").withStyle(ChatFormatting.AQUA);
            }
            case 3 -> {
            }
            case 4 -> {
            }
            case 5 -> {
            }
            case 6 -> {
                tooltipTexts = new MutableComponent[1];
                tooltipTexts[0] = Component.literal("The limits...").withStyle(ChatFormatting.DARK_PURPLE);
            }
            case 7 -> {
                tooltipTexts = new MutableComponent[1];
                tooltipTexts[0] = Component.literal("...Are broken").withStyle(ChatFormatting.DARK_PURPLE);
            }
            case 8 -> {
                tooltipTexts = new MutableComponent[1];
                tooltipTexts[0] = Component.literal("Power beyond... The imaginable...").withStyle(ChatFormatting.RED);
            }
            case 9, 10 -> {
                tooltipTexts = new MutableComponent[1];
                tooltipTexts[0] = Component.literal("...The unimaginable...").withStyle(ChatFormatting.BLACK);
            }
            default -> {
                tooltipTexts = new MutableComponent[1];
                tooltipTexts[0] = Component.literal("MH4T15M1ST").withStyle(ChatFormatting.BLACK).withStyle(ChatFormatting.OBFUSCATED);
            }
        }

    }

    private void outputRandomNumber(Player player, int range){
        player.sendSystemMessage(Component.literal("Your random number is: " + (getRandomNumber(range) + 1)));
    }

    private int getRandomNumber(int range){
        return RandomSource.createNewThreadLocalInstance().nextInt(range);
    }


    private int rolls;
    private boolean upgradeable;
    private int normalRollMax;

    private final int tier;
    private final int effectSlotsCount;
    private final MobEffect[] effectSlots;

    private MutableComponent[] tooltipTexts;


}

