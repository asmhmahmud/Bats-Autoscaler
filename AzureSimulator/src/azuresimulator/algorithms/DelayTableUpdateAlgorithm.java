/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.algorithms;

import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.statistics.ExpResult;

/**
 *
 * @author amahm008
 */
public class DelayTableUpdateAlgorithm extends BATS
{

        public DelayTableUpdateAlgorithm(String name)
        {
                super(name);
        }
        //private lastSlot
        @Override
        public Decision startOfSlot(int slotNo)
        {
                Decision decission = super.startOfSlot(slotNo);
                
                return  decission;

        }
        

        @Override
         public void endOfSlot(int slotNo, ExpResult expResult)
        {
                runDelay.addDelayTableEntry(
                        expResult.decission.getVmNumbers().getTotalVM(VMType.EXTRA_SMALL),
                        (int)expResult.decission.expInput.noOfClients, 
                        
                        expResult.getDelayMetric(), 
                        VMType.EXTRA_SMALL
                );
                //
//                if (slotNo % dataSavingFrequency == 0)
//                {
//
//                        runDelay.printDelayTable(VMType.EXTRA_SMALL);
//                }
//                
//                if(slotNo > 299)
//                {
//                        saveExperimentResult();
//                        System.exit(slotNo);
//                }
//                runDelay.printDelayTable(VMType.EXTRA_SMALL);
                
        }
        
        
}
