package ecs.components.skill;

import ecs.entities.Entity;
import tools.Constants;

public class Skill {

    private final ISkillFunction skillFunction;
    private final int coolDownInFrames;
    private int currentCoolDownInFrames;
    private final int durationInFrames;
    private int currentDurationInFrames;
    private final int framesPerManaPoint;
    private int manaFramecounter;
    private final ManaComponent mc;

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
        this.framesPerManaPoint = -1;
        this.mc = null;
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
        this.framesPerManaPoint = -1;
        this.mc = null;
    }
    /**
     * @param skillFunction Function of this skill
     * @param mc ManaComponent of the Entity
     */
    public Skill(ISkillFunction skillFunction, int framesPerManaComponent, ManaComponent mc) {
        this.skillFunction = skillFunction;
        this.coolDownInFrames = 0;
        this.currentCoolDownInFrames = 0;
        this.durationInFrames = 1;
        this.currentDurationInFrames = 0;
        this.framesPerManaPoint = framesPerManaComponent;
        this.mc = mc;
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
            if(mc.getCurrentPoints() > 0) {
                if(!isAktive()) {
                    aktivateManaFrameCounter();
                }
                // Fähigkeit soll nicht direkt nach weniger als einer sekunde deaktiviert werden sollen
                else if (manaFramecounter > Constants.FRAME_RATE) {
                    manaFramecounter = 0;
                }
                skillFunction.execute(entity);
            }
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
    /**
     * @return true if duration or manaFrameCounter is not 0, else false
     */
    public boolean isAktive() {return currentDurationInFrames > 0 || manaFramecounter > 0;}

    /** activate duration */
    public void activateDuration() {
        currentDurationInFrames = durationInFrames;
    }

    /** reduces the current duration down by frame */
    public void reduceDuration() {
        currentDurationInFrames = Math.max(0, --currentDurationInFrames);
        if(currentDurationInFrames == 1){activateCoolDown();}
    }

    /** activate Frame Counter by setting to 1 */
    public void aktivateManaFrameCounter() {manaFramecounter = 1;}

    /** increases the manaFrameCounter by one frame */
    private void increaseManaFrameCounter() {
        // when manaFrameCounter ist 0 it doesn't raise
        // setting manaFrameCounter to 1 "aktivates" this function
        if(manaFramecounter > 0)
            manaFramecounter++;
    }

    /** reduces ManaPoints of Component specific time given by framesPerManaPoint */
    public void depleteManaPoints() {
        if(isAktive() && mc != null && manaFramecounter % framesPerManaPoint == 0)
            mc.reduceManaPoints(1);
        increaseManaFrameCounter();
    }
}
