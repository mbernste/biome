package biome.controller.core;

import java.util.ArrayList;
import java.util.List;

import biome.model.organisms.Organism;

public class IterationResult 
{
	private List<Organism> dead;
	private List<Organism> born;
	private List<Organism> lastDead;
	
	public IterationResult()
	{
		dead = new ArrayList<>();
		born = new ArrayList<>();
		lastDead = new ArrayList<>();
	}
	
	public void setDead(List<Organism> dead)
	{
		this.dead = dead;
	}
	
	public List<Organism> getDead()
	{
		return this.dead;
	}
	
	public List<Organism> getLastDead()
	{
		return this.lastDead;
	}
	
	public void setLastDead(List<Organism> lastDead)
	{
		this.lastDead = lastDead;
	}
	
	public void setBorn(List<Organism> born)
	{
		this.born = born;
	}
	
	public List<Organism> getBorn()
	{
		return this.born;
	}
}
