package frankv.hbde;

import frankv.hbde.data.CapabilityToggleState;
import frankv.hbde.data.ToggleStateHandler;
import frankv.hbde.network.NetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("hbde")
public class HBDE {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "hbde";
    public static Logger getLOGGER() {
        return LOGGER;
    }

    public HBDE() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {

        CapabilityToggleState.register();
        NetworkHandler.register();

        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class,ToggleStateHandler::attachCapability);
        MinecraftForge.EVENT_BUS.addListener(ToggleStateHandler::playerClone);
        MinecraftForge.EVENT_BUS.addListener(ToggleStateHandler::playerTick);
        MinecraftForge.EVENT_BUS.addListener(ToggleStateHandler::dump);

    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        ClientEvents.setup();

        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new ClientEvents()));
        MinecraftForge.EVENT_BUS.addListener(ClientEvents::keyEvent);
        MinecraftForge.EVENT_BUS.addListener(ClientRender::onRender);
    }
}
