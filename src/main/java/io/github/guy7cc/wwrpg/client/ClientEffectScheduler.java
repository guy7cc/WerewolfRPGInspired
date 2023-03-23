package io.github.guy7cc.wwrpg.client;

import net.minecraft.network.FriendlyByteBuf;

import java.util.*;
import java.util.function.Function;

public class ClientEffectScheduler {
    private static int id = 0;
    private static Map<Class<? extends ClientEffect>, Integer> ids = new HashMap<>();
    private static List<Function<FriendlyByteBuf, ? extends ClientEffect>> factories = new ArrayList<>();

    static{
        register(ClientEffect.Title.class, ClientEffect.Title::new);
    }

    private static PriorityQueue<ClientEffect> queue = new PriorityQueue<>(Comparator.comparingInt(e -> e.tickLeft));

    public static <T extends ClientEffect> void register(Class<T> type, Function<FriendlyByteBuf, T> factory){
        ids.put(type, id);
        factories.add(factory);
        id++;
    }

    public static int getId(ClientEffect effect){
        return ids.get(effect.getClass());
    }

    public static ClientEffect fromId(int id, FriendlyByteBuf buf){
        return factories.get(id).apply(buf);
    }

    public static void enqueue(ClientEffect effect){
        queue.add(effect);
    }

    public static void clientTick(){
        while(queue.size() > 0 && queue.peek().tickLeft <= 0){
            ClientEffect effect = queue.poll();
            effect.apply();
        }
        for(ClientEffect schedule : queue){
            schedule.tickLeft--;
        }
    }

}
