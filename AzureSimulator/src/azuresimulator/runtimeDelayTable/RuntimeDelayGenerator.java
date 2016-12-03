/*******************************
** @author A S M Hasan Mahmud
*******************************/

package azuresimulator.runtimeDelayTable;

import azuresimulator.WorkloadGen;
import azuresimulator.*;
import azuresimulator.grahpdata.SimValidation;
import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.loadbalancer.EqualLoadBalancer;
import azuresimulator.loadbalancer.LoadBalancer;
import azuresimulator.predictor.AccuratePredictor;
import azuresimulator.predictor.Predictor;
import azuresimulator.statistics.ExpInput;
import java.util.ArrayList;
import java.util.Calendar;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
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
 * A simple example showing how to create a datacenter with one host and run one
 * cloudlet on it.
 */
public class RuntimeDelayGenerator
{

        public static void main(String[] args)
        {
                RuntimeDelayGenerator rdg = new RuntimeDelayGenerator();
                rdg.startGeneratingDelay();
                SimValidation simValid = new SimValidation();
                simValid.generateSimValidationDataRespVSCl();
                simValid.generateSimValidationDataRespVSInst();

        }

        private double[] getLoadFactor()
        {

//                double[] loadFactor = new double[]
//                {
//                        50,
//                        80,
//                        100,
//                        120
//
//                };
                ArrayList<Double> lfList = new ArrayList();

                double load = 0;
                double[] inc = new double[]
                {
                        //3, 1
                        2, 1, 3
                };
                int[] totItems = new int[]
                {
                        //15, 60
                        25, 50, 7
                };

                for (int i = 0; i < inc.length; i++)
                {

                        for (int j = 0; j < totItems[i]; j++)
                        {
                                load = load + inc[i];
                                lfList.add(load);
                                //System.out.println("Load: " + load);
                        }

                }
                double[] loadFactor = new double[lfList.size()];
                for (int i = 0; i < lfList.size(); i++)
                {
                        loadFactor[i] = lfList.get(i);
                }
                
                for (int i = 0; i < loadFactor.length; i++)
                {
                        System.out.println("LF: "+loadFactor[i]);
                }
                return loadFactor;
        }

        public void startGeneratingDelay()
        {

                double[] loadFactor = getLoadFactor();

                double[][] vmNumberLoadFactor = new double[][]
                {
                        {

                                1.2, //1    
                                2.2, //2  
                                4.5, // 4    
                                7, //6
                                9.5, //8 
                                12,//10
                                14, //12
                                17,//14
                                19, //16  
                                23,//20
                                30,//25
                                40, //32 
                                52,//40
                                66, //50
                                90, //64
                                112, //80
                                132,//95
                                155,//110
                                180,//128
                                210,//150
                                240,//170
                                270//190

                        },
                        {
                                1.2, //1    
                                2.2, //2  
                                4.5, // 4   
                                7,//6
                                9.5, //8 
                                11,//10
                                14, //12
                                17,//14
                                19, //16  
                                23,//20
                                30,//25
                                40, //32 
                                52,//40
                                66, //50
                                90, //64
                                112, //80
                                132,//95
                                155,//110
                                180,//128
                                210,//150
                                240,//170
                                270//190

                        },
                        {
                                1, //1    
                                2, //2  
                                4.1, // 4   
                                6.2,//6
                                9.5, //8 
                                11.5,//10
                                14, //12
                                17,//14
                                19, //16  
                                24,//20
                                30,//25
                                40, //32 
                                50,//40
                                60, //50
                                90, //64
                                112, //80
                                132,//95
                                155,//110
                                180,//128
                                210,//150
                                240,//170
                                270//190
                        }

                };

                double[] vmTypeLoadFactor = new double[]
                {
                        1, 3.2, 5.8
                };
                int maxVM = 50;
                //int noOfVM = 10;
//                int[] vmS = new int[]
//                {
//                        1,//0
//                        2,//1
//                        4,//2
//                        6,
//                        8,//3
//                        10,
//                        12,//4
//                        14,
//                        16,//5
//                        20,//6
//                        25,//6
//                        32,//7
//                        40,//8
//                        50,//9
//                };

                
                int[] vmS = new int[]
                {
                                1  
                                ,2  
                                ,4    
                                ,6
                                ,8 
                                ,10
                                ,12
                                ,14
                                ,16  
                                ,20
                                ,25
                                ,32 
                                ,40
                                ,50
                                ,64
                                ,80
                                ,95
                                ,110
                                ,128
                                ,150
                                ,170
                                ,190
                };                
                
                
                
                //for (int i = 1; i < RUBiSRuntimeDelay.DELAY_FILE_NAMES.length; i++)
                for (int i = 0; i < 1; i++)
                {
                        //System.out.println("Hereeee...");
                        startGeneratingDelay(maxVM, loadFactor, vmNumberLoadFactor[i], vmTypeLoadFactor[i], RUBiSRuntimeDelay.DELAY_FILE_NAMES[i], VMType.getVMTypeByTypeIndex(i), vmS);
                }

        }

