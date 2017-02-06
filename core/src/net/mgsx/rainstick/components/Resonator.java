package net.mgsx.rainstick.components;

import com.badlogic.ashley.core.Component;

import net.mgsx.game.core.annotations.Editable;
import net.mgsx.game.core.annotations.EditableComponent;
import net.mgsx.game.core.annotations.Storable;

@Storable("rainstick.resonator")
@EditableComponent
public class Resonator implements Component
{
	@Editable
	public float freq = 1000;
}
