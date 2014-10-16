package biome.model.organismattributes;

import com.jme3.scene.Node;

public class Attribute 
{
	protected Integer value;
	protected Integer numValues;
	
	protected Integer mutationRate;

	public int getValue()
	{
		return this.value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
	
	public Integer getMutationRate()
	{
		return this.mutationRate;
	}
	
	public Attribute mutate()
	{
		return null;
	}
	
	/*
	public Node getNode()
	{
		return null;
	}*/
}
