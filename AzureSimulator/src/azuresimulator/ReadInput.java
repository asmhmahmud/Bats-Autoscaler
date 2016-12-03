/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator;

import azuresimulator.statistics.ExpInput;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author Hasan
 */
public class ReadInput
{

        public ArrayList<ExpInput> getInput(String fileName)
        {
                // The name of the file to open.

                //String fileName = "azureLoadmsr_sc.csv";
                // This will reference one line at a time
                String line = null;
                ArrayList<ExpInput> inputs = new ArrayList<ExpInput>();
                int expSettingsCol = 1;

                try
                {
                        // FileReader reads text files in the default encoding.
                        FileReader fileReader = new FileReader(fileName);

                        // Always wrap FileReader in BufferedReader.
                        BufferedReader bufferedReader = new BufferedReader(fileReader);

                        //Skipping the header
                        //bufferedReader.readLine();
                        int index = 0;
                        for (int i = 0; i < Settings.WORKLOAD_ROW_START; i++)
                        {
                                bufferedReader.readLine();
                        }
                        while ((line = bufferedReader.readLine()) != null)
                        {

                                String[] values = line.split(",");
                                //info.globalSlotNo = Integer.parseInt(values[0]);

                                for (int i = 0; i < Settings.REPEAT_EACH_WORKLOAD_ROW; i++)
                                {
                                        ExpInput info = new ExpInput();
                                        info.globalSlotNo = index;
                                        info.noOfClients = (int) ((Double.parseDouble(values[0])) * Settings.WORKLOAD_MULTIPLIER);
                                        inputs.add(info);
                                }

                                index++;

                                if (index >= Settings.TOTAL_TIME_SLOTS)
                                {
                                        System.out.println(inputs.size());
                                        break;
                                }

                        }

                        // Always close files.
                        fileReader.close();
                        bufferedReader.close();
                }
                catch (Exception ex)
                {

                        ex.printStackTrace();
                }

                return inputs;
        }

        public void printExp(ArrayList<ExpInput> experiment)
        {
                for (ExpInput curSlot : experiment)
                {
                        System.out.println("Slot No: " + curSlot.globalSlotNo + ", Load: " + curSlot.noOfClients);
                }
        }

}
