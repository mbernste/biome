package biome.view.organismattributes;

import biome.application.Common;
import biome.model.organismattributes.TerrainType;
import biome.view.View;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class TerrainTypeNodeGenerator 
{
	private final static String TENTACLES = "Models/animal/tentacles.obj";
	
	public static Node generateNode(TerrainType terrainType)
	{
		Node node = new Node();
		Spatial model = null;
		
		View theView = (View) Common.BEAN_CONTEXT.getBean("theView");
		
		if (terrainType.getValue() == TerrainType.WATER)
 		{
			model = theView.getAssetManager().loadModel(TENTACLES);
			model.setLocalScale(Common.SCALE, Common.SCALE, Common.SCALE);
			node.attachChild(model);
		}
			
		return node;
	}
}
