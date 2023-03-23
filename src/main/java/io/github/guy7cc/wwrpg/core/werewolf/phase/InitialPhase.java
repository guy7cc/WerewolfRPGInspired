package io.github.guy7cc.wwrpg.core.werewolf.phase;

import io.github.guy7cc.wwrpg.client.ClientEffect;
import io.github.guy7cc.wwrpg.core.BasePhase;
import io.github.guy7cc.wwrpg.core.werewolf.PlayerProperty;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGame;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGameSettings;
import io.github.guy7cc.wwrpg.network.ClientboundSetEffectSchedulePacket;
import io.github.guy7cc.wwrpg.network.PacketManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InitialPhase extends WerewolfGamePhase {
    private boolean aborted = false;

    public InitialPhase(WerewolfGame game) {
        super(game);
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public BasePhase tick() {
        super.tick();
        if(aborted){
            return new AbortedPhase(asWwGame, AbortedPhase.Context.abortedByOp());
        }
        int phaseTick = getPhaseTick();
        List<ServerPlayer> players = game.getServer().getPlayerList().getPlayers();
        if(phaseTick == 1){
            setTitle(players, List.of(
                    new ClientEffect.Title(0, Component.literal("3"), Component.empty(), 5, 20, 10),
                    new ClientEffect.Title(20, Component.literal("2"), Component.empty(), 5, 20, 10),
                    new ClientEffect.Title(40, Component.literal("1"), Component.empty(), 5, 20, 10)
            ));
        }
        else if(phaseTick == 61){
            WerewolfGameSettings settings = asWwGame.getSettings();
            Map<UUID, PlayerProperty> propertyMap = asWwGame.getPlayers();
            players.forEach(p -> {
                setTitle(p, List.of(new ClientEffect.Title(0, Component.translatable("wwrpg.game.start"), Component.translatable("wwrpg.game.yourRole", propertyMap.get(p.getUUID()).getRole().getDisplayNameComponent()), 10, 70, 20)));
                Vec3 spawnPos = settings.get("SpawnPos");
                p.teleportTo(asWwGame.getLevel(), spawnPos.x, spawnPos.y, spawnPos.z, 0, 0);
            });
        }
        else if(phaseTick == 661){
            return new DayTimePhase(asWwGame);
        }
        return this;
    }

    private void setTitle(ServerPlayer player, List<ClientEffect> effects){
        PacketManager.sendToPlayer(player, new ClientboundSetEffectSchedulePacket(effects));
    }

    private void setTitle(Collection<ServerPlayer> players, List<ClientEffect> effects){
        for(ServerPlayer player : players){
            setTitle(player, effects);
        }
    }

    public void abort(){
        aborted = true;
    }

    @Override
    public void onLivingDeath(LivingDeathEvent event) {
        if(event.getEntity() instanceof Player) event.setCanceled(true);
    }
}
