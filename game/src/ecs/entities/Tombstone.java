package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AITools;
import starter.Game;

public class Tombstone extends Entity{
    private final String path = "dungeon/skull.png";
    private NpcGhost npcGhost;
    public Tombstone(NpcGhost npc){
        super();
        npcGhost = npc;
        new PositionComponent(this);
        new AnimationComponent(this, AnimationBuilder.buildAnimation(path));
    }

    /**Despawns random one Kind of Monster, if player, ghost and tombstone are in range*/
    public void despawnOneKindOfMonster(){
        if(AITools.playerInRange(npcGhost, 2) && AITools.playerInRange(this, 2)){
            Game.despawnMonster();
            Game.removeEntity(npcGhost);
        }
    }

    public void spawnNewMonster(){
        if(AITools.playerInRange(npcGhost, 2) && AITools.playerInRange(this, 2)){
            Game.spawnOneZombie();
            Game.removeEntity(npcGhost);
        }
    }
}
