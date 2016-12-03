/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.algorithms;

import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.statistics.ExpInput;

/**
 *
 * @author Hasan
 */
public class Decision
{

        public ExpInput expInput;
        protected double expectedDelay;
        private VMNumbers vmNumbers;
        LoadDistribution ldDist;

        public String getCSVHeader()
        {
                return ExpInput.getCSVHeader() + ","
                        + VMNumbers.getVMNumbersHeader() + ","
                        + "Expct Delay";
        }

        public String getCSVFormattedData()
        {
                //System.out.println(expInfo.globalSlotNo);
                return expInput.getCSVFormattedData() + ","
                        + getVmNumbers().getVMDecisionNumbers() + ","
                        + expectedDelay;
        }

        public void addVMNumberDecission(VMType vmType, int number)
        {
                vmNumbers.addVMNumberDecission(vmType, number);

        }



//        public int [] getVMNumbers()
//        {
//                return vmDecission.getVMNumbers();
//        }
        public Decision(ExpInput expInput)
        {
                this.expInput = expInput;
                vmNumbers = new VMNumbers();
                vmNumbers.clearVMNumbers();
                
        }

        public ExpInput getExpInput()
        {
                return expInput;
        }

        public void setExpInput(ExpInput expInput)
        {
                this.expInput = expInput;
        }

        /**
         * @return the expectedDelay
         */
        public double getExpectedDelay()
        {
                return expectedDelay;
        }

        /**
         * @param expectedDelay the expectedDelay to set
         */
        public void setExpectedDelay(double expectedDelay)
        {
                this.expectedDelay = expectedDelay;
        }

        public double getTotalCost()
        {
                return getVmNumbers().getTotalCost();

        }

        public VMNumbers getVmNumbers()
        {
                return vmNumbers;
        }

        public void setVmNumbers(VMNumbers vmDecission)
        {
                this.vmNumbers = vmDecission;
        }

}
