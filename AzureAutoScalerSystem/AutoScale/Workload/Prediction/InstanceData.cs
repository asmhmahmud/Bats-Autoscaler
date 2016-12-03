/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using AutoScaler.Service;
using System.Collections;

namespace AutoScaler.Workload.Prediction
{
    //class IncrementalPerformanceCounterDetail
    //{
    //    public double counterValue;
    //    public int noOfSamples;
    //}
    class InstanceData
    {
        public String instanceName;
        public List<PerformanceCountersEntity> resultEntries;

        public InstanceData()
        {
            resultEntries = new List<PerformanceCountersEntity>();
            instanceName = "";
        }

        public double getCounterAverage(String counterName)
        {
            double avgValue = 0;

            List<PerformanceCountersEntity> filteredResults = getEntriesByCounterName(counterName);
            foreach (PerformanceCountersEntity entity in filteredResults)
            {
                avgValue += entity.CounterValue;
            }

            if (filteredResults.Count > 0)
            {
                avgValue = avgValue / filteredResults.Count;
            }

            return avgValue;
        }

        public List<PerformanceCountersEntity> getEntriesByCounterName(String counterName)
        {
            List<PerformanceCountersEntity> filteredResults = new List<PerformanceCountersEntity>();
            foreach (PerformanceCountersEntity entity in resultEntries)
            {
                if (entity.CounterName.CompareTo(counterName) == 0)
                {
                    filteredResults.Add(entity);
                }
            }
            return filteredResults;

        }


        public double getIncrementalCounterAveragePerSec(String counterName, DateTimeOffset fromOffset, DateTimeOffset toOffset)
        {
            double avgValue = getIncrementalCounterValue(counterName);

            return avgValue / (toOffset.Subtract(fromOffset).TotalSeconds);
        }

        public double getIncrementalCounterValue(String counterName)
        {
            double first = 0, last = 0;
            //foreach (PerformanceCountersEntity entity in resultEntries)
            //{
            //    if (entity.CounterName.CompareTo(counterName) == 0)
            //    {
            //        TableDataPrintUtility.printPerformanceEntity(entity);
            //    }
            //}
            foreach (PerformanceCountersEntity entity in resultEntries)
            {
                if (entity.CounterName.CompareTo(counterName) == 0)
                {
                    first = entity.CounterValue;
                    break;
                }
            }

            PerformanceCountersEntity currentEntity;
            for (int i = resultEntries.Count - 1; i >= 0; i--)
            {
                currentEntity = resultEntries.ElementAt(i);
                if (currentEntity.CounterName.CompareTo(counterName) == 0)
                {
                    last = currentEntity.CounterValue;
                    break;
                }
            }

            return (last - first);
        }

        private int findTotalCountersUsed()
        {
            ArrayList instances = new ArrayList();


            foreach (PerformanceCountersEntity entity in resultEntries)
            {
                if (entity.CounterName == null)
                {
                    Console.WriteLine("Error Found in counters. Exitting....");
                    
                }
                else if (!instances.Contains(entity.CounterName))
                {
                    instances.Add(entity.CounterName);
                    //Console.WriteLine(entity.CounterName);
                }
            }

            //Console.WriteLine(instances.Count);
            return instances.Count;
        }











    }

 }
