/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.algorithms;

import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.statistics.ExpInput;

/**
 *
 * @author Hasan
 */
public class ReactiveDecision extends Decision
{

        private boolean reactivePartAdded;
        private Decision reactiveDecision;
        
        private double totalExpectedDelay;

        public ReactiveDecision(ExpInput expInput)
        {
                super(expInput);
                reactivePartAdded = false;
        }

        /**
         * @return the reactivePartAdded
         */
        public boolean isReactivePartAdded()
        {
                return reactivePartAdded;
        }

        @Override
        public String getCSVHeader()
        {
                return ExpInput.getCSVHeader() + ","
                        + VMNumbers.getVMNumbersHeader() + ","
                        + "Pro. Expct Delay,"
                        + "Reactive Enabled,"
                        + "Rec. load,"
                        + VMNumbers.getVMNumbersHeader() + ","
                        + "Rec. Expct Delay,"
                        + "Tot Expct Delay";
        }

        @Override
        public String getCSVFormattedData()
        {
                //System.out.println(expInfo.globalSlotNo);
                return expInput.getCSVFormattedData() + ","
                        + super.getVmNumbers().getVMDecisionNumbers() + ","
                        +super.getExpectedDelay()+","
                        + (reactivePartAdded?1:0) + ","
                        +getReactiveWorkloadCount()+","
                        + getVmNumbers().getVMDecisionNumbers()+ ","
                        + getExpectedDelay()+","
                        +getTotalExpectedDelay();
        }
        private double getReactiveWorkloadCount()
        {
                double workload = 0;
                
                if(reactivePartAdded)
                {
                        workload = getReactiveDecision().expInput.noOfClients;
                }
                return  workload;
        }

        @Override
        public ExpInput getExpInput()
        {
                if (reactivePartAdded)
                {
                        return getReactiveDecision().getExpInput();
                }
                else
                {
                        return super.getExpInput();
                }
        }

        @Override
        public void setExpInput(ExpInput expInput)
        {
                if (reactivePartAdded)
                {
                        reactiveDecision.expInput = expInput;
                }
                else
                {
                        this.expInput = expInput;
                }

        }

        /**
         * @return the expectedDelay
         */
        @Override
        public double getExpectedDelay()
        {
                if (reactivePartAdded)
                {
                        return getReactiveDecision().expectedDelay;
                }
                else
                {
                        return expectedDelay;
                }

        }

        /**
         * @param expectedDelay the expectedDelay to set
         */
        @Override
        public void setExpectedDelay(double expectedDelay)
        {
                if (reactivePartAdded)
                {
                        reactiveDecision.expectedDelay = expectedDelay;
                }
                else
                {
                        super.setExpectedDelay(expectedDelay);
                }

        }


        @Override
        public void addVMNumberDecission(VMType vmType, int number)
        {
                if (reactivePartAdded)
                {
                        getReactiveDecision().addVMNumberDecission(vmType, number);
                }
                else
                {
                        super.addVMNumberDecission(vmType, number);
                }

        }

        @Override
        public VMNumbers getVmNumbers()
        {
                if (reactivePartAdded)
                {
                        return getReactiveDecision().getVmNumbers();
                }
                else
                {
                        return super.getVmNumbers();
                }

        }
        








        /**
         * @return the reactiveDecision
         */
        public Decision getReactiveDecision()
        {
                return reactiveDecision;
        }

        /**
         * @param reactiveDecision the reactiveDecision to set
         */
        public void setReactiveDecision(Decision reactiveDecision)
        {
                this.reactiveDecision = reactiveDecision;
                reactivePartAdded = true;
        }

        public double getTotalExpectedDelay()
        {
                return totalExpectedDelay;
        }

        public void setTotalExpectedDelay(double totalExpectedDelay)
        {
                this.totalExpectedDelay = totalExpectedDelay;
        }
        
        @Override
        public double getTotalCost()
        {
                if (reactivePartAdded)
                {
                        return reactiveDecision.getTotalCost();
                }
                else
                {
                        return super.getTotalCost();
                }

        }
                

}
