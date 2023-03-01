package io.github.guy7cc.wwrpg.save.cap;

import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGamePreferences;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGameSettings;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class WwrpgPlayerSavedData implements INBTSerializable<CompoundTag> {
    public WerewolfGameSettings settings;
    public WerewolfGamePreferences preferences;

    public void copy(WwrpgPlayerSavedData cap){
        settings.copy(cap.settings);
        preferences.copy(cap.preferences);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("Settings", settings.serializeNBT());
        tag.put("Preferences", preferences.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        settings = new WerewolfGameSettings();
        preferences = new WerewolfGamePreferences();
        settings.deserializeNBT(nbt.getCompound("Settings"));
        preferences.deserializeNBT(nbt.getCompound("Preferences"));
    }
}
