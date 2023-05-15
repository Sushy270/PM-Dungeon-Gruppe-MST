package starter;

import ecs.entities.Entity;
import level.LevelAPI;
import level.elements.ILevel;
import level.elements.TileLevel;
import level.elements.tile.Tile;
import level.tools.DesignLabel;
import level.tools.LevelElement;

import java.io.*;
import java.util.logging.Level;

public class Save implements Serializable
{
    private class Data implements Serializable {
        public LevelElement[][] tileLevelLayout;
        public int indexStartTile;
    }

    private Data data = new Data();

	public void speichern(ILevel currentLevel)
	{
        data.tileLevelLayout = currentLevel.getLayoutLE();
        Tile tempStartTile = currentLevel.getStartTile();
        data.indexStartTile = currentLevel.getFloorTiles().indexOf(tempStartTile);

        // data serialisieren
        try (FileOutputStream fos = new FileOutputStream("SaveState");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data); oos.close();
            System.out.println("File writen!");
        } catch (IOException ex)
        {
            System.out.println("File not writen:");
            ex.printStackTrace();
        }
	}

	public void laden(LevelAPI levelAPI) throws FileNotFoundException
    {
        // SaveState auslesen
        try (FileInputStream fis = new FileInputStream("SaveState");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            data = (Data) ois.readObject(); ois.close();
            System.out.println("File read!");
        }catch (IOException | ClassNotFoundException ex)
        {
            System.out.println("File not read:");
            throw new FileNotFoundException();}

        ILevel tempLevel = new TileLevel(data.tileLevelLayout, DesignLabel.randomDesign(), data.indexStartTile);
        levelAPI.setCurrentLevel(tempLevel);
	}
}
