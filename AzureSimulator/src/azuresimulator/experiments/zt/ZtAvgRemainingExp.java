/*******************************
** @author A S M Hasan Mahmud
*******************************/

package azuresimulator.experiments.zt;

import azuresimulator.experiments.*;
import azuresimulator.Settings;
import azuresimulator.algorithms.Algorithm;
import azuresimulator.algorithms.BATS;
import azuresimulator.benchmark.EqualSC;
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
public class ZtAvgRemainingExp extends Experiment
{

        @Override
        protected void initalizeAlgorithm(ExpParams expParams)
        {
                ZtAvgRemaining algorithm = new ZtAvgRemaining("ZtAvgRemaining");
                algorithm.setParameters(expParams.broker, expParams.predictor, inputs, Settings.TOTAL_BUDGET, expParams.rdt, Settings.ALGORITHM_PREFIX+algorithm.getName() + ".csv");
                expParams.algorithm = algorithm;
                algorithm.setV(Settings.STANDARD_V - 2);
                
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
                Experiment exp = new ZtAvgRemainingExp("Experiment");
                ExpParams expParams = exp.initialize();
                expParams.broker.setEnableJobScheduling(Settings.ENABLE_JOB_SCHEDULING);
                expParams.wLoadGen.setDispatchJobs(Settings.ENABLE_JOB_SCHEDULING);
                exp.startExperiment();
                expParams.algorithm.saveExperimentResult();
        }

        public static void main(String[] args)
        {
                start();

        }

        public ZtAvgRemainingExp(String name)
        {
                super(name);
        }

}
