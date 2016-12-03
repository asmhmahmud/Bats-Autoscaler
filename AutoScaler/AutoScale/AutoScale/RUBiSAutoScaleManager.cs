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
    class RUBiSAutoScaleManager
    {
        
        private Scaler scaler;
        //private WorkloadPredictor wLoadAnalyzer;
        private SimulationSettings simSettings;
        private WorkloadGen workloadGen;


        private RuntimeDelay runtimeDelay;
        
        private List<SlotInfo> logs = new List<SlotInfo>();

        private int dataUnavailabilityPeriod;
        private OurAlg ourAlg;
        private DataManager dataManager;
        public RUBiSAutoScaleManager()
        {

            scaler = new Scaler();
            simSettings = new SimulationSettings();
            dataManager = new DataManager();
            //wLoadAnalyzer = new WorkloadPredictor(SimulationSettings.prevTimeSlotToConsider, SimulationSettings.timeSlotLength, SimulationSettings.repetativPatternLength);
            runtimeDelay = new RuntimeDelay(SimulationSettings.runtimeDelayInputFile);

            workloadGen = new WorkloadGen(SimulationSettings.workloadGenInputFile);
            ourAlg = new OurAlg(workloadGen, runtimeDelay);
            initData();

        }

        private String getCurtimeString()
        {
            DateTime curTime = DateTime.Now.ToLocalTime();
            return "" + curTime.DayOfYear+"_"+curTime.TimeOfDay.TotalMilliseconds;
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

            String outputFilePath = "Result_" + currTime + ".csv";
            String IntermediateOutputFilePath = "Inter_Result_" + currTime + ".csv";
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
                dataUnavailabilityPeriod =(int) Math.Ceiling(
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
            
            //double[] all_v = new double[] {0.01, 50 };
            //double[] all_v = new double[] { .001, 0.005, 0.01, 0.05, 0.1, 0.3, 0.7, 1, 3, 7, 10, 50, 100, 500, 1000 };
            //double[] all_v = new double[] {0.00001, 0.0001, 0.0003, .001, 0.01, 0.1, 100 };
            //double[] all_v = new double[] {  0.01, 0.1, 1, 10, 100,1000 };

            List<double> allVList = new List<double>();
            //allVList.Add(0.00000001);
            //allVList.Add(0.005);
            //allVList.Add(0.01);
            //allVList.Add(0.03);
            //allVList.Add(0.04);
            //allVList.Add(0.07);
            //allVList.Add(0.1);
            //allVList.Add(0.15);
            //allVList.Add(0.2);
            //allVList.Add(0.3);
            //allVList.Add(1);
            //allVList.Add(5);
            //allVList.Add(10);
            allVList.Add(10000);
            //allVList.Add(10000);
            //for (double i = -2; i <= -1; i += 0.125)
            //{
            //    allVList.Add(Math.Pow(10, i));
                
            //}


            double[] all_v = allVList.ToArray();
            //double[] all_v = new double[]{100};

            Array.Sort(all_v);
            int t_start = 0, t_end = workloadGen.availableHours()-1;
            
            //double avg_budget = SimulationSettings.budget / t_end;


            double v;
            Boolean budUnaware = false;



            //appendHeader();
            for (int i = 0; i < all_v.Count(); i++)
            {

                v = all_v[i];


                if (i == all_v.Count() - 1)
                {
                    budUnaware = true;
                }
                else
                {
                    budUnaware = false;
                }
                ourAlg.executeExperiment(v, SimulationSettings.budget, budUnaware, dataManager, scaler);

                Console.WriteLine("\n\n************ V Finished ********************");
            }
            dataManager.dumpLogs();
            dataManager.writeExpDetail(t_end - t_start + 1);
              
        }


    }
}
