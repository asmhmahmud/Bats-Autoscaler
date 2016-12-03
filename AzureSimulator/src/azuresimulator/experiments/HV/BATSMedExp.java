/*******************************
** @author A S M Hasan Mahmud
*******************************/

package azuresimulator.experiments.HV;

import azuresimulator.Settings;
import azuresimulator.algorithms.BATS;
import azuresimulator.experiments.ExpParams;
import azuresimulator.experiments.Experiment;
import azuresimulator.hetrogeneous.vm.VMType;
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
public class BATSMedExp extends Experiment
{

        @Override
        protected void initalizeAlgorithm(ExpParams expParams)
        {
                BATS algorithm = new BATS("BATS_MED");
                algorithm.setParameters(expParams.broker, expParams.predictor, inputs, Settings.TOTAL_BUDGET, expParams.rdt, Settings.ALGORITHM_PREFIX+algorithm.getName() + ".csv");
                algorithm.setVmType(VMType.MEDIUM);
                expParams.algorithm = algorithm;
                algorithm.setV(0);
                
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
                Experiment exp = new BATSMedExp("Experiment");
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

        public BATSMedExp(String name)
        {
                super(name);
        }

}
