package io.github.guy7cc.wwrpg.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public abstract class ClientEffect {
    public int tickLeft;

    public ClientEffect(int tickLeft) {
        this.tickLeft = tickLeft;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(tickLeft);
    }

    public abstract void apply();

    public static final class Title extends ClientEffect {
        public Component title;
        public Component subTitle;
        public int fadeIn;
        public int stay;
        public int fadeOut;

        public Title(int tickLeft, Component title, Component subTitle, int fadeIn, int stay, int fadeOut) {
            super(tickLeft);
            this.title = title;
            this.subTitle = subTitle;
            this.fadeIn = fadeIn;
            this.stay = stay;
            this.fadeOut = fadeOut;
        }

        @Override
        public void toBytes(FriendlyByteBuf buf) {
            super.toBytes(buf);
            buf.writeComponent(title);
            buf.writeComponent(subTitle);
            buf.writeInt(fadeIn);
            buf.writeInt(stay);
            buf.writeInt(fadeOut);
        }

        public Title(FriendlyByteBuf buf) {
            this(buf.readInt(), buf.readComponent(), buf.readComponent(), buf.readInt(), buf.readInt(), buf.readInt());
        }

        @Override
        public void apply() {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.gui.setTitle(title);
            minecraft.gui.setSubtitle(subTitle);
            minecraft.gui.setTimes(fadeIn, stay, fadeOut);
        }
    }
}
