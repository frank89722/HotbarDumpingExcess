package frankv.hbde.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityToggleState {

    @CapabilityInject(IToggleState.class)
    public static Capability<IToggleState> TOGGLE_STATE_STORAGE = null;

    public static void register(){
        CapabilityManager.INSTANCE.register(IToggleState.class);
    }
}
