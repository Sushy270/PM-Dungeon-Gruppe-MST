package ecs.components.skill;

import ecs.components.AnimationComponent;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.entities.Entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvisibilitySkill implements ISkillFunction {
    private Entity entity;
    // ist needed to switch Skill on and off
    private boolean isAktive;
    public InvisibilitySkill(Entity entity){
        this.entity = entity;
    }

    public void execute(Entity entity) {
        AnimationComponent ac =
            (AnimationComponent)
                entity.getComponent(AnimationComponent.class)
                    .orElseThrow(
                        () -> new MissingComponentException("PositionComponent"));
        VelocityComponent vc =
            (VelocityComponent)
                entity.getComponent(VelocityComponent.class)
                    .orElseThrow(
                        () -> new MissingComponentException("VelocityComponent"));

        List<List<String>> list = new ArrayList<>();
        list.add(ac.getIdleLeft().getAnimationFrames());
        list.add(ac.getIdleRight().getAnimationFrames());
        list.add(vc.getMoveLeftAnimation().getAnimationFrames());
        list.add(vc.getMoveRightAnimation().getAnimationFrames());
//        this.createTransparents(list);
        System.out.println("Toggle Invisibility");
    }

    private void createTransparents(List<List<String>> paths)
    {
        BufferedImage image;
        try {
            image = ImageIO.read(new FileInputStream("Beispiel.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
