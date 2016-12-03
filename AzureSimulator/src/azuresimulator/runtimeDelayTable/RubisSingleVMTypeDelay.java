/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.runtimeDelayTable;

import azuresimulator.AzureCloudlet;
import azuresimulator.hetrogeneous.vm.VMType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author Hasan
 */
public class RubisSingleVMTypeDelay
{

        private static final int SAFE_TABLE_LENGTH = 5;
        private static final int SEARCH_REMOVE_LENGTH = 5;
        private static final int MAX_DELAY_TO_ENTER_IN_TABLE = 20;
        private static final double MAX_DELAY_PERCENT_TO_IGNORE = 3;

        private int[] vmIndexes;
        private int updateProximity = 2;

        private DecimalFormat dft = new DecimalFormat("###.###");

        public int getUpdateProximity()
        {
                return updateProximity;
        }

        public void setUpdateProximity(int updateProximity)
        {
                this.updateProximity = updateProximity;
        }

        private class TableCell
        {

                double loadFactor;
                double avgDelay;
                boolean isInitialized = false;
                int proximity = 0;

                public TableCell(double loadFactor, double avgDelay)
                {
                        this(loadFactor, avgDelay, false, 0);
                }

                public TableCell(
                        double loadFactor,
                        double avgDelay,
                        boolean isInitialized,
                        int proximity
                )
                {
                        this.loadFactor = loadFactor;
                        this.avgDelay = avgDelay;
                        this.isInitialized = isInitialized;
                        this.proximity = proximity;
                }

                public boolean isPredictedValue()
                {
                        return proximity != 0;
                }

        }

        private class SingleVMDelays
        {

                ArrayList<TableCell> runTimeDelay;

                int noOfVM;

                public SingleVMDelays(int noOfVM, ArrayList<TableCell> runTimeDelay)
                {
                        this.noOfVM = noOfVM;
                        this.runTimeDelay = runTimeDelay;
                        //resetValues();
                }

        }

        private String inputFileName;
        //double[][] runTimeDelay;
        private VMType vmType;
        ArrayList<SingleVMDelays> runtimeDelaysVMs;

        public RubisSingleVMTypeDelay(String fileName)
        {
                //super(name);
                this.inputFileName = fileName;
                runtimeDelaysVMs = new ArrayList<>();
                readAllRuntimeDelay(inputFileName);
        }

        private void readAllRuntimeDelay(String inpFileName)
        {

                try
                {
                        // FileReader reads text files in the default encoding.
                        FileReader fileReader = new FileReader(inpFileName);

                        ArrayList<Double> load = new ArrayList<>();
                        ArrayList<Double> delay = new ArrayList<>();
                        ArrayList<Integer> vms = new ArrayList<>();

                        // Always wrap FileReader in BufferedReader.
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String line;
                        String[] values = bufferedReader.readLine().split(",");
                        int noOfItemsPerVM = Integer.parseInt(values[0]);
                        //System.out.println(noOfItemsPerVM);
                        //Skipping the header
                        bufferedReader.readLine();

                        line = bufferedReader.readLine();
                        while (line != null)
                        {

                                values = line.split(",");
                                vms.add(Integer.parseInt(values[0]));
                                load.add(Double.parseDouble(values[2]));
                                delay.add(Double.parseDouble(values[3]));
                                line = bufferedReader.readLine();
                                //System.out.println(line);
                        }

                        // Always close files.
                        fileReader.close();
                        bufferedReader.close();

                        SingleVMDelays vmDelays;

                        for (int i = 0; i < load.size() / noOfItemsPerVM; i++)
                        {
                                int itemStart = i * noOfItemsPerVM;
                                ArrayList<TableCell> runTimeDelay = new ArrayList<>();
                                for (int j = 0; j < noOfItemsPerVM; j++)
                                {
                                        TableCell tblCell = new TableCell(load.get(j + itemStart), delay.get(j + itemStart), false, 0);
                                        runTimeDelay.add(tblCell);
                                        //System.out.println(runTimeDelay[i][1]);
                                }

                                vmDelays = new SingleVMDelays(vms.get(itemStart), runTimeDelay);
                                //System.out.println(vmDelays.runTimeDelay[0][0]);
                                runtimeDelaysVMs.add(vmDelays);

                        }

                        makeVMIndexes();
//                        for (int j = 0; j < vmIndexes.length; j++)
//                        {
//                                System.out.println(j+" :  "+vmIndexes[j]);
//                        }
//                        System.out.println("*************Size: "+runtimeDelaysVMs.size());
                }
                catch (Exception ex)
                {

                        ex.printStackTrace();
                }

        }

