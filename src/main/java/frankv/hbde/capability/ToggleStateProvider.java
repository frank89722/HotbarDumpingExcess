package frankv.hbde.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ToggleStateProvider implements ICapabilitySerializable<CompoundTag> {

    private final ToggleState toggleState = new ToggleState();
    private final LazyOptional<IToggleState> toggleStateLazyOptional = LazyOptional.of(() -> toggleState);

    public void invalidate(){
        toggleStateLazyOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityToggleState.TOGGLE_STATE_STORAGE.orEmpty(cap, toggleStateLazyOptional);
    }

    @Override
    public CompoundTag serializeNBT() {
        if(CapabilityToggleState.TOGGLE_STATE_STORAGE == null){
            return new CompoundTag();
        }

        CompoundTag tag = new CompoundTag();
        tag.putIntArray("destate", toggleState.getToggleDEState());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        assert (CapabilityToggleState.TOGGLE_STATE_STORAGE != null);
        if (nbt.getIntArray("destate").length != 9){
            toggleState.setToggleDEState(new int[9]);
            return;
        }

        toggleState.setToggleDEState(nbt.getIntArray("destate"));
    }
}
