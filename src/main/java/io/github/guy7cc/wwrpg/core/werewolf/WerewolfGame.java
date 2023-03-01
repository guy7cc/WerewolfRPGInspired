package io.github.guy7cc.wwrpg.core.werewolf;

import io.github.guy7cc.wwrpg.core.BaseGame;
import io.github.guy7cc.wwrpg.core.werewolf.phase.AbortedPhase;
import io.github.guy7cc.wwrpg.core.werewolf.phase.WerewolfGamePhase;
import io.github.guy7cc.wwrpg.save.cap.WwrpgPlayerSavedData;
import io.github.guy7cc.wwrpg.save.cap.WwrpgPlayerSavedDataManager;
import io.github.guy7cc.wwrpg.util.MathUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class WerewolfGame extends BaseGame {
    private MinecraftServer server;

    private WerewolfGameSettings settings;
    private Map<UUID, PlayerProperty> players;

    public WerewolfGame(ServerPlayer sender, WerewolfGameSettings settings){
        server = sender.getServer();
        if(settings.isValid(server)){
            this.settings = settings;
            initialize();
        } else {
            sender.displayClientMessage(Component.translatable("wwrpg.message.invalidSettings").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
            phase = new AbortedPhase(this, AbortedPhase.AbortedContext.INVALID_SETTINGS);
        }
    }

    private void initialize(){
        Map<ServerPlayer, WwrpgPlayerSavedData> savedDataMap = new HashMap<>();
        for(ServerPlayer player : server.getPlayerList().getPlayers()) {
            savedDataMap.put(player, WwrpgPlayerSavedDataManager.get(player));
        }

        // Setup for players
        Map<ServerPlayer, WerewolfGamePreferences> prefMap = new HashMap<>();
        for(Map.Entry<ServerPlayer, WwrpgPlayerSavedData> entry : savedDataMap.entrySet()) {
            prefMap.put(entry.getKey(), entry.getValue().preferences);
        }
        settings.fillVillagerNum(server);
        List<ServerPlayer> playerList = server.getPlayerList().getPlayers();
        int[] random = MathUtil.randomPermutation(playerList.size());
        Map<Role, Integer> roleNums = settings.copyRoleMap();
        List<ServerPlayer> undecided = new ArrayList<>();
        Map<ServerPlayer, Role> result = new HashMap<>();
        for(int i = 0; i < playerList.size(); i++){
            ServerPlayer p = playerList.get(random[i]);
            WerewolfGamePreferences pref = prefMap.get(p);
            Role role = pref.getDesiredRole();
            if(pref.isEnabled() && roleNums.get(role) > 0){
                result.put(p, role);
                roleNums.put(role, roleNums.get(role) - 1);
            } else {
                undecided.add(p);
            }
        }
        int roleOrder = 0;
        for(ServerPlayer p : undecided){
            while(true){
                Role role = Role.values()[roleOrder];
                if(roleNums.get(role) == 0) roleOrder++;
                else {
                    result.put(p, role);
                    break;
                }
            }
        }
        players = new HashMap<>();
        for(Map.Entry<ServerPlayer, Role> entry : result.entrySet()){
            players.put(entry.getKey().getUUID(), new PlayerProperty(entry.getKey(), entry.getValue()));
        }

        // Saves
        for(WwrpgPlayerSavedData data : savedDataMap.values()){
            data.settings = settings;
        }
    }

    @Override
    public boolean finished() {
        return ((WerewolfGamePhase) phase).finished();
    }
}
