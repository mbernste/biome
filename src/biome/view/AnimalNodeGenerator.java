package biome.view;

import org.springframework.beans.factory.annotation.Autowired;

import biome.application.Common;
import biome.model.organismattributes.Diet;
import biome.model.organisms.Animal;
import biome.view.organismattributes.TerrainTypeNodeGenerator;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class AnimalNodeGenerator 
{
	private final TerrainTypeNodeGenerator terrainTypeGenerator;
	private final View theView;
	
	private final static String BODY = "Models/animal/body.obj";

	
	@Autowired
	public AnimalNodeGenerator(View view, TerrainTypeNodeGenerator terrainTypeGenerator)
	{
		this.theView = view;
		this.terrainTypeGenerator = terrainTypeGenerator;
	}
	
	public Node generateNode(Animal an)
	{		
		Spatial bodyModel = theView.getAssetManager().loadModel(BODY);
		Node node = new Node(an.toString());
		
		if (an.getDiet().getValue() == Diet.DIET_HERBIVORE)
		{
			Material mat = new Material(theView.getAssetManager(),
			          "Common/MatDefsq/Misc/Unshaded.j3md");  
			        mat.setColor("Color", ColorRGBA.Blue);   
			        bodyModel.setMaterial(mat);                   
		}
		else if (an.getDiet().getValue() == Diet.DIET_HERBIVORE)
		{
			Material mat = new Material(theView.getAssetManager(),
			          "Common/MatDefs/Misc/Unshaded.j3md");  
			        mat.setColor("Color", ColorRGBA.Red);   
			        bodyModel.setMaterial(mat);                   
		}
		
		bodyModel.setLocalScale(Common.SCALE, Common.SCALE, Common.SCALE);
		
		node.attachChild(bodyModel);
		node.attachChild(terrainTypeGenerator.generateNode(an.getTerrain()));
		node.attachChild(an.getDiet().getNode());
		node.attachChild(an.getVision().getNode());
		
		node.setShadowMode(ShadowMode.CastAndReceive);
		
		return node;
	}

}
