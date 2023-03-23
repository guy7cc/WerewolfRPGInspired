package io.github.guy7cc.wwrpg.network;

import io.github.guy7cc.wwrpg.client.screen.WerewolfGameScreen;
import io.github.guy7cc.wwrpg.core.GameType;
import io.github.guy7cc.wwrpg.core.PlayerPreferences;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSetPreferencesPacket {
    private PlayerPreferences preferences;

    public ClientboundSetPreferencesPacket(PlayerPreferences preferences){
        this.preferences = preferences;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(preferences.serializeNBT());
    }

    public ClientboundSetPreferencesPacket(FriendlyByteBuf buf){
        preferences = new PlayerPreferences();
        preferences.deserializeNBT(buf.readNbt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft minecraft = Minecraft.getInstance();
                if(minecraft.screen instanceof WerewolfGameScreen screen){
                    screen.applyPreferences(preferences);
                }
            });
        });
    }
}
