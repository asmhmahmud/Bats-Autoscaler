/*******************************
** @author A S M Hasan Mahmud
*******************************/

package azuresimulator.experiments;

import azuresimulator.SampleUtil;
import azuresimulator.Settings;
import azuresimulator.algorithms.BATS;
import azuresimulator.experiments.ExpParams;
import azuresimulator.predictor.AccuratePredictor;
import azuresimulator.predictor.ErrorPredictor;
import org.cloudbus.cloudsim.Log;

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
public class PredErrorExp extends Experiment
{

        private static int errorPerc = 10;

        @Override
        protected void initializePredictor(ExpParams expParams)
        {
                ErrorPredictor errPrec = new ErrorPredictor();
                expParams.predictor = errPrec;

                for (int i = 0; i < inputs.size(); i++)
                {
                        expParams.predictor.addSample(inputs.get(i).noOfClients);
                }

                errPrec.setErrorPercentage(errorPerc);
        }

        @Override
        protected void initalizeAlgorithm(ExpParams expParams)
        {
                BATS algorithm = new BATS("PredError");
                algorithm.setParameters(expParams.broker, expParams.predictor, inputs, Settings.TOTAL_BUDGET, expParams.rdt, Settings.ALGORITHM_PREFIX + algorithm.getName() + errorPerc + ".csv");
                expParams.algorithm = algorithm;

        }

        @Override
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

        public static void start()
        {
                int[] allErrorPercs = new int[]
                {
                        10
                };
                for (int i = 0; i < allErrorPercs.length; i++)
                {
                        errorPerc = allErrorPercs[i];

                        Experiment exp = new PredErrorExp("Experiment");
                        ExpParams expParams = exp.initialize();
                        expParams.broker.setEnableJobScheduling(Settings.ENABLE_JOB_SCHEDULING);
                        expParams.wLoadGen.setDispatchJobs(Settings.ENABLE_JOB_SCHEDULING);
                        exp.startExperiment();
                        expParams.algorithm.saveExperimentResult();
                }
        }

        public static void main(String[] args)
        {
                start();
        }

        public PredErrorExp(String name)
        {
                super(name);
        }

}
