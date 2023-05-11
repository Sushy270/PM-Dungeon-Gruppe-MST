package ecs.components.skill;

import ecs.components.AnimationComponent;
import ecs.components.MissingComponentException;
import ecs.components.VelocityComponent;
import ecs.components.ai.AIComponent;
import ecs.components.ai.transition.RangeTransition;
import ecs.entities.Entity;
import starter.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvisibilitySkill implements ISkillFunction {
    private Entity entity;
    // ist needed to switch Skill on and off
    private boolean isAktive;
    private List<List<String>> transparentPaths;
    private List<List<String>> normalPaths;
    private AnimationComponent ac;
    private VelocityComponent vc;
    public InvisibilitySkill(Entity entity){

        this.entity = entity;
        isAktive = false;

        ac = getAnimationComponent();
        vc = getVelocityComponent();

        normalPaths = new ArrayList<>();
        normalPaths.add(ac.getIdleLeft().getAnimationFrames());
        normalPaths.add(ac.getIdleRight().getAnimationFrames());
        normalPaths.add(vc.getMoveLeftAnimation().getAnimationFrames());
        normalPaths.add(vc.getMoveRightAnimation().getAnimationFrames());

        transparentPaths = createTransparents(normalPaths);
    }

    public void execute(Entity entity) {
        isAktive = !isAktive;

        if(isAktive) {
            setAnimations(transparentPaths);
            changeRangeEntities(0);
        }
        else {
            setAnimations(normalPaths);
            changeRangeEntities(5);
        }
    }

    private List<List<String>> createTransparents(List<List<String>> paths)
    {
        // creates the direcktories
        String firstPath = paths.get(0).get(0);
        String parentDirectory = new File(new File(firstPath).getParent()).getParent();
        String transparentDirectory = parentDirectory + File.separator + "Transparents";
        File transparentDir = new File(transparentDirectory);
        if (!transparentDir.exists()) {
            if (transparentDir.mkdirs()) {
                System.out.println("Transparents directory created: " + transparentDirectory);
            }
        }
        else {
            System.out.println("Transparents directory already exists: " + transparentDirectory);
        }

        List<List<String>> transparentsMain = new ArrayList<>();

        for (List<String> pathList : paths) {
            List<String> transparents = new ArrayList<>();
            String pathFirst = new File(pathList.get(0)).getParent();
            int lastSeparator = pathFirst.lastIndexOf("\\");
            String lastDirectory = pathFirst.substring(lastSeparator);
            String complete = transparentDirectory + lastDirectory;

            File path = new File(complete);
            if (!path.exists())
                path.mkdirs();
            for(String png : pathList) {
                transparents.add(createTransparentsPNG(png, complete));
            }
            transparentsMain.add(transparents);
        }
        return transparentsMain;
    }


    private String createTransparentsPNG(String pathPNG, String destination) {
        try {
            // Lade das PNG-Bild
            BufferedImage image = ImageIO.read(new File(pathPNG));

            // Erstelle ein neues BufferedImage mit gleicher Größe und Transparenz
            BufferedImage transparentImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

            // Kopiere das Originalbild auf das neue Bild mit reduzierter Transparenz
            Graphics2D g2d = transparentImage.createGraphics();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            // Schreibe das neue Bild in das Zielverzeichnis
            int lastSeparator = destination.lastIndexOf("\\");
            String pngName = pathPNG.substring(lastSeparator + 1);
            String destinationPath = destination + File.separator + pngName;
            ImageIO.write(transparentImage, "PNG", new File(destinationPath));

            System.out.println("Transparentes PNG wurde erstellt: " + destinationPath);
            return destinationPath;
        } catch (IOException e) {
            System.err.println("Fehler beim Erstellen des transparenten PNG: " + e.getMessage());return null;
        }
    }

    private AnimationComponent getAnimationComponent() {
        return (AnimationComponent)
            entity.getComponent(AnimationComponent.class)
                .orElseThrow(
                    () -> new MissingComponentException("PositionComponent"));
    }

    private VelocityComponent getVelocityComponent() {
        return (VelocityComponent)
            entity.getComponent(VelocityComponent.class)
                .orElseThrow(
                    () -> new MissingComponentException("VelocityComponent"));
    }

    private void setAnimations(List<List<String>> paths) {
        ac.setIdleLeftPath(paths.get(0));
        ac.setIdleRightPath(paths.get(1));
        vc.setMoveLeftPath(paths.get(2));
        vc.setMoveRightPath(paths.get(3));
    }

    private void changeRangeEntities(int range) {
        Game.getEntities().stream()
            // Consider only entities that have a SkillComponent
            .flatMap(e -> e.getComponent(AIComponent.class).stream())
            .forEach(sc -> {
                ((RangeTransition) ((AIComponent) sc).getTransitionAI()).setRange(range);
            });
    }
}
