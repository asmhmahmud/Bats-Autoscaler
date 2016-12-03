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

/**
 *
 * @author amahm008
 */
public class VMNumbers
{

        private int[] vmCount = new int[VMType.totalVMType()];
        
        public VMNumbers()
        {
                
        }

        public static String getVMNumbersHeader()
        {

                String vmNumbers = VMType.getVMTypeByTypeIndex(0).toString();
                for (int i = 1; i < VMType.totalVMType(); i++)
                {
                        vmNumbers += "," + VMType.getVMTypeByTypeIndex(i).toString();
                }

                return vmNumbers;
        }
        protected String getVMDecisionNumbers()
        {
                String vmNumbersString = "" + vmCount[0];

                for (int i = 1; i < vmCount.length; i++)
                {
                        vmNumbersString += "," + vmCount[i];
                }

                return vmNumbersString;
        }

        public void addVMNumberDecission(VMType vmType, int number)
        {
                vmCount[VMType.getVMTypeIndexByVMType(vmType)] = number;

        }

        public int[] getVMNumbers()
        {
                return vmCount;
        }

        public void clearVMNumbers()
        {
                for (int i = 0; i < vmCount.length; i++)
                {
                        vmCount[i] = 0;
                }
        }

        public int getTotalVM()
        {

                int totalVM = 0;
                for (int i = 0; i < vmCount.length; i++)
                {
                        totalVM += vmCount[i];
                }
                return totalVM;

        }

        public int getTotalVM(VMType vMType)
        {

                return vmCount[VMType.getVMTypeIndexByVMType(vMType)];

        }

        public double getTotalCost()
        {
                double totalCost = 0;
                for (int i = 0; i < vmCount.length; i++)
                {
                        totalCost = totalCost + vmCount[i] * VMType.getVMTypeByTypeIndex(i).getPrice();
                }

                return totalCost;

        }
        
        public VMType getNonZeroVMType()
        {
                int typeIndex =0;
                for (int i = 0; i < 10; i++)
                {
                        if(vmCount[i] != 0)
                        {
                                typeIndex = i;
                                break;
                        }
                }
                
                return VMType.getVMTypeByTypeIndex(typeIndex);
        }
}
