/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.experiments.delaytable;

/**
 *
 * @author Hasan
 */
public class AllDelayExp
{

        public static void start()
        {
                DelayTablEmpty.start();
                DelayTablMM1.start();
        }

        public static void main(String[] args)
        {
                //use subversion revision 91
                start();

        }        
}
