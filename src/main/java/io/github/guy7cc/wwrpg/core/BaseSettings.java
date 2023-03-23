package io.github.guy7cc.wwrpg.core;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

public class BaseSettings implements INBTSerializable<CompoundTag> {
    private Map<String, Component<?>> components = new HashMap<>();

    public BaseSettings(){
        this(List.of());
    }

    public BaseSettings(List<Component<?>> clientSettings){
        addAll(clientSettings);
    }

    public void add(Component<?> component){
        components.put(component.name, component);
    }

    public void addAll(List<Component<?>> settings){
        for(Component<?> c : settings){
            components.put(c.name, c);
        }
    }

    public boolean contains(String key){
        return components.containsKey(key);
    }

    public <T> T get(String key){
        return (T)components.get(key).value;
    }

    public <T> Component<T> getComponent(String key){
        return (Component<T>)components.get(key);
    }

    public <T> void set(String key, T value){
        Component<T> component = getComponent(key);
        component.value = value;
    }

    public static BaseSettings copy(BaseSettings original){
        BaseSettings settings = new BaseSettings();
        for(Component<?> component : original.components.values()){
            settings.add(new Component<>(component));
        }
        return settings;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        for(Component<?> c : components.values()){
            nbt.put(c.name, c.serializeNBT());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for(Component<?> c : components.values()){
            Tag tag = nbt.get(c.name);
            c.deserializeNBT(tag);
        }
    }

    public static class Component<T> {
        public final String name;
        public T value;
        private final BiFunction<T, MinecraftServer, Boolean> check;
        private final Function<T, Tag> serializer;
        private final Function<Tag, T> deserializer;

        public Component(String name, T defaultValue, BiFunction<T, MinecraftServer, Boolean> check, Function<T, Tag> serializer, Function<Tag, T> deserializer){
            this.name = name;
            value = defaultValue;
            this.check = check;
            this.serializer = serializer;
            this.deserializer = deserializer;
        }

        public Component(Component<T> other){
            name = other.name;
            check = other.check;
            serializer = other.serializer;
            deserializer = other.deserializer;
            value = deserializer.apply(serializer.apply(other.value));
        }

        public boolean isValid(@Nullable MinecraftServer server){
            return check.apply(value, server);
        }

        public Tag serializeNBT(){
            return serializer.apply(value);
        }

        public void deserializeNBT(Tag tag){
            value = deserializer.apply(tag);
        }
    }

    public static class SerializableComponent<T extends INBTSerializable<U>, U extends Tag> extends Component<T>{
        public SerializableComponent(String name, T defaultValue, BiFunction<T, MinecraftServer, Boolean> check, Supplier<T> supplier) {
            super(name, defaultValue, check, T::serializeNBT, tag -> {
                T obj = supplier.get();
                obj.deserializeNBT((U)tag);
                return obj;
            });
        }
    }

    public static class ByteComponent extends Component<Byte>{
        public ByteComponent(String name, byte defaultValue, BiFunction<Byte, MinecraftServer, Boolean> check) {
            super(name, defaultValue, check, ByteTag::valueOf, tag -> ((NumericTag)tag).getAsByte());
        }
    }

    public static class ShortComponent extends Component<Short>{
        public ShortComponent(String name, short defaultValue, BiFunction<Short, MinecraftServer, Boolean> check) {
            super(name, defaultValue, check, ShortTag::valueOf, tag -> ((NumericTag)tag).getAsShort());
        }
    }

    public static class IntComponent extends Component<Integer>{
        public IntComponent(String name, int defaultValue, BiFunction<Integer, MinecraftServer, Boolean> check) {
            super(name, defaultValue, check, IntTag::valueOf, tag -> ((NumericTag)tag).getAsInt());
        }
    }

    public static class LongComponent extends Component<Long>{
        public LongComponent(String name, long defaultValue, BiFunction<Long, MinecraftServer, Boolean> check) {
            super(name, defaultValue, check, LongTag::valueOf, tag -> ((NumericTag)tag).getAsLong());
        }
    }

    public static class FloatComponent extends Component<Float>{
        public FloatComponent(String name, float defaultValue, BiFunction<Float, MinecraftServer, Boolean> check) {
            super(name, defaultValue, check, FloatTag::valueOf, tag -> ((NumericTag)tag).getAsFloat());
        }
    }

    public static class DoubleComponent extends Component<Double>{
        public DoubleComponent(String name, double defaultValue, BiFunction<Double, MinecraftServer, Boolean> check) {
            super(name, defaultValue, check, DoubleTag::valueOf, tag -> ((NumericTag)tag).getAsDouble());
        }
    }

    public static class BooleanComponent extends Component<Boolean>{
        public BooleanComponent(String name, boolean defaultValue, BiFunction<Boolean, MinecraftServer, Boolean> check) {
            super(name, defaultValue, check, ByteTag::valueOf, tag -> ((NumericTag)tag).getAsByte() != 0);
        }
    }

    public static class StringComponent extends Component<String>{
        public StringComponent(String name, String defaultValue, BiFunction<String, MinecraftServer, Boolean> check) {
            super(name, defaultValue, check, StringTag::valueOf, Tag::getAsString);
        }
    }

    public static class Vec3Component extends Component<Vec3>{
        public Vec3Component(String name, Vec3 defaultValue, BiFunction<Vec3, MinecraftServer, Boolean> check) {
            super(name, defaultValue, check, Vec3Component::serialize, Vec3Component::deserialize);
        }

        public static CompoundTag serialize(Vec3 pos){
            CompoundTag tag = new CompoundTag();
            tag.putDouble("X", pos.x);
            tag.putDouble("Y", pos.y);
            tag.putDouble("Z", pos.z);
            return tag;
        }

        public static Vec3 deserialize(Tag tag){
            CompoundTag tag2 = (CompoundTag) tag;
            Vec3 vec = new Vec3(tag2.getDouble("X"), tag2.getDouble("Y"), tag2.getDouble("Z"));
            return vec;
        }
    }
}
