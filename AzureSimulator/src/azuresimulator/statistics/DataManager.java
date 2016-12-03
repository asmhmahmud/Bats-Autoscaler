/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Frank
 */
public class DataManager
{

        private String fileName;
        String filePath;
        String detailFolderName;
        private int previousSlotAppended =0;        
        private boolean fileHeaderCreated = false;        
        private ArrayList<ExpResult> experimentResult;

        
        

        
       public DataManager(String fileName,  ArrayList<ExpResult> experimentResult)
        {

                this.fileName = fileName;
                this.experimentResult = experimentResult;
                

                
                

        }

        private void createHeaders( String fileName)
        {
                try
                {
                        if(!experimentResult.isEmpty()  && !fileHeaderCreated)
                        {
                                FileWriter writer = new FileWriter(fileName);
                                //System.out.println(experimentResult.get(0).getClass());
                                writer.append(experimentResult.get(0).getCSVHeader());
                                writer.append("\n");

                                writer.flush();
                                writer.close();
                                fileHeaderCreated = true;
                        }
                }
                catch (IOException e)
                {
                        e.printStackTrace();
                }
        }

        public void appendLog(ExpResult slotInfo)
        {

                try
                {
                        FileWriter writer = new FileWriter(getFileName(), true);
                        writer.append(slotInfo.getCSVFormattedData());
                        writer.append("\n");

                        writer.flush();
                        writer.close();

                }
                catch (IOException ex)
                {
                        Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                }


        }

        private void writeSlot(FileWriter writer, ExpResult slotInfo) throws Exception
        {


                writer.append(slotInfo.getCSVFormattedData());

                writer.append("\n");

        }
        
        
//        public void saveAllExperiment()
//        {
//
//                
//                try
//                {
//                        
//                        FileWriter writer = new FileWriter(getFileName(), true);
//                        System.out.println("Size: "+experimentResult.size());
//                        for(ExpResult slotInfo:experimentResult)
//                        {
//                                writeSlot(writer, slotInfo);
//                        }
//                        
//                        writer.flush();
//                        writer.close();
//
//                }
//                catch (Exception ex)
//                {
//                        Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//
//        }



        public void saveExperimentResults()
        {
                if(!fileHeaderCreated)
                {
                        createHeaders(fileName);
                }

                try
                {
                        FileWriter writer = new FileWriter(fileName, true);
                        int lastAvailableSlot = experimentResult.size();
                        
                        for (int i = previousSlotAppended; i < lastAvailableSlot; i++)
                        {
                                ExpResult slotInfo = experimentResult.get(i);
                                writeSlot(writer, slotInfo);
                        }
                        
                        previousSlotAppended = lastAvailableSlot;
                        
                        
                        
                        writer.flush();
                        writer.close();

                }
                catch (Exception ex)
                {
                        Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                }

        }

//        public void writeDetailStat(ExpResult slotInfo)
//        {
//                String folderDir = filePath + "/" + detailFolderName;
//                File newFolder = new File(folderDir);
//
//                newFolder.mkdir();
//
//                DetailStat dtStat = slotInfo.getDetailStat();
//                if (dtStat != null && enablePercentile)
//                {
//                        try
//                        {
//                                FileWriter writer = new FileWriter(folderDir + "/" + slotInfo.decission.getExpInput().globalSlotNo + ".csv");
//                                writer.append("Category Name, Total Count (With Back Browsing), Count");
//                                writer.append("\n");
////                                for (int i = 0; i < dtStat.getDetailStatList().length; i++)
////                                {
////                                        writer.append(dtStat.getCSVValue(i));
////                                        writer.append("\n");
////                                }
//
//                                writer.flush();
//                                writer.close();
//                        }
//                        catch (IOException e)
//                        {
//                                e.printStackTrace();
//                        }
//                }
//
//        }



        /**
         * @return the fileName
         */
        public String getFileName()
        {
                return fileName;
        }

        /**
         * @param fileName the fileName to set
         */
        public void setFileName(String fileName)
        {
                this.fileName = fileName;
        }
}
