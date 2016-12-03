/*******************************
** @author A S M Hasan Mahmud
*******************************/

package azuresimulator.test;

import azuresimulator.statistics.DetailStat;
import java.util.ArrayList;
import java.util.Random;

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
public class PercentileTest
{

        public static void main(String[] args)
        {

                Random rndm = new Random(123);
                DetailStat dtStat = new DetailStat();

                for (int i = 0; i < 1000; i++)
                {
                        dtStat.add((double) rndm.nextInt(1000));
                }

                dtStat.setPercentileList(
                        new double[]
                        {
                                100
                        }
                );
                dtStat.computePercentile();

                System.out.println(dtStat.getCSVFormattedData());

        }

}
