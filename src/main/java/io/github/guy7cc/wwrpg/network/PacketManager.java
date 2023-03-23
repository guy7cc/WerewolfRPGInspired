package io.github.guy7cc.wwrpg.network;

import io.github.guy7cc.wwrpg.WerewolfRPGInspired;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class PacketManager {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void registerMessages(String name) {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(WerewolfRPGInspired.ID, name))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.registerMessage(id(), ClientboundSetEffectSchedulePacket.class, ClientboundSetEffectSchedulePacket::toBytes, ClientboundSetEffectSchedulePacket::new, ClientboundSetEffectSchedulePacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        net.registerMessage(id(), ClientboundSetPreferencesPacket.class, ClientboundSetPreferencesPacket::toBytes, ClientboundSetPreferencesPacket::new, ClientboundSetPreferencesPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        net.registerMessage(id(), ClientboundSetSettingsPacket.class, ClientboundSetSettingsPacket::toBytes, ClientboundSetSettingsPacket::new, ClientboundSetSettingsPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        net.registerMessage(id(), ServerboundSaveSettingsPacket.class, ServerboundSaveSettingsPacket::toBytes, ServerboundSaveSettingsPacket::new, ServerboundSaveSettingsPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        net.registerMessage(id(), ServerboundStartWerewolfGamePacket.class, ServerboundStartWerewolfGamePacket::toBytes, ServerboundStartWerewolfGamePacket::new, ServerboundStartWerewolfGamePacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        net.registerMessage(id(), ServerboundOpenGameScreenPacket.class, ServerboundOpenGameScreenPacket::toBytes, ServerboundOpenGameScreenPacket::new, ServerboundOpenGameScreenPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message){
        INSTANCE.send(target, message);
    }

    public static <MSG> void sendToPlayer(ServerPlayer player, MSG message){
        send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }
}
