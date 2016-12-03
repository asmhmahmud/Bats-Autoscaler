/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.runtimeDelayTable;

import azuresimulator.hetrogeneous.vm.VMType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.cloudbus.cloudsim.DatacenterBroker;

/**
 *
 * @author Hasan
 */
public class RUBiSRuntimeDelay
{

        private String[] inputFileName;
        ArrayList<RubisSingleVMTypeDelay> runtimeDelays;

        public static final String[] DELAY_FILE_NAMES = new String[]
        {
                "RuntimeDelay0.csv",
                "RuntimeDelay1.csv",
                "RuntimeDelay2.csv",

        };

        public RUBiSRuntimeDelay()
        {
                this(DELAY_FILE_NAMES);
        }

        public RUBiSRuntimeDelay(String[] fileName)
        {
                //super(name);
                this.inputFileName = fileName;
                runtimeDelays = new ArrayList<>();
                readRuntimeDelay();
        }

        private void readRuntimeDelay()
        {
                for (int i = 0; i < inputFileName.length; i++)
                {
                        RubisSingleVMTypeDelay vmDelays = new RubisSingleVMTypeDelay(inputFileName[i]);         
                        vmDelays.setVmType(VMType.getVMTypeByTypeIndex(i));
                        runtimeDelays.add(vmDelays);
                }
        }


        public void printDelayTable(VMType vmType)
        {
                
                //for (int i = 0; i < runtimeDelays.size(); i++)
                {
                        runtimeDelays.get(VMType.getVMTypeIndexByVMType(vmType)).printDelayTable();

                }
        }
        
        public void addDelayTableEntry(int noOfVM, int numberOfClients,  double avgDelay,  VMType vmType)
        {
                
                RubisSingleVMTypeDelay  vmDelay=  runtimeDelays.get(VMType.getVMTypeIndexByVMType(vmType));                
                vmDelay.addDelayTableEntry( noOfVM,numberOfClients, avgDelay);
        }
        
        public void initializeDelayTable(double serviceRate, VMType vmType)
        {

                RubisSingleVMTypeDelay vmDelay = runtimeDelays.get(VMType.getVMTypeIndexByVMType(vmType));
                vmDelay.initializeDelayTable(serviceRate);
        }
        

        public double getDelay(int totalVM, int noOfClients, VMType vmType)
        {
                
                return getDelay(totalVM, noOfClients, VMType.getVMTypeIndexByVMType(vmType));
        }

        public double getDelay(int totalVM, int noOfClients, int vmTypeIndex)
        {

                return runtimeDelays.get(vmTypeIndex).getDelay(totalVM, noOfClients);
        }
}
