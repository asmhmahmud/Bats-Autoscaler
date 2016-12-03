/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.experiments.zt;

import azuresimulator.algorithms.*;
import azuresimulator.predictor.Predictor;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.statistics.ExpInput;
import java.util.ArrayList;

/**
 *
 * @author Hasan
 */
public class ZtAvgEven extends BATS
{

        public ZtAvgEven(String name)
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

        }
        protected int totalVM = 0;



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
