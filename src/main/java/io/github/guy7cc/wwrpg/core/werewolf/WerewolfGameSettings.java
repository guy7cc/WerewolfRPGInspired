package io.github.guy7cc.wwrpg.core.werewolf;

import io.github.guy7cc.wwrpg.WerewolfRPGInspired;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class WerewolfGameSettings implements INBTSerializable<CompoundTag> {
    private Map<Role, Integer> roles;
    private int dayTime;
    private int nightTime;
    private int smallEnemy;
    private int mediumEnemy;
    private int largeEnemy;

    public WerewolfGameSettings(){
        this(new HashMap<>(), 2000, 2000, 50, 5, 1);
    }

    public WerewolfGameSettings(Map<Role, Integer> roles, int dayTime, int nightTime, int smallEnemy, int mediumEnemy, int largeEnemy){
        this.roles = roles;
        this.dayTime = Math.max(dayTime, 1);
        this.nightTime = Math.max(nightTime, 1);
        this.smallEnemy = Math.max(smallEnemy, 0);
        this.mediumEnemy = Math.max(mediumEnemy, 0);
        this.largeEnemy = Math.max(largeEnemy, 0);
    }

    public int getRoleNum(Role role){
        return roles.get(role);
    }

    public Map<Role, Integer> copyRoleMap(){
        return new HashMap<>(roles);
    }

    public boolean isValid(MinecraftServer server){
        int villagerNum = server.getPlayerCount();
        for(Role key : roles.keySet()){
            if(key != Role.VILLAGER) villagerNum -= roles.get(key);
        }
        int werewolfNum = roles.getOrDefault(Role.WEREWOLF, 0);
        return villagerNum > 0 && werewolfNum > 0;
    }

    public void fillVillagerNum(MinecraftServer server){
        int villagerNum = server.getPlayerCount();
        for(Role key : roles.keySet()){
            if(key != Role.VILLAGER) villagerNum -= roles.get(key);
        }
        roles.put(Role.VILLAGER, villagerNum);
    }

    public void copy(WerewolfGameSettings settings){
        roles = new HashMap<>(settings.roles);
        dayTime = settings.dayTime;
        nightTime = settings.nightTime;
        smallEnemy = settings.smallEnemy;
        mediumEnemy = settings.mediumEnemy;
        largeEnemy = settings.largeEnemy;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        CompoundTag rolesTag = new CompoundTag();
        for(Role role : roles.keySet()){
            rolesTag.putInt(role.toString(), roles.get(role));
        }
        tag.put("Roles", rolesTag);
        tag.putInt("DayTime", dayTime);
        tag.putInt("NightTime", nightTime);
        tag.putInt("SmallEnemy", smallEnemy);
        tag.putInt("MediumEnemy", mediumEnemy);
        tag.putInt("LargeEnemy", largeEnemy);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag rolesTag = nbt.getCompound("Roles");
        roles.clear();
        for(String key : rolesTag.getAllKeys()){
            try{
                Role role = Enum.valueOf(Role.class, key);
                roles.put(role, rolesTag.getInt(key));
            } catch(IllegalArgumentException exception){
                WerewolfRPGInspired.LOGGER.error(key + " is an invalid value for the player roles.");
                WerewolfRPGInspired.LOGGER.error(exception.getStackTrace());
            }
        }
        dayTime = nbt.getInt("DayTime");
        nightTime = nbt.getInt("NightTime");
        smallEnemy = nbt.getInt("SmallEnemy");
        mediumEnemy = nbt.getInt("MediumEnemy");
        largeEnemy = nbt.getInt("LargeEnemy");
    }
}
