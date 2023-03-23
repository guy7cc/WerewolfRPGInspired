package io.github.guy7cc.wwrpg.core.werewolf;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public enum Team {
    VILLAGERS("villagers", Style.EMPTY.withColor(ChatFormatting.GREEN)),
    WEREWOLVES("werewolves", Style.EMPTY.withColor(ChatFormatting.RED)),
    VAMPIRES("vampires", Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE)),
    DUMMY;

    public final String name;
    public final Style style;

    Team(){
        this("", Style.EMPTY);
    }

    Team(String name, Style style){
        this.name = name;
        this.style = style;
    }

    public Component getComponent(){
        return Component.translatable("wwrpg.team." + name).withStyle(style);
    }
}
