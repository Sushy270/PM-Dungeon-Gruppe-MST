package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.idle.ReturntostartWalk;
import graphic.Animation;
import level.LevelAPI;

public class Mumie extends Monster{
    private final float xSpeed = 0.2f;
    private final float ySpeed = 0.2f;
    private final int lebenspunkte = 3;
    private final int level = LevelAPI.getlevelnummer();

    private final String pathToIdleLeft = "character/monster/chort/idleLeft";
    private final String pathToIdleRight = "character/monster/chort/idleRight";
    private final String pathToRunLeft = "character/monster/chort/runLeft";
    private final String pathToRunRight = "character/monster/chort/runRight";

    /**
     * Entity with Components
     */
    public Mumie() {
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
            (you, other, direction) -> System.out.println("MumieCollisionEnter"),
            (you, other, direction) -> System.out.println("MumieCollisionLeave"));
    }
}
