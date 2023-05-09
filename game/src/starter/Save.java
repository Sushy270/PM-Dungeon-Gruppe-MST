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
//        this.elementToInt(currentLevel.getLayoutLE());
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

//        LevelElement[][] temp = this.intToElement(data.tileLevelLayout);
        ILevel tempLevel = new TileLevel(data.tileLevelLayout, DesignLabel.randomDesign(), data.indexStartTile);
        levelAPI.setCurrentLevel(tempLevel);
	}

//    private void elementToInt(LevelElement[][] matrix)
//    {
//        data.tileLevelLayout = new int[matrix.length][matrix[0].length];
//        for(int i = 0; i < matrix.length; i++)
//        {
//            for(int j = 0; j < matrix[0].length; j++)
//            {
//                switch(matrix[i][j])
//                {
//                    case SKIP -> {
//                        data.tileLevelLayout[i][j] = 0;
//                        break;
//                    }
//                    case FLOOR -> {
//                        data.tileLevelLayout[i][j] = 1;
//                        break;
//                    }
//                    case WALL -> {
//                        data.tileLevelLayout[i][j] = 2;
//                        break;
//                    }
//                    case HOLE -> {
//                        data.tileLevelLayout[i][j] = 3;
//                        break;
//                    }
//                    case EXIT -> {
//                        data.tileLevelLayout[i][j] = 4;
//                        break;
//                    }
//                    case DOOR -> {
//                        data.tileLevelLayout[i][j] = 5;
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    private LevelElement[][] intToElement(int[][] layout)
//    {
//        LevelElement[][] temp = new LevelElement[layout.length][layout[0].length];
//        for(int i = 0; i < layout.length; i++)
//        {
//            for(int j = 0; j < layout[0].length; j++)
//            {
//                switch(layout[i][j])
//                {
//                    case 0 -> {
//                        temp[i][j] = LevelElement.SKIP;
//                        break;
//                    }
//                    case 1 -> {
//                        temp[i][j] = LevelElement.FLOOR;
//                        break;
//                    }
//                    case 2 -> {
//                        temp[i][j] = LevelElement.WALL;
//                        break;
//                    }
//                    case 3 -> {
//                        temp[i][j] = LevelElement.HOLE;
//                        break;
//                    }
//                    case 4 -> {
//                        temp[i][j] = LevelElement.EXIT;
//                        break;
//                    }
//                    case 5 -> {
//                        temp[i][j] = LevelElement.DOOR;
//                        break;
//                    }
//                }
//            }
//        }
//        return temp;
//    }
}