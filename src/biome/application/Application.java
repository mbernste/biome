package biome.application;

import java.util.Set;

import jme3tools.optimize.GeometryBatchFactory;

import pair.Pair;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.water.SimpleWaterProcessor;

import biome.core.SimulationEngine;
import biome.core.Square;
import biome.organisms.Animal;
import biome.organisms.Organism;
import biome.view.Common;

import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/**
 * Main class
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 */
public class Application extends SimpleApplication 
{
	/**
	 * External file addresses
	 */
	private final static String UNSHADED = "Common/MatDefs/Misc/Unshaded.j3md";
	
	/**
	 * Camera speed
	 */
	private final static int FLY_CAM_MOVE_SPEED = 40;
	private final static int FLY_CAM_ROTATE_SPEED = 40;

	/**
	 *  Effects
	 */
	private SimpleWaterProcessor waterProcessor;

	private float timeVar = 0;

	private SimulationEngine engine; 

	private Spatial skybox;
	
	/**
	 * Main method
	 * 
	 * @param args commain line arguments
	 */
	public static void main(String[] args) 
	{	
		Application app = new Application();
		
		app.start();
	}

	/**
	 * Initialize the camera
	 */
	private void initCam()
	{
		flyCam.setEnabled(true);                        
		flyCam.setMoveSpeed(FLY_CAM_MOVE_SPEED);	
		flyCam.setRotationSpeed(FLY_CAM_ROTATE_SPEED);  
		flyCam.setDragToRotate(true);
		//cam.setFrustumFar(300);
	}

	/**
	 * Initialize the lights in the scene
	 */
	private void initLights()
	{			
		AmbientLight ambient = new AmbientLight();
		ambient.setColor(ColorRGBA.White);
		rootNode.addLight(ambient);
		
		AmbientLight ambient1 = new AmbientLight();
		ambient1.setColor(ColorRGBA.White);
		rootNode.addLight(ambient1);

		DirectionalLight sun1 = new DirectionalLight();
        sun1.setColor(ColorRGBA.White);
        sun1.setDirection(new Vector3f(.5f,-.5f,.5f).normalizeLocal());
        rootNode.addLight(sun1);
		
		DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(.5f,-.5f,.5f).normalizeLocal());
        rootNode.addLight(sun);
 
