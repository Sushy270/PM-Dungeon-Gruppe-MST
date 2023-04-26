package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.ai.AIComponent;
import ecs.components.ai.AITools;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.fight.MeleeAI;
import ecs.components.ai.idle.PatrouilleWalk;
import ecs.components.ai.idle.RadiusWalk;
import ecs.components.skill.*;
import graphic.Animation;
import level.generator.randomwalk.RandomWalkGenerator;
import starter.Game;

/**
 * The Hero is the player character. It's entity in the ECS. This class helps to setup the hero with
 * all its components and attributes .
 */

public class Monster extends Entity {
    private final float xSpeed = 0.3f;
    private final float ySpeed = 0.3f;

    private final String pathToIdleLeft = "character/monster/chort/idleLeft";
    private final String pathToIdleRight = "character/monster/chort/idleRight";
    private final String pathToRunLeft = "character/monster/imp/runLeft";
    private final String pathToRunRight = "character/monster/imp/runRight";

    /**
     * Entity with Components
     */
    public Monster() {
        super();
        new PositionComponent(this);
        setupAnimationComponent();
        setupVelocityComponent();
        setupHitboxComponent();
        new AIComponent(this);
        new HealthComponent(this);
        new CollideAI(1);

    }

    public void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    public void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    private void setupHitboxComponent() {
        new HitboxComponent(
            this,
            (you, other, direction) -> System.out.println("MonsterCollisionEnter"),
            (you, other, direction) -> System.out.println("MonsterCollisionLeave"));
    }
}