        private void makeVMIndexes()
        {
                int maxVM = runtimeDelaysVMs.get(runtimeDelaysVMs.size() - 1).noOfVM;
                vmIndexes = new int[maxVM ];
                int lastFillIndex = -1;
                int nextFillIndex = 0;
                
                for (int i = 0; i < runtimeDelaysVMs.size() ; i++)
                {
                        
                        nextFillIndex = runtimeDelaysVMs.get(i).noOfVM +
                                (
                                runtimeDelaysVMs.get(Math.min(i+1,runtimeDelaysVMs.size()-1) ).noOfVM - 
                                runtimeDelaysVMs.get(i).noOfVM) / 2  ;

                        for (int j = lastFillIndex + 1; j < nextFillIndex && j <vmIndexes.length; j++)
                        {
                                
                                vmIndexes[j] = i;
                                
                        }

                        lastFillIndex = nextFillIndex;
                }

        }

        public void printDelayTable()
        {
                for (SingleVMDelays vmDelays : runtimeDelaysVMs)
                {
                        printDelayTable(vmDelays);
                }

        }

        public void printDelayTable(int noOfVM)
        {
                for (SingleVMDelays vmDelays : runtimeDelaysVMs)
                {
                        if (vmDelays.noOfVM == noOfVM)
                        {
                                printDelayTable(vmDelays);
                        }
                }

        }

        private void printDelayTable(SingleVMDelays vmDelays)
        {

                System.out.println("\n\nVM Number: " + vmDelays.noOfVM);
                DecimalFormat dft = new DecimalFormat("###.###");
                TableCell curCell;
                String formatString = "%10s%10s%6s%6s%6s\n";
                System.out.format(formatString, "LF", "Delay", "Pred?", "Init?", "Proxi");
                for (int i = 0; i < vmDelays.runTimeDelay.size(); i++)
                {
                        curCell = vmDelays.runTimeDelay.get(i);
                        System.out.format(formatString,
                                dft.format(curCell.loadFactor),
                                dft.format(curCell.avgDelay),
                                curCell.isPredictedValue(),
                                (curCell.isInitialized ? "init" : "N"),
                                curCell.proximity
                        );

                }

        }

        private int getArrayListIndex(int noOfVM)
        {

                int arrayListIndex = 0;
//
//                arrayListIndex = (int) Math.round(Math.log(noOfVM) / Math.log(2));
//
//                arrayListIndex = Math.min(arrayListIndex, runtimeDelaysVMs.size() - 1);
//                arrayListIndex = Math.max(arrayListIndex, 0);

//                if (noOfVM > runtimeDelaysVMs.get(runtimeDelaysVMs.size() - 1).noOfVM)
//                {
//                        arrayListIndex = runtimeDelaysVMs.size() - 1;
//                }
//                else if (noOfVM < runtimeDelaysVMs.get(0).noOfVM)
//                {
//                        arrayListIndex = 0;
//                }
//                else
//                {
//                        int first = 0;
//                        int last = runtimeDelaysVMs.size() - 1;
//                        int middle = (first + last) / 2;
//                        while (first <= last)
//                        {
//                                if (runtimeDelaysVMs.get(middle).array[middle] < search)
//                                {
//                                        first = middle + 1;
//                                }
//                                else if (array[middle] == search)
//                                {
//                                        System.out.println(search + " found at location " + (middle + 1) + ".");
//                                        break;
//                                }
//                                else
//                                {
//                                        last = middle - 1;
//                                }
//
//                                middle = (first + last) / 2;
//                        }
//                        if (first > last)
//                        {
//                                System.out.println(search + " is not present in the list.\n");
//                        }
//                }
                if (noOfVM >= vmIndexes.length)
                {
                        arrayListIndex = vmIndexes[vmIndexes.length - 1];
                }
                else
                {
                        arrayListIndex = vmIndexes[noOfVM];
                }

                //System.out.println("No Of VM: " + noOfVM + ",   indexToUse: " + arrayListIndex);

                return arrayListIndex;
        }

