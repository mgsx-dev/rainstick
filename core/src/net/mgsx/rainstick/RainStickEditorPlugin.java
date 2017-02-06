package net.mgsx.rainstick;

import net.mgsx.game.core.EditorScreen;
import net.mgsx.game.core.annotations.PluginDef;
import net.mgsx.game.core.plugins.EditorPlugin;
import net.mgsx.game.plugins.DefaultEditorPlugin;
import net.mgsx.rainstick.tools.GrainGeneratorTool;

@PluginDef(dependencies=RainStickPlugin.class)
public class RainStickEditorPlugin extends EditorPlugin implements DefaultEditorPlugin{

	@Override
	public void initialize(EditorScreen editor) 
	{
		editor.addTool(new GrainGeneratorTool(editor));
	}

}
