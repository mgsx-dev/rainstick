package net.mgsx.rainstick.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Pool.Poolable;

import net.mgsx.game.core.annotations.EditableComponent;
import net.mgsx.game.core.annotations.Storable;

@Storable("rainstick.mask")
@EditableComponent(autoClone=true)
public class Mask implements Component, Poolable{
	
	public final static ComponentMapper<Mask> components = ComponentMapper.getFor(Mask.class);
	
	public transient ModelInstance modelInstance;

	@Override
	public void reset() {
		// TODO free model ?
	}
}
