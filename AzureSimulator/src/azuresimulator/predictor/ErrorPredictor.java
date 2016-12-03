/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.predictor;

import java.util.Random;

/**
 *
 * @author Hasan
 */
public class ErrorPredictor extends Predictor
{
        private double error = 0;
        Random rnd = new Random(12345);
        double max = Double.MAX_VALUE, min = 1;
        
        public  ErrorPredictor()
        {
                super();
        }
        
        /**
         *
         * @param max
         * @param min
         * @param error in percentage
         */
        public void setParams(double max , double min, double error)
        {
                this.max = max;
                this.min = min;
                this.error = error;
        }
        double maxError = 0;
        @Override
        public double getPredictedValue(int slotNo)
        {
                double sample = sampleData.get(slotNo);
                sample = sample * ( (1- error/100) + 2 *  rnd.nextDouble() * error/100);
                
                sample = Math.max(sample, min);
                sample = Math.min(sample, max);
                
                maxError = Math.max(maxError, (sampleData.get(slotNo) - sample)/sampleData.get(slotNo)*100);
                
                //System.out.println("Max Error: "+maxError);
                
                
                return sample;
        }

        /**
         * @return the error
         */
        public double getError()
        {
                return error;
        }

        /**
         * @param error the error to set
         */
        public void setErrorPercentage(double error)
        {
                this.error = error;
        }
        
}
