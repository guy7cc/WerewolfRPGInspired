package io.github.guy7cc.wwrpg.save.cap;

import io.github.guy7cc.wwrpg.core.BaseSettings;
import io.github.guy7cc.wwrpg.core.GameType;
import io.github.guy7cc.wwrpg.core.PlayerPreferences;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class WwrpgPlayerSavedData implements INBTSerializable<CompoundTag> {
    private Map<GameType, BaseSettings> settingsMap;
    public PlayerPreferences preferences;

    public WwrpgPlayerSavedData(){
        settingsMap = new HashMap<>();
        for(GameType type : GameType.values()){
            settingsMap.put(type, type.defaultSettings.get());
        }
        preferences = new PlayerPreferences();
    }

    public BaseSettings getSettings(GameType type){
        return settingsMap.get(type);
    }

    public void putSettings(GameType type, BaseSettings settings){
        if(!settings.getClass().equals(type.settingsClass)) throw new IllegalArgumentException("Settings type is wrong.");
        settingsMap.put(type, settings);
    }

    public void copy(WwrpgPlayerSavedData cap){
        for(GameType type : GameType.values()){
            settingsMap.put(type, BaseSettings.copy(cap.settingsMap.get(type)));
        }
        preferences.copy(cap.preferences);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        CompoundTag settingsTag = new CompoundTag();
        for(GameType type : GameType.values()){
            settingsTag.put(type.toString(), settingsMap.get(type).serializeNBT());
        }
        tag.put("Settings", settingsTag);
        tag.put("Preferences", preferences.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        settingsMap.clear();
        CompoundTag settingsTag = nbt.getCompound("Settings");
        for(GameType type : GameType.values()){
            BaseSettings s = type.defaultSettings.get();
            s.deserializeNBT(settingsTag.getCompound(type.toString()));
            settingsMap.put(type, s);
        }
        preferences = new PlayerPreferences();
        preferences.deserializeNBT(nbt.getCompound("Preferences"));
    }
}
