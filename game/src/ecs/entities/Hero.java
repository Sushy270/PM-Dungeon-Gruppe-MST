package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.*;
import ecs.components.xp.XPComponent;
import ecs.systems.CollisionSystem;
import graphic.Animation;
import tools.Constants;

/**
 * The Hero is the player character. It's entity in the ECS. This class helps to setup the hero with
 * all its components and attributes.
 */
public class Hero extends Entity {

    private final int fireballCoolDown = 1;
    private final int invisibilityCoolDown = 20;
    private final int invisibilityDuration = 10;
    private final double speedSkillIncrease = 1.5;
    private final int speedSkillManaPointsDepletion = 1;
    private final float xSpeed = 0.3f;
    private final float ySpeed = 0.3f;

    private final String pathToIdleLeft = "knight/idleLeft";
    private final String pathToIdleRight = "knight/idleRight";
    private final String pathToRunLeft = "knight/runLeft";
    private final String pathToRunRight = "knight/runRight";
    private final String pathToHit = "knight/knight_m_hit_anim_f0";
    //q fireball
    private Skill firstSkill;
    // r invisibility
    private Skill secondSkill;
    // f speed
    private Skill thirdSkill;

    /** Entity with Components */
    public Hero() {
        super();
        new PositionComponent(this);
        setupVelocityComponent();
        setupAnimationComponent();
        new HealthComponent(this);
        new CollisionSystem();
        new ManaComponent(this, 15, 0, Constants.FRAME_RATE);
        new XPComponent(this);

        setupFireballSkill();
        setupInvisibilitySkill();
        setupSpeedSkill();

        setupSkillComponent();
        setupPlayableComponent();

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

    private void setupInvisibilitySkill() {
        secondSkill =
            new Skill(
                new InvisibilitySkill(this), invisibilityCoolDown, invisibilityDuration);
    }

    private void setupSpeedSkill() {
        ManaComponent mc = (ManaComponent) getComponent(ManaComponent.class)
            .orElseThrow(
                () -> new MissingComponentException("ManaComponent"));

        thirdSkill =
            new Skill(
                new SpeedSkill(this, speedSkillIncrease), speedSkillManaPointsDepletion * Constants.FRAME_RATE, mc);
    }

    private void setupSkillComponent() {
        SkillComponent sc = new SkillComponent(this);
            sc.addSkill(firstSkill);
            sc.addSkill(secondSkill);
            sc.addSkill(thirdSkill);
    }

    private void setupPlayableComponent() {
        PlayableComponent pc = new PlayableComponent(this);
        pc.setSkillSlot1(firstSkill);
        pc.setSkillSlot2(secondSkill);
        pc.setSkillSlot3(thirdSkill);
    }

    public void levelUp() {
        System.out.println("levelUP!!!");
        int i = (int) (Math.random() * 3);
        switch (i) {
            case 0:
                firstSkill.increaseRandomValue();
                break;
            case 1:
                secondSkill.increaseRandomValue();
                break;
            case 2:
                thirdSkill.increaseRandomValue();
                break;
        }
    }
}
