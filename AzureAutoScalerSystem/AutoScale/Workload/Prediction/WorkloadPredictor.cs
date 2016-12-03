/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using AutoScaler.Service;
using System.Collections;
using AutoScaler.AutoScale;
//using AutoScaler.AutoScale;

namespace AutoScaler.Workload.Prediction
{
    class WorkloadPredictor
    {
        
        private ArrayList[] prevWorkloadData;
        private TableDataProvider tblDtProvider;
        private int prevTimeSlotToConsider,  repetativPatternLength;
        private TimeSpan timeSlotLength;

        readonly public static int timeSlotBack = 2;



        public WorkloadPredictor(int prevTimeSlotToConsider, TimeSpan timeSlotLength, int repetativPatternLength)
        {
            this.prevTimeSlotToConsider = prevTimeSlotToConsider;
            this.timeSlotLength = timeSlotLength;
            this.repetativPatternLength = repetativPatternLength;

            tblDtProvider = new TableDataProvider();
            prevWorkloadData = new ArrayList[repetativPatternLength];

            for (int i = 0; i < repetativPatternLength; i++)
            {
                prevWorkloadData[i] = new ArrayList(prevTimeSlotToConsider);
            }

        }


        public double  startOfSlot(int slotNo)
        {
            //currentSlot = slotNo;
            return addAverageWorkloadFromTable(slotNo);
        }

        public double getAvgWorkload()
        {

            try
            {


                DateTimeOffset fromOffset = DateTimeOffset.UtcNow.Subtract(SimulationSettings.reactiveCPUUsage);

                DateTimeOffset toOffset = DateTimeOffset.UtcNow;

                double avgWorkload;
                avgWorkload = tblDtProvider.GetAvgWorkload(fromOffset, toOffset);
                Console.WriteLine("Workload Arrival: " + avgWorkload + " Req/Sec");
                
                return avgWorkload;

            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex.ToString());
            }

            return 0;

        }


        //assuming slot no starts from 0
        private double addAverageWorkloadFromTable(int currentSlotNo)
        {

            //Minimum value should be 2


            if (currentSlotNo < timeSlotBack)
            {
                return 0;
            }

            int targetSlot = currentSlotNo % repetativPatternLength - timeSlotBack;

            if (targetSlot < 0)
            {
                targetSlot = targetSlot + repetativPatternLength;
            }


            try
            {
                //DateTimeOffset currentTime = DateTimeOffset.UtcNow.AddMinutes(-3 * Settings.timeSlotLength);
                //Int64 addTick = currentTime.Ticks % TimeSpan.TicksPerSecond;
                //DateTimeOffset fromOffset = currentTime.AddTicks(-addTick);

                DateTimeOffset fromOffset = DateTimeOffset.UtcNow.AddSeconds(-timeSlotBack * timeSlotLength.TotalSeconds);

                DateTimeOffset toOffset = fromOffset.Add(timeSlotLength);

                Console.WriteLine("Retrieving Data For Slot " + (currentSlotNo - timeSlotBack));

                double avgWorkload;
                avgWorkload = tblDtProvider.GetAvgWorkload(fromOffset, toOffset);
                //Console.WriteLine("Start: "+fromOffset.ToLocalTime()+", End: "+toOffset.ToLocalTime());
                //if (currentSlotNo % 2 == 0)
                //{
                //    avgWorkload = 14;
                //}
                //else
                //{
                //    avgWorkload = 74;
                //}

                Console.WriteLine("Workload Arrival For Time Slot " + (currentSlotNo - timeSlotBack) + ": " + avgWorkload + " Req/Sec");
                addWorkloadInPredictor(targetSlot, avgWorkload);

                return avgWorkload;

            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex.ToString());
            }

            return 0;

        }

        //assuming slot no starts from 0
        public double getAverageCPUUsage()
        {

            try
            {

                DateTimeOffset fromOffset = DateTimeOffset.UtcNow.Subtract(SimulationSettings.reactiveCPUUsage);

                DateTimeOffset toOffset = DateTimeOffset.UtcNow;

                //Console.WriteLine("Retrieving Data For Slot " + (currentSlotNo - timeSlotBack));

                double avgWorkload;
                
                avgWorkload = tblDtProvider.getCounterAverage(fromOffset, toOffset, TableDataProvider.processorExecTime);
                //Console.WriteLine("Workload Arrival For Time Slot " + (currentSlotNo - timeSlotBack) + ": " + avgWorkload + " Req/Sec");
                
                return avgWorkload;

            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex.ToString());
            }

            return 0;

        }


        private void addWorkloadInPredictor(int slotNo, double slotWorkloadValue)
        {
  
            if (prevWorkloadData[slotNo].Count >= prevTimeSlotToConsider)
            {
                prevWorkloadData[slotNo].RemoveAt(prevTimeSlotToConsider-1);
            }

            prevWorkloadData[slotNo].Insert(0, slotWorkloadValue);
        }



        public double getWorkloadPrediction(int slotNo)
        {
            double totalPredictionValue = 0, predictedWorkload=0;
            int targetSlot = slotNo % repetativPatternLength;
            Array slotValues = prevWorkloadData[targetSlot].ToArray();
            for (int i = 0; i < prevTimeSlotToConsider && i< slotValues.Length; i++)
            {
                totalPredictionValue += (double)slotValues.GetValue(i);
                //Console.WriteLine((double)slotValues.GetValue(i));
            }

            if (slotValues.Length > 0)
            {
                predictedWorkload = totalPredictionValue / slotValues.Length;
            }

            return totalPredictionValue / slotValues.Length;
        }

    }
}
