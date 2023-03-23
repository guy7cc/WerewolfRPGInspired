package io.github.guy7cc.wwrpg.client;

import net.minecraft.client.Minecraft;

import java.util.concurrent.Callable;

public class ClientExecutor {
    public static Callable<Integer> getOnlinePlayerNum(){
        return () -> {
            Minecraft minecraft = Minecraft.getInstance();
            return minecraft.player.connection.getOnlinePlayers().size();
        };
    }
}
