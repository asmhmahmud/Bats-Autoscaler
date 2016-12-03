/*******************************
** @author A S M Hasan Mahmud
*******************************/

package azuresimulator.experiments;

import azuresimulator.Settings;
import azuresimulator.algorithms.AdaptiveV;
import azuresimulator.algorithms.BATS;
import azuresimulator.experiments.ExpParams;
import static azuresimulator.experiments.PredErrorExp.start;
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
 * A simple example showing how to create a datacenter with one host and run one cloudlet on it.
 */
public class AdaptiveVExperiment extends Experiment
{

        private static double initialV = Settings.STANDARD_V;

        private String getStandarString(double v)
        {
                if (((int) v) == v)
                {
                        return ((int) v) + "";
                }
                else
                {
                        return v + "";
                }
        }

        @Override
        protected void initalizeAlgorithm(ExpParams expParams)
        {
                AdaptiveV algorithm = new AdaptiveV("AdaptV");
                algorithm.setInitialV(initialV);

                algorithm.setParameters(expParams.broker, expParams.predictor, inputs, Settings.TOTAL_BUDGET, expParams.rdt,
                        Settings.ALGORITHM_PREFIX + algorithm.getName() + "_" + getStandarString(initialV) + ".csv"
                );
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
                double[] initialVs = new double[]
                {
                        100, 1, 0.001
                };
                for (int i = 0; i < initialVs.length; i++)
                {
                        initialV = initialVs[i];
                        Experiment exp = new AdaptiveVExperiment("Experiment");
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

        public AdaptiveVExperiment(String name)
        {
                super(name);
        }

}
