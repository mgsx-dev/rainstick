package net.mgsx.rainstick.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;

import net.mgsx.game.core.annotations.EditableComponent;
import net.mgsx.game.core.annotations.Storable;

@Storable("rainstick.mask")
@EditableComponent
public class Mask implements Component{
	
	public final static ComponentMapper<Mask> components = ComponentMapper.getFor(Mask.class);
	
	public transient RenderableProvider mesh;
	// public transient Mesh m;
}
