/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biome.model.organisms;

import java.util.Collections;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import biome.application.Common;
import biome.controller.actions.Action;
import biome.controller.actions.Death;
import biome.controller.behaviors.Behavior;
import biome.controller.behaviors.CarnivoreFeed;
import biome.controller.behaviors.Fear;
import biome.controller.behaviors.HerbivoreFeed;
import biome.controller.behaviors.RandomWalk;
import biome.controller.behaviors.ReproduceByBudding;
import biome.model.board.Board;
import biome.model.organismattributes.Diet;
import biome.model.organismattributes.Speed;
import biome.model.organismattributes.TerrainType;
import biome.model.organismattributes.Vision;
import biome.view.OrganismViewData;


public class Animal extends Organism 
{	
	private final static int HUNGER_DEATH = 40;
	
	/**
	 * Attributes
	 */
	private Diet diet;
	private Vision vision;
	private Speed speed;
	private TerrainType terrain;
	
	private int hunger;

	public Animal(TerrainType terrain, Diet diet)
	{	
		super();
		
		this.terrain = terrain;
		this.hunger = 10;
		this.libido = 0;
		this.diet = diet;
		this.nutritionalValue = 10;
		
		if (this.diet.getValue() == Diet.DIET_HERBIVORE)
		{
			behaviors.add(new HerbivoreFeed(this));
			behaviors.add(new Fear(this));
			behaviors.add(new ReproduceByBudding(this, 5));
			this.vision = new Vision(Common.HERBIVORE_VISION);
			this.speed = new Speed(Common.HERBIVORE_SPEED);
		}
		else if (this.diet.getValue() == Diet.DIET_CARNIVORE)
		{
			behaviors.add(new CarnivoreFeed(this));
			behaviors.add(new ReproduceByBudding(this, 15));
			this.vision = new Vision(Common.HERBIVORE_VISION);
			this.speed = new Speed(Common.HERBIVORE_SPEED);
		}
		
		behaviors.add(new RandomWalk());
		
	}

	public void setTerrainType(int terrain)
	{
		this.terrain.setValue(terrain);
	}
	
	public int getTerrainType()
	{
		return terrain.getValue();
	}
	
	public TerrainType getTerrain() 
	{
		return this.terrain;
	}
	
	public void setVision(int vision)
	{
		this.vision.setValue(vision);
	}
	
	public void setSpeed(int speed)
	{
		this.speed.setValue(speed);
	}
	
	public Vision getVision()
	{
		return this.vision;
	}
	
	public int getSpeed()
	{
		return this.speed.getValue();
	}
	
	public Diet getDiet()
	{
		return this.diet;
	}
	
	public void setDiet(int diet)
	{
		this.diet.setValue(diet);
	}
	
	public int getHunger()
	{
		return this.hunger;
	}
	
	public int getLibido()
	{
		return this.libido;
	}
	
	public void satiateLibido()
	{
		this.libido = 0;
	}
	
	public void feed(int amount)
	{
		this.hunger -= amount;
	}
	
	@Override
	public Action act()
    {		
		System.out.println("Animal " + this + " is deciding what to do.");
		iterateBehaviors();

		hunger++;
		libido++;
		
		if (hunger >= HUNGER_DEATH)
		{
			return new Death();
		}
		else
		{
			Collections.sort(behaviors, Behavior.generateComparator());
			System.out.println("Ranking of behaviors: " + behaviors);
			for (int i = 0; i < behaviors.size(); i++)
			{
				Action action = behaviors.get(i).run(simState.getBoard(), simState.getBuffer(), this);
				if (action != null) return action;
			}
			
			return null;
		}
    }
	
	@Override 
	public String toString()
	{
		return "ANIMAL_" + orgId;
	}
	
	@Override
	public Organism giveBirth()
	{
		Diet childDiet = (Diet) this.diet.mutate();
		Vision childVision = (Vision) this.vision.mutate();
		Speed childSpeed = (Speed) this.speed.mutate();
		TerrainType childTerrain = (TerrainType) this.terrain.mutate();
		
		Animal child = new Animal(childTerrain, childDiet);
		child.vision = childVision;
		child.speed = childSpeed;
		
		return child;	
	}
	
}