        private ArrayList<TableCell> addDelayTableEntry(double loadFrac, double avgDelay, SingleVMDelays singVMDelay, boolean isInitialized, int proximity)
        {

                ArrayList<TableCell> tblCells = singVMDelay.runTimeDelay;
                int largestSmallestIndex = tblCells.size();
                for (int i = 0; i < tblCells.size(); i++)
                {
                        if (tblCells.get(i).loadFactor > loadFrac)
                        {
                                largestSmallestIndex = i;
                                break;

                        }

                }

                TableCell tblCell = new TableCell(loadFrac, avgDelay, isInitialized, proximity);

                boolean addEndtry = false;

                if (!tblCell.isPredictedValue() || tblCells.isEmpty())
                {
                        addEndtry = true;
                }
                //add if the left endtry is predicted
                else if (tblCells.get((int) Math.min(largestSmallestIndex, tblCells.size() - 1)).isPredictedValue())
                {
                        addEndtry = true;
                }
                //add in the middle of the  table
                else if (largestSmallestIndex < tblCells.size()
                        && (tblCells.size() < SAFE_TABLE_LENGTH))
                {
                        addEndtry = true;
                }
                else
                {
                        //System.out.println("Not added");
                }

                if (tblCell.isPredictedValue())
                {
                        for (int i = largestSmallestIndex; i < tblCells.size(); i++)
                        {
                                if ((!tblCells.get(i).isPredictedValue())
                                        && (tblCells.get(i).avgDelay < avgDelay))
                                {
                                        addEndtry = false;
                                        break;
                                }
                        }
                }

                if (addEndtry)
                {
                        tblCells.add(largestSmallestIndex, tblCell);
                }

                if (!tblCell.isPredictedValue())
                {
                        searchAndRemove(tblCells, largestSmallestIndex);
                }
                else if (addEndtry)
                {
                        searchAndRemoveInit(tblCells, largestSmallestIndex);
                }

                return tblCells;
        }

        private void searchAndRemoveInit(ArrayList<TableCell> tblCells, int index)
        {
                index = Math.min(index, tblCells.size() - 1);

                //System.out.println("Searching and removing init");
                TableCell opCell = tblCells.get(index);

                for (int i = index - 1, j = 0; i >= 0; i--, j++)
                {
                        if (tblCells.size() < SAFE_TABLE_LENGTH
                                || j > SEARCH_REMOVE_LENGTH)
                        {
                                break;
                        }
                        if (tblCells.get(i).isInitialized)
                        {
                                tblCells.remove(i);
                        }

                }

                index = tblCells.indexOf(opCell);
                TableCell curCell;
                for (int i = index - 1, j = 0; i >= 0; i--, j++)
                {
                        curCell = tblCells.get(i);
                        if ((curCell.avgDelay > opCell.avgDelay) && !curCell.isPredictedValue())
                        {
                                tblCells.remove(i);
                        }

                }

        }

        private void searchAndRemove(ArrayList<TableCell> tblCells, int index)
        {

                TableCell opCell = tblCells.get(index);

                for (int i = index - 1, j = 0; i >= 0; i--, j++)
                {
                        if (tblCells.size() < SAFE_TABLE_LENGTH
                                || j > SEARCH_REMOVE_LENGTH)
                        {
                                break;
                        }
                        if (tblCells.get(i).isPredictedValue())
                        {
                                tblCells.remove(i);
                        }

                }

                index = tblCells.indexOf(opCell);
                TableCell curCell;
                for (int i = index - 1, j = 0; i >= 0; i--, j++)
                {
                        curCell = tblCells.get(i);
                        if (curCell.avgDelay > opCell.avgDelay)
                        {
                                tblCells.remove(i);
                        }

                }

        }

        public void initializeDelayTable(double serviceRate)
        {

                boolean predicted = true;

                for (int i = 0; i < runtimeDelaysVMs.size(); i++)
                {
                        SingleVMDelays singVMDelay = runtimeDelaysVMs.get(i);
                        singVMDelay.runTimeDelay.clear();

                }

                double initThres = 2.5, j;
                for (int i = 0; i < runtimeDelaysVMs.size(); i++)
                {
                        SingleVMDelays singVMDelay = runtimeDelaysVMs.get(i);
                        for (j = 0; j < serviceRate; j = j + initThres)
                        {

                                double avgDelay = 1 / (serviceRate - j);
                                addDelayTableEntryToVMNumbers(j, avgDelay, predicted, singVMDelay, true, 0);

                        }

                        for (j = j - initThres; j < serviceRate; j = j + 0.25)
                        {

                                double avgDelay = 1 / (serviceRate - j);
                                addDelayTableEntryToVMNumbers(j, avgDelay, predicted, singVMDelay, true, 0);

                        }

                }

        }

        public void addDelayTableEntry(int noOfVM, int noOfClients, double avgDelay)
        {

                if (avgDelay > MAX_DELAY_TO_ENTER_IN_TABLE)
                {
                        return;
                }

                System.out.println("Updating Table- Total VM:" + noOfVM + ", No of Client: " + noOfClients + ",  Avg Delay: " + dft.format(avgDelay));
                int arrayListIndex = getArrayListIndex(noOfVM);
                boolean predicted;
                double loadFrac = 1.0 * noOfClients / noOfVM;
                for (int i = 0; i < runtimeDelaysVMs.size(); i++)
                {
                        SingleVMDelays singVMDelay = runtimeDelaysVMs.get(i);
                        if (i == arrayListIndex)
                        {
                                predicted = false;
                        }
                        else
                        {
                                predicted = true;
                        }

                        addDelayTableEntryToVMNumbers(loadFrac, avgDelay, predicted, singVMDelay, false, i - arrayListIndex);

                }

        }

