/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.test;

import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.predictor.WikiWorkloadPredictor;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.runtimeDelayTable.RubisSingleVMTypeDelay;

/**
 *
 * @author amahm008
 */
public class RuntimeDelayTest
{
        public static void main(String args[])
        {
                //RubisSingleVMTypeDelay vmt = new RubisSingleVMTypeDelay(RUBiSRuntimeDelay.DELAY_FILE_NAMES[0]);
                //System.out.println(vmt.getDelay(8, 1200));
                
                //RUBiSRuntimeDelay rntime = new RUBiSRuntimeDelay();                
                //System.out.println(rntime.getDelay(8, 1200, VMType.EXTRA_SMALL));
                //vmt.printDelayTable(8);
                
                RUBiSRuntimeDelay rntime = new RUBiSRuntimeDelay();    
                
                
//                rntime.addDelayTableEntry(100, 250, 0.5, VMType.EXTRA_SMALL);
//                rntime.addDelayTableEntry(100, 300, 0.7, VMType.EXTRA_SMALL);
//                rntime.addDelayTableEntry(100, 400, 1, VMType.EXTRA_SMALL);
//                rntime.addDelayTableEntry(100, 500, 1.2, VMType.EXTRA_SMALL);
//                rntime.addDelayTableEntry(64, (int) (64*4.5), 1.2, VMType.EXTRA_SMALL);
//                rntime.addDelayTableEntry(100, (int) (100*4.22), 3, VMType.EXTRA_SMALL);
//                rntime.addDelayTableEntry(100, (int) (100*4.23), 2, VMType.EXTRA_SMALL);
//                rntime.addDelayTableEntry(100, (int) (100*4.24), 1.5, VMType.EXTRA_SMALL);
//                rntime.addDelayTableEntry(64, (int) (64*4.25), 10.2, VMType.EXTRA_SMALL);
//                  rntime.addDelayTableEntry(64, (int) (64*200), 5.2, VMType.EXTRA_SMALL);
                //rntime.printDelayTable(VMType.EXTRA_SMALL);
                  
                  
                  System.out.println("*****************************");
                  System.out.println("Delay: "+rntime.getDelay(128, 14220, VMType.EXTRA_SMALL));

        }
}
