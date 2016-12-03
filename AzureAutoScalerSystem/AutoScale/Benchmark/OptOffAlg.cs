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
    class OptOffAlg : OurAlg
    {
        public OptOffAlg(WorkloadGen workloadGen, RuntimeDelay runtimeDelay)
            : base(workloadGen, runtimeDelay)
        {

        }

        public void executeExperiment(double v, double queueLength, double totalBudget, DataManager dataManager, Scaler scaler)
        {
            double[] hourlyBudget = getHourlyBudget(totalBudget);
            int current_t, t_start = 0, t_end = workloadGen.availableHours() - 1;

            //double avg_budget = SimulationSettings.budget / t_end;

            DateTime experimentStartTime = DateTime.Now, nextSlotFinishTime;
            double budDeficit = 0;


            //if (SimulationSettings.strictTiming == true)
            //{
            //    experimentStartTime = dtUtility.SleepUntillStartTime();
            //}
            nextSlotFinishTime = experimentStartTime.Add(SimulationSettings.timeSlotLength);

            int optimal_instances = 0, prevOptimalInstance = 0;
            double  predicted_workload;
            current_t = t_start;

            predicted_workload = workloadGen.getWorkloadArrival(current_t);
            optimal_instances = findOptimalInstanceCount(v, queueLength, predicted_workload);
            budDeficit = SimulationSettings.azureInstancePrice * optimal_instances - hourlyBudget[current_t];
            dataManager.addLog(DateTime.Now, queueLength, current_t, 0, predicted_workload, optimal_instances, v, budDeficit );

            if (SimulationSettings.EnableSystem)
            {
                scaler.ScaleDeployment(optimal_instances);
            }
            prevOptimalInstance = optimal_instances;

            for (current_t = t_start + 1; current_t <= t_end; current_t++)
            {

                predicted_workload = workloadGen.getWorkloadArrival(current_t);
                //Console.WriteLine(predicted_workload);  
                optimal_instances = findOptimalInstanceCount(v, queueLength, predicted_workload);
                //optimal_instances = findOptimalInstanceCount(v, 0, predicted_workload);




                if (SimulationSettings.EnableSystem)
                {
                    if (prevOptimalInstance < optimal_instances)
                    {
                        Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now) - SimulationSettings.vmStartUpTime);
                        scaler.ScaleDeployment(optimal_instances);
                        Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));
                        //wLoadAnalyzer.startOfSlot(current_t);
                    }
                    else
                    {
                        Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));
                        //wLoadAnalyzer.startOfSlot(current_t);
                        Thread.Sleep(TimeSpan.FromSeconds(5));
                        scaler.ScaleDeployment(optimal_instances);
                    }
                }
                //Console.WriteLine(current_t);
                budDeficit = budDeficit + (double)SimulationSettings.azureInstancePrice * optimal_instances - hourlyBudget[current_t];
                dataManager.addLog(DateTime.Now, queueLength, current_t, 0, predicted_workload, optimal_instances, v, budDeficit );


                nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);
                prevOptimalInstance = optimal_instances;


            }


            for (int j = 0; j < SimulationSettings.emptySlotBetweenVs; j++)
            {
                if (SimulationSettings.EnableSystem)
                {
                    Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));
                }
                dataManager.addLog(DateTime.Now, 0, current_t, 1, 1, 1, v, 1 );
                nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);
            }
            if (SimulationSettings.EnableSystem)
            {
                Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));
            }
            nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);

            //Console.WriteLine("\n\n************ V Finished ********************");
            if (!SimulationSettings.EnableSystem)
            {
                Console.WriteLine("Current Buddef: " + budDeficit + ", q=" + queueLength);
                Console.WriteLine("\n\n");
            }


        }


        public double getAppropriateQ(double budget, double v)
        {
            double[] hourlyBudget = getHourlyBudget(budget);
            int current_t, t_start = 0, t_end = workloadGen.availableHours() - 1;
            double budDeficit = 0;
            double predicted_workload;
            double lhs = 0, rhs = 1000, optimal_instances;
            double queueLength = double.MaxValue;
            int iteration = 0;


            budDeficit = Double.MaxValue;
            iteration = 0;

            while (Math.Abs(budDeficit) > .001 && iteration < maxIteration)
            {
                queueLength = (lhs + rhs) / 2;
                budDeficit = 0;
                for (current_t = t_start; current_t <= t_end; current_t++)
                {

                    predicted_workload = workloadGen.getWorkloadArrival(current_t);
                    optimal_instances = findOptimalInstanceCount(v, queueLength, predicted_workload);
                    budDeficit = budDeficit + (double)SimulationSettings.azureInstancePrice * optimal_instances - hourlyBudget[current_t];

                }

                if (budDeficit > 0)
                {
                    lhs = queueLength;
                }

                else
                {
                    rhs = queueLength;
                }
                iteration++;

            }

            return queueLength;
        }






    }



}
