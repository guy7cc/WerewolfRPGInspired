package io.github.guy7cc.wwrpg.core.werewolf;

import io.github.guy7cc.wwrpg.util.IntRange;

import java.util.*;

@FunctionalInterface
public interface RoleDistributionProtocol {
    Map<Role, Integer> get(Map<Role, IntRange> ranges, int playerNum);

    RoleDistributionProtocol FROM_VALID_SETTINGS = (ranges, playerNum) -> {
        Map<Role, Integer> map = new HashMap<>();
        Map<Role, Integer> indexes = new HashMap<>();
        ranges.put(Role.VILLAGER, IntRange.simple(1, playerNum - 1));
        for(Role role : Role.values()){
            int minValue = 0;
            if(role == Role.VILLAGER || role == Role.WEREWOLF) minValue++;
            int member = ranges.get(role).floor(minValue);
            map.put(role, member);
            indexes.put(role, ranges.get(role).indexOf(member));
            playerNum -= member;
        }
        Random random = new Random();
        List<Role> availableRoles = Arrays.stream(Role.values()).toList();
        while(playerNum > 0){
            Role role = availableRoles.get(random.nextInt(availableRoles.size()));
            if(indexes.get(role) == ranges.get(role).size() - 1){
                availableRoles.remove(role);
                continue;
            }
            int nextMember = ranges.get(role).get(indexes.get(role) + 1);
            if(nextMember - map.get(role) > playerNum){
                availableRoles.remove(role);
                continue;
            }
            map.put(role, nextMember);
            playerNum -= nextMember - map.get(role);
        }
        return map;
    };

    RoleDistributionProtocol FORCE_START = (ranges, playerNum) -> {
        Random r = new Random();
        Map<Role, Integer> map = new HashMap<>();
        if(playerNum == 1){
            for(Role role : Role.values()){
                map.put(role, 0);
            }
            if(r.nextInt(2) == 0) map.put(Role.VILLAGER, 1);
            else map.put(Role.WEREWOLF, 1);
            return map;
        } else {
            for(Role role : Role.values()){
                switch(role){
                    case VILLAGER:
                    case WEREWOLF:
                        map.put(role, 1);
                    default:
                        map.put(role, 0);
                }
            }
            playerNum -= 2;
            while(playerNum > 0){
                Role role = Role.values()[r.nextInt(Role.values().length)];
                map.compute(role, (rl, v) -> v + 1);
            }
            return map;
        }
    };
}
