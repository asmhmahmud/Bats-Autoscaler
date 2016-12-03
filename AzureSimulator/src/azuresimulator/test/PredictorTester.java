/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.test;

import azuresimulator.predictor.ErrorPredictor;
import azuresimulator.predictor.WikiWorkloadPredictor;

/**
 *
 * @author Hasan
 */
public class PredictorTester
{
        public static void main(String args[])
        {
                ErrorPredictor workLoadPred = new ErrorPredictor();
                
                for (int i = 0; i < 100; i++)
                {
                        workLoadPred.addSample(i);
                }
                
                workLoadPred.setErrorPercentage(20);
                
                for (int i = 0; i < 100; i++)
                {
                        System.out.println(""+workLoadPred.getPredictedValue(i));
                }
                
                
                
                
                
        }
        
}
