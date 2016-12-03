/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.experiments;

import azuresimulator.experiments.delaytable.AllDelayExp;
import azuresimulator.experiments.zt.AllZtExp;

/**
 *
 * @author Hasan
 */
public class AllExp
{
        public static void start()
        {
                AllBenchMark.start();
                AllDelayExp.start();
                AllZtExp.start();
                PredErrorExp.start();
        }

        public static void main(String[] args)
        {
                start();

        }            
}
