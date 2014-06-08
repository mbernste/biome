/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package biome.organisms;

import java.util.Collections;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import biome.actions.Action;
import biome.actions.Death;
import biome.behaviors.Behavior;
import biome.behaviors.CarnivoreFeed;
import biome.behaviors.Fear;
import biome.behaviors.HerbivoreFeed;
import biome.behaviors.RandomWalk;
import biome.behaviors.ReproduceByBudding;
import biome.environment.Board;
import biome.organisms.attributes.Diet;
import biome.organisms.attributes.Speed;
import biome.organisms.attributes.Vision;
import biome.organisms.attributes.TerrainType;
import biome.view.Common;
import biome.view.ViewData;

/**
 *
 * @author matthewbernstein
 */
public class Animal extends Organism 
{	
	private final static String BODY = "Models/animal/body.obj";
	
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
		
		this.view = new ViewData(this);

	}

	public void setTerrainType(int terrain)
	{
		this.terrain.setValue(terrain);
	}
	
	public int getTerrainType()
	{
		return terrain.getValue();
	}
	
	public int getTerrain() 
	{
		return this.terrain.getValue();
	}
	
	public void setVision(int vision)
	{
		this.vision.setValue(vision);
	}
	
	public void setSpeed(int speed)
	{
		this.speed.setValue(speed);
	}
	
	public int getVision()
	{
		return this.vision.getValue();
	}
	
	public int getSpeed()
	{
		return this.speed.getValue();
	}
	
	public int getDiet()
	{
		return this.diet.getValue();
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
	 public Action act(Board board, Board buffer)
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
			Collections.sort(behaviors, Behavior.generateComparator(buffer));
			System.out.println("Ranking of behaviors: " + behaviors);
			for (int i = 0; i < behaviors.size(); i++)
			{
				Action action = behaviors.get(i).run(board, buffer, this);
				System.out.println("here");
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
	
	@Override
	public Node getNode()
	{
		Node orgNode = new Node(this.toString());		

		Spatial bodyModel = Common.ASSET_MANAGER.loadModel(BODY);
		
		if (diet.getValue() == Diet.DIET_HERBIVORE)
		{
			Material mat = new Material(Common.ASSET_MANAGER,
			          "Common/MatDefs/Misc/Unshaded.j3md");  
			        mat.setColor("Color", ColorRGBA.Blue);   
			        bodyModel.setMaterial(mat);                   
		}
		else if (diet.getValue() == Diet.DIET_HERBIVORE)
		{
			Material mat = new Material(Common.ASSET_MANAGER,
			          "Common/MatDefs/Misc/Unshaded.j3md");  
			        mat.setColor("Color", ColorRGBA.Red);   
			        bodyModel.setMaterial(mat);                   
		}
		
		bodyModel.setLocalScale(Common.SCALE, Common.SCALE, Common.SCALE);
		
		orgNode.attachChild(bodyModel);
		orgNode.attachChild(terrain.getNode());
		orgNode.attachChild(diet.getNode());
		orgNode.attachChild(vision.getNode());
		
		orgNode.setShadowMode(ShadowMode.CastAndReceive);
		
		return orgNode;
	}
}
