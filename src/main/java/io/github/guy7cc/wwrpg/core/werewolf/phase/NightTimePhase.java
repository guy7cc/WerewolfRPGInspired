package io.github.guy7cc.wwrpg.core.werewolf.phase;

import io.github.guy7cc.wwrpg.core.BasePhase;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGame;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGameSettings;

public class NightTimePhase extends MainPhase {
    public NightTimePhase(WerewolfGame game) {
        super(game);
    }

    @Override
    public BasePhase tick() {
        BasePhase phase = super.tick();
        if(phase != this) return phase;

        WerewolfGameSettings settings = asWwGame.getSettings();
        if(getPhaseTick() >= settings.<Integer>get("NightTime")){
            return new DayTimePhase(asWwGame);
        }
        return this;
    }

    @Override
    public boolean finished() {
        return false;
    }
}
