package ecs.components.skill;

import ecs.components.Component;
import ecs.entities.Entity;

public class ManaComponent extends Component{
    private final int maxPoints;
    private int currentPoints;
    private final int framesTilManapoint;
    private int currentFramesTilManapoint;
    private boolean generateManaPoints;

    public ManaComponent(Entity entity, int maxPoints, int currentPoints, int framesTilManapoint) {
        super(entity);
        this.maxPoints = maxPoints;
        this.currentPoints = currentPoints;
        this.framesTilManapoint = framesTilManapoint;
        this.currentFramesTilManapoint = framesTilManapoint;
        this.generateManaPoints = true;
    }

    private void increaseManapoints(int value) {
        currentPoints = Math.min(maxPoints, currentPoints + value);
    }

    /**
     *
     * @param value The value current ManaPoints reduced by.
     */
    public void reduceManaPoints(int value) {
        currentPoints = Math.max(0, currentPoints - value);
    }

    /**
     * Increases current ManaPoints in a time Frame given by Attribute
     */
    public void generatePoints() {
        currentFramesTilManapoint--;
        if(currentFramesTilManapoint <= 0 && generateManaPoints){
            increaseManapoints(1);
            currentFramesTilManapoint = framesTilManapoint;
            System.out.println("ManaPoints: " + currentPoints);
        }
    }

    /**
     *
     * @return return current ManaPoints
     */
    public int getCurrentPoints() {
        return currentPoints;
    }

    /**
     * toggles boolean generateManaPoints
     */
    public void toggleGenerateManaPoints(){generateManaPoints = !generateManaPoints;}
}
