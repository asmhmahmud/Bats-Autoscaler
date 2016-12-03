/*******************************
** @author A S M Hasan Mahmud
*******************************/

package azuresimulator.experiments.delaytable;

import azuresimulator.experiments.*;
import azuresimulator.Settings;
import azuresimulator.algorithms.DelayTableUpdateAlgorithm;
import azuresimulator.experiments.ExpParams;
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
public class DelayTablMM1 extends Experiment
{

        @Override
        protected void initalizeAlgorithm(ExpParams expParams)
        {
                DelayTableUpdateAlgorithm algorithm = new DelayTableUpdateAlgorithm("DelayTBlMM1");
                algorithm.setV(Settings.STANDARD_V);
                algorithm.setParameters(expParams.broker, expParams.predictor, inputs, Settings.TOTAL_BUDGET, expParams.rdt, Settings.ALGORITHM_PREFIX+algorithm.getName() + ".csv");
                expParams.algorithm = algorithm;
                expParams.rdt.initializeDelayTable(110, VMType.EXTRA_SMALL);
                //expParams.rdt.printDelayTable(VMType.EXTRA_SMALL);
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
                Experiment exp = new DelayTablMM1("Experiment");
                ExpParams expParams = exp.initialize();
                expParams.broker.setEnableJobScheduling(Settings.ENABLE_JOB_SCHEDULING);
                expParams.wLoadGen.setDispatchJobs(Settings.ENABLE_JOB_SCHEDULING);
                exp.startExperiment();
                expParams.algorithm.saveExperimentResult();
                //expParams.rdt.printDelayTable(VMType.SMALL);
        }

        public static void main(String[] args)
        {
                //use subversion revision 91
                start();

        }

        public DelayTablMM1(String name)
        {
                super(name);
        }

}
