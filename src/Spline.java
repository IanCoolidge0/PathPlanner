
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
		this.dydtAtWP = new float[numWP];
		
		create();
	}

	public void getX()
	{
		
	}
	
	public void getY()
	{
		
	}
	
	public void getVelX()
	{
		
	}
	
	public void getVelY()
	{
		
	}
	
	public void getAccX()
	{
		
	}
	
	public void getAccY()
	{
		
	}
	
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
		
		//boundary condition at beginning
		float x0 = wayPoints[0];
		float y0 = wayPoints[1];
		float t0 = wayPoints[2];
		
		float x1 = wayPoints[3];
		float y1 = wayPoints[4];
		float t1 = wayPoints[5];
		
		xMatrix[0][0] = 2 / (t1 - t0);
		xMatrix[0][1] = 1 / (t1 - t0);
		xVector[0] = 3 * (x1 - x0) / ( (t1-t0)*(t1-t0) );
		
		yMatrix[0][0] = 2 / (t1 - t0);
		yMatrix[0][1] = 1 / (t1 - t0);
		yVector[0] = 3 * (y1 - y0) / ( (t1-t0)*(t1-t0) );
		
		//boundary condition at end
		float xn = wayPoints[3*(numWP-1)];
		float yn = wayPoints[3*(numWP-1) + 1];
		float tn = wayPoints[3*(numWP-1) + 2];
		
		float xnm = wayPoints[3*(numWP-2)];
		float ynm = wayPoints[3*(numWP-2) + 1];
		float tnm = wayPoints[3*(numWP-2) + 2];
		
		xMatrix[numWP-1][numWP-2]   = 1 / (tn - tnm);
		xMatrix[numWP-1][numWP-1]   = 2 / (tn - tnm);
		xVector[numWP-1] = 3 * (xn - xnm) / ( (tn-tnm)*(tn-tnm) );
		
		yMatrix[numWP-1][numWP-2]   = 1 / (tn - tnm);
		yMatrix[numWP-1][numWP-1]   = 2 / (tn - tnm);
		yVector[numWP-1] = 3 * (yn - ynm) / ( (tn-tnm)*(tn-tnm) );
		
		dxdtAtWP = LinearSystem.solveLinearSystem(xMatrix, xVector);
		dydtAtWP = LinearSystem.solveLinearSystem(yMatrix, yVector);
	}
	
}
