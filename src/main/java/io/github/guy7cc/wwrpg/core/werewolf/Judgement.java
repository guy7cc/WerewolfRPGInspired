package io.github.guy7cc.wwrpg.core.werewolf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Judgement {
    private boolean finished;
    private Team winner;

    private Judgement(boolean finished, Team winner){
        this.finished = finished;
        this.winner = winner;
    }

    public static Judgement judge(Collection<? extends RoleGetter> roles){
        Map<Team, Integer> teamMap = getTeamNums(roles);
        Map<Role, Integer> roleMap = getRoleNums(roles);
        boolean villagers = teamMap.get(Team.VILLAGERS) > 0;
        boolean werewolves = roleMap.get(Role.WEREWOLF) > 0;
        boolean vampires = roleMap.get(Role.VAMPIRE) > 0;
        if(villagers && werewolves && vampires) return new Judgement(false, Team.DUMMY);
        else if(villagers && !werewolves && !vampires) return new Judgement(true, Team.VILLAGERS);
        else if(!villagers && werewolves && !vampires) return new Judgement(true, Team.VAMPIRES);
        else if(vampires) return new Judgement(true, Team.VAMPIRES);
        else return new Judgement(true, Team.DUMMY);
    }

    public boolean finished(){
        return finished;
    }

    public boolean draw(){
        return finished && winner == Team.DUMMY;
    }

    public Team winner(){
        return winner;
    }

    private static Map<Team, Integer> getTeamNums(Collection<? extends RoleGetter> roles){
        Map<Team, Integer> map = new HashMap<>();
        Team[] teams = new Team[]{ Team.VILLAGERS, Team.WEREWOLVES, Team.WEREWOLVES };
        for(int i = 0; i < 3; i++){
            int j = i;
            map.put(teams[j], (int)roles.stream().filter(r -> r.getRole().team == teams[j]).count());
        }
        return map;
    }

    private static Map<Role, Integer> getRoleNums(Collection<? extends RoleGetter> roles){
        Map<Role, Integer> map = new HashMap<>();
        for(int i = 0; i < Role.values().length; i++){
            int j = i;
            map.put(Role.values()[j], (int)roles.stream().filter(r -> r.getRole() == Role.values()[j]).count());
        }
        return map;
    }
}
