/**
* The class <code>Lagrange</code> has methods to interpolate a gap
* in a time series based on some base points using Lagrange polynomials. 
* According to the number of points, the interpolation will be of one, 
* two or three degrees.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package preimputation;

import weka.core.Instances;

public class Lagrange {
    
    /**
     * Interpolates a gap of an attribute.
     * 
     * 
     * @param data dataset
     * @param start start point of a gap
     * @param end end point of a gap
     * @param att attribute to interpolate
     * @param x base points indexes
     * @param fx values of the base points
     */
    public static void interpolate(Instances data, int start, int end, int att, double x[], double fx[]) {
		double eq[] = findEquation(x, fx);
		data.setClassIndex(att);
        
        for(int i = start; i <= end; i++) 
            data.instance(i).setClassValue(calcPoint(eq, data.instance(i).value(0)));
        
    }
    
    /**
     * Calculates the value of a sample based on a equation.
     * 
     * @param eq equation
     * @param x time value - value of the sample in the 
     *          attribute 0 at the same index of the 
     *          value to be estimated
     * @return value of the sample
     */
    public static double calcPoint(double[] eq, double x) {
        double sum = 0.0;
        for(int i = 0; i < eq.length; i++)
            sum += eq[eq.length-i-1]*Math.pow(x,eq.length-i-1); 
        
        return sum;
    }
    
    /**
     * Finds the polynomial that represents a gap.
     * 
     * @param x base points indexes
     * @param y value of the base points
     * @return equation that represents the gap
     */
    public static double[] findEquation(double[] x, double[] y) {
        if((x.length != y.length) || x.length > 4 || x.length < 2)
            System.err.println("Invalid number of points!");
        else {
            if(x.length == 2)
                return inter2pts(x[0],x[1],y[0],y[1]);
            else if(x.length == 3)
                return inter3pts(x[0],x[1],x[2],y[0],y[1],y[2]);
            else
                return inter4pts(x[0],x[1],x[2],x[3],y[0],y[1],y[2],y[3]);
        }
        return null;
    }
    
    /**
     * Apply the Lagrange formula in a set of two points.
     * 
     * @param x0 point 1 index
     * @param x1 point 2 index
     * @param y0 value of point 1
     * @param y1 value of point 2
     * @return 1-degree equation
     */
    public static double[] inter2pts (double x0, double x1, double y0, double y1) {
        double coef[] = new double[2];
        
        coef[1] = y0/(x0-x1) + y1/(x1-x0);
        coef[0] = -x1*y0/(x0-x1) + -x0*y1/(x1-x0);
        return coef;
       
    }
    
    /**
     * Apply the Lagrange formula in a set of three points.
     * 
     * @param x0 point 1 index
     * @param x1 point 2 index
     * @param x2 point 3 index
     * @param y0 value of point 1
     * @param y1 value of point 2
     * @param y2 value of point 3
     * @return 2-degree equation
     */
    public static double[] inter3pts (double x0, double x1, double x2, double y0, double y1, double y2) {
        double coef[] = new double[3];
        
        coef[2] = y0/((x0-x1)*(x0-x2)) + y1/((x1-x0)*(x1-x2)) + y2/((x2-x0)*(x2-x1));
        coef[1] = (-y0*(x2+x1))/((x0-x1)*(x0-x2)) + (-y1*(x2+x0))/((x1-x0)*(x1-x2)) 
                + (-y2*(x1+x0))/((x2-x0)*(x2-x1));
        coef[0] = (x1*x2*y0)/((x0-x1)*(x0-x2)) + (x0*x2*y1)/((x1-x0)*(x1-x2)) 
                + (x0*x1*y2)/((x2-x0)*(x2-x1));
        return coef;
    }
    
    /**
     * Apply the Lagrange formula in a set of four points.
     * 
     * @param x0 point 1 index
     * @param x1 point 2 index
     * @param x2 point 3 index
     * @param x3 point 4 index
     * @param y0 value of point 1
     * @param y1 value of point 2
     * @param y2 value of point 3
     * @param y3 value of point 4
     * @return 2-degree equation
     */
    public static double[] inter4pts (double x0, double x1, double x2, double x3, double y0, double y1, double y2, double y3) {
        double coef[] = new double[4];
        coef[3] = y0/((x0-x1)*(x0-x2)*(x0-x3)) + y1/((x1-x0)*(x1-x2)*(x1-x3)) + y2/((x2-x0)*(x2-x1)*(x2-x3)) 
                + y3/((x3-x0)*(x3-x1)*(x3-x2));
        coef[2] = (-y0*(x2+x1+x3))/((x0-x1)*(x0-x2)*(x0-x3)) + (-y1*(x2+x0+x3))/((x1-x0)*(x1-x2)*(x1-x3)) 
                + (-y2*(x0+x1+x3))/((x2-x0)*(x2-x1)*(x2-x3)) + (-y3*(x2+x1+x0))/((x3-x0)*(x3-x1)*(x3-x2));
        coef[1] = (y0*(x1*x2+x2*x3+x1*x3))/((x0-x1)*(x0-x2)*(x0-x3)) + (y1*(x0*x2+x2*x3+x0*x3))/((x1-x0)*(x1-x2)*(x1-x3)) 
                + (y2*(x1*x0+x0*x3+x1*x3))/((x2-x0)*(x2-x1)*(x2-x3)) + (y3*(x1*x2+x2*x0+x1*x0))/((x3-x0)*(x3-x1)*(x3-x2));
        coef[0] = (-y0*(x1*x2*x3))/((x0-x1)*(x0-x2)*(x0-x3)) + (-y1*(x0*x2*x3))/((x1-x0)*(x1-x2)*(x1-x3)) 
                + (-y2*(x1*x0*x3))/((x2-x0)*(x2-x1)*(x2-x3)) + (-y3*(x1*x2*x0))/((x3-x0)*(x3-x1)*(x3-x2));
        return coef;
    }  
}
