/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.algorithms;

import edu.fiu.scg.util.MLab;

/**
 *
 * @author amahm008
 */
public class LoadDistribution
{
        public double totalLoad;
        public double [] loadFractions;
        public double [] delayFractions;
        
        public double getTotalDelay()
        {
                double delay = MLab.sum(MLab.mulR(loadFractions, delayFractions));
                
                delay = delay / MLab.sum(loadFractions);
                
                return delay;
        }
        
}
