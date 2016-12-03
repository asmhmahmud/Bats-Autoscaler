/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;
using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Auth;
using Microsoft.WindowsAzure.Storage.Table;
using Microsoft.WindowsAzure;
using System.Configuration;
using System.IO;
using AutoScaler.Workload.Prediction;


namespace AutoScaler.Service
{


    public class TableStorage
    {
        private CloudTable tableStg = null;
        private String tableName;
        private String connectionString;


        public TableStorage(String tableName, String connectionString )
        {
            this.tableName = tableName;
            this.connectionString = connectionString;

        }

        private CloudTable Connect()
        {
            CloudStorageAccount storageAccount = CloudStorageAccount.Parse(connectionString);            
            
            // Create the table client.
            CloudTableClient tableClient = storageAccount.CreateCloudTableClient();            
            CloudTable table = tableClient.GetTableReference(tableName);

            

            return table;

        }


        //private static double processorUsage = 0;
        public IEnumerable<PerformanceCountersEntity> GetCounterResults(DateTimeOffset fromOffset, DateTimeOffset toOffset, String counterName)
        {

            if (tableStg == null)
            {
                tableStg = Connect();
            }
            TableQuery<PerformanceCountersEntity> query = new TableQuery<PerformanceCountersEntity>();

            String whereCond;

            whereCond = TableQuery.CombineFilters(
                TableQuery.CombineFilters(
                        TableQuery.GenerateFilterCondition("PartitionKey", QueryComparisons.GreaterThanOrEqual, "0" + fromOffset.Ticks.ToString()),
                        TableOperators.And,
                        TableQuery.GenerateFilterCondition("PartitionKey", QueryComparisons.LessThan, "0" + toOffset.Ticks.ToString())
                ),

                TableOperators.And, 
                TableQuery.CombineFilters(
                    TableQuery.GenerateFilterCondition("Role", QueryComparisons.Equal, "WasabiDemoWebRole"),
                    TableOperators.And, 
                    TableQuery.GenerateFilterCondition("CounterName", QueryComparisons.Equal, counterName)
                    )
                
                
                );


            
            query.Where(whereCond);
            //Console.WriteLine(whereCond);


            //"TimeStamp,EventTickCount,DeploymentId,Role,RoleInstance,CounterName,CounterValue"
            String[] columns = new String[] { "EventTickCount", "Timestamp", "RoleInstance", "CounterName", "CounterValue" };
            query.Select(columns);

            // Execute the table query.
            IEnumerable<PerformanceCountersEntity> result = tableStg.ExecuteQuery(query);

            return result;
        }


        //private static double processorUsage = 0;
        public IEnumerable<PerformanceCountersEntity> GetResults(DateTimeOffset fromOffset, DateTimeOffset toOffset)
        {
            //double prevValue = processorUsage;
            //processorUsage = processorUsage + 1;
            //return prevValue;

            // Create the table query, filter on a specific CounterName, DeploymentId and RoleInstance.

            if (tableStg == null)
            {
                tableStg = Connect();
            }
            TableQuery<PerformanceCountersEntity> query = new TableQuery<PerformanceCountersEntity>();

            String whereCond;
            //whereCond = TableQuery.CombineFilters(
            //    TableQuery.CombineFilters(
            //    TableQuery.CombineFilters(
                

            //        TableQuery.GenerateFilterCondition("CounterName", QueryComparisons.Equal, @"\ASP.NET Applications(__Total__)\Requests Executing")
            //        ,TableOperators.Or,
                    
            //        TableQuery.GenerateFilterCondition("CounterName", QueryComparisons.Equal, @"\ASP.NET Applications(__Total__)\Requests/Sec"))
            //        ,TableOperators.Or,
            //        TableQuery.GenerateFilterCondition("CounterName", QueryComparisons.Equal, @"\ASP.NET\Requests Queued")
            //        ),

            //                 TableOperators.And,
            //                            TableQuery.GenerateFilterCondition("Role", QueryComparisons.Equal, "WasabiDemoWebRole")
            //            );



            

            whereCond = TableQuery.CombineFilters(
                TableQuery.CombineFilters(
                        TableQuery.GenerateFilterCondition("PartitionKey", QueryComparisons.GreaterThanOrEqual, "0"+fromOffset.Ticks.ToString()),
                TableOperators.And,
                TableQuery.GenerateFilterCondition("PartitionKey", QueryComparisons.LessThan, "0" + toOffset.Ticks.ToString())
                ),

                TableOperators.And, TableQuery.GenerateFilterCondition("Role", QueryComparisons.Equal, "WasabiDemoWebRole"));
            
            query.Where(whereCond );
            //Console.WriteLine(whereCond);


            //"TimeStamp,EventTickCount,DeploymentId,Role,RoleInstance,CounterName,CounterValue"
            String[] columns = new String[] { "EventTickCount", "Timestamp", "RoleInstance", "CounterName", "CounterValue" };
            query.Select(columns);


            
            //return;

            // Execute the table query.
            IEnumerable<PerformanceCountersEntity> result = tableStg.ExecuteQuery(query);

           return result;
        }


    }
}
