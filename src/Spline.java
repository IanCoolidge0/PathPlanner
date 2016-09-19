
/**
 * Overall class that interpolates a cubic spline from a number of way points.
 * 
 * @author cooli
 *
 */
public class Spline {

	private float[] wayPoints;
	private int numWP;
	
	private float[] dxdtAtWP;
	private float[] dydtAtWP;
	
	public Spline(float[] wayPoints) 
	{
		this.wayPoints = wayPoints;
		this.numWP = wayPoints.length / 3;
		
		this.dxdtAtWP = new float[numWP];
		
		create();
	}
//row then column
	private void create()
	{	
		float[][] xMatrix = new float[numWP][numWP];
		float[]   xVector = new float[numWP];
		
		float[][] yMatrix = new float[numWP][numWP];
		float[]   yVector = new float[numWP];
		
		//inject n-1 mandatory equations into the matrix
		for(int i=1; i<numWP-1; i++)
		{
			float xi = wayPoints[3*i];
			float yi = wayPoints[3*i + 1];
			float ti = wayPoints[3*i + 2];
			
			float xim = wayPoints[3*(i-1)];
			float yim = wayPoints[3*(i-1) + 1];
			float tim = wayPoints[3*(i-1) + 2];
			
			float xip = wayPoints[3*(i+1)];
			float yip = wayPoints[3*(i+1) + 1];
			float tip = wayPoints[3*(i+1) + 2];
			
			xVector[i] = 3 * ( (xi - xim)/((ti - tim)*(ti - tim)) 
							 + (xip - xi)/((tip - ti)*(tip - ti)) );
			
			yVector[i] = 3 * ( (yi - yim)/((ti - tim)*(ti - tim)) 
					 		 + (yip - yi)/((tip - ti)*(tip - ti)) );
			
			xMatrix[i][i-1] = yMatrix[i][i-1] = 1/(yi - yim);
			xMatrix[i][i]   = yMatrix[i][i]   = 2 * ( 1/(yi - yim) + 1/(yip - yi) );
			xMatrix[i][i+1] = yMatrix[i][i+1] = 1/(yip - yi);
		}
		
		dxdtAtWP = LinearSystem.solveLinearSystem(xMatrix, xVector);
		dydtAtWP = LinearSystem.solveLinearSystem(yMatrix, yVector);
	}
	
}
