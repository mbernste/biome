package biome.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import jme3tools.optimize.GeometryBatchFactory;
import biome.model.board.Square;
import biome.model.core.SimulationState;
import biome.model.organisms.Organism;
import biome.application.Common;

import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;
import com.jme3.water.SimpleWaterProcessor;

public class ViewInitializer {
	
	/**
	 * External file addresses
	 */
	private final static String UNSHADED = "Common/MatDefs/Misc/Unshaded.j3md";
	
	private final View view;

	/**
	 * Camera speed
	 */
	private final static int FLY_CAM_MOVE_SPEED = 40;
	private final static int FLY_CAM_ROTATE_SPEED = 40;
	
	private final SimulationState simState;
	
	@Autowired
	public ViewInitializer(SimulationState simState, View view)
	{
		this.simState = simState;
		this.view = view;
	}
	
	public void init() 
	{
				
		initCam();
		initLights();
		initSky();
		// initFog();
		initOrganisms();
		createBoard();
		initWater();

		
		view.start();
	}
	
	/**
	 * Initialize the skybox
	 */
	private void initSky()
	{
	   Spatial skybox = SkyFactory.createSky(
				view.getAssetManager(), 
				view.getAssetManager().loadTexture("Textures/sky/skyrender0001.tga"),
				view.getAssetManager().loadTexture("Textures/sky/skyrender0004.tga"),
				view.getAssetManager().loadTexture("Textures/sky/skyrender0002.tga"),
				view.getAssetManager().loadTexture("Textures/sky/skyrender0005.tga"),
				
				view.getAssetManager().loadTexture("Textures/sky/skyrender0003.tga"),
				view.getAssetManager().loadTexture("Textures/sky/skyrender0006.tga"));
		 
		view.setSkyBox(skybox);
		view.getRootNode().attachChild(skybox);
	}
	
	public void initFog()
	{
		FilterPostProcessor fpp =new FilterPostProcessor(view.getAssetManager());
		FogFilter fog = new FogFilter();
		fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
		fog.setFogDistance(400);
		fog.setFogDensity(1.5f);
		fpp.addFilter(fog);
		view.getViewPort().addProcessor(fpp);
	}
	
	/**
	 * Initialize the lights in the scene
	 */ 
	private void initLights()
	{			
		AmbientLight ambient = new AmbientLight();
		ambient.setColor(ColorRGBA.White);
		view.getRootNode().addLight(ambient);
		
		AmbientLight ambient1 = new AmbientLight();
		ambient1.setColor(ColorRGBA.White);
		view.getRootNode().addLight(ambient1);

		DirectionalLight sun1 = new DirectionalLight();
        sun1.setColor(ColorRGBA.White);
        sun1.setDirection(new Vector3f(.5f,-.5f,.5f).normalizeLocal());
        view.getRootNode().addLight(sun1);
		
		DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(.5f,-.5f,.5f).normalizeLocal());
        view.getRootNode().addLight(sun);

        /*
         *  Drop shadows 
         */ 
        final int SHADOWMAP_SIZE=1024;
        DirectionalLightShadowRenderer dlsr 
        	= new DirectionalLightShadowRenderer(view.getAssetManager(), SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        view.getViewPort().addProcessor(dlsr);
 
        DirectionalLightShadowFilter dlsf 
        	= new DirectionalLightShadowFilter(view.getAssetManager(), SHADOWMAP_SIZE, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(view.getAssetManager());
        fpp.addFilter(dlsf);
        view.getViewPort().addProcessor(fpp);
	} 

	public void initWater()
	{
		/*
		 *  Create a water processor
		 */
		SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(view.getAssetManager());
		
		waterProcessor.setReflectionScene(view.getRootNode());

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
		waterProcessor.setDistortionScale(0.03f);   // strength of waves
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
		Material mat = new Material(view.getAssetManager(), Common.UNSHADED);  
	    mat.setColor("Color", ColorRGBA.Blue); 
		blue.setMaterial(mat);
		blue.setLocalTranslation(0, -0.2f, (Common.NUM_COLS - 1) * Common.SQUARE_WIDTH);
		blue.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
		view.getRootNode().attachChild(blue);
		
		 		
		Geometry water = new Geometry("water", quad);
		water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
		water.setLocalTranslation(0f, 0.4f, (Common.NUM_COLS - 1) * Common.SQUARE_WIDTH);
		water.setShadowMode(ShadowMode.Receive);
		water.setMaterial(waterProcessor.getMaterial());
		view.getRootNode().attachChild(water);
		
		view.getViewPort().addProcessor(waterProcessor);
		view.setWaterProcessor( waterProcessor );
		
	}
	
	public void initOrganisms()
	{				
		for (Organism org : simState.getOrganisms())
		{
			view.addOrganismToScene(org);
			view.placeOrganism(org, 0); 
		}
	}
	
	
	/**
	 * Initialize the camera
	 */
	private void initCam()
	{		
		FlyByCamera flyCam = view.getFlyByCamera(); 
		
		flyCam.setEnabled(true);                        
		flyCam.setMoveSpeed(FLY_CAM_MOVE_SPEED);	
		flyCam.setRotationSpeed(FLY_CAM_ROTATE_SPEED);  
		flyCam.setDragToRotate(true);
		//cam.setFrustumFar(300);
	}
	
	public void createBoard() 
	{    
		int squareCount = 0;
		
		AssetManager assetManager = view.getAssetManager();
		
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
		
		Material rockMat = new Material(assetManager, UNSHADED);
		rockMat.setTexture("ColorMap", 
				assetManager.loadTexture("Textures/stone_2.png")); 
		
		Node rockBoxes = new Node("ROCK_BOXES");
		Node grassBoxes = new Node("GRASS_BOXES");
						
		Mesh box = new Box(Common.SQUARE_WIDTH / 2, 
						   Common.SQUARE_WIDTH / 2, 
						   Common.SQUARE_WIDTH / 2 );
		TangentBinormalGenerator.generate(box);           // for lighting effect
				
		for (int r = 0; r < Common.NUM_ROWS; r++) 
		{
			for (int c = 0; c < Common.NUM_COLS; c++) 
			{
				Geometry geom = null;
				
				switch( simState.getSquare(r,c).getTerrain() ) 
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
				}

			}
		}  
			
		Spatial optimizedRockBoxes = GeometryBatchFactory.optimize(rockBoxes);
		Spatial optimizedGrassBoxes = GeometryBatchFactory.optimize(grassBoxes);
		optimizedRockBoxes.setMaterial(rockMat);
		optimizedGrassBoxes.setMaterial(grassMat);
			
		optimizedGrassBoxes.setShadowMode(ShadowMode.Receive);
		optimizedRockBoxes.setShadowMode(ShadowMode.Receive);
		
		view.getRootNode().attachChild(optimizedRockBoxes);
		view.getRootNode().attachChild(optimizedGrassBoxes); 
	}

	
}
