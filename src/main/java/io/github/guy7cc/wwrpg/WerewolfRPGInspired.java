package io.github.guy7cc.wwrpg;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(WerewolfRPGInspired.ID)
public class WerewolfRPGInspired {
    public static final String ID = "wwrpg";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public WerewolfRPGInspired() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }

}