        /*
         *  Drop shadows 
         */
        final int SHADOWMAP_SIZE=1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);
 
        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
	}

	/**
	 * Initialize the skybox
	 */
	private void initSky()
	{
		skybox = SkyFactory.createSky(
				assetManager, 
				assetManager.loadTexture("Textures/sky/skyrender0001.tga"),
				assetManager.loadTexture("Textures/sky/skyrender0004.tga"),
				assetManager.loadTexture("Textures/sky/skyrender0002.tga"),
				assetManager.loadTexture("Textures/sky/skyrender0005.tga"),
				
				assetManager.loadTexture("Textures/sky/skyrender0003.tga"),
				assetManager.loadTexture("Textures/sky/skyrender0006.tga"));
		
		rootNode.attachChild(skybox);
	}

	public Set<Animal> getAnimals()
	{
		return engine.getAnimals();
	}
	
	@Override
	public void simpleInitApp() 
	{    	
		Common.ASSET_MANAGER = assetManager;
		
		engine = new SimulationEngine(Common.NUM_ROWS, Common.NUM_COLS);

		initCam();
		initLights();
		initSky();
		initOrganisms();
		//initFog();
		
		initWater();
		renderBoard();
		
		viewPort.addProcessor(waterProcessor);
	}

	public void initFog()
	{
		FilterPostProcessor fpp =new FilterPostProcessor(assetManager);
		FogFilter fog = new FogFilter();
		fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
		fog.setFogDistance(400);
		fog.setFogDensity(1.5f);
		fpp.addFilter(fog);
		viewPort.addProcessor(fpp);
	}
	
	public void initWater()
	{
		/*
		 *  Create a water processor
		 */
		waterProcessor = new SimpleWaterProcessor(assetManager);
		waterProcessor.setReflectionScene(rootNode);

		/*
		 *  Set the water plane
		 */
		Vector3f waterLocation=new Vector3f(0,0,0);
		waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));

		/*
		 *  Set wave properties
		 */
		waterProcessor.setWaterDepth(0.2f);         // transparency of water
		waterProcessor.setWaterTransparency(0.2f);
		waterProcessor.setDistortionScale(0.03f); // strength of waves
		waterProcessor.setWaveSpeed(0.04f);       
		waterProcessor.setLightPosition( new Vector3f(Common.NUM_ROWS * 
													  Common.SQUARE_WIDTH / 2, 70f,
													  Common.NUM_COLS * 
													  Common.SQUARE_WIDTH / 2));
		waterProcessor.setWaterColor(new ColorRGBA((float) 102 / 255, 
												   (float) 102 / 255, 
												   (float) 1f, 0f));
		
		/*
		 *  We define the wave size by setting the size of the texture 
		 *  coordinates
		 */
		Quad quad = new Quad(Common.NUM_ROWS * Common.SQUARE_WIDTH, 
							 Common.NUM_COLS * Common.SQUARE_WIDTH);
		quad.scaleTextureCoordinates(new Vector2f(4f,4f));
		
		/*
		 * A large blue quad under the water gives the water a better blue color
		 */
		Quad blueQuad = new Quad(Common.NUM_ROWS * Common.SQUARE_WIDTH, 
								 Common.NUM_COLS * Common.SQUARE_WIDTH);
		Geometry blue = new Geometry("blue", blueQuad);
		Material mat = new Material(Common.ASSET_MANAGER, Common.UNSHADED);  
	    mat.setColor("Color", ColorRGBA.Blue); 
		blue.setMaterial(mat);
		blue.setLocalTranslation(0, -0.2f, (Common.NUM_COLS - 1) * Common.SQUARE_WIDTH);
		blue.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
		rootNode.attachChild(blue);
		
		 		
		Geometry water=new Geometry("water", quad);
		water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
		water.setLocalTranslation(0f, 0.4f, (Common.NUM_COLS - 1) * Common.SQUARE_WIDTH);
		water.setShadowMode(ShadowMode.Receive);
		water.setMaterial(waterProcessor.getMaterial());
		rootNode.attachChild(water);
	}
	
	public void initOrganisms()
	{				
		for (Organism org : engine.getOrganisms())
		{			
			org.resetView();
			addOrganismToScene(org);
			placeOrganism(org, 0); 
		}
	}
	
	public void addOrganismToScene(Organism org)
	{
		Node node = org.getNode();
		System.out.println("ADDING " + org + " TO SCENE");
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
		
		IterationResult result = engine.iterate();
		addBornModels(result.getBorn());
		removeDeadModels(result.getLastDead());
		rotateAnimals();		
	}
	
	/**
	 * Add models of newly born organisms to the scene
	 * 
	 * @param born the newly born organisms
	 */
	private void addBornModels(Set<Organism> born) 
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
	private void removeDeadModels(Set<Organism> dead)
	{
		for (Organism org : dead)
		{
			System.out.println("REMOVING " + org);
			
			rootNode.getChild(org.toString()).removeFromParent();	
		}
	}
	
	@Override
	public void simpleRender(RenderManager rm) { }

	public void rotateAnimals()
	{
		System.out.println(engine.getOrganisms().size());
		
		for (Animal an : engine.getAnimals())
		{	
			Spatial orgSpatial = rootNode.getChild(an.toString());
			
			float angle = an.getView().getRotationAngle();
			Vector3f axis = new Vector3f(0, 1, 0);
			Quaternion rotQuat = new Quaternion();
			rotQuat.fromAngleAxis(angle, axis);
						
			orgSpatial.setLocalRotation(rotQuat);
		}
	}
	
	public void moveAnimals(float interpVal)
	{
		for (Organism org : engine.getAnimals())
		{
			placeOrganism(org, interpVal);   
		}
	}

	private void placeOrganism(Organism org, float interpVal)
	{	
		Spatial spatial = rootNode.getChild(org.toString());
		
		Pair<Float, Float> origin = org.getView().getOrigin();
		Pair<Float, Float> destination = org.getView().getDestination();

		float interpRow = Common.interpolate(origin.getFirst(), 
											 destination.getFirst(), 
											 interpVal);
		
		float interpCol = Common.interpolate(origin.getSecond(), 
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
		
		int squareType = engine.getSquare(Math.round(interpRow), Math.round(interpCol)).getTerrainType();
		
		float y;
		if (squareType == Square.LAND)
		{
			y = Common.SQUARE_WIDTH / 2;
		}
		else
		{
			y = org.getView().getYOffSet();
		}
		
		float xOff = org.getView().getXOffSet();
		float zOff = org.getView().getZOffSet();
			
		spatial.setLocalTranslation(coord.getFirst() + xOff, y, coord.getSecond() + zOff);   
	}
		
	public void renderBoard() 
	{    
		int squareCount = 0;
		
		 Material grassMat = new Material(assetManager, 
			        "Common/MatDefs/Light/Lighting.j3md"); 
	    grassMat.setTexture("DiffuseMap", 
		        assetManager.loadTexture("Textures/grass.jpg"));
		grassMat.setTexture("NormalMap", 
		        assetManager.loadTexture("Textures/grass_normal.png"));
		grassMat.setBoolean("UseMaterialColors",true);    
		grassMat.setColor("Diffuse",ColorRGBA.Green);
		grassMat.setColor("Specular",ColorRGBA.Green);
		grassMat.setFloat("Shininess", 1f);
		

		Material rockMaterial = new Material(assetManager, UNSHADED);
		
		rockMaterial.setTexture("ColorMap", 
				assetManager.loadTexture("Textures/stone_2.png")); 
		
		Material waterMaterial = new Material(assetManager, UNSHADED);
		waterMaterial.setTexture("ColorMap", 
				assetManager.loadTexture("Textures/water.jpg"));

		Node rockBoxes = new Node("ROCK_BOXES");
		Node grassBoxes = new Node("GRASS_BOXES");
		Node waterBoxes = new Node("WATER_BOXES");
		
		Node tmpRockBoxes = new Node();
		Node tmpGrassBoxes = new Node();
		
		
		Mesh quad = new Quad(Common.SQUARE_WIDTH, Common.SQUARE_WIDTH);
		Mesh box = new Box(Common.SQUARE_WIDTH / 2, Common.SQUARE_WIDTH / 2, Common.SQUARE_WIDTH / 2 );
		TangentBinormalGenerator.generate(box);           // for lighting effect
				
		for (int r = 0; r < engine.getNumRows(); r++) 
		{
			for (int c = 0; c < engine.getNumColumns(); c++) 
			{
				Geometry geom = null;
				
				switch( engine.getSquare(r,c).getTerrain() ) 
				{
				case Square.GRASS_TYPE: 
					geom = new Geometry("SQUARE_" + Integer.toString(squareCount), box);
					geom.move((r * Common.SQUARE_WIDTH + (Common.SQUARE_WIDTH / 2)), 
							   0, 
							  (c * Common.SQUARE_WIDTH) - (Common.SQUARE_WIDTH / 2));
					grassBoxes.attachChild(geom);
					break;

				case Square.ROCK_TYPE: 
					geom = new Geometry("SQUARE_" + Integer.toString(squareCount), box);
					geom.move(((r * Common.SQUARE_WIDTH) + (Common.SQUARE_WIDTH / 2)), 
							   0, 
							   (c * Common.SQUARE_WIDTH) - (Common.SQUARE_WIDTH / 2));
					rockBoxes.attachChild(geom);
					break;

				case Square.WATER_TYPE: 
					/*
					geom = new Geometry("SQUARE_" + Integer.toString(squareCount), quad);
					geom.rotate( ((float)-Math.PI)/2f, 0f, 0f);
					geom.move((r * Common.SQUARE_WIDTH), 
							   0, 
							   (c * Common.SQUARE_WIDTH));
					waterBoxes.attachChild(geom); */
					break;   
				}

			}
		}  
		
		
		Spatial optimizedRockBoxes = GeometryBatchFactory.optimize(rockBoxes);
		Spatial optimizedGrassBoxes = GeometryBatchFactory.optimize(grassBoxes);
		Spatial optimizedWaterBoxes = GeometryBatchFactory.optimize(waterBoxes);
		optimizedRockBoxes.setMaterial(rockMaterial);
		optimizedGrassBoxes.setMaterial(grassMat);
		optimizedWaterBoxes.setMaterial(waterProcessor.getMaterial());
			
		optimizedGrassBoxes.setShadowMode(ShadowMode.Receive);
		optimizedRockBoxes.setShadowMode(ShadowMode.Receive);
		
		rootNode.attachChild(optimizedRockBoxes);
		rootNode.attachChild(optimizedGrassBoxes); 
	}
}
