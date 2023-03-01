package io.github.guy7cc.wwrpg.core.werewolf.phase;

import io.github.guy7cc.wwrpg.core.BasePhase;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGame;

public abstract class WerewolfGamePhase extends BasePhase {
    public WerewolfGamePhase(WerewolfGame game) {
        super(game);
    }

    public abstract boolean finished();

    public abstract WerewolfGamePhase tick(WerewolfGame game);
}
