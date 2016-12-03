/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.algorithms;

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
public class BATS extends Algorithm
{

        //Azure Sim Validation Settings
        protected double V ;

        //private double V = 2.75;
        protected double[] queueLength;
        protected double[] hourlyBudget;
        protected boolean fixedQueueLength = false;
        protected double fixedQueueLengthValue = 0;
        protected VMType vmType;
        protected ReactiveFeedBack reactiveFeedback;

        public BATS(String name)
        {
                super(name);
                V = Settings.STANDARD_V;
                vmType = VMType.EXTRA_SMALL;

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
                queueLength = new double[workload.size() + 1];
                hourlyBudget = getHourlyBudget(budget);
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

                return startProactiveSlot(slotNo);

        }

        private Decision startProactiveSlot(int slotNo)
        {
                ExpInput expInput = new ExpInput();
                expInput.noOfClients = (int) workloadPredictor.getPredictedValue(slotNo);
                System.out.println("Actual Workload: " + workload.get(slotNo).noOfClients + ",  Predicted: " + expInput.noOfClients);
                //expInput.noOfClients =100;
                expInput.globalSlotNo = slotNo;
                expInput.V = getV();
                expInput.vSlotNo = slotNo;

                ReactiveDecision decission = new ReactiveDecision(expInput);

                optimizeDecission(slotNo, decission, (int) expInput.noOfClients);

                resourceProvisioner.updateResourceProvision(decission,false);
                System.out.println("VM: " + decission.getVmNumbers().getTotalVM());

                send(getId(), Settings.REACTIVE_FEEDBACK_DELAY, ExperimentTags.REACTIVE_FEEDBACK, decission);

                return decission;
        }


        private Decision startReactiveSlot(int slotNo, ReactiveDecision reactiveDecision)
        {
                ExpInput expInput = new ExpInput();
                expInput.noOfClients = (int) reactiveFeedback.getReactiveValue(slotNo);

                double initDelay = 0;
                double fraction = 0;

                if (slotNo > 0)
                {
                        initDelay = runDelay.getDelay(reactiveDecision.getVmNumbers().getTotalVM(), (int) expInput.noOfClients, vmType);
                        fraction = 1.0 * (Settings.REACTIVE_FEEDBACK_DELAY + Settings.VM_CREATE_DELAY) / (double) Settings.TIME_SLOT_LENGTH;
                }
                else
                {
                        fraction = 1.0 * (Settings.REACTIVE_FEEDBACK_DELAY ) / (double) Settings.TIME_SLOT_LENGTH;
                }

                reactiveDecision.setReactiveDecision(new Decision(expInput));
                optimizeDecission(slotNo, reactiveDecision, (int) expInput.noOfClients);
                resourceProvisioner.updateResourceProvision(reactiveDecision,true);

                double currDelay = reactiveDecision.getExpectedDelay();                
                double totalExpectedDelay = initDelay * fraction + currDelay * (1 - fraction);
                reactiveDecision.setTotalExpectedDelay(totalExpectedDelay);
                
                System.out.println("Queue Length: " + queueLength[slotNo]);
                return reactiveDecision;
        }

        protected void optimizeDecission(int slotNo, Decision decission, int currentWorkload)
        {

                getOptimalVMInstances(currentWorkload, queueLength[slotNo], V, decission);

                if (fixedQueueLength)
                {
                        queueLength[slotNo + 1] = fixedQueueLengthValue;
                }
                else
                {
                        
                        queueLength[slotNo + 1] = Math.max(0, queueLength[slotNo] + vmType.getPrice() * decission.getVmNumbers().getTotalVM() - hourlyBudget[slotNo]);
                        //System.out.println("Queue Length: "+hourlyBudget[slotNo]);
                }
                //System.out.println("Slot no: " + slotNo + ",  VM: " + decission.getVMDecisionNumbers()+",   Hourly Bud: "+hourlyBudget[slotNo]);
                //System.out.println("Queue Length:++++++ "+fixedQueueLengthValue);
                totalVM = totalVM + decission.getVmNumbers().getTotalVM();

        }

