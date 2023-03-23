package io.github.guy7cc.wwrpg.core;


import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGameSettings;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public enum GameType {
    WEREWOLF("werewolf", WerewolfGameSettings.class, WerewolfGameSettings::new);

    public final String name;
    public final Class<? extends BaseSettings> settingsClass;
    public final Supplier<BaseSettings> defaultSettings;

    GameType(String name, Class<? extends BaseSettings> settingsClass, Supplier<BaseSettings> defaultSettings){
        this.name = name;
        this.settingsClass = settingsClass;
        this.defaultSettings = defaultSettings;
    }

    public Component getComponent(){
        return Component.translatable("wwrpg.gameType." + name);
    }
}
