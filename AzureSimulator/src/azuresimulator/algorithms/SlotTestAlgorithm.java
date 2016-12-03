/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.algorithms;

import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.statistics.ExpInput;

/**
 *
 * @author Hasan
 */
public class SlotTestAlgorithm extends Algorithm
{

        public SlotTestAlgorithm(String name)
        {
                super(name);
        }


        private int totalVM = 0;

        @Override
        public Decision startOfSlot(int slotNo)
        {

                ExpInput expInput = workload.get(slotNo).clone();
                expInput.globalSlotNo = slotNo;
               // int noOfVM = (slotNo+2)%3+1;
                int noOfVM =3;
                double delay = runDelay.getDelay(noOfVM, (int ) expInput.noOfClients,VMType.EXTRA_SMALL);
                

                Decision decission = new Decision(expInput);    
                decission.setExpectedDelay(delay);
                decission.addVMNumberDecission(VMType.EXTRA_SMALL, noOfVM);
                //System.out.println("Slot no: " + slotNo + ",  VM: " + decission.getNoOfVM());
                //totalVM = totalVM + decission.getNoOfVM();
                
                resourceProvisioner.updateResourceProvision(decission,true);
                return decission;
        }

        @Override
        public void simulationFinished()
        {
                //saveExperimentResult();
                //System.out.println("Total VM: "+totalVM);
        }

}
