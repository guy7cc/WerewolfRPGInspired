package io.github.guy7cc.wwrpg.core.werewolf;

import io.github.guy7cc.wwrpg.core.BasePlayerProperty;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class PlayerProperty extends BasePlayerProperty implements RoleGetter {
    private static final int OFFLINE_TICK_TO_DEATH = 200;

    private Role role;
    private boolean dead;
    private int offlineTick = 0;

    public PlayerProperty(ServerPlayer player, Role role) {
        super(player);
        this.role = role;
    }

    @Override
    public Role getRole() {
        return role;
    }

    public void tick(){
        ServerPlayer player = server.getPlayerList().getPlayer(playerUUID);
        if(player != null){
            offlineTick = 0;
        } else {
            offlineTick++;
            if(offlineTick >= OFFLINE_TICK_TO_DEATH){
                dead = true;
            }
        }
    }

    public void onDeath(ServerPlayer player){
        dead = true;
        player.setGameMode(GameType.SPECTATOR);
    }
}
