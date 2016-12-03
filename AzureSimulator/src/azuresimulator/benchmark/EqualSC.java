/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.benchmark;

import azuresimulator.ExperimentTags;
import azuresimulator.Settings;
import azuresimulator.algorithms.Algorithm;
import azuresimulator.algorithms.Decision;
import azuresimulator.algorithms.ReactiveDecision;
import azuresimulator.algorithms.ResourceProvisioner;
import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.predictor.Predictor;
import azuresimulator.reactive.ReactiveFeedBack;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.statistics.ExpInput;
import java.util.ArrayList;
import org.cloudbus.cloudsim.core.SimEvent;

/**
 *
 * @author Hasan
 */
public class EqualSC extends Algorithm
{

        private ReactiveFeedBack reactiveFeedback;

        private int vmPerSlot;
        private double perSlotBudget;
        private double additionalVMs;

        public EqualSC(String name)
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
                reactiveFeedback = new ReactiveFeedBack(workload);
                perSlotBudget = budget / getTotalTimeSlots();
                double instancePrice = VMType.EXTRA_SMALL.getPrice() * Settings.RESERVE_INSTANCE_DISCOUNT;
                
                vmPerSlot = (int) Math.floor(perSlotBudget / instancePrice);

                double additionalBudget = budget - vmPerSlot * getTotalTimeSlots() * instancePrice;
                additionalVMs = (int) Math.floor(additionalBudget / instancePrice);


        }
        protected int totalVM = 0;

        @Override
        public Decision startOfSlot(int slotNo)
        {
                if (slotNo % getDataSavingFrequency() == 0)
                {
                        dtm.saveExperimentResults();
                }
                ExpInput expInput = new ExpInput();
                //expInput.noOfClients = (int) workloadPredictor.getPredictedValue(slotNo);
                expInput.noOfClients = workload.get(slotNo).noOfClients;
                expInput.globalSlotNo = slotNo;
                expInput.vSlotNo = slotNo;

                Decision decission = new Decision(expInput);

                int vms = vmPerSlot;

                if (slotNo < additionalVMs)
                {
                        vms++;
                }

                decission.addVMNumberDecission(VMType.EXTRA_SMALL, vms);

                double delay = runDelay.getDelay(vms, (int) expInput.noOfClients, VMType.EXTRA_SMALL);
                decission.setExpectedDelay(delay);

                resourceProvisioner.updateResourceProvision(decission,false);
                return decission;

        }

}
