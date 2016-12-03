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
    class ReactSCAlg
    {

        protected WorkloadGen workloadGen;
        protected RuntimeDelay runtimeDelay;
        protected int maxIteration = 100;
        public ReactSCAlg(WorkloadGen workloadGen, RuntimeDelay runtimeDelay)
        {

            this.workloadGen = workloadGen;
            this.runtimeDelay = runtimeDelay;


        }
        public void setMaxIteration(int maxIteration)
        {
            this.maxIteration = maxIteration;
        }

        public int findOptimalInstanceCount(double v, double queueLength, double predictedWorkload)
        {
            double currentOptVal, minOptVal = double.MaxValue;
            int minInstCount = SimulationSettings.maxInstanceCount;


            double price = 0;
            double delayVal = 0;
            //Console.WriteLine(queueLength);
            if (queueLength <= 0)
            {

                for (int currentInstanceCount = 1; currentInstanceCount <= SimulationSettings.maxInstanceCount; currentInstanceCount++)
                {
                    if (runtimeDelay.getDelay(currentInstanceCount, predictedWorkload) <= SimulationSettings.delayThreshold)
                    {
                        //Console.WriteLine(runtimeDelay.getDelay(currentInstanceCount, predictedWorkload));
                        minInstCount = currentInstanceCount;
                        break;
                    }
                }


            }
            else
            {

                for (int currentInstanceCount = 1; currentInstanceCount <= SimulationSettings.maxInstanceCount; currentInstanceCount++)
                {

                    //Console.WriteLine("arrivals: " + arrivals[i] + ", inst: " + instances[instCount] + ", delay: " + runtimeDelayQueue[i, instCount] + ", Queue: " + queueLength);
                    //price = SimulationSettings.azurePrice * (currentInstanceCount )*SimulationSettings.beta;
                    //optVal = v * (runtimeDelay.getDelay(currentInstanceCount,predictedWorkload)+price) + queueLength * price;
                    delayVal = runtimeDelay.getDelay(currentInstanceCount, predictedWorkload);



                    price = SimulationSettings.azureInstancePrice * (currentInstanceCount) * SimulationSettings.beta;
                    //delayVal = runtimeDelay.getDelay(currentInstanceCount, predictedWorkload);




                    currentOptVal = v * (delayVal) + queueLength * price;


                    if (minOptVal > currentOptVal)
                    {
                        minOptVal = currentOptVal;
                        minInstCount = currentInstanceCount;
                    }
                    //Console.WriteLine("Opt Val: " + optVal + ", Min Inst: " + minInstCount);
                    if (delayVal <= SimulationSettings.delayThreshold)
                    {
                        //Console.WriteLine("Here");
                        //minInstCount = currentInstanceCount;
                        break;
                    }

                }


                for (int currentInstanceCount = minInstCount; currentInstanceCount <= SimulationSettings.maxInstanceCount; currentInstanceCount++)
                {
                    delayVal = runtimeDelay.getDelay(currentInstanceCount, predictedWorkload);
                    if (delayVal <= SimulationSettings.delayCap)
                    {
                        minInstCount = currentInstanceCount;
                        break;

                    }

                }



            }

            //price = SimulationSettings.azurePrice * (minInstCount);
            //delayVal = predictedWorkload * runtimeDelay.getDelay(minInstCount, predictedWorkload) * SimulationSettings.beta;

            //if (SimulationSettings.EnableSystem)
            //{
            //    Console.WriteLine("Delay: " + delayVal + ", Price: " + price);

            //    Console.WriteLine("V*Delay: " + v * (delayVal) + ", V*Price: " + v * (price) + ", Price: " + (price * queueLength));

            //    Console.WriteLine("Optimal Instance Count: " + minInstCount);
            //}
            return minInstCount;
        }

        public double[] getHourlyBudget(double totalBudget)
        {
            int totalHour = workloadGen.availableHours();
            int noOfHourInDays = SimulationSettings.repetativPatternLength;
            double[] hourlyBudget = new double[totalHour];
            double[] workload = workloadGen.getAllWorkloadArrival();

            double[] averageWorkload = new double[noOfHourInDays];

            for (int i = 0; i < totalHour; i++)
            {
                averageWorkload[i % noOfHourInDays] += workload[i];
            }
            double totalWorkload = 0;
            for (int i = 0; i < averageWorkload.Count(); i++)
            {
                totalWorkload += averageWorkload[i];
            }

            for (int i = 0; i < averageWorkload.Count(); i++)
            {
                averageWorkload[i] = averageWorkload[i] / totalWorkload;
            }

            int noOfDays = workload.Count() / noOfHourInDays;
            // Average workload contains the normalized value
            for (int i = 0; i < totalHour; i++)
            {
                hourlyBudget[i] = averageWorkload[i % noOfHourInDays] * (totalBudget / noOfDays);
            }

            double totBud = 0;
            for (int i = 0; i < totalHour; i++)
            {
                totBud += hourlyBudget[i];
                //Console.WriteLine(i+","+hourlyBudget[i]);
            }

            //Console.WriteLine(totBud + ", " + totalHour);
            return hourlyBudget;

        }

        public double getAppropriateV(double budget)
        {
            //Console.WriteLine(budget);
            double[] hourlyBudget = getHourlyBudget(budget);
            int current_t, t_start = 0, t_end = workloadGen.availableHours() - 1;

            double budDeficit = double.MaxValue;
            int optimal_instances = 0;
            double v = 1, predicted_workload;

            double[] budget_queue = new double[t_end + 3];
            budget_queue[0] = 0;
            double lhs = 0, rhs = 100000;
            int iteration = 0;

            while (Math.Abs(budDeficit) > .01 && iteration < maxIteration)
            {
                v = (lhs + rhs) / 2;


                current_t = t_start;
                budget_queue[current_t] = 0;
                budDeficit = 0;
                for (current_t = t_start; current_t <= t_end; current_t++)
                {

                    predicted_workload = workloadGen.getWorkloadArrival(current_t);
                    optimal_instances = findOptimalInstanceCount(v, budget_queue[current_t], predicted_workload);
                    budget_queue[current_t + 1] = Math.Max(0, budget_queue[current_t] + SimulationSettings.azureInstancePrice * optimal_instances - hourlyBudget[current_t]);
                    budDeficit = budDeficit + (double)SimulationSettings.azureInstancePrice * optimal_instances - hourlyBudget[current_t];

                }


                if (budDeficit < 0)
                {
                    lhs = v;
                }

                else
                {
                    rhs = v;
                }
                iteration++;

                //Console.WriteLine("Appropriate Bud: " + budDeficit + ", V: " + v);
            }



            return v;
        }

        public void executeExperiment(double v, double totalBudget, Boolean budUnaware, DataManager dataManager, Scaler scaler)
        {
            int current_t, t_start = 0, t_end = workloadGen.availableHours() - 1;
            DateTime experimentStartTime = DateTime.Now, nextSlotFinishTime;
            double budDeficit = 0;


            //if (SimulationSettings.strictTiming == true)
            //{
            //    experimentStartTime = dtUtility.SleepUntillStartTime();
            //}
            nextSlotFinishTime = experimentStartTime.Add(SimulationSettings.timeSlotLength);


            int optimal_instances = 0, prevOptimalInstance = 0;
            double predicted_workload;
            double[] budget_queue = new double[t_end + 3];
            double[] hourlyBudget = getHourlyBudget(totalBudget);

            if (!SimulationSettings.EnableSystem)
            {
                Console.WriteLine("Current V: " + v);
            }

            current_t = t_start;
            budget_queue[current_t] = 0;
            predicted_workload = workloadGen.getWorkloadArrival(current_t);


            optimal_instances = findOptimalInstanceCount(v, budget_queue[current_t], predicted_workload);


            budget_queue[current_t + 1] = Math.Max(0, budget_queue[current_t] + SimulationSettings.azureInstancePrice * optimal_instances - hourlyBudget[current_t]);

            //budget_queue[current_t + 1] =  budget_queue[current_t] + SimulationSettings.azurePrice * optimal_instances - avg_budget;
            budDeficit = budDeficit + SimulationSettings.azureInstancePrice * optimal_instances - hourlyBudget[current_t];
            dataManager.addLog(DateTime.Now, budget_queue[current_t + 1], current_t, 0, workloadGen.getWorkloadArrival(current_t+1), optimal_instances, v, budDeficit);

            if (SimulationSettings.EnableSystem)
            {
                scaler.ScaleDeployment(optimal_instances);
            }
            prevOptimalInstance = optimal_instances;

            for (current_t = t_start + 1; current_t <= t_end; current_t++)
            {

                predicted_workload = workloadGen.getWorkloadArrival(current_t);
                Console.WriteLine(predicted_workload+"*******************************");  

                budget_queue[current_t] = 0;

                //Console.WriteLine(budget_queue[current_t]);
                optimal_instances = findOptimalInstanceCount(v, budget_queue[current_t], predicted_workload);
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
                budget_queue[current_t + 1] = Math.Max(0, budget_queue[current_t] + SimulationSettings.azureInstancePrice * optimal_instances - hourlyBudget[current_t]);
                //budget_queue[current_t + 1] = budget_queue[current_t] + SimulationSettings.azurePrice * optimal_instances - avg_budget;
                budDeficit = budDeficit + (double)SimulationSettings.azureInstancePrice * optimal_instances - hourlyBudget[current_t];

                double logWorkload = workloadGen.getWorkloadArrival(current_t);
                if (current_t < workloadGen.availableHours() - 1)
                {
                    logWorkload = workloadGen.getWorkloadArrival(current_t+1);
                }

                dataManager.addLog(DateTime.Now, budget_queue[current_t + 1], current_t, 0, logWorkload, optimal_instances, v, budDeficit);


                nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);
                prevOptimalInstance = optimal_instances;


            }
            //Console.WriteLine(current_t + ", " + budDeficit);

            for (int j = 0; j < SimulationSettings.emptySlotBetweenVs; j++)
            {
                if (SimulationSettings.EnableSystem)
                {
                    Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));
                }
                dataManager.addLog(DateTime.Now, 0, current_t, 1, 1, 1, v, 1);
                nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);
            }
            if (SimulationSettings.EnableSystem)
            {
                Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));
            }
            nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);

            
        }




    }



}
