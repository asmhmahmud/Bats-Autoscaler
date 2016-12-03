/*******************************
** @author A S M Hasan Mahmud
*******************************/

package azuresimulator.experiments;

import azuresimulator.Settings;
import azuresimulator.algorithms.BATS;
import azuresimulator.algorithms.ImpReserve;
import azuresimulator.experiments.ExpParams;
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
public class ImpReserveExp extends Experiment
{

        private static double reserveBudFrac = 10;

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
                ImpReserve algorithm = new ImpReserve("ImpReserve");
                algorithm.setParameters(expParams.broker, expParams.predictor, inputs, Settings.TOTAL_PERF_OPT_BUDGET * reserveBudFrac, expParams.rdt, Settings.ALGORITHM_PREFIX + algorithm.getName() + getStandarString(reserveBudFrac) + ".csv");
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
                double[] budFrac = new double[]
                {
                        0.8,0.7,0.60
                };
                for (int i = 0; i < budFrac.length; i++)
                {
                        reserveBudFrac = budFrac[i] ;
                        Experiment exp = new ImpReserveExp("Experiment");
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

        public ImpReserveExp(String name)
        {
                super(name);
        }

}
