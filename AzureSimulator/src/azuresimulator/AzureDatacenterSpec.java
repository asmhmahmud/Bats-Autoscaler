/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator;

import azuresimulator.hetrogeneous.vm.VMConfiguration;
import azuresimulator.hetrogeneous.vm.HeterogenVM;
import azuresimulator.hetrogeneous.vm.VMType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
 *
 * @author Hasan
 */
public class AzureDatacenterSpec
{

        private int totalHosts;
        private int processorPerHost;
        private static int lastVMID = 0;
        //int proce

        public AzureDatacenterSpec(int totalHosts, int processorPerHost)
        {
                this.totalHosts = totalHosts;
                this.processorPerHost = processorPerHost;
        }

        public Datacenter createDatacenter(String name)
        {
                int mips = Settings.PROCESSOR_MAX_MIPS;

                // Here are the steps needed to create a PowerDatacenter:
                // 1. We need to create a list to store
                // our machine
                List<Host> hostList = new ArrayList<Host>();


                int ram = 1024 * 16; // host memory (MB)
                long storage = 1000000; // host storage
                int bw = 10000;

                for (int hostIndex = 0; hostIndex < totalHosts; hostIndex++)
                {
                        // 2. A Machine contains one or more PEs or CPUs/Cores.
                        // In this example, it will have only one core.
                        List<Pe> peList = new ArrayList<Pe>();



                        // 3. Create PEs and add these into a list.
                        for (int j = 0; j < processorPerHost; j++)
                        {
                                peList.add(new Pe(j, new PeProvisionerSimple(mips)));
                        }


                        // 4. Create Host with its id and list of PEs and add them to the list
                        // of machines



                        hostList.add(
                                new Host(
                                hostIndex,
                                new RamProvisionerSimple(ram),
                                new BwProvisionerSimple(bw),
                                storage,
                                peList,
                                new VmSchedulerTimeShared(peList)));

                }




                // 5. Create a DatacenterCharacteristics object that stores the
                // properties of a data center: architecture, OS, list of
                // Machines, allocation policy: time- or space-shared, time zone
                // and its price (G$/Pe time unit).
                String arch = "x86"; // system architecture
                String os = "Linux"; // operating system
                String vmm = Settings.VMM;
                double time_zone = 10.0; // time zone this resource located
                double cost = 3.0; // the cost of using processing in this resource
                double costPerMem = 0.05; // the cost of using memory in this resource
                double costPerStorage = 0.001; // the cost of using storage in this
                // resource
                double costPerBw = 0.0; // the cost of using bw in this resource
                LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
                // devices by now

                DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                        arch, os, vmm, hostList, time_zone, cost, costPerMem,
                        costPerStorage, costPerBw);

                // 6. Finally, we need to create a PowerDatacenter object.
                Datacenter datacenter = null;
                try
                {
                        datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }

                return datacenter;
        }
        
//        public ArrayList<Vm> createVMList(int brokerId)
//        {
//                return createVMList(brokerId, totalHosts*processorPerHost);
//        }
//        
//        public static ArrayList<Vm> createVMList(int brokerId, int noOfVM)
//        {
//                // Fourth step: Create one virtual machine
//                ArrayList<Vm> vmlist = new ArrayList<>();
//
//                // VM description
//                int mips = Settings.PROCESSOR_MAX_MIPS;
//                long size = 10000; // image size (MB)
//                int ram = 512; // vm memory (MB)
//                long bw = 1000;
//                int pesNumber = 1; // number of cpus
//                String vmm = Settings.VMM; // VMM name
//
//
//                // create VM
//                int currentVMID;
//                //for ( vmid = 0; vmid < totalHost*processorPerHost; vmid++)
//                for (currentVMID = 0; currentVMID < noOfVM ; currentVMID++)
//                {
//                        Vm vm = new Vm(currentVMID+lastVMID, brokerId, mips, pesNumber, ram, bw, size, vmm, new AzureCloudletScheduler());
//
//                        // add the VM to the vmList
//                        vmlist.add(vm);
//                }
//                
//                lastVMID = currentVMID+lastVMID;
//
//                return vmlist;
//        }
//        
        public static ArrayList<HeterogenVM> createVMList(int brokerId, int noOfVM, VMType vmType)
        {
                // Fourth step: Create one virtual machine
                ArrayList<HeterogenVM> vmlist = new ArrayList<>();

                String vmm = Settings.VMM; // VMM name

                VMConfiguration vMConfiguration  = vmType.getVMConfiguration();
                // create VM
                int currentVMID;
                //for ( vmid = 0; vmid < totalHost*processorPerHost; vmid++)
                for (currentVMID = 0; currentVMID < noOfVM ; currentVMID++)
                {
                        HeterogenVM vm = new HeterogenVM(
                                currentVMID+lastVMID, 
                                brokerId, 
                                vMConfiguration.mips, 
                                vMConfiguration.numberOfPes, 
                                vMConfiguration.ram, 
                                vMConfiguration.bw, 
                                vMConfiguration.size, 
                                vmm,
                                new AzureCloudletScheduler()
                                );
                        vm.setVmType(vmType);

                        // add the VM to the vmList
                        vmlist.add(vm);
                }
                
                lastVMID = currentVMID+lastVMID;

                return vmlist;
        }
        
}
