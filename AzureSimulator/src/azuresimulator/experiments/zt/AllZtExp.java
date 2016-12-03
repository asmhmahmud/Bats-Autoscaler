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
public class AllZtExp 
{



        public static void start()
        {
                ZtAvgEvenExp.start();
                ZtAvgHourExp.start();
                ZtAvgRemainingExp.start();
                
        }

        public static void main(String[] args)
        {
                start();

        }



}