        protected void getOptimalVMInstances(int currentWorkload, double currentQueueLength, double V, Decision decission)
        {
                double currentVal, optVal = Double.MAX_VALUE, delay = Double.MAX_VALUE;
                double optV = 0, optPrice = 0;
                
                delay = runDelay.getDelay(Settings.MAX_VM, currentWorkload, vmType);
                decission.addVMNumberDecission(vmType, Settings.MAX_VM);
                //System.out.println("Expct Delay1: "+delay);
                decission.setExpectedDelay(delay);

                for (int currentVM = 1; currentVM <= Settings.MAX_VM; currentVM++)
                {
                        delay = runDelay.getDelay(currentVM, currentWorkload, vmType);

                        currentVal = V * delay + currentQueueLength * vmType.getPrice() * currentVM;
                        //System.out.println("Cur VM: "+currentVM + ",   del: "+delay);

                        if (currentVal < optVal)
                        {
                                optVal = currentVal;
                                decission.addVMNumberDecission(vmType, currentVM);
                                //System.out.println("Expct Delay2: "+delay);
                                decission.setExpectedDelay(delay);

//                                optV = V * delay;
//                                optPrice = currentQueueLength * unitVMPrice * currentVM;                                
                        }

                        if (delay < Settings.MIN_DELAY)
                        {
                                //System.out.println("Breaking VM: "+currentVM);
                                //System.out.println("Delay: "+delay);
                                break;
                        }
                        

                }

                if (decission.getExpectedDelay() > Settings.MAX_DELAY)
                {
                        for (int currentVM = decission.getVmNumbers().getTotalVM() + 1; currentVM < Settings.MAX_VM; currentVM++)
                        {
                                delay = runDelay.getDelay(currentVM, currentWorkload, vmType);

                                if (delay < Settings.MAX_DELAY)
                                {
                                        decission.addVMNumberDecission(vmType, currentVM);
                                        decission.setExpectedDelay(delay);
                                        break;
                                }

                        }
                }
                
                //System.out.println("Delay: "+delay);

                //DecimalFormat dft = new DecimalFormat("###.###");
                //System.out.println("V: "+ dft.format(optV) + ",  Pr: "+dft.format(optPrice));
                

        }

        protected void resetQueues()
        {
                for (int i = 0; i < queueLength.length; i++)
                {
                        queueLength[i] = 0;
                }
        }

        @Override
        public void simulationFinished()
        {
                System.out.println("Total VM: " + totalVM);
                System.out.println("Cost: " + totalVM * 0.02);
        }

        public double getV()
        {
                return V;
        }

        public void setV(double V)
        {
                this.V = V;
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

        public double getFixedQueueLengthValue()
        {
                return fixedQueueLengthValue;
        }

        public void setFixedQueueLengthValue(double fixedQueueLengthValue)
        {
                
                this.fixedQueueLength = true;
                this.fixedQueueLengthValue = fixedQueueLengthValue;
        }

        @Override
        public double[] getHourlyBudget(double totalBudget)
        {

                double dailyRefBudInc = Settings.BUDGET_INCREMENT; //in percentage
                int totalHour = workload.size();
                int noOfHourInDays = 24;
                int noOfDays = (int) (totalHour/noOfHourInDays);
                double[] dividedBudget = new double[totalHour];
                int extraHour = totalHour-noOfDays*noOfHourInDays;
                double budPercInc=1;
                
                for (int i = 0; i < noOfDays; i++)
                {
                        
                        //System.out.println(budPercInc);
                        for (int j = 0; j < noOfHourInDays; j++)
                        {
                                dividedBudget[i*noOfHourInDays+j] =budPercInc;
                        }
                        budPercInc = 1+(dailyRefBudInc* (i+1)/100 );
                }
                for (int i = 0; i < extraHour; i++)
                {
                        dividedBudget [noOfDays*noOfHourInDays +i] = budPercInc;
                }
                
                double totalFracBudget = 0;
                for (int i = 0; i < dividedBudget.length; i++)
                {
                        totalFracBudget += dividedBudget[i];
                }

                for (int i = 0; i < dividedBudget.length; i++)
                {
                        dividedBudget[i] = dividedBudget[i] / totalFracBudget * totalBudget;
                }

                return dividedBudget;

        }
        
        protected double getTotalBudget(int timeSlotNo)
        {
                double totalBudget = 0;
                for (int i = 0; i < hourlyBudget.length && i <= timeSlotNo; i++)
                {
                        totalBudget = totalBudget + hourlyBudget[i];
                }
                return totalBudget;
        }

        /**
         * @return the vmType
         */
        public VMType getVmType()
        {
                return vmType;
        }

        /**
         * @param vmType the vmType to set
         */
        public void setVmType(VMType vmType)
        {
                this.vmType = vmType;
        }

}
