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
import azuresimulator.predictor.Predictor;
import azuresimulator.predictor.WikiWorkloadPredictor;
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
public class PredErrorComp extends Experiment
{


        public static void start()
        {
                PredErrorComp exp = new PredErrorComp("Experiment");
                ExpParams expParams = exp.initialize();
                exp.initializePredictor(expParams);
                expParams.predictor.saveComparison(Settings.ALGORITHM_PREFIX + "PredComp.csv");

//                expParams.broker.setEnableJobScheduling(Settings.ENABLE_JOB_SCHEDULING);
//                expParams.wLoadGen.setDispatchJobs(Settings.ENABLE_JOB_SCHEDULING);
//                exp.startExperiment();
//                expParams.algorithm.saveExperimentResult();
        }

        public static void main(String[] args)
        {

                start();

        }

        public PredErrorComp(String name)
        {
                super(name);
        }

}
