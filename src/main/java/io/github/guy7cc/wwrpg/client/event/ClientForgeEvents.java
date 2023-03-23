package io.github.guy7cc.wwrpg.client.event;

import io.github.guy7cc.wwrpg.WerewolfRPGInspired;
import io.github.guy7cc.wwrpg.client.ClientEffectScheduler;
import io.github.guy7cc.wwrpg.client.screen.WerewolfGameScreen;
import io.github.guy7cc.wwrpg.core.GameType;
import io.github.guy7cc.wwrpg.network.PacketManager;
import io.github.guy7cc.wwrpg.network.ServerboundOpenGameScreenPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = WerewolfRPGInspired.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.START){
            ClientEffectScheduler.clientTick();
        }
    }

    @SubscribeEvent
    public static void onInitScreenPre(ScreenEvent.Init.Post event){
        if(event.getScreen() instanceof InventoryScreen || event.getScreen() instanceof CreativeModeInventoryScreen){
            AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>)event.getScreen();
            event.addListener(new ImageButton(screen.getGuiLeft() + screen.getXSize() + 1, screen.getGuiTop() + screen.getYSize() - 22, 22, 22, 0, 0, 22, WerewolfGameScreen.INVENTORY_BUTTON_LOCATION, 32, 64, button -> {
                Minecraft.getInstance().setScreen(new WerewolfGameScreen());
                PacketManager.sendToServer(new ServerboundOpenGameScreenPacket(GameType.WEREWOLF));
            }));
        }
    }
}
