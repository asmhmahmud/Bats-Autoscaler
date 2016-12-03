/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.hetrogeneous.vm;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;

/**
 *
 * @author amahm008
 */



public class HeterogenVM extends Vm
{
        private VMType vmType;

        
        public HeterogenVM(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler)
        {
                super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
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
