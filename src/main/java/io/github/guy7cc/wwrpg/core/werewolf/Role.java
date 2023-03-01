package io.github.guy7cc.wwrpg.core.werewolf;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public enum Role {
    VILLAGER(Team.VILLAGERS, InvestigationResult.VILLAGER, Component.translatable("wwrpg.role.villager").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))),
    WEREWOLF(Team.WEREWOLVES, InvestigationResult.WEREWOLF, Component.translatable("wwrpg.role.werewolf").withStyle(Style.EMPTY.withColor(ChatFormatting.RED))),
    VAMPIRE(Team.VAMPIRES, InvestigationResult.VAMPIRE, Component.translatable("wwrpg.role.vampire").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE))),
    MADMAN(Team.WEREWOLVES, InvestigationResult.VILLAGER, Component.translatable("wwrpg.role.madman").withStyle(Style.EMPTY.withColor(ChatFormatting.RED))),
    IMMORALIST(Team.VAMPIRES, InvestigationResult.VILLAGER, Component.translatable("wwrpg.role.immoralist").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE))),
    HAUNTED(Team.VILLAGERS, InvestigationResult.WEREWOLF, Component.translatable("wwrpg.role.haunted").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))),
    SPECTATOR(null, null, Component.translatable("wwrpg.role.spectator"));

    public final Team team;
    public final InvestigationResult investigationResult;
    public final Component text;

    Role(Team team, InvestigationResult investigationResult, Component text){
        this.team = team;
        this.investigationResult = investigationResult;
        this.text = text;
    }

}
