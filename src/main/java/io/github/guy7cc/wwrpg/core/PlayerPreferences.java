package io.github.guy7cc.wwrpg.core;

import io.github.guy7cc.wwrpg.WerewolfRPGInspired;
import io.github.guy7cc.wwrpg.core.werewolf.Role;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class PlayerPreferences implements INBTSerializable<CompoundTag> {
    private boolean enabled;
    private Role desiredRole;

    public PlayerPreferences(){
        this(false, Role.VILLAGER);
    }

    public PlayerPreferences(boolean enabled, Role desiredRole){
        this.enabled = enabled;
        this.desiredRole = desiredRole;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public Role getDesiredRole(){
        return desiredRole;
    }

    public void copy(PlayerPreferences preferences){
        enabled = preferences.enabled;
        desiredRole = preferences.desiredRole;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Enabled", enabled);
        tag.putString("DesiredRole", desiredRole.toString());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        enabled = nbt.getBoolean("Enabled");
        try{
            desiredRole = Enum.valueOf(Role.class, nbt.getString("DesiredRole"));
        } catch(IllegalArgumentException exception){
            WerewolfRPGInspired.LOGGER.error(nbt.getString("DesiredRole") + " is an invalid value for the player roles.");
            WerewolfRPGInspired.LOGGER.error(exception.getStackTrace());
            desiredRole = Role.VILLAGER;
        }
    }
}
