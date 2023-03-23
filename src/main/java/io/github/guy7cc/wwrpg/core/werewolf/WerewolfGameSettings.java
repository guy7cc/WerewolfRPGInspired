package io.github.guy7cc.wwrpg.core.werewolf;

import io.github.guy7cc.wwrpg.client.ClientExecutor;
import io.github.guy7cc.wwrpg.core.BaseSettings;
import io.github.guy7cc.wwrpg.util.IntRange;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WerewolfGameSettings extends BaseSettings {
    private Map<Role, Integer> resultRoles;

    public WerewolfGameSettings(){
        this(new RoleSettings(), Vec3.ZERO, 2000, 2000);
    }

    public WerewolfGameSettings(RoleSettings roles, Vec3 spawnPos, int dayTime, int nightTime){
        super(List.of(
                new SerializableComponent<>("Roles", roles, RoleSettings::isValid, RoleSettings::new),
                new Vec3Component("SpawnPos", spawnPos, (value, server) -> true),
                new IntComponent("DayTime", dayTime, (value, server) -> value > 0),
                new IntComponent("NightTime", nightTime, (value, server) -> value > 0),
                new BooleanComponent("ForceStart", false, (value, server) -> true),
                new BooleanComponent("DontFinish", false, (value, server) -> true)
        ));
    }

    public Map<Role, Integer> copyRoles(){
        return new HashMap<>(resultRoles);
    }

    public boolean isValid(@Nullable MinecraftServer server){
        int villagerNum = server != null ? server.getPlayerCount() : DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> ClientExecutor.getOnlinePlayerNum());
        Map<Role, IntRange> roleSettings = this.<RoleSettings>get("Roles").get();
        for(Map.Entry<Role, IntRange> entry : roleSettings.entrySet()){
            int minValue = entry.getKey() == Role.WEREWOLF ? 1 : 0;
            IntRange range = entry.getValue();
            if(!range.isEmpty() && range.max() >= minValue){
                villagerNum -= range.floor(minValue);
            } else return false;
        }
        return villagerNum > 0;
    }

    // setup fields that is going to be used in initialization of game
    public void setup(MinecraftServer server){
        resultRoles = this.<RoleSettings>get("Roles").getResultOrThrow(server, get("ForceStart"));
    }
}
