package io.github.guy7cc.wwrpg.core.werewolf.phase;

import io.github.guy7cc.wwrpg.core.BasePhase;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGame;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public abstract class WerewolfGamePhase extends BasePhase {
    protected WerewolfGame asWwGame;

    public WerewolfGamePhase(WerewolfGame game) {
        super(game);
        asWwGame = game;
    }

    public abstract boolean finished();

    public void onLivingDeath(LivingDeathEvent event){}
}
