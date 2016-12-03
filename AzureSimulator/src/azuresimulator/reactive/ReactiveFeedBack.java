/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.reactive;

import azuresimulator.statistics.ExpInput;
import java.util.ArrayList;

/**
 *
 * @author Hasan
 */
public class ReactiveFeedBack
{
        protected ArrayList<Double> sampleData ;
        
        
        public ReactiveFeedBack(ArrayList<ExpInput> workload)
        {
                sampleData = new ArrayList<>();
                
                for (int i = 0; i < workload.size(); i++)
                {
                        sampleData.add(workload.get(i).noOfClients);
                }
        }
        
        public void addSample(double sampleValue)
        {
                sampleData.add(sampleValue);
        }
        
        public  double getReactiveValue(int slotNo)
        {
                return sampleData.get(slotNo);
        }
        
}
