package io.github.guy7cc.wwrpg.core.werewolf.phase;

import io.github.guy7cc.wwrpg.core.BaseGame;
import io.github.guy7cc.wwrpg.core.BasePhase;
import io.github.guy7cc.wwrpg.core.werewolf.Judgement;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGame;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGameSettings;

public class MainPhase extends WerewolfGamePhase {
    private boolean aborted = false;

    public MainPhase(WerewolfGame game) {
        super(game);
    }

    @Override
    public BasePhase tick() {
        WerewolfGameSettings settings = asWwGame.getSettings();
        Judgement judgement = Judgement.judge(asWwGame.getPlayers().values());
        if(judgement.finished() && !settings.<Boolean>get("DontFinish")){

        } else if(aborted){
            return new AbortedPhase(asWwGame, AbortedPhase.Context.abortedByOp());
        }
        return super.tick();
    }

    @Override
    public boolean finished() {
        return false;
    }

    public void abort(){
        aborted = true;
    }
}
