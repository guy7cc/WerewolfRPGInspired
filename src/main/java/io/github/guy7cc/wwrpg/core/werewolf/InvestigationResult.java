package io.github.guy7cc.wwrpg.core.werewolf;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public enum InvestigationResult {
    VILLAGER(Component.translatable("wwrpg.role.villager").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))),
    WEREWOLF(Component.translatable("wwrpg.role.werewolf").withStyle(Style.EMPTY.withColor(ChatFormatting.RED))),
    VAMPIRE(Component.translatable("wwrpg.role.vampire").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE)));

    public final Component text;

    InvestigationResult(Component text){
        this.text = text;
    }
}
