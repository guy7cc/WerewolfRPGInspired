package io.github.guy7cc.wwrpg.core.werewolf;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public enum InvestigationResult {
    VILLAGER("villager", Team.VILLAGERS.style),
    WEREWOLF("werewolf", Team.WEREWOLVES.style),
    VAMPIRE("vampire", Team.VAMPIRES.style);

    public final String name;
    public final Style style;


    InvestigationResult(String name, Style style){
        this.name = name;
        this.style = style;
    }

    public Component getComponent(){
        return Component.translatable("wwrpg.role." + name).withStyle(style);
    }
}
