/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.predictor;

/**
 *
 * @author Hasan
 */
public class AccuratePredictor extends Predictor
{

        @Override
        public double getPredictedValue(int slotNo)
        {
                return sampleData.get(slotNo);
        }
        
}
