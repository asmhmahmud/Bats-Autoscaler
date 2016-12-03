/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator;

import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;

/**
 *
 * @author Hasan
 */
public class AzureCloudletScheduler extends CloudletSchedulerSpaceShared
{

        public AzureCloudletScheduler()
        {
                super();
        }

//        public void cancelAllCloudLets()
//        {
//
//
//                
//                List <ResCloudlet> currentList;
//                ResCloudlet rcl;
//                
//                
//                
//                currentList = getCloudletFinishedList();
//                for (int i = 0; i < currentList.size(); i++)
//                {
//                        rcl = currentList.get(i);
//                        rcl.setCloudletStatus(Cloudlet.CANCELED);
//                }
//                currentList.clear();
//                currentList = getCloudletWaitingList();
//                for (int i = 0; i < currentList.size(); i++)
//                {
//                        rcl = currentList.get(i);
//                        rcl.setCloudletStatus(Cloudlet.CANCELED);
//                }
//                currentList.clear();
//
////                // Now, looks in the paused queue
////                for (ResCloudlet rcl : getCloudletPausedList())
////                {
////                        if (rcl.getCloudletId() == cloudletId)
////                        {
////                                getCloudletPausedList().remove(rcl);
////                                return rcl.getCloudlet();
////                        }
////                }
////
////                // Finally, looks in the waiting list
////                for (ResCloudlet rcl : getCloudletWaitingList())
////                {
////                        if (rcl.getCloudletId() == cloudletId)
////                        {
////                                rcl.setCloudletStatus(Cloudlet.CANCELED);
////                                getCloudletWaitingList().remove(rcl);
////                                return rcl.getCloudlet();
////                        }
////                }
//        }
}
