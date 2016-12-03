/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.experiments.HV;

/**
 *
 * @author Hasan
 */
public class AllScaleExperiment
{
        public static void main (String []args)
        {
                BATSHVExperiment.start();
                BATSESmallExp.start();
                BATSSmallExp.start();
                BATSMedExp.start();
        }
}
