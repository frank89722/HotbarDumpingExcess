package frankv.hbde.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ToggleStateProvider implements ICapabilitySerializable<CompoundNBT> {

    private final ToggleState toggleState = new ToggleState();
    private final LazyOptional<IToggleState> toggleStateLazyOptional = LazyOptional.of(() -> toggleState);

    public void invalidate(){
        toggleStateLazyOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return toggleStateLazyOptional.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if(CapabilityToggleState.TOGGLE_STATE_STORAGE == null){
            return new CompoundNBT();
        } else {
            return (CompoundNBT) CapabilityToggleState.TOGGLE_STATE_STORAGE.writeNBT(toggleState, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if(CapabilityToggleState.TOGGLE_STATE_STORAGE != null){
            CapabilityToggleState.TOGGLE_STATE_STORAGE.readNBT(toggleState, null, nbt);
        }
    }
}
