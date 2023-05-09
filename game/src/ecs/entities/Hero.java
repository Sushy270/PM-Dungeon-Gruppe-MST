package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.RadiusWalk;
import ecs.components.skill.*;
import ecs.damage.DamageType;
import ecs.systems.CollisionSystem;
import graphic.Animation;

/**
 * The Hero is the player character. It's entity in the ECS. This class helps to setup the hero with
 * all its components and attributes.
 */
public class Hero extends Entity {

    private final int fireballCoolDown = 1;
    private final int invisibilityCoolDown = 20;
    private final float xSpeed = 0.3f;
    private final float ySpeed = 0.3f;

    private final String pathToIdleLeft = "knight/idleLeft";
    private final String pathToIdleRight = "knight/idleRight";
    private final String pathToRunLeft = "knight/runLeft";
    private final String pathToRunRight = "knight/runRight";
    private final String pathToHit = "knight/knight_m_hit_anim_f0";
    private Skill firstSkill;
    private Skill secondSkill;
    private Skill thirdSkill;

    /** Entity with Components */
    public Hero() {
        super();
        new PositionComponent(this);
        setupVelocityComponent();
        setupAnimationComponent();
        PlayableComponent pc = new PlayableComponent(this);

        setupFireballSkill();
        pc.setSkillSlot1(firstSkill);
        setUpInvisibilitySkill();
        pc.setSkillSlot2(secondSkill);

        setupSkillComponent();
        new HealthComponent(this);
        new CollisionSystem();
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    private void setupFireballSkill() {
        firstSkill =
                new Skill(
                        new FireballSkill(SkillTools::getCursorPositionAsPoint), fireballCoolDown);
    }

    private void setUpInvisibilitySkill() {
        secondSkill =
            new Skill(
                new InvisibilitySkill(this), invisibilityCoolDown);
    }

    private void setupSkillComponent() {
        SkillComponent sc = new SkillComponent(this);
            sc.addSkill(firstSkill);
            sc.addSkill(secondSkill);
//            sc.addSkill(thirdSkill);


    }
}
