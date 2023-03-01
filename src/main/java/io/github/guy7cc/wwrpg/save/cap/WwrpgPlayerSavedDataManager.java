package io.github.guy7cc.wwrpg.save.cap;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.LazyOptional;

public class WwrpgPlayerSavedDataManager {
    public static WwrpgPlayerSavedData get(ServerPlayer player){
        return player.getCapability(WwrpgPlayerSavedDataProvider.WWRPG_PLAYER_SAVED_DATA_CAPABILITY).orElse(new WwrpgPlayerSavedData());
    }
}
