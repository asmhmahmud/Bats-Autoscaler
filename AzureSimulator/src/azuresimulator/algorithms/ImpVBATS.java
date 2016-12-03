/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.algorithms;

/**
 *
 * @author amahm008
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import azuresimulator.predictor.Predictor;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.statistics.ExpInput;
import java.util.ArrayList;

/**
 *
 * @author Hasan
 */
public class ImpVBATS extends BATS
{
       //private double [] vs = new double []{2000000};

//        private double[] vs = new double[]
//        {
//                0.00001,
//                0.005,
//                0.01,
//                0.0250,
//                0.05,
//                0.1,
//                0.2,
//                0.5,
//                0.8,
//                1,
//                10000000
//        };
        
        private double[] vs = new double[]
        {
                0.00001,
                0.001,
                0.03,
                0.1,
                0.2,
                0.4,
                1,
                2,
                5,
                8,
                10000000
        };        
        
        public ImpVBATS(String name)
        {
                super(name);
        }
        
        private void changeVs()
        {
                int multiplyFactor = 200;
                for (int i = 0; i < vs.length; i++)
                {
                        vs[i] = vs[i] * multiplyFactor;
                }
        }

        @Override
        public void setParameters(
                ResourceProvisioner resourceProvisioner,
                Predictor workloadPredictor,
                ArrayList<ExpInput> workload, 
                double budget , 
                RUBiSRuntimeDelay runDelay, 
                String outputFileName 
        
        )
        {
                super.setParameters(resourceProvisioner, workloadPredictor, workload, budget,  runDelay, outputFileName);
                //changeVs();
        }

        @Override
        public Decision startOfSlot(int slotNo)
        {
                //System.out.println("Slot no: "+slotNo);
                int vIndex = slotNo / workload.size();
                int vSlotNo = slotNo % workload.size();

                if (vSlotNo == 0)
                {
                        resetQueues();
                        //dtm.saveExperimentResults();
                        
                        if(vIndex == vs.length -1)
                        {
                                super.setFixedQueueLengthValue(0);
                        }
                        else
                        {
                                super.setV(vs[vIndex]);
                        }
                        
                        
                        if (vIndex > 0)
                        {
                                System.out.println("V: " + vs[vIndex - 1] + ",  TotVM: " + totalVM);
                                totalVM = 0;
                        }                        

                }

                Decision decission = super.startOfSlot(vSlotNo);
                decission.getExpInput().globalSlotNo = slotNo;
                
                return decission;
        }


        @Override
        public int getTotalTimeSlots()
        {
                int totalSlot;

                totalSlot = workload.size() * vs.length;


                return totalSlot;
        }
        
       @Override
        public void simulationFinished()
        {

        }


}
