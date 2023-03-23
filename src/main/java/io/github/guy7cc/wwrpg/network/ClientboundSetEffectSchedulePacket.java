package io.github.guy7cc.wwrpg.network;

import io.github.guy7cc.wwrpg.client.ClientEffect;
import io.github.guy7cc.wwrpg.client.ClientEffectScheduler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ClientboundSetEffectSchedulePacket {
    private List<ClientEffect> effects;

    public ClientboundSetEffectSchedulePacket(List<ClientEffect> effects){
        this.effects = effects;
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(effects.size());
        for(ClientEffect effect : effects){
            buf.writeInt(ClientEffectScheduler.getId(effect));
            effect.toBytes(buf);
        }
    }

    public ClientboundSetEffectSchedulePacket(FriendlyByteBuf buf){
        int size = buf.readInt();
        effects = new ArrayList<>();
        for(int i = 0; i < size; i++){
            int id = buf.readInt();
            effects.add(ClientEffectScheduler.fromId(id, buf));
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                effects.forEach(ClientEffectScheduler::enqueue);
            });
        });
    }
}
