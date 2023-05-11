package ecs.components.skill;

import ecs.components.MissingComponentException;
import ecs.components.VelocityComponent;
import ecs.entities.Entity;

public class SpeedSkill implements ISkillFunction {
    private final double speedIncrease;
    private double currentSpeedIncrease;

    // is needed to switch Skill on and off
    private boolean isAktive;
    private Entity entity;
    public SpeedSkill(Entity entity, double speedIncrease) {
        this.entity = entity;
        this.speedIncrease = speedIncrease;
    }

    public void execute(Entity entity) {
        isAktive = !isAktive;
        VelocityComponent vc =
            (VelocityComponent)
                entity.getComponent(VelocityComponent.class)
                    .orElseThrow(
                        () -> new MissingComponentException("VeloccityComponent"));
        if(isAktive) {
            vc.setXVelocity((float) (vc.getXVelocity() * speedIncrease));
            vc.setYVelocity((float) (vc.getYVelocity() * speedIncrease));
            currentSpeedIncrease = speedIncrease;
        }
        else {
            vc.setXVelocity((float) (vc.getXVelocity() / currentSpeedIncrease));
            vc.setYVelocity((float) (vc.getYVelocity() / currentSpeedIncrease));
            currentSpeedIncrease = 1;
        }

    }
}
