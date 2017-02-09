package net.mgsx.rainstick.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Array;

import net.mgsx.game.core.EditorScreen;
import net.mgsx.game.core.tools.Tool;
import net.mgsx.rainstick.components.Ball;

public class GrainResetTool extends Tool
{

	public GrainResetTool(EditorScreen editor) {
		super("Rainstick - Reset", editor);
	}
	
	@Override
	protected void activate() {
		super.activate();
		
		Array<Entity> entities = new Array<Entity>();
		for(Entity e : getEngine().getEntitiesFor(Family.all(Ball.class).get())){
			entities.add(e);
		}
		for(Entity e : entities)
		{
			getEngine().removeEntity(e);
		}
		
		end();
	}

}
