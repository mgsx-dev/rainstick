package net.mgsx.rainstick.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.annotations.EditableComponent;
import net.mgsx.game.core.annotations.Storable;

@Storable("rainstick.resonator")
@EditableComponent(autoClone=true)
public class Resonator implements Component
{
	public final static ComponentMapper<Resonator> components = ComponentMapper.getFor(Resonator.class);
	
	@Editable
	public int material;
}
