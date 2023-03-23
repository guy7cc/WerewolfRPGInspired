package io.github.guy7cc.wwrpg.network;

import io.github.guy7cc.wwrpg.client.screen.WerewolfGameScreen;
import io.github.guy7cc.wwrpg.core.BaseSettings;
import io.github.guy7cc.wwrpg.core.GameType;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSetSettingsPacket {
    private GameType type;
    private BaseSettings settings;

    public ClientboundSetSettingsPacket(GameType type, BaseSettings settings){
        this.type = type;
        this.settings = settings;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(type.ordinal());
        buf.writeNbt(settings.serializeNBT());
    }

    public ClientboundSetSettingsPacket(FriendlyByteBuf buf){
        type = GameType.values()[buf.readInt()];
        switch(type){
            case WEREWOLF:
                settings = new WerewolfGameSettings();
                settings.deserializeNBT(buf.readNbt());
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft minecraft = Minecraft.getInstance();
                if(minecraft.screen instanceof WerewolfGameScreen screen){
                    screen.applySettings((WerewolfGameSettings) settings);
                }
            });
        });
    }
}
