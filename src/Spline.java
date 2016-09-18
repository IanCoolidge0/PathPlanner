
/**
 * Overall class that interpolates a cubic spline from a number of way points.
 * 
 * @author cooli
 *
 */
public class Spline {

	private int[] wayPoints;

	public Spline(int[] wayPoints) 
	{
		this.wayPoints = wayPoints;
		create();
	}

	private void create()
	{
		int numWP = wayPoints.length / 3;
		
		
	}
	
}
