package io.github.guy7cc.wwrpg.core.werewolf;

import io.github.guy7cc.wwrpg.core.BasePlayerProperty;
import net.minecraft.server.level.ServerPlayer;

public class PlayerProperty extends BasePlayerProperty implements RoleGetter {
    private Role role;

    public PlayerProperty(ServerPlayer player, Role role) {
        super(player);
        this.role = role;
    }

    @Override
    public Role getRole() {
        return role;
    }
}
