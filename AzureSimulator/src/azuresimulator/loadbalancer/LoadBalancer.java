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
import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSimTags;

/**
 *
 * @author Hasan
 */
abstract public class LoadBalancer
{
        protected List <HeterogenVM> vmList;
        
        protected LoadBalancerFeedback lbFeedback;
        
        public LoadBalancer()
        {
        }
        abstract public void scheduleCloudlet(Cloudlet cloudLet) ;
        abstract public void scheduleCloudlets(List <? extends Cloudlet > cloudLets) ;
        abstract public void reset() ;

        /**
         * @return the vmList
         */
        public List <HeterogenVM> getVmList()
        {
                return vmList;
        }

        /**
         * @param vmList the vmList to set
         */
        public void setVmList(List <? extends Vm > vmList)
        {
                this.vmList =  (List <HeterogenVM >) vmList;
        }

        
        
        
        
//                private void scheduleCloudlets(ArrayList<AzureCloudlet> azureCloudletList, int totalVMs)
//        {
//                //System.out.println("++++ Size: "+cloudletList.size());
//                //System.out.println("Total VM "+totalVMs);
//
//
//
//                if (getVmsCreatedList().isEmpty())
//                {
//                        return;
//                }
//
//                int vmIndex = nextVMToBeScheduled;
//
//                totalVMs = Math.min(totalVMs, getVmsCreatedList().size());
//
//
//                if (vmIndex >= totalVMs)
//                {
//                        vmIndex = 0;
//                }
//
//
//
//                List<AzureCloudlet> cList = getCloudletList();
//                cList.addAll(azureCloudletList);
//                AzureCloudlet cloudlet;
//                double arrivalTime;
//
//                for (int index = 0; index < cList.size(); index++)
//                {
//                        Vm vm;
//                        cloudlet = cList.get(index);
//                        vm = getVmsCreatedList().get(vmIndex);
//                        cloudlet.setVmId(vm.getId());
//
//                        arrivalTime = cloudlet.getArrivalTime();
//
//                        send(getVmsToDatacentersMap().get(vm.getId()), arrivalTime, CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
//                        cloudletsSubmitted++;
//                        vmIndex = (vmIndex + 1) % totalVMs;
//                        getCloudletSubmittedList().add(cloudlet);
//                        //System.out.println("Cloudlet Scheduled: "+ arrivalTime+ ", Cloudlet Length: "+cloudlet.getCloudletLength()/1000.0+", VMID: "+cloudlet.getVmId());
//
//                }
//
//                nextVMToBeScheduled = vmIndex;
//
//                cList.clear();
//
//        }

        /**
         * @return the lbFeedback
         */
        public LoadBalancerFeedback getLbFeedback()
        {
                return lbFeedback;
        }

        /**
         * @param lbFeedback the lbFeedback to set
         */
        public void setLbFeedback(LoadBalancerFeedback lbFeedback)
        {
                this.lbFeedback = lbFeedback;
        }


        
}
