/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.util;

import edu.fiu.scg.util.MLab;

/**
 *
 * @author amahm008
 */
public class TestUtil
{
        public static void main(String [] args)
        {
                double [] datas = new double[]{1,2,3,4,5,1,2,3,4,1,2,3};
                double [] index = MLab.assign(datas, new int[] { 3,6 },new double [] {90,100} );
                
                for (int i = 0; i < index.length; i++)
                {
                        System.out.print(index[i]+",");
                }
                System.out.println("\n");
                
                
                
        }
}
