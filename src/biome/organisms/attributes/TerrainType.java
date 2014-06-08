package biome.organisms.attributes;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import biome.view.Common;

public class TerrainType extends Attribute
{
	private final static String TENTACLES = "Models/animal/tentacles.obj";
	
	public static int LAND = 0;
	public static int WATER = 1;
	
	public TerrainType(int value)
	{
		this.value = value;
		this.numValues = 2;
		this.mutationRate = 4;
	}
	
	public Attribute mutate()
	{
		TerrainType terrain = new TerrainType(this.value);
		
		if (Common.RAND.nextInt(mutationRate) == 0)
		{
			terrain.value = Common.RAND.nextInt(numValues);
		}
		
		return terrain;
	}
	
	@Override
	public Node getNode()
	{
		Node node = new Node();
		Spatial model = null;
		
		if (value == WATER)
 		{
			model = Common.ASSET_MANAGER.loadModel(TENTACLES);
			model.setLocalScale(Common.SCALE, Common.SCALE, Common.SCALE);
			node.attachChild(model);
		}
			
		return node;
	}

}
