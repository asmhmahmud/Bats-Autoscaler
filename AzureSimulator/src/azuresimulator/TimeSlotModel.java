/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator;

import azuresimulator.algorithms.Algorithm;
import azuresimulator.algorithms.Decision;
import azuresimulator.loadbalancer.LoadBalancer;
import azuresimulator.statistics.ExpResult;
import azuresimulator.util.CommonUtil;
import java.util.ArrayList;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

/**
 *
 * @author Hasan
 */
public class TimeSlotModel extends SimEntity
{

        private WorkloadGen workloadGen;
        private Algorithm algorithm;
        private ExpResult currentExpResult;
        private AzureBroker azureBroker;
        private boolean enableJobPrint = false;
        
        
        public TimeSlotModel(String name)
        {
                super(name);
        }

        public void setParameters(WorkloadGen workloadGen, Algorithm algorithm, AzureBroker azureBroker)
        {
                this.workloadGen = workloadGen;
                this.algorithm = algorithm;
                this.azureBroker = azureBroker;
        }
        
        public boolean isEnableJobPrint()
        {
                return enableJobPrint;
        }

        public void setEnableJobPrint(boolean enableJobPrint)
        {
                this.enableJobPrint = enableJobPrint;
        }

        public void jobPrint()
        {
                        CommonUtil.printCloudletList(azureBroker.getCloudletReceivedList());
                        CommonUtil.printStatisticsDetail(azureBroker.getCloudletReceivedList());

        }
        
        long startTime;
        private void processSlotStarted(SimEvent ev)
        {

                startTime = System.currentTimeMillis();
                
                int slotNumber = (Integer) ev.getData();
                
                int internalSlotNo = slotNumber % algorithm.getSlotRepeationPeriod();

                currentExpResult = new ExpResult();
                Decision currentDecission = algorithm.startOfSlot(slotNumber);
                currentExpResult.decission = currentDecission;
                currentExpResult.workloadLevel = workloadGen.getWorkload(internalSlotNo).noOfClients;
                
                ArrayList<AzureCloudlet> azureCloudletList = workloadGen.generateWorkload(internalSlotNo, algorithm.getTimeSlotLength(), CloudSim.clock());
                currentExpResult.submittedJobs = azureCloudletList.size();
                send(getId(), algorithm.getTimeSlotLength(), ExperimentTags.SLOT_ENDED, new Integer(slotNumber));

                //System.out.println("----Slot: "+slotNumber+", Submitted: " + cloudletsSubmitted);
        }

                
                
        
        private void processSlotEnded(SimEvent ev)
        {
                int slotNumber = (Integer) ev.getData();

                //System.out.println(CloudSim.clock() + ",  " + "Slot: " + (slotNumber) + ", Submitted: " + getCloudletSubmittedList().size() + ", Rec: " + getCloudletReceivedList().size());
                azureBroker.setCurrentExpStatistics(currentExpResult, Settings.PERCENTILE_LIST);
                algorithm.saveLastSlotResults(currentExpResult);
                algorithm.endOfSlot(slotNumber, currentExpResult);
                if (enableJobPrint)
                {
                        jobPrint();
                }
                azureBroker.resetBrokerState();
                

                if (slotNumber < algorithm.getTotalTimeSlots() - 1)
                {
                        sendNow(getId(), ExperimentTags.SLOT_STARTED, new Integer(slotNumber + 1));
                }
                else
                {
                        algorithm.simulationFinished();
                        azureBroker.clearDatacenters();
                }
                
                
                
                
                long curTime = (System.currentTimeMillis() - startTime) / 1000;
                System.out.println(
                        "Slot: "+slotNumber+",   Time Taken: "+curTime+"  Sec,  Incurred Delay: "
                        +Settings.dft.format(currentExpResult.getDelayMetric())
                                +",   Expect Delay: "+currentExpResult.decission.getExpectedDelay()
                        +",   Total VM: "+currentExpResult.decission.getVmNumbers().getTotalVM()
                
                );
                System.out.println("*****************************************");
        }

        @Override
        public void startEntity()
        {
                sendNow(getId(), ExperimentTags.SLOT_STARTED, new Integer(0));
        }

        @Override
        public void processEvent(SimEvent ev)
        {
                switch (ev.getTag())
                {
                        // Resource characteristics request
                        case ExperimentTags.SLOT_STARTED:
                                processSlotStarted(ev);
                                break;
                        // Resource characteristics request
                        case ExperimentTags.SLOT_ENDED:
                                processSlotEnded(ev);
                                break;

                        // other unknown tags are processed by this method
                        default:
                                processOtherEvent(ev);
                                break;

                }
        }

        @Override
        public void shutdownEntity()
        {

        }

        protected void processOtherEvent(SimEvent ev)
        {
                if (ev == null)
                {
                        Log.printLine(getName() + ".processOtherEvent(): " + "Error - an event is null.");
                        return;
                }

                Log.printLine(getName() + ".processOtherEvent(): "
                        + "Error - event unknown by this DatacenterBroker.");
        }

}
