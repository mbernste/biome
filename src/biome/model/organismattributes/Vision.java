package biome.model.organismattributes;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import biome.application.Common;
import biome.utils.MathUtils;
import biome.view.View;

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
		
		if (MathUtils.RAND.nextInt(mutationRate) == 0)
		{
			vision.value += MathUtils.generateRandInt(MAX_INCREASE);
		}
		
		return vision;
	}
	
	public Node getNode()
	{
		Node node = new Node();
		Spatial model = null;
		
		// TODO REFACTOR
		View theView = (View) Common.BEAN_CONTEXT.getBean("theView");
		
		if (value < 10)
		{
			model = theView.getAssetManager().loadModel(ONE_EYES);
		}
		else if (value < 15)
		{
			model = theView.getAssetManager().loadModel(TWO_EYES);
		}
		else
		{
			model = theView.getAssetManager().loadModel(THREE_EYES);
		}
		
		model.setLocalScale(Common.SCALE, Common.SCALE, Common.SCALE);
		node.attachChild(model);
		
		return node;
	}
}