        public void startGeneratingDelay(int maxVM,
                double[] loadFactors,
                double[] vmNumberLoadFactors,
                double vmTypeLoadFactor,
                String outputFileName,
                VMType vmType,
                int[] vmS
        )
        {
                try
                {
                        // First step: Initialize the CloudSim package. It should be called
                        // before creating any entities.
                        int num_user = 1; // number of cloud users
                        Calendar calendar = Calendar.getInstance();
                        boolean trace_flag = false; // mean trace events

                        long startTime = System.currentTimeMillis();

                        // Initialize the CloudSim library
                        CloudSim.init(num_user, calendar, trace_flag);
                        Log.disable();
                        // Second step: Create Datacenters
                        // Datacenters are the resource providers in CloudSim. We need at
                        // list one of them to run a CloudSim simulation
                        AzureDatacenterSpec azureDatacenter = new AzureDatacenterSpec(Settings.NUMBER_OF_HOSTS, Settings.NUMBER_OF_PROCESSOR_PER_HOST);
                        Datacenter datacenter0 = azureDatacenter.createDatacenter("Datacenter_0");

                        // Third step: Create Broker
                        AzureBroker broker = new AzureBroker("Broker");

                        ArrayList<ExpInput> inputs = new ArrayList<ExpInput>();
                        int i;
                        for (i = 0; i < vmS.length; i++)
                        //i = 18;
                        {
                                for (int index = 0; index < loadFactors.length; index++)
                                {
                                        ExpInput info = new ExpInput();
                                        info.globalSlotNo = index;
                                        info.noOfClients = vmTypeLoadFactor * loadFactors[index] * vmNumberLoadFactors[i];
                                        inputs.add(info);
                                }

                        }

                        WorkloadGen wLoadGen = new WorkloadGen("Workload_Gen", Settings.RUBIS_DIST_FILE_NAME, inputs);
                        RuntimeDelayAlgorithm algorithm = new RuntimeDelayAlgorithm("RuntimeDelayAlgorithm");

                        Predictor predictor = new AccuratePredictor();
                        for (int j = 0; j < inputs.size(); j++)
                        {
                                predictor.addSample(inputs.get(j).noOfClients);
                        }

                        algorithm.setParameters(broker, predictor, inputs, 0, null, outputFileName);

                        //System.out.println(algorithm.getTotalTimeSlots());
//                        algorithm.setTotalVM(new int[]
//                        {
//                                vmS[i]
//                        });
                        algorithm.setTotalVM(vmS);

                        algorithm.setVmType(vmType);
                        algorithm.setRotationCycle(loadFactors.length);

                        LoadBalancer loadBalancer = new EqualLoadBalancer();
                        broker.setParameters(loadBalancer);
                        wLoadGen.setBroker(broker);
                        //wLoadGen.setDispatchJobs(false);

                        //wLoadGen.setJobDistributionParameter(meanServiceTime, meanInterArrival, serviceTimeCap, totalJobs);
                        TimeSlotModel slottedModel = new TimeSlotModel("TimeSlot_Model");
                        slottedModel.setParameters(wLoadGen, algorithm, broker);
                        // Sixth step: Starts the simulation
                        CloudSim.startSimulation();

                        CloudSim.stopSimulation();

                        algorithm.saveExperimentResult();

                        Log.printLine("Simulation finished!");
                        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000));
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                        Log.printLine("Unwanted errors happen");
                }
        }
//                public static void main (String [] args)
//        {
//                RUBiSRuntimeDelay rdt = new RUBiSRuntimeDelay(Settings.RUBIS_RUNTIME_DELAY_FILE);
//                rdt.printDelayTable();
//                System.out.println("++++++++++++++++++++");
//                System.out.println(""+rdt.getDelay(100, 405));
//        }
}
