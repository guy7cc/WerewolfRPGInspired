package io.github.guy7cc.wwrpg.core;

import net.minecraftforge.common.MinecraftForge;

public class GameManager {
    private BaseGame game;

    public void tick(){
        if(game != null) {
            game.tick();
            if(game.finished()){
                MinecraftForge.EVENT_BUS.unregister(game);
                game = null;
            }
        }
    }

    public void startGame(BaseGame game){
        if(this.game != null) MinecraftForge.EVENT_BUS.unregister(this.game);
        this.game = game;
        MinecraftForge.EVENT_BUS.register(this.game);
    }

}
