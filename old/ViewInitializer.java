package biome.view;

import jme3tools.optimize.GeometryBatchFactory;
import biome.model.board.Square;

import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.TangentBinormalGenerator;

public class ViewInitializer {
	
	
	/**
	 * External file addresses
	 */
	private final static String UNSHADED = "Common/MatDefs/Misc/Unshaded.j3md";
	
	View theView;
	
	

	/**
	 * Camera speed
	 */
	private final static int FLY_CAM_MOVE_SPEED = 40;
	private final static int FLY_CAM_ROTATE_SPEED = 40;
	
	public void init() {
		theView.start();
	}

	/**
	 * Initialize the camera
	 */
	private void initCam()
	{
		FlyByCamera flyCam = theView.getFlyByCamera(); 
		
		flyCam.setEnabled(true);                        
		flyCam.setMoveSpeed(FLY_CAM_MOVE_SPEED);	
		flyCam.setRotationSpeed(FLY_CAM_ROTATE_SPEED);  
		flyCam.setDragToRotate(true);
		//cam.setFrustumFar(300);
	}
	
	public void createBoard() 
	{    
		int squareCount = 0;
		
		AssetManager assetManager = theView.getAssetManager();
		
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
