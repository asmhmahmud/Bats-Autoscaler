/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.benchmark;

import azuresimulator.algorithms.*;
import azuresimulator.ExperimentTags;
import azuresimulator.Settings;
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
public class ReactiveAlgorithm extends Algorithm
{

        private ReactiveFeedBack reactiveFeedback;

        public ReactiveAlgorithm(String name)
        {
                super(name);

        }

        public void setParameters(
                ResourceProvisioner resourceProvisioner,
                Predictor workloadPredictor,
                ArrayList<ExpInput> workload,
                double budget,
                RUBiSRuntimeDelay runDelay,
                String outputFileName)
        {
                super.setParameters(resourceProvisioner, workloadPredictor, workload, budget, runDelay, outputFileName);
                reactiveFeedback = new ReactiveFeedBack(workload);

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
                expInput.noOfClients = (int) workloadPredictor.getPredictedValue(slotNo);
                //expInput.noOfClients =100;
                expInput.globalSlotNo = slotNo;
                expInput.vSlotNo = slotNo;

                ReactiveDecision decission ;

                if (slotNo == 0)
                {
                        decission = startProactiveSlot(slotNo);
                }
                else
                {
                        decission = new ReactiveDecision(expInput);
                }

                send(getId(), Settings.REACTIVE_FEEDBACK_DELAY, ExperimentTags.REACTIVE_FEEDBACK, decission);

                return decission;

        }

        private ReactiveDecision startProactiveSlot(int slotNo)
        {
                ExpInput expInput = new ExpInput();
                expInput.noOfClients = (int) workloadPredictor.getPredictedValue(slotNo);
                System.out.println("Actual Workload: " + workload.get(slotNo).noOfClients + ",  Predicted: " + expInput.noOfClients);
                //expInput.noOfClients =100;
                expInput.globalSlotNo = slotNo;
                expInput.vSlotNo = slotNo;

                ReactiveDecision decission = new ReactiveDecision(expInput);

                setOptimalVMInstances((int) expInput.noOfClients, decission);

                resourceProvisioner.updateResourceProvision(decission, false);
                System.out.println("VM: " + decission.getVmNumbers().getTotalVM());

                send(getId(), Settings.REACTIVE_FEEDBACK_DELAY, ExperimentTags.REACTIVE_FEEDBACK, decission);

                return decission;
        }

        private Decision lastSlotDecission;

        private Decision startReactiveSlot(int slotNo, ReactiveDecision reactiveDecision)
        {
                ExpInput expInput = new ExpInput();
                expInput.noOfClients = (int) reactiveFeedback.getReactiveValue(slotNo);

                reactiveDecision.setReactiveDecision(new Decision(expInput));

                setOptimalVMInstances((int) expInput.noOfClients, reactiveDecision);

                resourceProvisioner.updateResourceProvision(reactiveDecision, true);

                double initDelay = 0;

                if (slotNo > 0)
                {
                        initDelay = runDelay.getDelay(lastSlotDecission.getVmNumbers().getTotalVM(), (int) expInput.noOfClients, VMType.EXTRA_SMALL);
                }

                double currDelay = reactiveDecision.getExpectedDelay();
                double fraction = 1.0 * (Settings.REACTIVE_FEEDBACK_DELAY + Settings.VM_CREATE_DELAY) / (double) Settings.TIME_SLOT_LENGTH;

                double totalExpectedDelay = initDelay * fraction + currDelay * (1 - fraction);
                reactiveDecision.setTotalExpectedDelay(totalExpectedDelay);

                //resourceProvisioner.updateResourceProvision(reactiveDecision);             
                lastSlotDecission = reactiveDecision;
                return reactiveDecision;
        }

        private void setOptimalVMInstances(int currentWorkload, Decision decission)
        {
                double delay;
                delay = runDelay.getDelay(Settings.MAX_VM, currentWorkload, VMType.EXTRA_SMALL);
                decission.addVMNumberDecission(VMType.EXTRA_SMALL, Settings.MAX_VM);
                decission.setExpectedDelay(delay);

                for (int currentVM = 1; currentVM <= Settings.MAX_VM; currentVM++)
                {
                        delay = runDelay.getDelay(currentVM, currentWorkload, VMType.EXTRA_SMALL);

                        if (delay < Settings.MIN_DELAY)
                        {
                                decission.addVMNumberDecission(VMType.EXTRA_SMALL, currentVM);
                                decission.setExpectedDelay(delay);
                                break;
                        }

                }

        }

        @Override
        public void simulationFinished()
        {
                System.out.println("Total VM: " + totalVM);
                System.out.println("Cost: " + totalVM * 0.02);
        }

        @Override
        public void processEvent(SimEvent ev)
        {
                //System.out.println("Got Process Event:"+ev.getTag());
                if (ev.getTag() == ExperimentTags.REACTIVE_FEEDBACK)
                {
                        //System.out.println("Received ReactiveFeedBack Feedback Delay");
                        ReactiveDecision rcDec = (ReactiveDecision) ev.getData();
                        startReactiveSlot(rcDec.expInput.vSlotNo, rcDec);
                }
        }
}
