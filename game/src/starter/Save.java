package starter;

import ecs.entities.Entity;
import level.LevelAPI;
import java.util.Set;

public class Save
{
	private Save(){}

	public static void speichern()
	{
//        Game.getHero().ifPresent();
		Set<Entity> entities = Game.getEntities();
//		LevelAPI api = Game.getAPI();
		// implementation
	}

	public static void laden()
	{
		// implementation
	}
}
