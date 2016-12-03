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
using AutoScaler.AutoScale;

namespace AutoScaler.Benchmark
{
    class ReactSC
    {

        private Scaler scaler;
        //private WorkloadPredictor wLoadAnalyzer;
        private SimulationSettings simSettings;
        private WorkloadGen workloadGen;


        private RuntimeDelay runtimeDelay;

        private List<SlotInfo> logs = new List<SlotInfo>();

        private int dataUnavailabilityPeriod;
        private ReactSCAlg ourAlg;
        private DataManager dataManager;
        public ReactSC()
        {

            scaler = new Scaler();
            simSettings = new SimulationSettings();
            dataManager = new DataManager();
            //wLoadAnalyzer = new WorkloadPredictor(SimulationSettings.prevTimeSlotToConsider, SimulationSettings.timeSlotLength, SimulationSettings.repetativPatternLength);
            runtimeDelay = new RuntimeDelay(SimulationSettings.runtimeDelayInputFile);

            workloadGen = new WorkloadGen(SimulationSettings.workloadGenInputFile_sc);
            //workloadGen.printWorkload();
            ourAlg = new ReactSCAlg(workloadGen, runtimeDelay);
            initData();

        }

        private String getCurtimeString()
        {
            DateTime curTime = DateTime.Now.ToLocalTime();
            return "" + curTime.DayOfYear + "_" + curTime.TimeOfDay.TotalMilliseconds;
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

            String outputFilePath = "ReactSC_Result_" + currTime + ".csv";
            String IntermediateOutputFilePath = "ReactSC_Inter_Result_" + currTime + ".csv";
            dataManager.setOutputFileName(outputFilePath, IntermediateOutputFilePath);
            //this.arrivals = simSettings.arrivals;
            //this.runtimeDelayQueue = simSettings.runtimeDelayQueue;


            using (StreamWriter sw = File.CreateText(IntermediateOutputFilePath))
            {
                sw.WriteLine(SlotInfo.getCSVHeader());
            }

            dataUnavailabilityPeriod = SimulationSettings.repetativPatternLength;
            if ((WorkloadPredictor.timeSlotBack + 1) > SimulationSettings.repetativPatternLength)
            {
                dataUnavailabilityPeriod = (int)Math.Ceiling(
                    ((double)WorkloadPredictor.timeSlotBack + 1) / SimulationSettings.repetativPatternLength)
                    * SimulationSettings.repetativPatternLength;
            }

        }


        private int setInitialInstanceCount()
        {
            int initialInstanceCount = scaler.GetCurrentInstanceCount();
            int initialTargetInstanceCount = 2;

            if (initialInstanceCount != 2)
            {
                Console.WriteLine("Setting the initial instance count to " + initialTargetInstanceCount);
                scaler.ScaleDeployment(initialTargetInstanceCount);
            }

            return initialTargetInstanceCount;
        }


        public void StartAutoScaling()
        {

            ourAlg.executeExperiment(1, SimulationSettings.budget, true, dataManager, scaler);

            Console.WriteLine("\n\nReactSC finished");

            dataManager.dumpLogs();


        }


    }
}
