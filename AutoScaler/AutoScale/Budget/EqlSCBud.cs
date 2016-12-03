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

namespace AutoScaler.Budget
{
    class EqlSCBud
    {

        private Scaler scaler;
        //private WorkloadPredictor wLoadAnalyzer;
        private SimulationSettings simSettings;
        private WorkloadGen workloadGen;

        private RuntimeDelay runtimeDelay;

        private List<SlotInfo> logs = new List<SlotInfo>();
        private DataManager dataManager;

        public EqlSCBud()
        {

            scaler = new Scaler();
            simSettings = new SimulationSettings();
            dataManager = new DataManager();
            //wLoadAnalyzer = new WorkloadPredictor(SimulationSettings.prevTimeSlotToConsider, SimulationSettings.timeSlotLength, SimulationSettings.repetativPatternLength);
            runtimeDelay = new RuntimeDelay(SimulationSettings.runtimeDelayInputFile);

            workloadGen = new WorkloadGen(SimulationSettings.workloadGenInputFile);

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

            String outputFilePath = "UserBud_EqlSC_Result_" + currTime + ".csv";
            String IntermediateOutputFilePath = "UserBud_EqlSC_Inter_Result_" + currTime + ".csv";

            dataManager.setOutputFileName(outputFilePath, IntermediateOutputFilePath);

        }

        public void StartAutoScaling()
        {
            Console.WriteLine("Starting EqlSCBud");
            
            int current_t, t_start = 0, t_end = workloadGen.availableHours() - 1;

            //double avg_budget = SimulationSettings.budget / t_end;

            DateTime experimentStartTime = DateTime.Now, nextSlotFinishTime = DateTime.Now;
            double budDeficit = 0, perslotBudDef = 0; ;


            //if (SimulationSettings.strictTiming == true)
            //{
            //    experimentStartTime = dtUtility.SleepUntillStartTime();
            //}


            nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);

            double[] all_bud = SimulationSettings.budFraction;
            double currentBudget;
            int optimal_instances = 0, prevOptimalInstance = 0;
            double predicted_workload;
            double meanBudget;
            for (int i = 0; i < all_bud.Count(); i++)
            {
                currentBudget = all_bud[i] * SimulationSettings.buBudget;                
                optimal_instances = (int)Math.Round(currentBudget / workloadGen.availableHours() / SimulationSettings.azureInstancePrice/SimulationSettings.reserveDiscount);
                meanBudget = currentBudget / workloadGen.availableHours();
                current_t = t_start;
                predicted_workload = workloadGen.getWorkloadArrival(current_t);
                budDeficit = 0;
                perslotBudDef = SimulationSettings.azureInstancePrice * optimal_instances * SimulationSettings.reserveDiscount - meanBudget;
                dataManager.addLog(DateTime.Now, -100, current_t, -100, predicted_workload, optimal_instances, -100, budDeficit);

                if (SimulationSettings.EnableSystem)
                {
                    scaler.ScaleDeployment(optimal_instances);
                }

                if (SimulationSettings.EnableSystem)
                {
                    Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));
                }
                nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);

                prevOptimalInstance = optimal_instances;

                for (current_t = t_start + 1; current_t <= t_end; current_t++)
                {

                    predicted_workload = workloadGen.getWorkloadArrival(current_t);
                    //Console.WriteLine(predicted_workload);  

                    if (SimulationSettings.EnableSystem)
                    {

                        Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));

                    }

                    //Console.WriteLine(current_t);
                    budDeficit = budDeficit + perslotBudDef;
                    dataManager.addLog(DateTime.Now, 0, current_t, 0, predicted_workload, optimal_instances, 0, budDeficit);


                    nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);
                    prevOptimalInstance = optimal_instances;


                }


                for (int j = 0; j < SimulationSettings.emptySlotBetweenVs; j++)
                {
                    if (j != (SimulationSettings.emptySlotBetweenVs -1) && SimulationSettings.EnableSystem)
                    {
                        Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));
                    }
                    dataManager.addLog(DateTime.Now, 0, current_t, 1, 1, 1, 0, 1 );
                    nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);
                }

            }
            dataManager.dumpLogs();

        }
    }
}
