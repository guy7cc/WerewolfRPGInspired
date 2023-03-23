package io.github.guy7cc.wwrpg.event;

import com.mojang.brigadier.CommandDispatcher;
import io.github.guy7cc.wwrpg.WerewolfRPGInspired;
import io.github.guy7cc.wwrpg.command.WwrpgCommand;
import io.github.guy7cc.wwrpg.core.GameManager;
import io.github.guy7cc.wwrpg.save.cap.WwrpgPlayerSavedData;
import io.github.guy7cc.wwrpg.save.cap.WwrpgPlayerSavedDataProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WerewolfRPGInspired.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event){
        if(event.phase == TickEvent.Phase.START) GameManager.serverTick();
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof ServerPlayer){
            event.addCapability(WwrpgPlayerSavedDataProvider.WWRPG_PLAYER_SAVED_DATA_LOCATION, new WwrpgPlayerSavedDataProvider(WwrpgPlayerSavedData::new));
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        WwrpgCommand.register(dispatcher);
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        event.getOriginal().getCapability(WwrpgPlayerSavedDataProvider.WWRPG_PLAYER_SAVED_DATA_CAPABILITY).ifPresent(oldCap -> {
            event.getEntity().getCapability(WwrpgPlayerSavedDataProvider.WWRPG_PLAYER_SAVED_DATA_CAPABILITY).ifPresent(newCap -> {
                newCap.copy(oldCap);
            });
        });
    }
}
