package frankv.hbde.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityToggleState {

    @CapabilityInject(IToggleState.class)
    public static Capability<IToggleState> TOGGLE_STATE_STORAGE = null;

    public static void register(){
        CapabilityManager.INSTANCE.register(IToggleState.class, new Storage(), ToggleState::new);
    }

    public static class Storage implements Capability.IStorage<IToggleState> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IToggleState> capability, IToggleState instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putIntArray("destate", instance.getToggleDEState());
            return tag;
        }

        @Override
        public void readNBT(Capability<IToggleState> capability, IToggleState instance, Direction side, INBT nbt) {
            int[] state = new int[9];
            if (((CompoundNBT) nbt).getIntArray("destate").length != 9){
                instance.setToggleDEState(state);
            } else {
                instance.setToggleDEState(((CompoundNBT) nbt).getIntArray("destate"));
            }
        }
    }
}
