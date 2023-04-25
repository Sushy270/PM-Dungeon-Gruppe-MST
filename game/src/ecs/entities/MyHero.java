package ecs.entities;

import ecs.components.PositionComponent;
import ecs.components.AnimationComponent;
import graphic.Animation;
import dslToGame.AnimationBuilder;

public class MyHero extends Entity
{
	public MyHero()
	{
		super();

		new PositionComponent(this, 0, 0);

		Animation idleRight = AnimationBuilder.buildAnimation("character/knight/idleRight");
		Animation idleLeft = AnimationBuilder.buildAnimation("character/knight/idleLeft");
		new AnimationComponent(this, idleLeft, idleRight);
	}
}
