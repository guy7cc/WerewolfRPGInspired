package io.github.guy7cc.wwrpg.core.werewolf;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class WerewolfGameHistory {
    private List<Event> events;

    public static class Event{
        private int tick;

        public Event(int tick){
            this.tick = tick;
        }
    }

    public static class KillEvent extends Event{
        private Component killer;
        private Component killed;

        public KillEvent(int tick, ServerPlayer killer, ServerPlayer killed){
            super(tick);
            this.killer = killer.getName();
            this.killed = killed.getName();
        }
    }
}
