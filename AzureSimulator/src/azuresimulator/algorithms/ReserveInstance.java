/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.algorithms;

import azuresimulator.Settings;
import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.statistics.ExpInput;

/**
 *
 * @author amahm008
 */
public class ReserveInstance extends BATS
{

        private VMNumbers reserveInstances;

        public ReserveInstance(String name)
        {
                super(name);

        }

        public VMNumbers getReserveInstances()
        {
                return reserveInstances;
        }

        private double getOptimalV()
        {

                int currentT;

                double budDeficit;
                double lhs = 0.001, rhs = 10000;
                double curV = Double.MAX_VALUE;
                
                int iteration = 0;
                int maxIteration = 100;

                budDeficit = Double.MAX_VALUE;
                

                while ((Math.abs(budDeficit) > .001) && (iteration < maxIteration))
                {

                        curV = (lhs + rhs) / 2;
                        budDeficit = 0;
                        resetQueues();
                        setV(curV);
                        int totalTimeSlots = workload.size();
                        //System.out.println("++++++++++++++++++++++++++++++"+totalTimeSlots);
                        for (currentT = 0; currentT < totalTimeSlots; currentT++)
                        {
                                ExpInput expInput = new ExpInput();
                                expInput.noOfClients = (int) workloadPredictor.getPredictedValue(currentT);
                                expInput.globalSlotNo = currentT;
                                expInput.V = getV();
                                expInput.vSlotNo = currentT;
                                Decision decision = new Decision(expInput);

                                super.optimizeDecission(currentT, decision, (int) expInput.noOfClients);
                                budDeficit = budDeficit + decision.getTotalCost() - hourlyBudget[currentT];

                        }

                        if (budDeficit < 0)
                        {
                                lhs = curV;
                        }

                        else
                        {
                                rhs = curV;
                        }
                        iteration++;
                        //System.out.println("Budget Deficit: "+budDeficit);
                }

                resetQueues();
                //System.out.println("CurV: "+curV);
                return curV;
        }

        public void setReserveInstances(VMNumbers reserveInstances)
        {
                this.reserveInstances = reserveInstances;
                this.setV(getOptimalV());
        }

        private double getVMTotalCost(Decision decission)
        {

                return getVMTotalCost(decission.getVmNumbers().getTotalVM());
        }

        private double getVMTotalCost(int extraSmallInstance)
        {
                double vmCost = VMType.EXTRA_SMALL.getPrice() * (extraSmallInstance - reserveInstances.getTotalVM())
                        + VMType.EXTRA_SMALL.getPrice() * Settings.RESERVE_INSTANCE_DISCOUNT * reserveInstances.getTotalVM();

                return vmCost;
        }

        @Override
        protected void optimizeDecission(int slotNo, Decision decission, int currentWorkload)
        {

                getOptimalVMInstances(currentWorkload, queueLength[slotNo], V, decission);

                if (fixedQueueLength)
                {
                        queueLength[slotNo + 1] = fixedQueueLengthValue;
                }
                else
                {

                        queueLength[slotNo + 1] = Math.max(0, queueLength[slotNo] + getVMTotalCost(decission) - hourlyBudget[slotNo]);
                        //System.out.println("Queue Length: "+hourlyBudget[slotNo]);
                }
                //System.out.println("Slot no: " + slotNo + ",  VM: " + decission.getVMDecisionNumbers()+",   Hourly Bud: "+hourlyBudget[slotNo]);
                //System.out.println("Queue Length:++++++ "+fixedQueueLengthValue);
                totalVM = totalVM + decission.getVmNumbers().getTotalVM();

        }

        @Override
        protected void getOptimalVMInstances(int currentWorkload, double currentQueueLength, double V, Decision decission)
        {
                //System.out.println("In Reserve Opt Instances");
                double currentVal, optVal = Double.MAX_VALUE, delay = Double.MAX_VALUE;
                double optV = 0, optPrice = 0;

                delay = runDelay.getDelay(Settings.MAX_VM, currentWorkload, VMType.EXTRA_SMALL);
                decission.addVMNumberDecission(VMType.EXTRA_SMALL, Settings.MAX_VM);
                decission.setExpectedDelay(delay);

                for (int currentVM = Math.max(1, reserveInstances.getTotalVM()); currentVM <= Settings.MAX_VM; currentVM++)
                {
                        delay = runDelay.getDelay(currentVM, currentWorkload, VMType.EXTRA_SMALL);

                        currentVal = V * delay + currentQueueLength * getVMTotalCost(currentVM);

                        if (currentVal < optVal)
                        {
                                optVal = currentVal;
                                decission.addVMNumberDecission(VMType.EXTRA_SMALL, currentVM);
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
                                delay = runDelay.getDelay(currentVM, currentWorkload, VMType.EXTRA_SMALL);

                                if (delay < Settings.MAX_DELAY)
                                {
                                        decission.addVMNumberDecission(VMType.EXTRA_SMALL, currentVM);
                                        decission.setExpectedDelay(delay);
//                                        optV = V * delay;
//                                        optPrice = currentQueueLength * unitVMPrice * currentVM;

                                        break;
                                }

                        }
                }

                //System.out.println("Delay: "+delay);
                //DecimalFormat dft = new DecimalFormat("###.###");
                //System.out.println("V: "+ dft.format(optV) + ",  Pr: "+dft.format(optPrice));
        }

}
