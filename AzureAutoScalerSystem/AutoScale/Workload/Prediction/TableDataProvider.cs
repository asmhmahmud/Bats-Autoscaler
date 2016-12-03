/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;
using AutoScaler.Service;
using AutoScaler.AutoScale;

namespace AutoScaler.Workload.Prediction
{
    class TableDataProvider
    {
        private TableStorage tblStorage;
        private String reqExecuting=@"\ASP.NET Applications(__Total__)\Requests Executing";
        private String reqPerSec = @"\ASP.NET Applications(__Total__)\Requests/Sec";
        private String reqQueued = @"\ASP.NET\Requests Queued";
        private String reqRejected = @"\ASP.NET\Requests Rejected";
        private String reqDisconnected = @"\ASP.NET\Requests Disconnected";
        public static readonly String processorExecTime = @"\Processor(_Total)\% Processor Time";

        public TableDataProvider()
        {
            tblStorage = new TableStorage(Settings.pcTableName, Settings.pcConnString);
        }


        private List<InstanceData> GetRoleInstances(IEnumerable<PerformanceCountersEntity> results)
        {
            ArrayList instances = new ArrayList();

            //Console.WriteLine(results.Count());

            List<InstanceData> instResults = new List<InstanceData>();
            
            foreach (PerformanceCountersEntity entity in results)
            {
                InstanceData instData = findInstanceData(instResults, entity.RoleInstance);
                if (instData == null)
                {
                    //Console.WriteLine("New Instance");
                    instData = new InstanceData();
                    instData.instanceName = entity.RoleInstance;
                    instResults.Add(instData);
                }

                instData.resultEntries.Add(entity);


            }

            //printInstanceDataList(instResults);
            return instResults;
        }
        private InstanceData findInstanceData(List<InstanceData> instResults, String instanceName)
        {
            InstanceData targetInstData = null;

            foreach (InstanceData entity in instResults)
            {
                if (entity.instanceName.CompareTo(instanceName) == 0)
                {
                    targetInstData = entity;
                    break;
                }
            }
            return targetInstData;
        }



        
        public double GetAvgWorkload(DateTimeOffset fromOffset, DateTimeOffset toOffset)
        {
            IEnumerable<PerformanceCountersEntity> results = tblStorage.GetResults(fromOffset, toOffset);




            return GetTotalRequestPerSec(results, fromOffset, toOffset);
        }

        public double getCounterAverage(DateTimeOffset fromOffset, DateTimeOffset toOffset, String counterName)
        {

            IEnumerable<PerformanceCountersEntity> results = tblStorage.GetCounterResults(fromOffset, toOffset, counterName);
            
            IEnumerable<InstanceData> instanceResults = GetRoleInstances(results);
            
            double avgCounterUsage = 0;
            int maxEntry = getMaxEntry(instanceResults);;

            Console.Write("Max Entry: " + maxEntry + ", Instance Avg: ");

            
            
            foreach (InstanceData entity in instanceResults)
            {
                double counterVal = entity.getCounterAverage(processorExecTime);
                double avgInstanceCounterUsage = ((double)entity.resultEntries.Count / maxEntry) * (counterVal);

                avgCounterUsage = avgCounterUsage + avgInstanceCounterUsage;
                Console.Write(
                    +avgInstanceCounterUsage + "("
                    + entity.resultEntries.Count + ","
                    + counterVal + ","
                    + "), ");

            }
            Console.WriteLine();


            //Console.WriteLine(avgCounterUsage);

            return avgCounterUsage;

        }

        private int getMaxEntry(IEnumerable<InstanceData> instanceResults)
        {
            int maxEntry = int.MinValue;
            foreach (InstanceData entity in instanceResults)
            {
                if (maxEntry < entity.resultEntries.Count)
                {
                    maxEntry = entity.resultEntries.Count;
                }
            }
            return maxEntry;
        }


        private double GetTotalRequestPerSec(IEnumerable<PerformanceCountersEntity> result, DateTimeOffset fromOffset, DateTimeOffset toOffset)
        {
            

            IEnumerable<InstanceData> instanceResults = GetRoleInstances(result);

            //printPerformEntityList(instanceResults.ElementAt(0).results);

            double avgCounterUsage = 0;
            int maxEntry = getMaxEntry(instanceResults);

            Console.Write("Max Entry: " + maxEntry + ", Instance Avg: ");
            
            double avgInstanceCounterUsage = 0;
            //TableDataPrintUtility.printInstanceDataList(instanceResults);
            foreach (InstanceData entity in instanceResults)
            {

                double avgReqQueued = entity.getIncrementalCounterAveragePerSec(reqQueued, fromOffset, toOffset);
                double avgReqRejected = entity.getIncrementalCounterAveragePerSec(reqRejected, fromOffset, toOffset);
                double ReqDisconnected = entity.getIncrementalCounterAveragePerSec(reqDisconnected, fromOffset, toOffset);
                double dreqPerSec = entity.getCounterAverage(reqPerSec);
                double reqExecPerSec = entity.getCounterAverage(reqExecuting);
                double proportionalReqPerSeq = ((double)entity.resultEntries.Count / maxEntry) * (dreqPerSec + reqExecPerSec);

                avgInstanceCounterUsage = avgReqQueued + avgReqRejected + ReqDisconnected + proportionalReqPerSeq;

                    avgCounterUsage = avgCounterUsage + avgInstanceCounterUsage;
                    Console.Write(
                        + avgInstanceCounterUsage + "("
                        + entity.resultEntries.Count + ","
                        + dreqPerSec + ","
                        + proportionalReqPerSeq + ","
                        + avgReqQueued + ","
                        + avgReqRejected+","
                        + ReqDisconnected
                        + "), ");

            }
            Console.WriteLine();


            //Console.WriteLine(avgCounterUsage);

            return avgCounterUsage;

        }

    }
}
