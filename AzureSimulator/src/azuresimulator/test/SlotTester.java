/*******************************
** @author A S M Hasan Mahmud
*******************************/

package azuresimulator.test;

import azuresimulator.WorkloadGen;
import azuresimulator.*;
import azuresimulator.algorithms.Algorithm;
import azuresimulator.algorithms.BATS;
import azuresimulator.algorithms.SlotTestAlgorithm;
import azuresimulator.loadbalancer.EqualLoadBalancer;
import azuresimulator.loadbalancer.LoadBalancer;
import azuresimulator.predictor.AccuratePredictor;
import azuresimulator.predictor.Predictor;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.runtimeDelayTable.RuntimeDelayAlgorithm;
import azuresimulator.statistics.ExpInput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */
/**
 * A simple example showing how to create a datacenter with one host and run one cloudlet on it.
 */
public class SlotTester
{

        private ArrayList<ExpInput> inputs;

        public SlotTester(String name)
        {

                ReadInput inputReader = new ReadInput();
                //inputs = inputReader.getInput(Settings.inputFileName);
                //inputReader.printExp(inputs);
                inputs = new ArrayList<>();

                for (int i = 0; i < 2; i++)
                {
                        inputs.add(new ExpInput(i, 0, 1, 10));
                }

        }

        public static void main(String[] args)
        {
                SlotTester rdg = new SlotTester("Slot Tester");
                rdg.startExperiment();

        }

        public void startExperiment()
        {
                Log.printLine("Starting CloudSimExample1...");

                try
                {
                        // First step: Initialize the CloudSim package. It should be called
                        // before creating any entities.
                        int num_user = 1; // number of cloud users
                        Calendar calendar = Calendar.getInstance();
                        boolean trace_flag = false; // mean trace events

                        int totalHost = 40;
                        int processorPerHost = 4;

                        long startTime = System.currentTimeMillis();

                        // Initialize the CloudSim library
                        CloudSim.init(num_user, calendar, trace_flag);
                        //Log.disable();
                        // Second step: Create Datacenters
                        // Datacenters are the resource providers in CloudSim. We need at
                        // list one of them to run a CloudSim simulation
                        AzureDatacenterSpec azureDatacenter = new AzureDatacenterSpec(totalHost, processorPerHost);
                        Datacenter datacenter0 = azureDatacenter.createDatacenter("Datacenter_0");

                        // Third step: Create Broker
                        AzureBroker broker = new AzureBroker("Broker");

                        //List<Vm> vmlist = azureDatacenter.createVMList(broker.getId(),1);
                        // submit vm list to the broker
                        //broker.submitVmList(vmlist);
                        WorkloadGen wLoadGen = new WorkloadGen("Workload_Gen", Settings.RUBIS_DIST_FILE_NAME, inputs);
                        wLoadGen.setMaxJobs(500);

                        RUBiSRuntimeDelay rdt = new RUBiSRuntimeDelay();

                        SlotTestAlgorithm algorithm = new SlotTestAlgorithm("Slot_Test");
                        Predictor predictor = new AccuratePredictor();

                        for (int i = 0; i < inputs.size(); i++)
                        {
                                predictor.addSample(inputs.get(i).noOfClients);
                        }

                        algorithm.setParameters(broker, predictor, inputs, 360, rdt, "Slot_Test_Result.csv");
                        LoadBalancer loadBalancer = new EqualLoadBalancer();
                        broker.setParameters(loadBalancer);
                        wLoadGen.setBroker(broker);
                        //wLoadGen.setJobDistributionParameter(meanServiceTime, meanInterArrival, serviceTimeCap, totalJobs);
                        TimeSlotModel slottedModel = new TimeSlotModel("TimeSlot_Model");
                        slottedModel.setParameters(wLoadGen, algorithm, broker);
                        slottedModel.setEnableJobPrint(true);

                        // Sixth step: Starts the simulation
                        CloudSim.startSimulation();

                        CloudSim.stopSimulation();

                        algorithm.saveExperimentResult();

                        //Final step: Print results when simulation is over
                        List<Cloudlet> newList = broker.getCloudletReceivedList();
                        //printStatisticsDetail(newList);
                        Log.printLine("Simulation finished!");
                        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000));
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                        Log.printLine("Unwanted errors happen");
                }
        }
}
