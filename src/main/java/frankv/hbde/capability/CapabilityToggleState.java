package frankv.hbde.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityToggleState {

    public static Capability<IToggleState> TOGGLE_STATE_STORAGE = CapabilityManager.get(new CapabilityToken<>() {});

    public static void capRegister(RegisterCapabilitiesEvent event){
        event.register(IToggleState.class);
    }
}
