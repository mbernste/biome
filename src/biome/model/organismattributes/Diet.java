package biome.model.organismattributes;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import biome.application.Common;
import biome.view.View;

/**
 *
 * @author matthewbernstein
 */
public class Diet extends Attribute
{
	private final static String ANTLERS_MODEL = "Models/animal/antlers.obj";
	
	public static int DIET_CARNIVORE = 0;
	public static int DIET_HERBIVORE = 1;
	
	public Diet(int value)
	{
		this.value = value;
		this.numValues = 2;
		this.mutationRate = 8;
	}
	
	public Attribute mutate()
	{
		Diet diet = new Diet(this.value);
		
		if (Common.RAND.nextInt(mutationRate) == 0)
		{
			diet.value = Common.RAND.nextInt(numValues);
		}
		
		return diet;
	}
	
	@Override
	public Node getNode()
	{	
		Node node = new Node();
		Spatial model = null;
		
		// TODO REFACTOR
		View theView = (View) Common.BEAN_CONTEXT.getBean("theView");
		
		if (value == DIET_HERBIVORE)
		{
			model = theView.getAssetManager().loadModel(ANTLERS_MODEL);
			model.setLocalScale(Common.SCALE, Common.SCALE, Common.SCALE);
			node.attachChild(model);
		}	
		
		return node;
	}
}
