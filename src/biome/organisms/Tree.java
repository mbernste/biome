package biome.organisms;

import biome.view.ViewData;

public class Tree extends Organism 
{
	public Tree()
	{
		super();
		
    	this.view = new ViewData(this);
	}

}