        private void addDelayTableEntryToVMNumbers(double loadFrac, double avgDelay, boolean predicted, SingleVMDelays singVMDelay, boolean isInitialized, int proximity)
        {

                double tableDelay = getDelay(loadFrac, singVMDelay.runTimeDelay);
                double diff = Math.abs(tableDelay - avgDelay) / tableDelay * 100;
                //System.out.println("Difference: "+dft.format(diff) + "%");
                if (diff > MAX_DELAY_PERCENT_TO_IGNORE)
                {

                        ArrayList<TableCell> tblCells = addDelayTableEntry(loadFrac, avgDelay, singVMDelay, isInitialized, proximity);

                }

        }

        public double getDelay(int noOfVM, int noOfClients)
        {
                int arrayListIndex = getArrayListIndex(noOfVM);

                //System.out.println("VM Selected3: " + noOfVM);
                double delay = getDelay(1.0 * noOfClients / noOfVM, runtimeDelaysVMs.get(arrayListIndex).runTimeDelay);
                return delay;

        }

        private double getDelay(double loadFrac, ArrayList<TableCell> runTimeDelay)
        {
                //System.out.println("LoadFrac: " + loadFrac);
                double delay = Double.MAX_VALUE;

                if (runTimeDelay == null || runTimeDelay.isEmpty())
                {
                        //System.out.println("Delay Null");
                        delay = Double.MAX_VALUE;
                }
                else if (loadFrac <= runTimeDelay.get(0).loadFactor)
                {
                        delay = runTimeDelay.get(0).avgDelay;
                }

                else if (runTimeDelay.size() > 1)
                {
                        //System.out.println("Load Frac: " + loadFrac);
                        int lastSmallestIndex = 0;
                        if (loadFrac >= runTimeDelay.get(runTimeDelay.size() - 1).loadFactor)
                        {
//                                double exponent = loadFrac / runTimeDelay[runTimeDelay.length - 1][0];
//                                System.out.println("Exponent: " + exponent);
//                                exponent = Math.min(10, exponent);
//                                delay = Math.pow(runTimeDelay[runTimeDelay.length - 1][1], exponent);
                                //System.out.println("Runtime Delay greater: "+loadFrac+",  exp: "+loadFrac / runTimeDelay[runTimeDelay.length - 1][0]+",  base: "+runTimeDelay[runTimeDelay.length - 1][1]);
                                lastSmallestIndex = Math.max(0, runTimeDelay.size() - 2);
                        }
                        else
                        {  //implement a binary search here ... will increase the speed

                                for (int j = 0; j < runTimeDelay.size(); j = j + 1)
                                {
                                        //System.out.println("Test");
                                        if (loadFrac < runTimeDelay.get(j).loadFactor)
                                        {
                                                lastSmallestIndex = Math.max(0, j - 1);
                                                break;
                                        }
                                }
                        }
                        //System.out.println("last SM index: " + lastSmallestIndex);
                        int nextIndex = Math.min(runTimeDelay.size() - 1, lastSmallestIndex + 1);

                        double arrival_x1 = runTimeDelay.get(lastSmallestIndex).loadFactor;
                        double arrival_x2 = runTimeDelay.get(nextIndex).loadFactor;
                        double delay_y1 = runTimeDelay.get(lastSmallestIndex).avgDelay;
                        double delay_y2 = runTimeDelay.get(nextIndex).avgDelay;

                        delay = delay_y1 + (loadFrac - arrival_x1) * (delay_y2 - delay_y1) / (arrival_x2 - arrival_x1);

                        //if (delay < 0.1)
//                        {
//                                System.out.println(
//                                        "Small Index: " + lastSmallestIndex
//                                        + ",   Length: " + runTimeDelay.length
//                                        + ",   x1 " + arrival_x1
//                                        + ",   x2 " + arrival_x2
//                                        + ",   y1 " + delay_y1
//                                        + ",   y2 " + delay_y2
//                                        + ",  delay: " + delay);
//                        }
                }

                //System.out.println("LoadFrac: "+loadFrac+",  Delay: "+delay);
                return delay;
        }

        public VMType getVmType()
        {
                return vmType;
        }

        public void setVmType(VMType vmType)
        {
                this.vmType = vmType;
        }
}
