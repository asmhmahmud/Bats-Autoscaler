/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator;

import azuresimulator.algorithms.Decision;
import azuresimulator.algorithms.ResourceProvisioner;
import azuresimulator.hetrogeneous.vm.HeterogenVM;
import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.loadbalancer.LoadBalancer;
import azuresimulator.loadbalancer.LoadBalancerFeedback;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.runtimeDelayTable.RuntimeDelayGenerator;
import azuresimulator.statistics.DetailStat;
import azuresimulator.statistics.ExpResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.VmList;

/**
 *
 * @author Hasan
 */
public class AzureBroker extends DatacenterBroker
        implements LoadBalancerFeedback, ResourceProvisioner
{

        private boolean enableJobScheduling = true;
        private LoadBalancer loadBalancer;
        private RUBiSRuntimeDelay backupRDT;

        public void setParameters(LoadBalancer loadBalancer)
        {
                this.loadBalancer = loadBalancer;
                loadBalancer.setVmList(getVmsCreatedList());
                loadBalancer.setLbFeedback(this);

        }

        public AzureBroker(String name) throws Exception
        {
                super(name);

        }

        @Override
        public void processEvent(SimEvent ev)
        {
                // System.out.println(getId());
                //System.out.println(ev.getTag());
                switch (ev.getTag())
                {
                        // Resource characteristics request
                        case ExperimentTags.CLOUDLET_ARRIVED:
                                cloudLetArrived(ev);
                                //System.out.println("+++++++"+getVmList().size());
                                break;

                        // Resource characteristics request
                        case CloudSimTags.RESOURCE_CHARACTERISTICS_REQUEST:
                                //System.out.println("+++++++"+getVmList().size());
                                processResourceCharacteristicsRequest(ev);
                                break;

                        // Resource characteristics answer
                        case CloudSimTags.RESOURCE_CHARACTERISTICS:
                                //System.out.println("+++++++"+getVmList().size());
                                processResourceCharacteristics(ev);
                                break;

                        // VM Creation answer
                        case CloudSimTags.VM_CREATE_ACK:
                                //System.out.println("+++++++"+getVmList().size());
                                //System.exit(0);
                                processVmCreate(ev);
                                break;

                        // VM Creation answer
                        case CloudSimTags.VM_DESTROY_ACK:
                                //System.out.println("+++++++"+getVmList().size());
                                //System.exit(0);
                                processVmDestroy(ev);
                                break;

                        // A finished cloudlet returned
                        case CloudSimTags.CLOUDLET_RETURN:
                                processCloudletReturn(ev);
                                break;

                        // if the simulation finishes
                        case CloudSimTags.END_OF_SIMULATION:
                                shutdownEntity();
                                break;

                        // other unknown tags are processed by this method
                        default:
                                processOtherEvent(ev);
                                break;
                }
        }

        private void processVmDestroy(SimEvent ev)
        {
                int[] data = (int[]) ev.getData();
                int datacenterId = data[0];
                int vmId = data[1];
                int result = data[2];

                if (result == CloudSimTags.TRUE)
                {

                        Vm vm = VmList.getById(getVmsCreatedList(), vmId);
                        getVmsCreatedList().remove(vm);

                        Log.printLine(CloudSim.clock() + ": " + getName() + ": VM #" + vmId
                                + " has been destroyed in Datacenter #" + datacenterId);

                }
                else
                {
                        Log.printLine(CloudSim.clock() + ": " + getName() + ": Destruction of VM #" + vmId
                                + " failed in Datacenter #" + datacenterId);
                }
        }

        /**
         * Process the ack received due to a request for VM creation.
         *
         * @param ev a SimEvent object
         * @pre ev != null
         * @post $none
         */
        @Override
        protected void processVmCreate(SimEvent ev)
        {
                int[] data = (int[]) ev.getData();
                int datacenterId = data[0];
                int vmId = data[1];
                int result = data[2];

                if (result == CloudSimTags.TRUE)
                {
                        getVmsToDatacentersMap().put(vmId, datacenterId);
                        Vm vm = VmList.getById(getVmList(), vmId);
                        getVmsCreatedList().add(vm);
                        getVmList().remove(vm);

                        Log.printLine(CloudSim.clock() + ": " + getName() + ": VM #" + vmId
                                + " has been created in Datacenter #" + datacenterId + ", Host #"
                                + VmList.getById(getVmsCreatedList(), vmId).getHost().getId());

                }
                else
                {
                        Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmId
                                + " failed in Datacenter #" + datacenterId);
                }

                incrementVmsAcks();

                if (getVmsRequested() == getVmsAcks())
                {
                        loadBalancer.scheduleCloudlets(getCloudletList());

                }

        }

        /**
         * Process the return of a request for the characteristics of a
         * PowerDatacenter.
         *
         * @param ev a SimEvent object
         * @pre ev != $null
         * @post $none
         */
        protected void processResourceCharacteristics(SimEvent ev)
        {
                DatacenterCharacteristics characteristics = (DatacenterCharacteristics) ev.getData();
                getDatacenterCharacteristicsList().put(characteristics.getId(), characteristics);

                if (getDatacenterCharacteristicsList().size() == getDatacenterIdsList().size())
                {
                        setDatacenterRequestedIdsList(new ArrayList<Integer>());
                }
        }

        public ExpResult setCurrentExpStatistics(ExpResult expResult, double [] percentileList)
        {

                if (enableJobScheduling)
                {
                        expResult.finishedJobs = getCloudletReceivedList().size();

                        List<AzureCloudlet> list = getCloudletReceivedList();
                        AzureCloudlet cloudlet;
                        double totalTime = 0;
                        double indivTime = 0;
                        
                        DetailStat dtStat = new DetailStat();
                        dtStat.setPercentileList(percentileList);

                        for (int i = 0; i < list.size(); i++)
                        {
                                cloudlet = list.get(i);
                                //Log.print(indent + cloudlet.getCloudletId() + indent + indent);

                                if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS)
                                {
                                        indivTime = (cloudlet.getFinishTime() - cloudlet.getArrivalTime());
                                        totalTime += indivTime;
                                        dtStat.add(indivTime);

                                }
                        }

                        
                        expResult.totalDelay = totalTime;
                        expResult.setDetailStat(dtStat);
                        expResult.delayMetric = dtStat.computePercentile(95);
                        
                        dtStat.computePercentile();
                        dtStat.clearDataList();                        
                        
                }
                else
                {
                        expResult.finishedJobs = (int) expResult.decission.expInput.noOfClients;
                        expResult.totalDelay = backupRDT.getDelay(expResult.decission.getVmNumbers().getTotalVM(), (int) expResult.finishedJobs, expResult.decission.getVmNumbers().getNonZeroVMType()) * expResult.finishedJobs;
                }
                return expResult;
        }

        @Override
        protected void processCloudletReturn(SimEvent ev)
        {
                getCloudletReceivedList().add((AzureCloudlet) ev.getData());
                cloudletsSubmitted--;
        }

        public void resetBrokerState()
        {

                getCloudletList().clear();
                getCloudletReceivedList().clear();
                getCloudletSubmittedList().clear();

                cloudletsSubmitted = 0;
        }

        private void cloudLetArrived(SimEvent ev)
        {
                if (!enableJobScheduling)
                {
                        return;
                }

                if (getVmsCreatedList().isEmpty())
                {
                        getCloudletList().add((Cloudlet) ev.getData());
                        return;
                }

                if (!getCloudletList().isEmpty())
                {
                        loadBalancer.scheduleCloudlets(getCloudletList());

                }
                loadBalancer.scheduleCloudlet((Cloudlet) ev.getData());

        }

        public boolean isEnableJobScheduling()
        {
                return enableJobScheduling;
        }

        public void setEnableJobScheduling(boolean enableJobScheduling)
        {
                this.enableJobScheduling = enableJobScheduling;
                if (!enableJobScheduling)
                {
                        backupRDT = new RUBiSRuntimeDelay(
                                new String[]
                                {
                                        "RuntimeDelay0.backup.csv",
                                        "RuntimeDelay1.csv",
                                        "RuntimeDelay2.csv"
                                }
                        );
                }
        }

        boolean firstTimeSlot = true;

        @Override
        public boolean updateResourceProvision(Decision decission, boolean enableVMCreateDelay)
        {
                int[] vmDec = decission.getVmNumbers().getVMNumbers();

                double vmCreateDelay = 0;

                if (firstTimeSlot)
                {
                        firstTimeSlot = false;
                }
                else if (enableVMCreateDelay)
                {
                        vmCreateDelay = Settings.VM_CREATE_DELAY;
                }

                for (int q = 0; q < vmDec.length; q++)
                {

                        VMType vMType = VMType.getVMTypeByTypeIndex(q);

                        acquireVMs(vmDec[q], vmCreateDelay, vMType);
                }

                return true;

        }

        public void acquireVMs(int number, double startupDelay, VMType vmType)
        {

                List<HeterogenVM> vmList = getVmsCreatedList();
                HeterogenVM targetVM;
                int totalVMofCurrentType = 0;

                for (int i = 0; i < vmList.size(); i++)
                {

                        if (vmList.get(i).getVmType() == vmType)
                        {
                                totalVMofCurrentType++;
                        }
                }

                //ArrayList <Vm> tempVMList = new ArrayList<>();
                if (totalVMofCurrentType > number)
                {
                        int diff = totalVMofCurrentType - number;
                        int deletedVM = 0;

                        for (int i = vmList.size() - 1; i >= 0 && deletedVM < diff; i--)
                        {
                                targetVM = vmList.get(i);
                                if (targetVM.getVmType() == vmType)
                                {
                                        sendNow(getVmsToDatacentersMap().get(targetVM.getId()), CloudSimTags.VM_DESTROY_ACK, targetVM);
                                        //vmList.remove(i);
                                        deletedVM++;
                                }

                                //tempVMList.add(targetVM);
                        }

                }
                else if (totalVMofCurrentType < number)
                {

                        int diff = number - totalVMofCurrentType;
                        createVmsInDatacenter(getDatacenterIdsList().get(0), diff, startupDelay, vmType);

                }

        }

        protected void createVmsInDatacenter(int datacenterId, int numberOfNewVMs, double delay, VMType vmType)
        {
                //System.out.println("+++++++++++++++Trying to create Total VMs of: "+numberOfNewVMs);
                ArrayList<HeterogenVM> newVMList = AzureDatacenterSpec.createVMList(getId(), numberOfNewVMs, vmType);
                getVmList().addAll(newVMList);
                createVmsInDatacenter(datacenterId, delay);

        }

        /**
         * Create the virtual machines in a datacenter.
         *
         *
         * @param datacenterId Id of the chosen PowerDatacenter
         * @pre $none
         * @post $none
         */
        protected void createVmsInDatacenter(int datacenterId, double delay)
        {
                // send as much vms as possible for this datacenter before trying the next one
                int requestedVms = 0;
                String datacenterName = CloudSim.getEntityName(datacenterId);
                for (Vm vm : getVmList())
                {
                        if (!getVmsToDatacentersMap().containsKey(vm.getId()))
                        {
                                Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm.getId()
                                        + " in " + datacenterName);
                                send(datacenterId, delay, CloudSimTags.VM_CREATE_ACK, vm);
                                requestedVms++;
                        }
                }

                getDatacenterRequestedIdsList().add(datacenterId);
                setVmsRequested(requestedVms);
                setVmsAcks(0);
        }

        @Override
        public void dispatchCloudlet(Cloudlet cloudlet)
        {
                sendNow(getVmsToDatacentersMap().get(cloudlet.getVmId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
                cloudletsSubmitted++;
                getCloudletSubmittedList().add(cloudlet);
        }
}
