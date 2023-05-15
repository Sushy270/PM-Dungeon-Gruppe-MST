package ecs.entities;
import level.LevelAPI;

/**
 * The Monster is the character. It's entity in the ECS. This class helps to setup the monsters with
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
