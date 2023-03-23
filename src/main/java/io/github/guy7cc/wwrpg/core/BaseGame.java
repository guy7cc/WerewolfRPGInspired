package io.github.guy7cc.wwrpg.core;

import net.minecraft.server.MinecraftServer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BaseGame {
    protected MinecraftServer server;

    private int tick = 0;
    protected BasePhase phase;

    public BaseGame(MinecraftServer server){
        this.server = server;
    }

    public void tick(){
        tick++;
        phase = phase.tick();
    }

    public int getTick(){
        return tick;
    }

    public MinecraftServer getServer(){
        return server;
    }

    public abstract boolean finished();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{tick=" + tick + ", phase=" + phase + '}';
    }
}
