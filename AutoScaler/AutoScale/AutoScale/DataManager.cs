/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Samples.WindowsAzure.ServiceManagement;
using AutoScaler.Workload.Prediction;
using System.Threading;
using System.IO;

namespace AutoScaler.AutoScale
{
    class DataManager
    {
        

        private SimulationSettings simSettings;

        Random rnd = new Random(56894251);

        private String outputFilePath;
        private String IntermediateOutputFilePath;
        private RuntimeDelay runtimeDelay;
        
        private List<SlotInfo> logs = new List<SlotInfo>();
        bool headerCreated = false;
        Boolean reactSC = false;
        public DataManager()
        {

            simSettings = new SimulationSettings();
            //wLoadAnalyzer = new WorkloadPredictor(SimulationSettings.prevTimeSlotToConsider, SimulationSettings.timeSlotLength, SimulationSettings.repetativPatternLength);
            runtimeDelay = new RuntimeDelay(SimulationSettings.runtimeDelayInputFile);


            initData();

        }

        public void setReactSC(Boolean ReactSC)
        {
            this.reactSC = ReactSC;
        }

        private String getCurtimeString()
        {
            DateTime curTime = DateTime.Now.ToLocalTime();
            return "" + curTime.DayOfYear+"_"+curTime.TimeOfDay.TotalMilliseconds;
        }

        public void setOutputFileName(String outputFilePath, String IntermediateOutputFilePath)
        {
            this.outputFilePath = outputFilePath;
            this.IntermediateOutputFilePath = IntermediateOutputFilePath;
        }
        private void initData()
        {
            String currTime;

            if (SimulationSettings.EnableSystem)
            {
                currTime = getCurtimeString();
            }
            else
            {
                currTime = "0";

            }

            outputFilePath = "Result_" + currTime + ".csv";
            IntermediateOutputFilePath = "Inter_Result_" + currTime + ".csv";


        }

        public void dumpLogs(  )
        {
            writeToOutputFile();

        }

        private void writeToOutputFile()
        {
            using (System.IO.StreamWriter file = new System.IO.StreamWriter(outputFilePath))
            {
                file.WriteLine(SlotInfo.getCSVHeader());
                foreach (SlotInfo slot in logs)
                {

                    file.WriteLine(slot.getCSVLines());
                    
                }
            }




        }


        private void appendToFile(SlotInfo slot)
        {
            if (!headerCreated)
            {
                using (StreamWriter swh = File.CreateText(IntermediateOutputFilePath))
                {
                    swh.WriteLine(SlotInfo.getCSVHeader());
                }
                headerCreated = true;

            }
            StreamWriter sw = File.AppendText(IntermediateOutputFilePath);
            {
                sw.WriteLine(slot.getCSVLines());

            }
            sw.Close();
        }

        public void addLog(
            DateTime currentSlotStartTime, 
            double queueLength, 
            int slotNo,
            double measuredWorkload, 
            double predictedWorkload,            
            int instance,
            double v,
            double budDeficit

            
            )
        {


            SlotInfo currentSlot = new SlotInfo();
            currentSlot.slotStartTime = currentSlotStartTime;
            currentSlot.queueLength = queueLength;
            currentSlot.slotNo = slotNo;
            currentSlot.measuredWorkload = measuredWorkload;
            currentSlot.predictedWorkload = predictedWorkload;
            currentSlot.instances = instance;
            currentSlot.v = v;
            currentSlot.budDeficit = budDeficit;
            if (!SimulationSettings.EnableSystem)
            {
                double rndm = (0.9+ 1.0*rnd.Next(1,20000)/100000.0);
                Console.WriteLine(rndm);
                currentSlot.responseTime = rndm * runtimeDelay.getDelay(currentSlot.instances, currentSlot.predictedWorkload) ;
                currentSlot.throughPut = rndm * runtimeDelay.getThroughput(currentSlot.instances, currentSlot.predictedWorkload);
                //currentSlot.responseTime = runtimeDelay.getDelay(currentSlot.instances, workloadGen.geActualtWorkloadArrival(currentSlot.slotNo));
            }




            logs.Add(currentSlot);

            if (SimulationSettings.EnableSystem)
            {
                appendToFile(currentSlot);
                Console.WriteLine("Current V: " + currentSlot.v + ", Time Slot: " + currentSlot.slotNo);
                Console.WriteLine("Start Time: " + currentSlot.slotStartTime);
                Console.WriteLine("Current Predicted Workload: " + currentSlot.predictedWorkload + ", Instance Count: " + currentSlot.instances);
                Console.WriteLine("Budget Queue [" + (slotNo + 1) + "]: " + currentSlot.queueLength);
            }

        }


        public void writeExpDetail(int totalTimeSlots)
        {
            using (System.IO.StreamWriter file = new System.IO.StreamWriter(SimulationSettings.expDetailFileName))
            {
                file.WriteLine("fileName," + outputFilePath);
                file.WriteLine("timeslot," + totalTimeSlots);
                file.WriteLine("emptySlot," + SimulationSettings.emptySlotBetweenVs);
                file.WriteLine("simEnable," + (SimulationSettings.EnableSystem ? 0 : 1));
                file.WriteLine("budget," + SimulationSettings.budget);
                file.WriteLine("instancePrice," + SimulationSettings.azureInstancePrice);
                file.WriteLine("reserveDiscount," + SimulationSettings.reserveDiscount);
                file.WriteLine("buBudget," + SimulationSettings.buBudget);

            }
        }

    }
}
