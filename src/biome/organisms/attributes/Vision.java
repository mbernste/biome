package biome.organisms.attributes;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import biome.view.Common;

public class Vision extends Attribute 
{
	private final static String ONE_EYES = "Models/animal/1eye.obj";
	private final static String TWO_EYES = "Models/animal/2eyes.obj";
	private final static String THREE_EYES = "Models/animal/3eyes.obj";
	
	private final static int MAX_INCREASE = 4;
	
	public Vision(int value)
	{
		this.value = value;
		this.numValues = null;
		this.mutationRate = 4;
	}
	
	public Attribute mutate()
	{
		Vision vision = new Vision(this.value);
		
		if (Common.RAND.nextInt(mutationRate) == 0)
		{
			vision.value += Common.generateRandInt(MAX_INCREASE);
		}
		
		return vision;
	}
	
	@Override
	public Node getNode()
	{
		Node node = new Node();
		Spatial model = null;
		
		if (value < 10)
		{
			model = Common.ASSET_MANAGER.loadModel(ONE_EYES);
		}
		else if (value < 15)
		{
			model = Common.ASSET_MANAGER.loadModel(TWO_EYES);
		}
		else
		{
			model = Common.ASSET_MANAGER.loadModel(THREE_EYES);
		}
		
		model.setLocalScale(Common.SCALE, Common.SCALE, Common.SCALE);
		node.attachChild(model);
		
		return node;
	}
}
