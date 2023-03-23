package io.github.guy7cc.wwrpg.event;

import io.github.guy7cc.wwrpg.WerewolfRPGInspired;
import io.github.guy7cc.wwrpg.save.cap.WwrpgPlayerSavedData;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.util.thread.EffectiveSide;

@Mod.EventBusSubscriber(modid = WerewolfRPGInspired.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event){
        event.register(WwrpgPlayerSavedData.class);
    }
}
