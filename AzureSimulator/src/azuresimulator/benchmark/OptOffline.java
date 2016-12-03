/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.benchmark;

import azuresimulator.algorithms.*;
import azuresimulator.predictor.Predictor;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.statistics.ExpInput;
import java.util.ArrayList;

/**
 *
 * @author Hasan
 */
public class OptOffline extends BATS
{

        public OptOffline(String name)
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
                
                double optimalQueL = getOptimalQueueLength();
                setFixedQueueLengthValue(optimalQueL);
                resetQueues();
                System.out.println("Optimal Queue Length: *****************"+optimalQueL);

        }

        private double getOptimalQueueLength()
        {
               
                int currentT;
                
                double budDeficit ;
                double lhs = 0, rhs = 1000;
                double fixedQueueLengthTemp = Double.MAX_VALUE;
                int iteration = 0;
                int maxIteration = 100;

                budDeficit = Double.MAX_VALUE;
                
                while ( (Math.abs(budDeficit) > .001 )&& ( iteration < maxIteration))
                {
                        
                        fixedQueueLengthTemp = (lhs + rhs) / 2;
                        budDeficit = 0;
                        resetQueues();
                        setFixedQueueLengthValue(fixedQueueLengthTemp);
                        for (currentT = 0; currentT < getTotalTimeSlots() ; currentT++)
                        {
                                ExpInput expInput = new ExpInput();
                                expInput.noOfClients = (int) workloadPredictor.getPredictedValue(currentT);
                                expInput.globalSlotNo = currentT;
                                expInput.V = getV();
                                expInput.vSlotNo = currentT;
                                Decision decision  = new Decision(expInput);
                                
                                 super.optimizeDecission(currentT,decision, (int)expInput.noOfClients);
                                budDeficit = budDeficit + decision.getTotalCost() - hourlyBudget[currentT];

                        }

                        if (budDeficit > 0)
                        {
                                lhs = fixedQueueLengthTemp;
                        }

                        else
                        {
                                rhs = fixedQueueLengthTemp;
                        }
                        iteration++;
                        //System.out.println("Budget Deficit: "+budDeficit);
                }

                
                resetQueues();
                return fixedQueueLengthTemp;
        }

        @Override
        public Decision startOfSlot(int slotNo)
        {

                return super.startOfSlot(slotNo);

        }

}
