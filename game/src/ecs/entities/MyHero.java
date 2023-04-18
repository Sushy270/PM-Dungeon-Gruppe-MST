package game.src.ecs.entities;

import ecs.components.PositionComponent;
import ecs.components.AnimationComponent;
import graphic.Animation;

public class MyHero extends Entity
{
	public MyHero()
	{
		super();

		new PositionComponent(this, 0, 0);

		Animation idleRight = AnimationBuilder.buildAnimatoin("character/knight/idleRight");
		Animation idleLeft = AnimationBuilder.buildAnimatoin("character/knight/idleLeft");
		new AnimationComponent(this, idleLeft, idleRight);
	}
}