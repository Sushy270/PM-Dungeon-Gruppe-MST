package ecs.systems;

import com.badlogic.gdx.Gdx;
import configuration.KeyboardConfig;
import ecs.components.MissingComponentException;
import ecs.components.PlayableComponent;
import ecs.components.VelocityComponent;
import ecs.entities.Entity;
import ecs.tools.interaction.InteractionTool;
import starter.Game;

import java.util.ArrayList;

/** Used to control the player */
public class PlayerSystem extends ECS_System {

    private record KSData(Entity e, PlayableComponent pc, VelocityComponent vc) {}

    @Override
    public void update() {
        Game.getEntities().stream()
                .flatMap(e -> e.getComponent(PlayableComponent.class).stream())
                .map(pc -> buildDataObject((PlayableComponent) pc))
                .forEach(this::checkKeystroke);
    }

    // isKeyPressed expects an Integer, so that is what we save
    private ArrayList<Integer> keyStrokeRanking = new ArrayList<>();

    private void updateKeyStrokeRanking(Integer keyStroke)
    {
        if(keyStrokeRanking.contains(keyStroke)
        && !Gdx.input.isKeyPressed(keyStroke))
            keyStrokeRanking.remove(keyStroke);

        else if(!keyStrokeRanking.contains(keyStroke)
            && Gdx.input.isKeyPressed(keyStroke))
            keyStrokeRanking.add(keyStroke);
    }

    private void setVelocity(KSData ksd)
    {
        Integer current;
        if(keyStrokeRanking.isEmpty())
            current = null;
        else
            current = keyStrokeRanking.get(keyStrokeRanking.size() - 1);
        if(current == KeyboardConfig.MOVEMENT_UP.get())
            ksd.vc.setCurrentYVelocity(1 * ksd.vc.getYVelocity());
        else if (current == KeyboardConfig.MOVEMENT_DOWN.get())
            ksd.vc.setCurrentYVelocity(-1 * ksd.vc.getYVelocity());
        else if (current == KeyboardConfig.MOVEMENT_RIGHT.get())
            ksd.vc.setCurrentXVelocity(1 * ksd.vc.getXVelocity());
        else if (current == KeyboardConfig.MOVEMENT_LEFT.get())
            ksd.vc.setCurrentXVelocity(-1 * ksd.vc.getXVelocity());
    }

    private void checkKeystroke(KSData ksd) {
        // KeyStrokeRanking updaten
        updateKeyStrokeRanking(KeyboardConfig.MOVEMENT_UP.get());
        updateKeyStrokeRanking(KeyboardConfig.MOVEMENT_DOWN.get());
        updateKeyStrokeRanking(KeyboardConfig.MOVEMENT_RIGHT.get());
        updateKeyStrokeRanking(KeyboardConfig.MOVEMENT_LEFT.get());

        // letzter Eintrag soll ausgeführt werden
        setVelocity(ksd);

        if (Gdx.input.isKeyPressed(KeyboardConfig.INTERACT_WORLD.get()))
            InteractionTool.interactWithClosestInteractable(ksd.e);

        // check skills
        else if (Gdx.input.isKeyPressed(KeyboardConfig.FIRST_SKILL.get()))
            ksd.pc.getSkillSlot1().ifPresent(skill -> skill.execute(ksd.e));
        else if (Gdx.input.isKeyPressed(KeyboardConfig.SECOND_SKILL.get()))
            ksd.pc.getSkillSlot2().ifPresent(skill -> skill.execute(ksd.e));

    }

    private KSData buildDataObject(PlayableComponent pc) {
        Entity e = pc.getEntity();

        VelocityComponent vc =
                (VelocityComponent)
                        e.getComponent(VelocityComponent.class)
                                .orElseThrow(PlayerSystem::missingVC);

        return new KSData(e, pc, vc);
    }

    private static MissingComponentException missingVC() {
        return new MissingComponentException("VelocityComponent");
    }
}
