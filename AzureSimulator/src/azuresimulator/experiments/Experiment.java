/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.experiments;

import azuresimulator.AzureBroker;
import azuresimulator.AzureDatacenterSpec;
import azuresimulator.ReadInput;
import azuresimulator.SampleUtil;
import azuresimulator.Settings;
import azuresimulator.TimeSlotModel;
import azuresimulator.WorkloadGen;
import azuresimulator.algorithms.Algorithm;
import azuresimulator.algorithms.BATS;
import azuresimulator.loadbalancer.EqualLoadBalancer;
import azuresimulator.predictor.AccuratePredictor;
import azuresimulator.predictor.Predictor;
import azuresimulator.predictor.WikiWorkloadPredictor;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.statistics.ExpInput;
import java.util.ArrayList;
import java.util.Calendar;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;

/**
 *
 * @author Hasan
 */
public class Experiment
{

        protected ArrayList<ExpInput> inputs;
        protected ExpParams expParamsDefined;

        public Experiment(String name)
        {

                ReadInput inputReader = new ReadInput();
                inputs = inputReader.getInput(Settings.inputFileName);
                expParamsDefined = new ExpParams();

                int num_user = 1; // number of cloud users
                Calendar calendar = Calendar.getInstance();
                boolean trace_flag = false; // mean trace events

                // Initialize the CloudSim library
                CloudSim.init(num_user, calendar, trace_flag);
                Log.disable();

        }

        protected void initializeDataCenter(ExpParams expParams) throws Exception
        {

                AzureDatacenterSpec azureDatacenter = new AzureDatacenterSpec(Settings.NUMBER_OF_HOSTS, Settings.NUMBER_OF_PROCESSOR_PER_HOST);
                expParams.datacenter = azureDatacenter.createDatacenter("Datacenter_0");
                // Third step: Create Broker

                expParams.loadBalancer = new EqualLoadBalancer();

                expParams.broker = new AzureBroker("Broker");
                expParams.broker.setEnableJobScheduling(true);
                expParams.broker.setParameters(expParams.loadBalancer);

                expParams.wLoadGen = new WorkloadGen("Workload_Gen", Settings.RUBIS_DIST_FILE_NAME, inputs);
                expParams.wLoadGen.setBroker(expParams.broker);
                expParams.wLoadGen.setDispatchJobs(true);

                expParams.rdt = new RUBiSRuntimeDelay();

        }

        /**
         * First call initializeDataCenter()
         */
        protected void initializePredictor(ExpParams expParams)
        {
                //System.out.println("Called");

                expParams.predictor = Settings.predictor;
                setDataToPredictor(expParams.predictor);

        }

        protected void setDataToPredictor(Predictor predictor)
        {
                if (!predictor.isPredictorInitialized())
                {
                        for (int i = 0; i < inputs.size(); i++)
                        {
                                predictor.addSample(inputs.get(i).noOfClients);
                        }
                        predictor.setPredictorInitialized(true);
                }
        }

        /**
         * First call initializeDataCenter() then initializePredictor()
         */
        protected void initalizeAlgorithm(ExpParams expParams)
        {
                Algorithm algorithm = new BATS("BATS");
                algorithm.setParameters(expParams.broker, expParams.predictor, inputs, Settings.TOTAL_BUDGET, expParams.rdt, Settings.ALGORITHM_PREFIX + algorithm.getName() + ".csv");
                expParams.algorithm = algorithm;
        }

        protected void initalizeTimeSlotModel(ExpParams expParams)
        {
                TimeSlotModel slottedModel = new TimeSlotModel("TimeSlot_Model");
                slottedModel.setParameters(expParams.wLoadGen, expParams.algorithm, expParams.broker);
        }

        public ExpParams initialize()
        {

                ExpParams expParams = new ExpParams();
                try
                {

                        initializeDataCenter(expParams);
                        initializePredictor(expParams);
                        initalizeAlgorithm(expParams);
                        initalizeTimeSlotModel(expParams);

                }
                catch (Exception e)
                {
                        e.printStackTrace();
                        Log.printLine("Unwanted errors happen");
                }

                return expParams;

        }

        public void startExperiment()
        {
                Log.printLine("Starting AzureSimulator");

                try
                {
                        long startTime = System.currentTimeMillis();
                        // Sixth step: Starts the simulation
                        CloudSim.startSimulation();

                        CloudSim.stopSimulation();

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
