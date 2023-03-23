package io.github.guy7cc.wwrpg.network;

import io.github.guy7cc.wwrpg.core.GameManager;
import io.github.guy7cc.wwrpg.core.SettingsModifier;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGame;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGameSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class ServerboundStartWerewolfGamePacket {
    private WerewolfGameSettings settings;

    public ServerboundStartWerewolfGamePacket(WerewolfGameSettings settings){
        this.settings = settings;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(settings.serializeNBT());
    }

    public ServerboundStartWerewolfGamePacket(FriendlyByteBuf buf){
        settings = new WerewolfGameSettings();
        settings.deserializeNBT(buf.readNbt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if(sender != null){
                SettingsModifier.apply(settings);
                GameManager.startGame(new WerewolfGame(sender, settings));
                ctx.get().setPacketHandled(true);
            }
        });
    }
}
