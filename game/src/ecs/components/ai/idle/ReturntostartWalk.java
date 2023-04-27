package ecs.components.ai.idle;

import com.badlogic.gdx.ai.pfa.GraphPath;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.components.ai.AITools;
import ecs.entities.Entity;
import level.elements.tile.Tile;
import tools.Constants;
import tools.Point;

public class ReturntostartWalk implements IIdleAI {
    private int z = 0;
    private final float radius;
    private GraphPath<Tile> path;
    private final int breakTime;
    private int currentBreak = 0;
    private Point p;

    /**
     * Finds a point in the radius and then moves there. When the point has been reached, a new
     * point in the radius is searched for from there.
     *
     * @param radius Radius in which a target point is to be searched for
     * @param breakTimeInSeconds how long to wait (in seconds) before searching a new goal
     */
    public ReturntostartWalk(float radius, int breakTimeInSeconds, Entity entity) {
        this.radius = radius;
        this.breakTime = breakTimeInSeconds * Constants.FRAME_RATE;
    }

    private Point p(Entity entity){
        return ((PositionComponent)
            entity.getComponent(PositionComponent.class)
                .orElseThrow(
                    () ->
                        new MissingComponentException(
                            "PositionComponent")))
            .getPosition();
    }

    @Override
    public void idle(Entity entity) {
        if (path == null || AITools.pathFinishedOrLeft(entity, path)) {
            if (currentBreak >= breakTime) {
                currentBreak = 0;
                if(z == 0) {
                    p = p(entity);
                    path = AITools.calculatePathToRandomTileInRange(entity, radius);
                    z = 1;
                }
                else{
                    path = AITools.calculatePath(p(entity), p);
                    z = 0;
                }
                idle(entity);
            }
            currentBreak++;

        } else {
            AITools.move(entity, path);
        }
    }
}

