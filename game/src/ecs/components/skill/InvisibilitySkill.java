package ecs.components.skill;

import ecs.components.AnimationComponent;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.entities.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
        this.createTransparents(list);
        System.out.println("Toggle Invisibility");
    }

    private void createTransparents(List<List<String>> paths)
    {
        // creates the direcktories
        String firstPath = paths.get(0).get(0);
        String parentDirectory = new File(new File(firstPath).getParent()).getParent();
        String transparentDirectory = parentDirectory + File.separator + "Transparents";
        File transparentDir = new File(transparentDirectory);
        if (!transparentDir.exists()) {
            if (transparentDir.mkdirs()) {
                System.out.println("Transparents directory created: " + transparentDirectory);

                for (List<String> pathList : paths) {
                    String pathFirst = new File(pathList.get(0)).getParent();
                    int lastSeparator = pathFirst.lastIndexOf("\\");
                    String lastDirectory = pathFirst.substring(lastSeparator);
                    String complete = transparentDirectory + lastDirectory;

                    File path = new File(complete);
                    if (!path.exists())
                        path.mkdirs();
                    for(String png : pathList) {
                        createTransparentsPNG(png, complete);
                    }
                }
            }
            else {
                System.err.println("Failed to create Transparents directory: " + transparentDirectory);
            }
        }
        else {
            System.out.println("Transparents directory already exists: " + transparentDirectory);
        }
    }

    private void createTransparentsPNG(String pathPNG, String destination) {
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
        } catch (IOException e) {
            System.err.println("Fehler beim Erstellen des transparenten PNG: " + e.getMessage());
        }
    }
}
