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

    public void increaseManapoints(int value) {
        currentPoints = Math.min(maxPoints, currentPoints + value);
    }

    public void reduceManaPoints(int value) {
        currentPoints = Math.max(0, currentPoints - value);
    }

    public void generatePoints() {
        currentFramesTilManapoint--;
        if(currentFramesTilManapoint <= 0 && generateManaPoints){
            increaseManapoints(1);
            currentFramesTilManapoint = framesTilManapoint;
            System.out.println("ManaPoints: " + currentPoints);
        }
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void toggleGenerateManaPoints(){generateManaPoints = !generateManaPoints;}
}
