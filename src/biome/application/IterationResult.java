package biome.application;

import java.util.HashSet;
import java.util.Set;

import biome.organisms.Organism;

public class IterationResult 
{
	private Set<Organism> dead;
	private Set<Organism> born;
	private Set<Organism> lastDead;
	
	public IterationResult()
	{
		dead = new HashSet<Organism>();
		born = new HashSet<Organism>();
		lastDead = new HashSet<Organism>();
	}
	
	public void setDead(Set<Organism> dead)
	{
		this.dead = dead;
	}
	
	public Set<Organism> getDead()
	{
		return this.dead;
	}
	
	public Set<Organism> getLastDead()
	{
		return this.lastDead;
	}
	
	public void setLastDead(Set<Organism> lastDead)
	{
		this.lastDead = lastDead;
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
