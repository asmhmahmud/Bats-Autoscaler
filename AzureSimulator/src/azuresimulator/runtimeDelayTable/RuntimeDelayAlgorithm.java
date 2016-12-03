/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.runtimeDelayTable;

import azuresimulator.algorithms.Algorithm;
import azuresimulator.algorithms.Decision;
import azuresimulator.algorithms.VMNumbers;
import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.statistics.DataManager;
import azuresimulator.statistics.ExpInput;
import azuresimulator.statistics.ExpResult;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hasan
 */
public class RuntimeDelayAlgorithm extends Algorithm
{

        private int[] totalVM ;
        private int rotationCycle;
        private VMType vmType;

        public RuntimeDelayAlgorithm(String name)
        {
                super(name);
        }

        /**
         * @return the totalVM
         */
        public int[] getTotalVM()
        {
                return totalVM;
        }

        /**
         * @param aTotalVM the totalVM to set
         */
        public void setTotalVM(int [] aTotalVM)
        {
                totalVM = aTotalVM;
        }

       private long prevTime = System.currentTimeMillis();
        

        @Override
        public Decision startOfSlot(int slotNo)
        {

                ExpInput expInput = workload.get(slotNo).clone();
                expInput.globalSlotNo = slotNo;



                Decision decission = new Decision(expInput);
                decission.addVMNumberDecission(vmType, totalVM[slotNo/rotationCycle]);



                System.out.println("Slot no: "+slotNo + ",  VM: "+vmType.toString());

                resourceProvisioner.updateResourceProvision(decission,false);
                return decission;
        }


        
        
        @Override
        public void saveExperimentResult()
        {

                try
                {
                        double loadFrac;
                        double delay;

                        FileWriter writer = new FileWriter(dtm.getFileName());

                        writer.append(rotationCycle+"\n");
                        writer.append("VM,Load,Load Frac,Avg Delay");
                        writer.append("\n");
                        for (ExpResult slotInfo : experimentResults)
                        {
                                loadFrac = (double)slotInfo.decission.getExpInput().noOfClients / slotInfo.decission.getVmNumbers().getTotalVM(vmType);
                                delay = slotInfo.getDelayMetric();

                                writer.append(slotInfo.decission.getVmNumbers().getTotalVM(vmType)+","+slotInfo.decission.getExpInput().noOfClients+","+loadFrac+","+delay);
                                writer.append("\n");
                        }

                        writer.flush();
                        writer.close();

                }
                catch (Exception ex)
                {
                        Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        /**
         * @return the vmType
         */
        public VMType getVmType()
        {
                return vmType;
        }

        /**
         * @param vmType the vmType to set
         */
        public void setVmType(VMType vmType)
        {
                this.vmType = vmType;
        }

        /**
         * @return the rotationCycle
         */
        public int getRotationCycle()
        {
                return rotationCycle;
        }

        /**
         * @param rotationCycle the rotationCycle to set
         */
        public void setRotationCycle(int rotationCycle)
        {
                this.rotationCycle = rotationCycle;
        }


}
