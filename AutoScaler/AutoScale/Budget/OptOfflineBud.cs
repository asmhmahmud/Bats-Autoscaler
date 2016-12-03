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
using AutoScaler.Benchmark;

namespace AutoScaler.Budget
{
    class OptOfflineBud
    {

        private Scaler scaler;
        //private WorkloadPredictor wLoadAnalyzer;
        private SimulationSettings simSettings;
        private WorkloadGen workloadGen;

        private RuntimeDelay runtimeDelay;

        private List<SlotInfo> logs = new List<SlotInfo>();

        private int dataUnavailabilityPeriod;
        private OptOffAlg optOffAlgo;
        private DataManager dataManager;

        public OptOfflineBud()
        {

            scaler = new Scaler();
            simSettings = new SimulationSettings();
            //wLoadAnalyzer = new WorkloadPredictor(SimulationSettings.prevTimeSlotToConsider, SimulationSettings.timeSlotLength, SimulationSettings.repetativPatternLength);
            runtimeDelay = new RuntimeDelay(SimulationSettings.runtimeDelayInputFile);

            workloadGen = new WorkloadGen(SimulationSettings.workloadGenInputFile);

            dataManager = new DataManager();
            optOffAlgo = new OptOffAlg(workloadGen, runtimeDelay);


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

            String outputFilePath = "UserBud_OptOffline_Result_" + currTime + ".csv";
            String IntermediateOutputFilePath = "UserBud_OptOffline_Inter_Result_" + currTime + ".csv";
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
        public void test()
        {
            if (SimulationSettings.EnableSystem)
            {

                scaler.ScaleDeployment(3);
            }
            else
            {
                Console.WriteLine("Unable to scale. Enable System");
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
            Console.WriteLine("Starting OptOfflineBud");

            double[] all_bud = SimulationSettings.budFraction;
            double v, totalBudget, queueLength;
            //appendHeader();
            for (int i = 0; i < all_bud.Count(); i++)
            {

                totalBudget = all_bud[i] * SimulationSettings.buBudget;
                v = optOffAlgo.getAppropriateV(totalBudget);                

                queueLength = optOffAlgo.getAppropriateQ(totalBudget, v);
                optOffAlgo.executeExperiment(v, queueLength, totalBudget, dataManager, scaler);

                Console.WriteLine("Total Bud: "+totalBudget+"V: " + v + ",  Q" + queueLength);
                
            }
            dataManager.dumpLogs();
            dataManager.writeExpDetail(workloadGen.availableHours());




        }
    }
}
