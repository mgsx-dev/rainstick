package net.mgsx.rainstick.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

import net.mgsx.game.core.annotations.EditableComponent;
import net.mgsx.game.core.annotations.Storable;

@Storable("rainstick.ball")
@EditableComponent(autoClone=true)
public class Ball implements Component
{
	public final static ComponentMapper<Ball> components = ComponentMapper.getFor(Ball.class);
	public float radius = 1;
	
	// physics cache info (static)
	public transient float mass = -1;
	
	// physics cache info (dynamic)
	public transient Vector2 position = new Vector2();
	public transient Vector2 velocity = new Vector2();
	public transient float angle;
	
}
