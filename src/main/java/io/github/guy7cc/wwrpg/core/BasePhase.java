package io.github.guy7cc.wwrpg.core;

public abstract class BasePhase {
    protected BaseGame game;
    private int phaseTick = 0;

    public BasePhase(BaseGame game){
        this.game = game;
    }

    public BasePhase tick(){
        phaseTick++;
        return null;
    }

    public int getPhaseTick(){
        return phaseTick;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{tick=" + phaseTick + '}';
    }
}
