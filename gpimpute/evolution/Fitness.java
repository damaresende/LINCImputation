/**
* The class <code>Fitness</code> has methods to calculate the fitness
* of a time series. Three metrics are considered: average, variance 
* and autocorrelation. These measurements are later combined in the fitness
* function and the values computed are used to evaluate each individual of the
* genetic programming in the class <code>OneImpute</code>.
* 
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package evolution;

import static java.lang.Math.abs;

public class Fitness {
    
    /**
     * Calculates the average of a time series.
     * 
     * @param timeserie time series
     * @return average
     */
    public static double avg (double[] timeserie) {
        double sum = 0;
        for(double d : timeserie)
            sum += d;
        
        return sum/timeserie.length;
    }
    
    
    /**
     * Calculates the variance of a time series.
     * 
     * @param timeserie time series
     * @param avg average value
     * @return variance
     */
    public static double var (double[] timeserie, double avg) {
        double sum = 0;
        for(double d : timeserie)
            sum += (d - avg)*(d - avg);
        
        return sum/(timeserie.length-1);
    }
    
    /**
     * Calculates the autocovariance of a time series.
     * 
     * @param timeserie time series
     * @param avg average value
     * @param h lag
     * @param n1 minimum index considered
     * @param n2 maximum index considered
     * @return autocorrelation
     */
    public static double acov (double[] timeserie, double avg, int h, int n1, int n2) {
        int lh = abs(h);
        double sum = 0;
        for(int i = n1; i <= (n2 - lh); i++)
            sum += (timeserie[i+lh] - avg)*(timeserie[i] - avg);
        
        return sum/(n2 - n1 + 1);
    }
    
    /**
     * Calculates the fitness of a time series. The fitness consist of
     * computing the difference of the statistical values of a imputed
     * time series and a reference time series. The statistical measurements
     * considered are average, variance and autocorrelation.
     * 
     * 
     * @param lts latest imputed time series
     * @param ats time series to be compared to
     * @param H maximum lag considered
     * @return fitness value
     */
    public static double fitness (double[] lts, double[] ats, int H) {
        double sum = 0;
        double aavg = avg(ats);
        double lavg = avg(lts);
        
        for(int h = 1; h <= H; h++) {
            double lacor = acov(lts,lavg,0,0,lts.length-1);
            double aacor = acov(ats,aavg,0,0,ats.length-1);
            if(lacor == 0 && aacor == 0)
                sum += abs(aavg - lavg) + abs(var(ats,aavg) - var(lts,lavg));
            else if (lacor == 0)
                sum += abs(0 - acov(ats,aavg,h,0,ats.length-1)/aacor) + 
                        abs(aavg - lavg) + abs(var(ats,aavg) - var(lts,lavg));
            else if (aacor == 0)
                sum += abs(acov(lts,lavg,h,0,lts.length-1)/lacor) + 
                        abs(aavg - lavg) + abs(var(ats,aavg) - var(lts,lavg));
            else
                sum += abs(acov(lts,lavg,h,0,lts.length-1)/acov(lts,lavg,0,0,lts.length-1) - 
                        acov(ats,aavg,h,0,ats.length-1)/acov(ats,aavg,0,0,ats.length-1)) + 
                        abs(aavg - lavg) + abs(var(ats,aavg) - var(lts,lavg));
        }
        return sum;
    }
}
