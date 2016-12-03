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
public class ZtAvgHour extends BATS
{

        public ZtAvgHour(String name)
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
                Algorithm algo = new Algorithm("AvgHour.csv")
                {
                        
                        @Override
                        public Decision startOfSlot(int timeSlotNo)
                        {
                                return null;
                        }
                };
                algo.setParameters(resourceProvisioner, workloadPredictor, workload, budget, runDelay, outputFileName);
                
                hourlyBudget = algo.getHourlyBudget(budget);

        }





}
