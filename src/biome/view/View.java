package biome.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import pair.Pair;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.water.SimpleWaterProcessor;

import biome.application.Common;
import biome.controller.core.IterationResult;
import biome.controller.core.SimulationEngine;
import biome.model.board.Square;
import biome.model.core.SimulationState;
import biome.model.organisms.Animal;
import biome.model.organisms.Organism;
import biome.utils.MathUtils;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Main class
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 */
public class View extends SimpleApplication 
{	

	/**
	 *  Effects
	 */
	private SimpleWaterProcessor waterProcessor;

	private float timeVar = 0;

	private OrganismViewData organismViewData;
	
	private Spatial skybox;

	/**
	 * Simulation engine
	 */
	private SimulationEngine simEngine; 
	
	/**
	 * State of the simulation
	 */
	private SimulationState simState;
	
	
	@Autowired
	public void setSimEngine(SimulationEngine engine)
	{
		this.simEngine = engine;
	}
	
	@Autowired
	public void setSimState(SimulationState simState)
	{
		this.simState = simState;
	}
	
	@Autowired
	public void setOrganismViewData(@Qualifier("theOrganismViewData") OrganismViewData organismViewData)
	{
		this.organismViewData = organismViewData;
	}
		
	@Override
	public void simpleInitApp() 
	{    			
		ViewInitializer viewInitializer = (ViewInitializer) Common.BEAN_CONTEXT.getBean("theViewInitializer");
		viewInitializer.init();
	}
	
	public void addOrganismToScene(Organism org)
	{
		System.out.println("ADDING " + org + " TO SCENE");			
		
		organismViewData.addOrganism(org);
		Node node = organismViewData.getOrganismViewData(org).getNode();
		rootNode.attachChild(node);			
	}
	
	@Override
	public void simpleUpdate(float tpf) 
	{	
		timeVar += tpf;
				
		// Update the animation between iterations
		animateUpdate();

		// Perform a board iteration
		if (timeVar >= Common.ITERATION_TIME)
		{
			iterateUpdate();
			timeVar = 0;     
		}
	}
	
	private void animateUpdate()
	{
		this.moveAnimals(timeVar / Common.ITERATION_TIME);
	}
	
	/**
	 * Updates the view for each new iteration of the simulation.
	 */
	private void iterateUpdate()
	{
		System.out.println("ITERATE");
			
		for (Organism org : simState.getOrganisms()) 
		{
			organismViewData.resetOrganismView(org);
		}
		
		IterationResult result = simEngine.iterate();
		
		// Process born and dead organisms
		addBornModels(result.getBorn());
		removeDeadModels(result.getLastDead());
		
		rotateAnimals();		
	}
	
	
	/**
	 * Add models of newly born organisms to the scene
	 * 
	 * @param born the newly born organisms
	 */
	private void addBornModels(List<Organism> born) 
	{
		for (Organism org : born)
		{
			addOrganismToScene(org);
			placeOrganism(org, 0);
		}
	}

	/**
	 * Remove the models that correspond to dead organisms
	 * 
	 * @param dead dead organisms for which the models need to be removed
	 */
	private void removeDeadModels(List<Organism> dead)
	{
		for (Organism org : dead)
		{
			System.out.println("REMOVING " + org + " FROM VIEW.");
			rootNode.getChild(org.toString()).removeFromParent();
			organismViewData.removeOrganism(org);
		}
	}
	
	@Override
	public void simpleRender(RenderManager rm) { }

	public void rotateAnimals()
	{
		System.out.println(simState.getOrganisms().size());
		
		for (Animal an : simState.getAnimals())
		{	
			Spatial orgSpatial = rootNode.getChild(an.toString());
			
			float angle = organismViewData.getRotationAngle(an);
			Vector3f axis = new Vector3f(0, 1, 0);
			Quaternion rotQuat = new Quaternion();
			rotQuat.fromAngleAxis(angle, axis);
						
			orgSpatial.setLocalRotation(rotQuat);
		}
	}
	
	public void moveAnimals(float interpVal)
	{
		for (Organism org : simState.getAnimals())
		{	
			placeOrganism(org, interpVal);   
		}
	}

	void placeOrganism(Organism org, float interpVal)
	{			
		Spatial spatial = rootNode.getChild(org.toString());
		
		Pair<Float, Float> origin = organismViewData.getOrigin(org);
		Pair<Float, Float> destination = organismViewData.getDestination(org);
		
		float interpRow = MathUtils.interpolate(origin.getFirst(), 
											 destination.getFirst(), 
											 interpVal);
		
		float interpCol = MathUtils.interpolate(origin.getSecond(), 
											 destination.getSecond(), 
											 interpVal);

		// Edge of board conditions
		if (interpRow >= Common.NUM_ROWS - 0.5)
		{
			interpRow -= Common.NUM_ROWS;
		}
		else if (interpRow < -0.5)
		{
			interpRow += Common.NUM_ROWS;
		}

		// Edge of board conditions
		if (interpCol >= Common.NUM_COLS - 0.5)
		{
			interpCol -= Common.NUM_COLS;
		}
		else if (interpCol < -0.5)
		{
			interpCol += Common.NUM_COLS;
		}

		Pair<Float, Float> coord = Common.convertToWorldCoords(interpRow, 
															   interpCol);
		
								
		int squareType = simState.getSquare(Math.round(interpRow), 
										  Math.round(interpCol)).getTerrainType();
		
		float y;
		if (squareType == Square.LAND)
		{
			y = Common.SQUARE_WIDTH / 2;
		}
		else
		{
			y = organismViewData.getYOffset(org);
		}
		
		float xOff = organismViewData.getXOffset(org);
		float zOff = organismViewData.getZOffset(org);
		
		
		spatial.setLocalTranslation(coord.getFirst() + xOff, 
									y, 
									coord.getSecond() + zOff);   
	}
	
	
	public SimpleWaterProcessor getWaterProcessor()
	{
		return this.waterProcessor;
	}

	protected void setWaterProcessor(SimpleWaterProcessor waterProcessor)
	{
		this.waterProcessor = waterProcessor;
	}
	
	protected void setSkyBox(Spatial skybox)
	{
		this.skybox = skybox;
	}
	
	public Spatial getSkyBox()
	{
		return this.skybox;
	}
}
