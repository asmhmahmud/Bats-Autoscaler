/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using AutoScaler.Service;

namespace AutoScaler.Workload.Prediction
{
    class TableDataPrintUtility
    {
        public static void printInstanceDataList(IEnumerable<InstanceData> instanceDataSet)
        {
            Console.WriteLine("Total instances: " + instanceDataSet.Count());
            foreach (InstanceData instance in instanceDataSet)
            {
                Console.WriteLine("Total result in entity " + instance.instanceName + " is:" + instance.resultEntries.Count());
                foreach (PerformanceCountersEntity entity in instance.resultEntries)
                {

                    printPerformEntityList(instance.resultEntries);
                }
                Console.WriteLine("\n");
            }
        }


        public static void printPerformEntityList(IEnumerable<PerformanceCountersEntity> results)
        {
            foreach (PerformanceCountersEntity entity in results)
            {

                printPerformanceEntity(entity);

            }
        }
        public static void printPerformanceEntity(PerformanceCountersEntity entity)
        {
            //DateTimeOffset checkTime = new DateTimeOffset(entity.EventTickCount, DateTimeOffset.UtcNow.Offset);
            DateTimeOffset partitionKeyTime = new DateTimeOffset(Int64.Parse(entity.PartitionKey), DateTimeOffset.UtcNow.Offset);

            Console.WriteLine(/*checkTime.ToLocalTime() + "," +*//* partitionKeyTime.ToLocalTime() + "," + */entity.RoleInstance + "," + entity.CounterName + "," + entity.CounterValue);

        }

    }
}
