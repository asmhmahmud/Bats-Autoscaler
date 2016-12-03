/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AutoScaler.AutoScale
{
    class SimulationSettings
    {
        //Workload 
        //readonly public static TimeSpan timeSlotLength = TimeSpan.FromSeconds(10); //In Minutes
        readonly public static TimeSpan timeSlotLength = TimeSpan.FromMinutes(15); //In Minutes
        readonly public static int repetativPatternLength = 24; //Usualy 24 (no of hour in a day)




        //Workload Prediction
        readonly public static int prevTimeSlotToConsider = 3;





        //Workload Prediction
        //readonly public static TimeSpan reactiveCPUUsage = TimeSpan.FromMinutes(5);
        //readonly public static TimeSpan vmStartUpTime = TimeSpan.FromMinutes(6);
        //readonly public static TimeSpan vmShutDownTime = TimeSpan.FromMinutes(2);

        readonly public static TimeSpan reactiveCPUUsage = TimeSpan.FromMinutes(5);
        readonly public static TimeSpan vmStartUpTime = TimeSpan.FromMinutes(10);
        readonly public static TimeSpan vmShutDownTime = TimeSpan.FromMinutes(3);



        readonly public static int maxInstanceCount = 20;

        
        readonly public static bool EnableSystem = true;


        //Service Info
        //readonly public static double serviceRatePerInstance = 22;
        //readonly public static double azurePrice = 0.32;
        //readonly public static int maxArrivalToConsider = 30;
        //readonly public static double budget = 75; //in $
        //readonly public static double beta = 0.05;

        ////with delay+price
        //readonly public static double serviceRatePerInstance = 22;
        //readonly public static double azurePrice = 0.32;
        //readonly public static int maxArrivalToConsider = 30;
        //readonly public static double budget = 50; //in $
        //readonly public static double beta = 0.05;

        ////last known good settings
        //readonly public static double serviceRatePerInstance = 22;
        //readonly public static double azurePrice = 0.32;
        //readonly public static int maxArrivalToConsider = 30;
        //readonly public static double budget = 75; //in $
        //readonly public static double beta = 0.05;

        ////Service Info
        //readonly public static double serviceRatePerInstance = 22;
        //readonly public static double azurePrice = 0.32;
        //readonly public static int maxArrivalToConsider = 30;
        //readonly public static double budget = 75; //in $
        //readonly public static double beta = 0.6;


        ////Full simulation run
        //readonly public static double serviceRatePerInstance = 22;
        //readonly public static double azurePrice = 0.32;
        //readonly public static int maxArrivalToConsider = 30;
        //readonly public static double budget = 3000; //in $
        //readonly public static double beta =0.001;
        //readonly public static double delayThreshold = 100;  //in ms

        ////Service Info
        //readonly public static double serviceRatePerInstance = 22;
        //readonly public static double azurePrice = 0.32;
        //readonly public static int maxArrivalToConsider = 30;
        //readonly public static double budget = 42; //in $
        //readonly public static double beta = 0.001;
        //readonly public static double delayThreshold = 100;  //in ms
        //readonly public static double maxDelayThreshold = 500;  //in ms

        //PHP run
        //readonly public static double serviceRatePerInstance = 22;
        //readonly public static double azurePrice = 0.32;

        ////readonly public static int maxArrivalToConsider = 30;
        //readonly public static int maxArrivalToConsider = 12;


        //readonly public static double budget = 42; //in $
        //readonly public static double beta = 0.001;
        //readonly public static double delayThreshold = 100;  //in ms
        //readonly public static double maxDelayThreshold = 500;  //in ms



        //readonly public static double serviceRatePerInstance = 22;
        //readonly public static double azureInstancePrice = 0.02;
        //readonly public static int maxArrivalToConsider = 23;
        //readonly public static double budget =7.68; //in $
        //readonly public static double beta = 3;
        //readonly public static double delayThreshold = 0.45;  //in s
        //readonly public static double maxDelayThreshold =2;  //in s

        readonly public static double serviceRatePerInstance = 22;
        readonly public static double azureInstancePrice = 0.02;
        readonly public static int maxArrivalToConsider = 23;
        readonly public static double budget =8.5; //in $
        //readonly public static double buBudget = 11.44; //in $
        //readonly public static double buBudget = 10.86; //in $
        readonly public static double buBudget = 9.86; //in $
        readonly public static double beta =1;
        readonly public static double delayThreshold = 0.52;  //in s
        readonly public static double delayCap = 1.5;  //in s


        readonly public static bool strictTiming = true;
        //readonly public static double[] budFraction = new double[] { 0.6042, 0.6713, 0.7385, 0.8056, 0.8727, 0.9399 };
        readonly public static double[] budFraction = new double[] {  0.5657,    0.6365,    0.7072,   0.7779,    0.8486,    0.9193,    1};
        readonly public static double reserveDiscount = 0.8;


        //readonly public static String runtimeDelayInputFile = "runtimeDelay.csv";
        readonly public static String runtimeDelayInputFile = "RUBISRuntimeDelayTable.csv";



        readonly public static int emptySlotBetweenVs =1;
        readonly public static String workloadGenInputFile = "load_msr_azr.csv";
        readonly public static String expDetailFileName = "expDetail.csv";
        readonly public static String workloadGenInputFile_sc = "load_msr_azr_sc.csv";

        //public double[] arrivals;
        //public double[,] runtimeDelayQueue; 


        public SimulationSettings()
        {
            initializData();
        }

        public void initializData()
        {

            
            ////No of arrivals for 5,10,20
            //int[] arrivalRateInc = new int[] {5,10,20 };
            //int[] ranges = new int[] {10,5,10};

            //arrivals = new double[ranges.Sum()];



            //for (int i = 0; i < ranges[0]; i++)
            //{
            //    arrivals[i] = (i + 1) * arrivalRateInc[0];
            //    //Console.WriteLine(arrivals[i]);

            //}
            //for (int i = 0; i < ranges[1]; i++)
            //{
            //    arrivals[ranges[0]+i] = ranges[0] * arrivalRateInc[0] + (i + 1) * 10;
            //    //Console.WriteLine(arrivals[i]);

            //}

            //for (int i = 0; i < ranges[2]; i++)
            //{
            //    arrivals[ranges[0]+ranges[1]+i] = ranges[0] * arrivalRateInc[0] + ranges[1] * arrivalRateInc[1] + (i + 1) * 20;
            //    //Console.WriteLine(arrivals[i]);

            //}


            //for (int i = 0; i < arrivals.Count(); i++)
            //{
            //    Console.WriteLine(arrivals[i]+", ");
            //}


            //runtimeDelayQueue = new double[arrivals.Count(), maxInstanceCount];




            //for (int i = 0; i < runtimeDelayQueue.GetLength(0); i++)
            //{
            //    for (int j = 0; j < runtimeDelayQueue.GetLength(1); j++)
            //    {
            //        //Console.Write(arrivals[i]+",");
            //        //Console.Write((serviceRatePerInstance - (arrivals[i] / instances[j]))+",");
            //        runtimeDelayQueue[i, j] = arrivals[i] / ((j+1) * serviceRatePerInstance - arrivals[i] );
            //        if (runtimeDelayQueue[i, j] < 0)
            //        {
            //            runtimeDelayQueue[i, j] = double.MaxValue;
            //        }
            //        //Console.Write(runtimeDelayQueue[i, j] + ",");
            //    }
            //    //Console.WriteLine("");

            //}
        }



    }
}
