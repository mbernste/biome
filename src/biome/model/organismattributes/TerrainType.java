package biome.model.organismattributes;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import biome.application.Common;
import biome.utils.MathUtils;
import biome.view.View;

public class TerrainType extends Attribute
{	
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
		
		if (MathUtils.RAND.nextInt(mutationRate) == 0)
		{
			terrain.value = MathUtils.RAND.nextInt(numValues);
		}
		
		return terrain;
	}
}
