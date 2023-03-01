package io.github.guy7cc.wwrpg.save.cap;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Supplier;

public abstract class AbstractCapabilityProvider<T extends INBTSerializable<CompoundTag>> implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    protected final Supplier<T> defaultSupplier;
    protected T handler = null;
    protected final LazyOptional<T> holder = LazyOptional.of(this::getHandler);

    public AbstractCapabilityProvider(Supplier<T> defaultSupplier){
        this.defaultSupplier = defaultSupplier;
    }

    protected T getHandler(){
        if(handler == null){
            handler = defaultSupplier.get();
        }
        return handler;
    }

    @Override
    public CompoundTag serializeNBT() {
        return getHandler().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getHandler().deserializeNBT(nbt);
    }
}
