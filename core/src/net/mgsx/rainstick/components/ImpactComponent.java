package net.mgsx.rainstick.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class ImpactComponent implements Component
{
	
	public final static ComponentMapper<ImpactComponent> components = ComponentMapper.getFor(ImpactComponent.class);
	
	public Vector2 position = new Vector2();
	public float life;
	public float energy;
	public int material;
}
