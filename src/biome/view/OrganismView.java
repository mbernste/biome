package biome.view;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import biome.application.Common;
import biome.model.organismattributes.Diet;
import biome.model.organisms.Animal;
import biome.model.organisms.Organism;
import biome.model.organisms.Plant;
import biome.model.organisms.Tree;

import pair.Pair;

public class OrganismView 
{
	
	private final static String BODY = "Models/animal/body.obj";
	public final static String HERBIVORE_MODEL = "Models/maya_animal_obj.obj";
	private final static String CARNIVORE_MODEL = "Models/predatorObj.obj";
	
	Pair<Float, Float> origin;
	Pair<Float, Float> destination;
	Float rotationAngle = 0.0f;
	
	float yOffSet;
	float xOffSet;
	float zOffSet;
	
	Node node;
	
	public OrganismView(Organism org)
	{
		if (org instanceof Animal)
		{
			yOffSet = -0.5f;
		}
		
		origin = new Pair<Float, Float>(0.0f, 0.0f);
		destination = new Pair<Float, Float>(0.0f, 0.0f);
		node = generateNode(org);
	}
	
	public void calculateRotationAngle()
	{
		double distance = Common.distance(origin, destination);
		
		if (distance != 0)
		{		
			float colVec = destination.getSecond() - origin.getSecond();
			float rowVec = destination.getFirst() - origin.getFirst();
			this.rotationAngle = (float) Math.acos(colVec / distance);
			
			
			Pair<Float, Float> dirVec = new Pair<Float, Float>();
			dirVec.setFirst(rowVec);
			dirVec.setSecond(colVec);
			
			Pair<Float, Float> unitCol = new Pair<Float, Float>(0f, 1f);
			
			if (Common.zCross(dirVec, unitCol) < 0)
			{
				this.rotationAngle = (float) (2 * Math.PI) - this.rotationAngle;
			}	
		}
	}
	
	public Float getRotationAngle()
	{
		return this.rotationAngle;
	}
	
	public void setOrigin(Pair<Float, Float> origin)
	{
		this.origin = origin;
	}
	
	public void setDestination(Pair<Float, Float> destination)
	{
		this.destination = destination;
	}
	
	public Pair<Float, Float> getOrigin()
	{
		return this.origin;
	}
	
	public Pair<Float, Float> getDestination()
	{
		return this.destination;
	}
	
