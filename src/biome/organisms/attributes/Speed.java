package biome.organisms.attributes;

import biome.view.Common;

public class Speed extends Attribute 
{
	private final static int MAX_INCREASE = 4;
	
	public Speed(int value)
	{
		this.value = value;
		this.numValues = null;
		this.mutationRate = 4;
	}
	
	public Attribute mutate()
	{
		Speed speed = new Speed(this.value);
		
		if (Common.RAND.nextInt(mutationRate) == 0)
		{
			speed.value += Common.RAND.nextInt(MAX_INCREASE);
		}
		
		return speed;
	}

}
