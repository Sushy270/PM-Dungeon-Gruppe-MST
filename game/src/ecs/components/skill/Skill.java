package ecs.components.skill;

import ecs.entities.Entity;
import tools.Constants;

import java.util.Optional;

public class Skill {

    private final ISkillFunction skillFunction;
    private final int coolDownInFrames;
    private int currentCoolDownInFrames;
    private final int durationInFrames;
    private int currentDurationInFrames;
    private int simpleFramecounter;

    /**
     * @param skillFunction Function of this skill
     * @param coolDownInSeconds
     */
    public Skill(ISkillFunction skillFunction, int coolDownInSeconds) {
        this.skillFunction = skillFunction;
        this.coolDownInFrames = coolDownInSeconds * Constants.FRAME_RATE;
        this.currentCoolDownInFrames = 0;
        this.durationInFrames = 1;
        this.currentDurationInFrames = 0;
    }

    /**
     * @param skillFunction Function of this skill
     * @param coolDownInSeconds cooldown of the Skill in seconds
     * @param durationInFrames duration of the Skill in seconds
     */
    public Skill(ISkillFunction skillFunction, int coolDownInSeconds, int durationInFrames) {
        this.skillFunction = skillFunction;
        this.coolDownInFrames = coolDownInSeconds * Constants.FRAME_RATE;
        this.currentCoolDownInFrames = 0;
        this.durationInFrames = durationInFrames * Constants.FRAME_RATE;
        this.currentDurationInFrames = 0;
    }

    /**
     * Execute the method of this skill
     *
     * @param entity entity which uses the skill
     */
    public void execute(Entity entity) {
        if(mc == null) {
            if (!isOnCoolDown()) {
                if (!isAktive())
                    activateDuration();
                    // Fähigkeit soll nicht direkt nach weniger als einer sekunde deaktiviert werden sollen
                else if (durationInFrames - currentDurationInFrames > Constants.FRAME_RATE) {
                    currentDurationInFrames = 0;
                    activateCoolDown();
                }
                skillFunction.execute(entity);
            }
        }
        else {
            simpleFramecounter = 0;
            if(simpleFramecounter%skillFunction == 0)
        }
    }

    /**
     * @return true if cool down is not 0, else false
     */
    public boolean isOnCoolDown() {
        return currentCoolDownInFrames > 0;
    }

    /** activate cool down */
    public void activateCoolDown() {
        currentCoolDownInFrames = coolDownInFrames;
    }

    /** reduces the current cool down by frame */
    public void reduceCoolDown() {
        // currentCollDownInFrames stops at 0. Doesn't reach -1.
        currentCoolDownInFrames = Math.max(0, --currentCoolDownInFrames);
    }
    public boolean isAktive() {return currentDurationInFrames > 0;}
    public void activateDuration() {
        currentDurationInFrames = durationInFrames;
    }

    /** reduces the current cool down by frame */
    public void reduceDuration() {
        currentDurationInFrames = Math.max(0, --currentDurationInFrames);
        if(currentDurationInFrames == 1){activateCoolDown();}
    }

    public void increaseFrameCounter() {simpleFramecounter++;}
}
