package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.idle.ReturntostartWalk;
import graphic.Animation;
import level.LevelAPI;

public class Wolf extends Monster{
    private final float xSpeed = 0.5f;
    private final float ySpeed = 0.5f;
    private final int lebenspunkte = 5;
    private final int level = LevelAPI.getlevelnummer();

    private final String pathToIdleLeft = "character/monster/imp/idleLeft";
    private final String pathToIdleRight = "character/monster/imp/idleRight";
    private final String pathToRunLeft = "character/monster/imp/runLeft";
    private final String pathToRunRight = "character/monster/imp/runRight";

    /**
     * Entity with Components
     */
    public Wolf() {
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
            (you, other, direction) -> System.out.println("WolfCollisionEnter"),
            (you, other, direction) -> System.out.println("WolfCollisionLeave"));
    }
}
