package io.github.guy7cc.wwrpg.save.cap;

import io.github.guy7cc.wwrpg.WerewolfRPGInspired;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class WwrpgPlayerSavedDataProvider extends AbstractCapabilityProvider<WwrpgPlayerSavedData> {
    public static final ResourceLocation WWRPG_PLAYER_SAVED_DATA_LOCATION = new ResourceLocation(WerewolfRPGInspired.ID, "wwrpg_player_capability");
    public static final Capability<WwrpgPlayerSavedData> WWRPG_PLAYER_SAVED_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public WwrpgPlayerSavedDataProvider(Supplier<WwrpgPlayerSavedData> defaultSupplier) {
        super(defaultSupplier);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == WWRPG_PLAYER_SAVED_DATA_CAPABILITY) return holder.cast();
        return LazyOptional.empty();
    }
}
