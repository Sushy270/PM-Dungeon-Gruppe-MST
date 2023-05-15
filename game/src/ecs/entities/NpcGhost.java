package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.GhostWalk;
import ecs.components.ai.idle.ReturntostartWalk;
import ecs.components.ai.transition.RangeTransition;
import ecs.components.collision.GhostCollide;
import graphic.Animation;
import level.LevelAPI;

public class NpcGhost extends Entity{
    private final float xSpeed = 0.4f;
    private final float ySpeed = 0.2f;

    private final String pathToIdleLeft = "character/monster/ghost/idleLeft";
    private final String pathToIdleRight = "character/monster/ghost/idleRight";
    private final String pathToRunLeft = "character/monster/ghost/runLeft";
    private final String pathToRunRight = "character/monster/ghost/runRight";


    public NpcGhost(){
        super();
        new PositionComponent(this);
        setupAnimationComponent();
        setupVelocityComponent();
        setupHitboxComponent();
        setupAIComponent();
    }


    public void setupAIComponent(){
        new AIComponent(this, new CollideAI(0), new GhostWalk(), new RangeTransition(0));
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
        new HitboxComponent(this);
    }
}
