package io.github.guy7cc.wwrpg;

import io.github.guy7cc.wwrpg.core.GameManager;
import io.github.guy7cc.wwrpg.network.PacketManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(WerewolfRPGInspired.ID)
public class WerewolfRPGInspired {
    public static final String ID = "wwrpg";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static GameManager gameManager;

    public WerewolfRPGInspired() {
        PacketManager.registerMessages("main");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }



}
