package ecs.systems;

import ecs.components.skill.ManaComponent;
import starter.Game;

public class ManaSystem extends ECS_System {

    /** reduces the cool down for all skills */
    @Override
    public void update() {
        Game.getEntities().stream()
            // Consider only entities that have a ManaComponent
            .flatMap(e -> e.getComponent(ManaComponent.class).stream())
            .forEach(mc -> ((ManaComponent) mc).generatePoints());
    }
}
