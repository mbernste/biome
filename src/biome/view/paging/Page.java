package biome.view.paging;

import java.util.HashSet;
import java.util.Set;

import com.jme3.scene.Geometry;

public class Page 
{
	public static int PAGE_SIZE = 20;
	
	Set<Geometry> geometries;
	
	int rowStart;
	int colStart;
	
	public Page()
	{
		geometries = new HashSet<Geometry>();
	}
	
	public Set<Geometry> getAllGeometries()
	{
		return geometries;
	}
	
	

}
