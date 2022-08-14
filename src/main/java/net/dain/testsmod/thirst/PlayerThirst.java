package net.dain.testsmod.thirst;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class PlayerThirst {
    public final int MIN_THIRST = 0;
    public final int MAX_THIRST = 20;
    private int thirst = MAX_THIRST;

    public final int MIN_SATIETY = 0;
    public final int MAX_SATIETY = 20;
    private int satiety = MAX_SATIETY;

    private int lastInventoryStateID;
    private int actualInventoryStateID;

    private Item lastItemMainHand;
    private Item actualItemMainHand;
    private Item lastItemOffHand;
    private Item actualItemOffHand;

    private int lastSlot;
    private int actualSlot;

    private boolean extendedState;

    public int getThirst() {
        return thirst;
    }
    public int getSatiety() {
        return satiety;
    }

    public void increaseWater(int addWater, int addSatiety){
        this.satiety = Math.min(satiety + addSatiety, MAX_SATIETY);
        this.thirst = Math.min(thirst + addWater, MAX_THIRST);
    }

    public void decreaseWater(int subWater){
        if(satiety > MIN_SATIETY) {
            this.satiety = Math.max(satiety - subWater, MIN_SATIETY);
            return;
        }
        this.thirst = Math.max(thirst - subWater, MIN_THIRST);
    }

    public int getLastInventoryStateID() {
        return lastInventoryStateID;
    }
    public int getActualInventoryStateID() {
        return actualInventoryStateID;
    }

    public void setLastInventoryStateID(int stateID) {
        this.lastInventoryStateID = stateID;
    }
    public void setActualInventoryStateID(int stateID) {
        setLastInventoryStateID(actualInventoryStateID);
        actualInventoryStateID = stateID;
    }

    public void updateInventory(Player player){
        setActualItemMainHand(player.getMainHandItem().getItem());
        setActualSlot(player.getInventory().selected);
        setActualItemOffHand(player.getOffhandItem().getItem());
        setActualInventoryStateID(player.inventoryMenu.getStateId());

        /*
        (getLastItemMainHand() == Items.POTION && getActualItemMainHand() == Items.GLASS_BOTTLE)
        (getLastItemOffHand() == Items.POTION && getActualItemOffHand() == Items.GLASS_BOTTLE)
        (getLastSlot() == getActualSlot())
        (getActualInventoryStateID() - getLastInventoryStateID() == 1)
        */

        if(     (getLastSlot() == getActualSlot()) &&
                ((getLastItemOffHand() == Items.POTION && getActualItemOffHand() == Items.GLASS_BOTTLE) ||
                (getLastItemMainHand() == Items.POTION && getActualItemMainHand() == Items.GLASS_BOTTLE)))
        {
            extendedState = true;
        } else if (!player.isHurt() && extendedState && (getActualInventoryStateID() - getLastInventoryStateID() != 1)) {
            extendedState = false;
        }
    }

    public Item getLastItemMainHand() {
        return lastItemMainHand;
    }
    public Item getActualItemMainHand() {
        return actualItemMainHand;
    }
    public Item getLastItemOffHand() {
        return lastItemOffHand;
    }
    public Item getActualItemOffHand() {
        return actualItemOffHand;
    }

    public void setLastItemMainHand(Item item) {
        this.lastItemMainHand = item;
    }
    public void setActualItemMainHand(Item item) {
        setLastItemMainHand(actualItemMainHand);
        this.actualItemMainHand = item;
    }
    public void setLastItemOffHand(Item item) {
        this.lastItemOffHand = item;
    }
    public void setActualItemOffHand(Item item) {
        setLastItemOffHand(actualItemOffHand);
        this.actualItemOffHand = item;
    }

    public int getLastSlot() {
        return lastSlot;
    }
    public int getActualSlot() {
        return actualSlot;
    }

    public void setLastSlot(int slot) {
        this.lastSlot = slot;
    }
    public void setActualSlot(int slot) {
        setLastSlot(actualSlot);
        actualSlot = slot;
    }

    public boolean hasFinishDrinking(){
        if(extendedState && (getActualInventoryStateID() - getLastInventoryStateID() == 1)) {
            extendedState = false;
            return true;
        }
        return false;
    }

    public void copyFrom(PlayerThirst source){
        this.satiety = source.satiety;
        this.thirst = source.thirst;
    }

    public void saveNBTData(CompoundTag nbt){
        nbt.putInt("satiety", satiety);
        nbt.putInt("thirst", thirst);
    }

    public void loadNBTData(CompoundTag nbt){
        satiety = nbt.getInt("satiety");
        thirst = nbt.getInt("thirst");
    }
}
