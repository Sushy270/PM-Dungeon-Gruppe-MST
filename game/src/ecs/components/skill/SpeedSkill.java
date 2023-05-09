package ecs.components.skill;

import ecs.entities.Entity;

public class SpeedSkill implements ISkillFunction {
    private final double speedIncrease;

    // ist needed to switch Skill on and off
    private boolean isAktive;
    private Entity entity;
    public SpeedSkill(Entity entity, double speedIncrease) {
        this.entity = entity;
        this.speedIncrease = speedIncrease;
    }

    public void execute(Entity entity) {
        System.out.println("toggle Speed");
    }
}
