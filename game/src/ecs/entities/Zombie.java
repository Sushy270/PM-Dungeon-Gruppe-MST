package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.idle.ReturntostartWalk;
import graphic.Animation;
import level.LevelAPI;

public class Zombie extends Monster{
    private final float xSpeed = 0.06f;
    private final float ySpeed = 0.1f;
    private final int lebenspunkte = 10;
    private final int level = LevelAPI.getlevelnummer();

    private final String pathToIdleLeft = "character/monster/Zombie/idleLeft";
    private final String pathToIdleRight = "character/monster/Zombie/idleRight";
    private final String pathToRunLeft = "character/monster/Zombie/runLeft";
    private final String pathToRunRight = "character/monster/Zombie/runRight";

    /**
     * Entity with Components
     */
    public Zombie() {
        super();
        new PositionComponent(this);
        setupAnimationComponent();
        setupVelocityComponent();
        setupHitboxComponent();
        setupAIComponent();
        setupHealthComponent();
    }

    public void setupHealthComponent(){
        new HealthComponent(this).setMaximalHealthpoints(lebenspunkte + level);
    }
    public void setupAIComponent(){
        new AIComponent(this).setIdleAI(new ReturntostartWalk(5,1,this));
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

    public void setupHitboxComponent() {
        new HitboxComponent(
            this,
            (you, other, direction) -> System.out.println("ZombieCollisionEnter"),
            (you, other, direction) -> System.out.println("ZombieCollisionLeave"));
    }
}
