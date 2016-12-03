/*******************************
** @author A S M Hasan Mahmud
*******************************/

package azuresimulator.experiments;

import azuresimulator.Settings;
import azuresimulator.experiments.ExpParams;

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
public class BudBenchMark 
{


        public static void main(String[] args)
        {
               BATSExperiment.start();
               EqualSCExp.start();
               OptOfflineExperiment.start();
        }



}
