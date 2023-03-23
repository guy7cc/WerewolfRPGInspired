package io.github.guy7cc.wwrpg.core;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class BasePlayerProperty {
    protected MinecraftServer server;
    protected UUID playerUUID;

    public BasePlayerProperty(ServerPlayer player){
        server = player.getServer();
        playerUUID = player.getUUID();
    }
}
