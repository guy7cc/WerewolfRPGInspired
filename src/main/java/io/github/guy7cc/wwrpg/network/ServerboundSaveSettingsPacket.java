package io.github.guy7cc.wwrpg.network;

import io.github.guy7cc.wwrpg.core.BaseSettings;
import io.github.guy7cc.wwrpg.core.GameType;
import io.github.guy7cc.wwrpg.save.cap.WwrpgPlayerSavedDataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundSaveSettingsPacket {
    private GameType type;
    private BaseSettings settings;

    public ServerboundSaveSettingsPacket(GameType type, BaseSettings settings){
        this.type = type;
        this.settings = settings;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(type.ordinal());
        buf.writeNbt(settings.serializeNBT());
    }

    public ServerboundSaveSettingsPacket(FriendlyByteBuf buf){
        type = GameType.values()[buf.readInt()];
        settings = type.defaultSettings.get();
        settings.deserializeNBT(buf.readNbt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            WwrpgPlayerSavedDataManager.get(sender).putSettings(type, settings);
        });
    }
}
