package io.github.guy7cc.wwrpg.core;

import net.minecraftforge.common.MinecraftForge;

import java.util.function.Consumer;

public class GameManager {
    private static BaseGame game;

    public static void serverTick(){
        if(game != null) {
            game.tick();
            if(game.finished()){
                MinecraftForge.EVENT_BUS.unregister(game);
                game = null;
            }
        }
    }

    public static void startGame(BaseGame game){
        if(GameManager.game != null) MinecraftForge.EVENT_BUS.unregister(GameManager.game);
        GameManager.game = game;
        MinecraftForge.EVENT_BUS.register(GameManager.game);
    }

    public static <T extends BaseGame> boolean apply(Class<T> type, Consumer<T> consumer){
        if(game != null && game.getClass() == type) {
            consumer.accept((T) game);
            return true;
        }
        return false;
    }

    public static String getGameAsString(){
        return game != null ? game.toString() : "NoGame";
    }
}
