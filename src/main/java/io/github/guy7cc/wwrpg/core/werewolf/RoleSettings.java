package io.github.guy7cc.wwrpg.core.werewolf;

import io.github.guy7cc.wwrpg.client.ClientExecutor;
import io.github.guy7cc.wwrpg.util.IntRange;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RoleSettings implements INBTSerializable<CompoundTag> {
    private Map<Role, IntRange> ranges;

    public RoleSettings() {
        this(new HashMap<>());
        for (Role role : Role.values()) {
            switch (role) {
                case VILLAGER:
                    break;
                case WEREWOLF:
                    ranges.put(role, IntRange.simple(1));
                    break;
                default:
                    ranges.put(role, IntRange.simple(0));
            }
        }
    }

    public RoleSettings(Map<Role, IntRange> ranges){
        this.ranges = ranges;
    }

    public Map<Role, IntRange> get(){
        return Collections.unmodifiableMap(ranges);
    }

    public boolean isValid(@Nullable MinecraftServer server) {
        int villagerNum = server != null ? server.getPlayerCount() : DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> ClientExecutor.getOnlinePlayerNum());
        for (Map.Entry<Role, IntRange> entry : ranges.entrySet()) {
            int minValue = entry.getKey() == Role.WEREWOLF ? 1 : 0;
            IntRange range = entry.getValue();
            if (!range.isEmpty() && range.max() >= minValue) {
                villagerNum -= range.floor(minValue);
            } else return false;
        }
        return villagerNum > 0;
    }

    public Map<Role, Integer> getResultOrThrow(@Nonnull MinecraftServer server, boolean forceStart) {
        Map<Role, Integer> result;
        if (isValid(server)) {
            result = RoleDistributionProtocol.FROM_VALID_SETTINGS.get(ranges, server.getPlayerCount());
        } else if (forceStart) {
            result = RoleDistributionProtocol.FORCE_START.get(ranges, server.getPlayerCount());
        } else throw new IllegalArgumentException("Roles cannot be decided because settings are invalid.");
        return result;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<Role, IntRange> entry : ranges.entrySet()) {
            tag.putString(entry.getKey().toString(), entry.getValue().toString());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (Role role : Role.values()) {
            if (nbt.contains(role.toString())) {
                ranges.put(role, IntRange.fromString(nbt.getString(role.toString())));
            }
        }
    }
}
