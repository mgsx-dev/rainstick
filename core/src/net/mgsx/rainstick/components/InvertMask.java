package net.mgsx.rainstick.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

import net.mgsx.game.core.annotations.EditableComponent;
import net.mgsx.game.core.annotations.Storable;
@EditableComponent
@Storable("rainstick.mask-invert")
public class InvertMask implements Component
{
	
	public final static ComponentMapper<InvertMask> components = ComponentMapper.getFor(InvertMask.class);
}
