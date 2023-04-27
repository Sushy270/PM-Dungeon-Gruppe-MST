package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.ai.AIComponent;
import ecs.components.ai.idle.ReturntostartWalk;
import graphic.Animation;
import level.LevelAPI;

/**
 * The Hero is the player character. It's entity in the ECS. This class helps to setup the hero with
 * all its components and attributes .
 */

public abstract class Monster extends Entity {
    private float xSpeed;
    private float ySpeed;
    private int lebenspunkte;
    private int level = LevelAPI.getlevelnummer();

    private String pathToIdleLeft;
    private String pathToIdleRight;
    private String pathToRunLeft;
    private String pathToRunRight;

    /**
     * Entity with Components
     */
    public Monster() {
        super();
    }

    public abstract void setupHealthComponent();
    public abstract void setupAIComponent();

    public abstract void setupAnimationComponent();

    public abstract void setupVelocityComponent();

    public abstract void setupHitboxComponent();
}
