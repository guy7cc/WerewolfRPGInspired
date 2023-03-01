package io.github.guy7cc.wwrpg.core;

import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class BasePlayerProperty {
    protected UUID playerUUID;

    public BasePlayerProperty(ServerPlayer player){
        this.playerUUID = player.getUUID();
    }
}
