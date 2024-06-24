package net.dain.testsmod.block.entity;

import com.sun.jdi.connect.spi.TransportService;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReactorBlockEntity extends BlockEntity implements MenuProvider {
    protected final ContainerData data;

    private int usage = 0;
    private int topUsage;
    private int maxUsage;
    private boolean powered = false;
    private boolean entityOnTop = false;
    private boolean cooled = false;
    private boolean unstable = false;


    public ReactorBlockEntity(BlockPos pos, BlockState state){
        this(pos, state, 1200, 2400);
    }
    public ReactorBlockEntity(BlockPos pos, BlockState state, int topUsage, int maxUsage) {
        super(ModBlockEntities.REACTOR_BLOCK_ENTITY.get(), pos, state);

        this.topUsage = topUsage;
        this.maxUsage = maxUsage;



        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ReactorBlockEntity.this.usage;
                    case 1 -> ReactorBlockEntity.this.topUsage;
                    case 2 -> ReactorBlockEntity.this.maxUsage;
                    default -> 0;
                };
            }

            @Override
            public void set(int p_39285_, int p_39286_) {

            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Reactor").withStyle(ChatFormatting.YELLOW);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return null;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();

    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("usage", usage);
        nbt.putInt("top_usage", topUsage);
        nbt.putInt("max_usage", maxUsage);

        nbt.putBoolean("unstable", unstable);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        usage = nbt.getInt("usage");
        topUsage = nbt.getInt("top_usage");
        maxUsage = nbt.getInt("max_usage");

        unstable = nbt.getBoolean("unstable");

        super.load(nbt);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ReactorBlockEntity blockEntity){
        if(level.isClientSide()){
           return;
        }
        level.shouldTickBlocksAt(pos);
        setChanged(level, pos, state);
    }
}
