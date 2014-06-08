package biome.actions;

import java.util.HashSet;
import java.util.Set;

import biome.organisms.Organism;

public class ActionResult 
{
	private Set<Organism> dead;
	private Set<Organism> born;
	
	public ActionResult()
	{
		dead = new HashSet<Organism>();
		born = new HashSet<Organism>();
	}
	
	public void setDead(Set<Organism> dead)
	{
		this.dead = dead;
	}
	
	public Set<Organism> getDead()
	{
		return this.dead;
	}
	
	public void setBorn(Set<Organism> born)
	{
		this.born = born;
	}
	
	public Set<Organism> getBorn()
	{
		return this.born;
	}

}
