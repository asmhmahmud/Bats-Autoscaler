/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.grahpdata;

import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.runtimeDelayTable.RuntimeDelayGenerator;
import azuresimulator.statistics.DataManager;
import azuresimulator.statistics.ExpResult;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amahm008
 */
public class SimValidation
{
        
                public static void main(String[] args)
        {
                SimValidation simValid = new SimValidation();
                simValid.generateSimValidationDataRespVSCl();
                simValid.generateSimValidationDataRespVSInst();

        }
        
        
        public void generateSimValidationDataRespVSCl()
        {
                String fileName1 = "respVsCl.csv";
                try
                {
                        RUBiSRuntimeDelay rbsDelay = new RUBiSRuntimeDelay();
                        
                        double loadFrac;
                        double delay;

                        FileWriter writer = new FileWriter(fileName1);

                        int[] vms = new int[]
                        {
                                10,16
                        };
                        int loadMulFact = 75;
                        for (int j = 0; j < vms.length; j++)
                        {
                                String loadS = vms[j]+"";
                                String resps = vms[j]+"";
                                
                                for (int k = 1; k * loadMulFact < 1600; k++)
                                {
                                        int load = k * loadMulFact;
                                        loadS = loadS+","+load;
                                        resps = resps + "," + rbsDelay.getDelay(vms[j], load, VMType.EXTRA_SMALL);

                                }
                                writer.append(loadS+"\n");
                                writer.append(resps+"\n");
                                
                        }


                        writer.flush();
                        writer.close();

                }
                catch (Exception ex)
                {
                        Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        }

        public void generateSimValidationDataRespVSInst()
        {
                String fileName1 = "respVsInst.csv";
                try
                {
                        RUBiSRuntimeDelay rbsDelay = new RUBiSRuntimeDelay();
                        
                        FileWriter writer = new FileWriter(fileName1);

                        int[] client = new int[]
                        {
                                250, 500
                        };
                        for (int j = 0; j < client.length; j++)
                        {
                                String instants = client[j]+"";
                                String resps = client[j]+"";
                                
                                for (int k = 1; k <= 20; k++)
                                {
                                        instants = instants+","+k;
                                        resps = resps + "," + rbsDelay.getDelay(k, client[j], VMType.EXTRA_SMALL);

                                }
                                writer.append(instants+"\n");
                                writer.append(resps+"\n");
                                
                        }


                        writer.flush();
                        writer.close();

                }
                catch (Exception ex)
                {
                        Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        }
                
        
}
