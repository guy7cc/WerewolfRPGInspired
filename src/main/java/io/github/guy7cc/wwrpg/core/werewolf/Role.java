package io.github.guy7cc.wwrpg.core.werewolf;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public enum Role {
    VILLAGER("villager", Team.VILLAGERS, InvestigationResult.VILLAGER),
    WEREWOLF("werewolf", Team.WEREWOLVES, InvestigationResult.WEREWOLF),
    VAMPIRE("vampire", Team.VAMPIRES, InvestigationResult.VAMPIRE),
    MADMAN("madman", Team.WEREWOLVES, InvestigationResult.VILLAGER),
    IMMORAL("immoral", Team.VAMPIRES, InvestigationResult.VILLAGER),
    HAUNTED("haunted", "villager", Team.VILLAGERS, InvestigationResult.WEREWOLF),
    SPECTATOR("spectator", Team.DUMMY, InvestigationResult.VILLAGER);

    public final String realName;
    public final String displayName;
    public final Team team;
    public final InvestigationResult investigationResult;

    Role(String name, Team team, InvestigationResult investigationResult){
        this(name, name, team, investigationResult);
    }

    Role(String realName, String displayName, Team team, InvestigationResult investigationResult){
        this.realName = realName;
        this.displayName = displayName;
        this.team = team;
        this.investigationResult = investigationResult;
    }

    public Component getRealNameComponent(){
        return Component.translatable("wwrpg.role." + realName).withStyle(team.style);
    }

    public Component getDisplayNameComponent(){
        return Component.translatable("wwrpg.role." + displayName).withStyle(team.style);
    }
}
