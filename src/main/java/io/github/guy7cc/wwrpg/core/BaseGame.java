package io.github.guy7cc.wwrpg.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BaseGame {
    private int tick = 0;
    protected BasePhase phase;

    public void tick(){
        tick++;
        phase = phase.tick();
    }

    public int getTick(){
        return tick;
    }

    public abstract boolean finished();
}
