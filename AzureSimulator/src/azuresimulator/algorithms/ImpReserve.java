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
import azuresimulator.Settings;
import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.predictor.Predictor;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.statistics.ExpInput;
import edu.fiu.scg.util.CSVUtil;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hasan
 */
public class ImpReserve extends ReserveInstance
{

        private int[] reserveInst;

        public ImpReserve(String name)
        {
                super(name);

        }

        private int[] getOptimalResInstVar()
        {
                int maxInstToConsider = 1000;
                int inc = 10;

                int totItems = maxInstToConsider / inc + 1;

                ArrayList<Integer> instList = new ArrayList<>();

                //System.out.println(totItems);
                for (int i = 0; i < totItems; i++)
                {

                        if (i *inc* workload.size() * Settings.RESERVE_INSTANCE_DISCOUNT * VMType.EXTRA_SMALL.getPrice() > budget)
                        {
                                break;
                        }
                        instList.add(i * inc);
                        // System.out.println("Res: "+reserveInst[i]);
                }
                int[] insts = new int[instList.size()];
                for (int i = 0; i < insts.length; i++)
                {
                        insts[i] = instList.get(i);
                }
                
//                System.out.println("Max: "+instList.get(instList.size()-1));
//                System.exit(0);
                return insts;

        }

        @Override
        public void setParameters(
                ResourceProvisioner resourceProvisioner,
                Predictor workloadPredictor,
                ArrayList<ExpInput> workload,
                double budget,
                RUBiSRuntimeDelay runDelay,
                String outputFileName
        )
        {
                super.setParameters(resourceProvisioner, workloadPredictor, workload, budget, runDelay, outputFileName);
                setDataSavingFrequency(1000);
                reserveInst = getOptimalResInstVar();
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

                        VMNumbers resInst = new VMNumbers();
                        resInst.addVMNumberDecission(VMType.EXTRA_SMALL, reserveInst[vIndex]);
                        super.setReserveInstances(resInst);

                }

                Decision decission = super.startOfSlot(vSlotNo);
                decission.getExpInput().globalSlotNo = slotNo;

                return decission;
        }

        @Override
        public int getTotalTimeSlots()
        {
                int totalSlot;

                totalSlot = workload.size() * reserveInst.length;

                return totalSlot;
        }
        
        
        @Override
        public void simulationFinished()
        {
                System.out.println("\n\n**********************************************************Resereve Inst\n");
                //CSVUtil.writeCSV(columnHeader, queueLength);
                for (int i = 0; i < reserveInst.length; i++)
                {
                        System.out.print(reserveInst[i]+",");
                }
        }

}
