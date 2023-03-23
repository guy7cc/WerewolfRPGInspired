package io.github.guy7cc.wwrpg.core;

import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGameSettings;

public class SettingsModifier {
    public static boolean forceStart = false;
    public static boolean dontFinish = false;

    public static void apply(BaseSettings settings){
        if(settings instanceof WerewolfGameSettings){
            apply((WerewolfGameSettings) settings);
        }
    }

    public static void apply(WerewolfGameSettings settings){
        settings.set("ForceStart", forceStart);
        settings.set("DontFinish", dontFinish);
    }
}
