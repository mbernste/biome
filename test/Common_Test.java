import biome.utils.MathUtils;


public class Common_Test 
{
	public static void main(String[] args)
	{
		testInterpolate();
	}
	
	public static void testInterpolate()
	{
		System.out.println(MathUtils.interpolate(0f, 10f, 0.3f));
	}

}
