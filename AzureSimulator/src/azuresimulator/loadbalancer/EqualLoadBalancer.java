/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.loadbalancer;

import azuresimulator.AzureCloudlet;
import azuresimulator.hetrogeneous.vm.HeterogenVM;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSimTags;

/**
 *
 * @author Hasan
 */
public class EqualLoadBalancer extends LoadBalancer
{
        
        private int nextVMToBeScheduled = 0;


        @Override
        public void scheduleCloudlets(List<? extends Cloudlet> cloudLets)
        {
                
                if (vmList.isEmpty())
                {
                        return;
                }
                
                for (Cloudlet cloudlet : cloudLets)
                {
                        scheduleCloudlet(cloudlet);                        
                        
                }
                cloudLets.clear();
                
        }
        
        public void scheduleCloudlet(Cloudlet cloudlet)
        {
                nextVMToBeScheduled = (nextVMToBeScheduled + 1) % vmList.size();
                Vm vm;
                vm = vmList.get(nextVMToBeScheduled);

//                        Log.printLine(CloudSim.clock() + ": " + getName() + ": Sending cloudlet "
//                                + cloudlet.getCloudletId() + " to VM #" + vm.getId());
                cloudlet.setVmId(vm.getId());
                getLbFeedback().dispatchCloudlet(cloudlet);
                
        }
        
        @Override
        public void reset()
        {
                nextVMToBeScheduled = 0;
        }
        
}