	public Node generateNode(Organism org)
	{
		Node node = new Node(org.toString());
		
		if (org instanceof Plant)
		{
			
			yOffSet = 0.45f;
			xOffSet = Common.RAND.nextFloat() * (Common.SQUARE_WIDTH / 2);
			zOffSet = Common.RAND.nextFloat() * (Common.SQUARE_WIDTH / 2);
			if (Common.RAND.nextBoolean()) xOffSet = -xOffSet;
			if (Common.RAND.nextBoolean()) zOffSet = -zOffSet;
			
			Spatial plant = Common.ASSET_MANAGER.loadModel("Models/plant.obj");
			Spatial fruit = Common.ASSET_MANAGER.loadModel("Models/fruit.obj");
			
			float scaleAmount = 1.5f + Common.RAND.nextFloat();
		
			Material fruitMat = new Material(Common.ASSET_MANAGER, Common.UNSHADED);
			fruitMat.setTexture("ColorMap", 
					Common.ASSET_MANAGER.loadTexture("Textures/fruit.jpg")); 
			
			Material plantMat = new Material(Common.ASSET_MANAGER, Common.UNSHADED);
			plantMat.setTexture("ColorMap", 
					Common.ASSET_MANAGER.loadTexture("Textures/lilyTexture.png")); 
			
			fruit.setMaterial(fruitMat);
			plant.setMaterial(plantMat);
			
			node.attachChild(plant);
			node.attachChild(fruit);
			
			float randScale = Common.RAND.nextInt(20) / 150f;
			fruit.setLocalScale( 0.1f + randScale, 0.1f + randScale, 0.1f + randScale);
			randScale = Common.RAND.nextInt(20) / 1000f;
			plant.setLocalScale( 0.03f + randScale, 0.03f + randScale, 0.03f + randScale);
			
			float randAngle = (Common.RAND.nextFloat() + Common.RAND.nextInt(2)) * FastMath.PI;
			node.setLocalRotation(new Quaternion().fromAngleAxis(randAngle, Vector3f.UNIT_Y));
			
		}
		else if (org instanceof Tree)
		{
			if (Common.RAND.nextBoolean())
			{
				Spatial trunk = Common.ASSET_MANAGER.loadModel("Models/tree3_trunk.obj");
				Spatial leaves = Common.ASSET_MANAGER.loadModel("Models/tree3_leaves.obj");
				
				float scaleAmount = 1.5f + Common.RAND.nextFloat();
				
				Material barkMat = new Material(Common.ASSET_MANAGER, Common.UNSHADED);
				barkMat.setTexture("ColorMap", 
						Common.ASSET_MANAGER.loadTexture("Textures/bark.png")); 
				
				Material leavesMat = new Material(Common.ASSET_MANAGER, Common.UNSHADED);
				leavesMat.setTexture("ColorMap", 
						Common.ASSET_MANAGER.loadTexture("Textures/leaves.jpg")); 
				
			    float randAngle = (Common.RAND.nextFloat() + Common.RAND.nextInt(2)) * FastMath.PI;
				//trunk.setLocalRotation(new Quaternion().fromAngleAxis(randAngle, Vector3f.UNIT_Y));
				//model.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.PI, Vector3f.UNIT_X));
				
				trunk.setMaterial(barkMat);
				leaves.setMaterial(leavesMat);
				
				node.attachChild(trunk);
				node.attachChild(leaves);
				
				node.setLocalRotation(new Quaternion().fromAngleAxis(randAngle, Vector3f.UNIT_Y));
			}
			else
			{
				yOffSet = 5.0f;
				
				Spatial model = Common.ASSET_MANAGER.loadModel("Models/treeObj.obj");
				float scaleAmount = 1.5f + Common.RAND.nextFloat();
				
				model.setLocalScale( scaleAmount, scaleAmount, scaleAmount);
				
			    float randAngle = (Common.RAND.nextFloat() + Common.RAND.nextInt(2)) * FastMath.PI;
				model.setLocalRotation(new Quaternion().fromAngleAxis(randAngle, Vector3f.UNIT_Y));
				
				node.attachChild(model);
			}
		}
		else if (org instanceof Animal)
		{
			Animal an = (Animal) org;
			
			Spatial bodyModel = Common.ASSET_MANAGER.loadModel(BODY);
			
			
			if (an.getDiet().getValue() == Diet.DIET_HERBIVORE)
			{
				Material mat = new Material(Common.ASSET_MANAGER,
				          "Common/MatDefs/Misc/Unshaded.j3md");  
				        mat.setColor("Color", ColorRGBA.Blue);   
				        bodyModel.setMaterial(mat);                   
			}
			else if (an.getDiet().getValue() == Diet.DIET_HERBIVORE)
			{
				Material mat = new Material(Common.ASSET_MANAGER,
				          "Common/MatDefs/Misc/Unshaded.j3md");  
				        mat.setColor("Color", ColorRGBA.Red);   
				        bodyModel.setMaterial(mat);                   
			}
			
			bodyModel.setLocalScale(Common.SCALE, Common.SCALE, Common.SCALE);
			
			node.attachChild(bodyModel);
			node.attachChild(an.getTerrain().getNode());
			node.attachChild(an.getDiet().getNode());
			node.attachChild(an.getVision().getNode());
			
			node.setShadowMode(ShadowMode.CastAndReceive);
		}
	
		node.setShadowMode(ShadowMode.CastAndReceive);
		
		return node;
	}
	
	public float getYOffSet()
	{
		return this.yOffSet;
	}
	
	public float getXOffSet()
	{
		return this.xOffSet;
	}
	
	public float getZOffSet()
	{
		return this.zOffSet;
	}
	
	public Node getNode()
	{
		return this.node;
	}
	
	@Override
	public String toString()
	{
		return "Origin=" + origin + ",Destination=" + destination;
	}
}
