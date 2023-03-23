package io.github.guy7cc.wwrpg.core.werewolf;

import io.github.guy7cc.wwrpg.core.BaseGame;
import io.github.guy7cc.wwrpg.core.GameType;
import io.github.guy7cc.wwrpg.core.PlayerPreferences;
import io.github.guy7cc.wwrpg.core.werewolf.phase.AbortedPhase;
import io.github.guy7cc.wwrpg.core.werewolf.phase.InitialPhase;
import io.github.guy7cc.wwrpg.core.werewolf.phase.MainPhase;
import io.github.guy7cc.wwrpg.core.werewolf.phase.WerewolfGamePhase;
import io.github.guy7cc.wwrpg.save.cap.WwrpgPlayerSavedData;
import io.github.guy7cc.wwrpg.save.cap.WwrpgPlayerSavedDataManager;
import io.github.guy7cc.wwrpg.util.MathUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class WerewolfGame extends BaseGame {
    private WerewolfGameSettings settings;
    private Map<UUID, PlayerProperty> players;
    private ServerLevel level;

    public WerewolfGame(ServerPlayer sender, WerewolfGameSettings settings){
        super(sender.getServer());
        if(settings.<Boolean>get("ForceStart") || settings.isValid(server)){
            this.settings = settings;
            initialize();
            phase = new InitialPhase(this);
        } else {
            phase = new AbortedPhase(this, AbortedPhase.Context.invalidSettings(sender));
        }
        level = sender.getLevel();
    }

    private void initialize() {
        Map<ServerPlayer, WwrpgPlayerSavedData> savedDataMap = new HashMap<>();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            savedDataMap.put(player, WwrpgPlayerSavedDataManager.get(player));
        }

        // Setup for players
        Map<ServerPlayer, PlayerPreferences> prefMap = new HashMap<>();
        for (Map.Entry<ServerPlayer, WwrpgPlayerSavedData> entry : savedDataMap.entrySet()) {
            prefMap.put(entry.getKey(), entry.getValue().preferences);
        }
        settings.setup(server);
        List<ServerPlayer> playerList = server.getPlayerList().getPlayers();
        int[] random = MathUtil.randomPermutation(playerList.size());
        Map<Role, Integer> roleNums = settings.copyRoles();
        List<ServerPlayer> undecided = new ArrayList<>();
        Map<ServerPlayer, Role> result = new HashMap<>();
        for (int i = 0; i < playerList.size(); i++) {
            ServerPlayer p = playerList.get(random[i]);
            PlayerPreferences pref = prefMap.get(p);
            Role role = pref.getDesiredRole();
            if (pref.isEnabled() && roleNums.get(role) > 0) {
                result.put(p, role);
                roleNums.put(role, roleNums.get(role) - 1);
            } else {
                undecided.add(p);
            }
        }
        int roleOrder = 0;
        for (ServerPlayer p : undecided) {
            while (true) {
                Role role = Role.values()[roleOrder];
                if (roleNums.get(role) == 0) roleOrder++;
                else {
                    result.put(p, role);
                    break;
                }
            }
        }
        players = new HashMap<>();
        for (Map.Entry<ServerPlayer, Role> entry : result.entrySet()) {
            players.put(entry.getKey().getUUID(), new PlayerProperty(entry.getKey(), entry.getValue()));
        }

        // Saves
        for (WwrpgPlayerSavedData data : savedDataMap.values()) {
            data.putSettings(GameType.WEREWOLF, settings);
        }
    }

    public WerewolfGameSettings getSettings(){
        return settings;
    }

    public Map<UUID, PlayerProperty> getPlayers() {
        return players;
    }

    public ServerLevel getLevel(){
        return level;
    }

    public void abort(){
        if(phase instanceof MainPhase mainPhase) mainPhase.abort();
        else if(phase instanceof InitialPhase initialPhase) initialPhase.abort();
    }

    @Override
    public boolean finished() {
        return ((WerewolfGamePhase) phase).finished();
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event){
        ((WerewolfGamePhase) phase).onLivingDeath(event);
    }
}
