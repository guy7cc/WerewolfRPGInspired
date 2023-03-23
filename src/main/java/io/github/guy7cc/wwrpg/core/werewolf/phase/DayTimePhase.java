package io.github.guy7cc.wwrpg.core.werewolf.phase;

import io.github.guy7cc.wwrpg.core.BasePhase;
import io.github.guy7cc.wwrpg.core.BaseSettings;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGame;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGameSettings;

public class DayTimePhase extends MainPhase {
    public DayTimePhase(WerewolfGame game) {
        super(game);
    }

    @Override
    public BasePhase tick() {
        super.tick();
        WerewolfGameSettings settings = asWwGame.getSettings();
        if(getPhaseTick() >= settings.<Integer>get("DayTime")){
            return new NightTimePhase(asWwGame);
        }
        return this;
    }

    @Override
    public boolean finished() {
        return false;
    }
}
