/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.experiments.zt;

import azuresimulator.algorithms.*;
import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.predictor.Predictor;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.statistics.ExpInput;
import java.util.ArrayList;

/**
 *
 * @author Hasan
 */
public class ZtAvgRemaining extends BATS
{

        public ZtAvgRemaining(String name)
        {
                super(name);

        }

        @Override
        public void setParameters(
                ResourceProvisioner resourceProvisioner,
                Predictor workloadPredictor,
                ArrayList<ExpInput> workload,
                double budget,
                RUBiSRuntimeDelay runDelay,
                String outputFileName
        )
        {
                super.setParameters(resourceProvisioner, workloadPredictor, workload, budget, runDelay, outputFileName);
                hourlyBudget = getHourlyBudget(budget);
                totalCostSoFar = new double [hourlyBudget.length] ;

        }

        private double [] totalCostSoFar ;

        @Override
        public Decision startOfSlot(int slotNo)
        {
                Decision decission = super.startOfSlot(slotNo);

                totalCostSoFar[slotNo] = VMType.EXTRA_SMALL.getPrice() * decission.getVmNumbers().getTotalVM();

                if (slotNo < hourlyBudget.length - 1)
                {
                        hourlyBudget[slotNo + 1] = (budget - getSum(totalCostSoFar)) / (hourlyBudget.length - slotNo - 1);
                }

                return decission;

        }
        
        
        
        
        
        private double getSum(double [] allVal)
        {
                double total = 0;
                for (int i = 0; i < allVal.length; i++)
                {
                        total += allVal[i];
                }
                return total;
        }

        @Override
        public double[] getHourlyBudget(double totalBudget)
        {
                int totalHour = workload.size();
                double[] dividedBudget = new double[totalHour];
                double perSlotBudget = budget / getTotalTimeSlots();

                for (int i = 0; i < dividedBudget.length; i++)
                {
                        dividedBudget[i] = perSlotBudget;
                }
                
                return dividedBudget;

        }

}
