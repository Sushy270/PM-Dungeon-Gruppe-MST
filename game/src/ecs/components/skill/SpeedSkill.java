package ecs.components.skill;

import ecs.entities.Entity;

public class SpeedSkill implements ISkillFunction {
    private final double speedIncrease;
    private final int framesPerManaPoint;
    private ManaComponent mc;

    // is needed to switch Skill on and off
    private boolean isAktive;
    private Entity entity;
    public SpeedSkill(Entity entity, double speedIncrease, int framesPerManaPoint, ManaComponent mc) {
        this.entity = entity;
        this.speedIncrease = speedIncrease;
        this.framesPerManaPoint = framesPerManaPoint;
        this.mc = mc;
    }

    public void execute(Entity entity) {
        System.out.println("toggle Speed");
    }

    public int getFramesPerManaPoint() {
        return framesPerManaPoint;
    }
}
