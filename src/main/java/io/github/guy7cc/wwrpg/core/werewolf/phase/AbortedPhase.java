package io.github.guy7cc.wwrpg.core.werewolf.phase;

import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGame;

public class AbortedPhase extends WerewolfGamePhase {
    public final AbortedContext ctx;

    public AbortedPhase(WerewolfGame game, AbortedContext ctx) {
        super(game);
        this.ctx = ctx;
    }

    @Override
    public boolean finished() {
        return true;
    }

    @Override
    public WerewolfGamePhase tick(WerewolfGame game) {
        return this;
    }

    public enum AbortedContext{
        INVALID_SETTINGS,
        ABORTED_BY_OP,
    }
}
