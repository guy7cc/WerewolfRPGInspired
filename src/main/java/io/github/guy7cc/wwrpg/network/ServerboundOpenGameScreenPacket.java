package io.github.guy7cc.wwrpg.network;

import io.github.guy7cc.wwrpg.core.BaseSettings;
import io.github.guy7cc.wwrpg.core.GameType;
import io.github.guy7cc.wwrpg.core.PlayerPreferences;
import io.github.guy7cc.wwrpg.core.SettingsModifier;
import io.github.guy7cc.wwrpg.save.cap.WwrpgPlayerSavedDataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundOpenGameScreenPacket {
    private GameType type;

    public ServerboundOpenGameScreenPacket(GameType type){
        this.type = type;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(type.ordinal());
    }

    public ServerboundOpenGameScreenPacket(FriendlyByteBuf buf){
        type = GameType.values()[buf.readInt()];
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            BaseSettings settings = WwrpgPlayerSavedDataManager.get(sender).getSettings(type);
            PlayerPreferences preferences = WwrpgPlayerSavedDataManager.get(sender).preferences;
            if(settings == null || preferences == null) return;
            SettingsModifier.apply(settings);
            PacketManager.sendToPlayer(sender, new ClientboundSetSettingsPacket(type, settings));
            PacketManager.sendToPlayer(sender, new ClientboundSetPreferencesPacket(preferences));
        });
    }
}
