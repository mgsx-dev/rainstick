package net.mgsx.rainstick.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

import net.mgsx.game.core.annotations.Storable;

@Storable("rainstick.ball")
public class Ball implements Component
{
	public final static ComponentMapper<Ball> components = ComponentMapper.getFor(Ball.class);
	public float radius = 1;
	
}
