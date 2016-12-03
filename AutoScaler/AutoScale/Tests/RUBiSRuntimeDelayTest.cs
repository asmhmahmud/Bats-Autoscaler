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

namespace AutoScaler.Tests
{
    
    class RUBiSRuntimeDelayTest
    {

        private Scaler scaler;
        public RUBiSRuntimeDelayTest()
        {

            scaler = new Scaler();
            



        }

        public void startScaling()
        {


            StreamWriter testLogFile = new System.IO.StreamWriter("RubisDelayTestLog.txt");

            TimeSpan scalingTime = TimeSpan.FromMinutes(10);
            TimeSpan phaseDuration = TimeSpan.FromMinutes(180);
            int instanceStart=8, instanceEnd=10;


            Console.WriteLine("Scaling Initial instance size: " + instanceStart);
            testLogFile.WriteLine("Scaling Initial instance size: " + instanceStart);
            scaler.ScaleDeployment(instanceStart);
            Thread.Sleep(scalingTime);
            int currentInst = scaler.GetCurrentInstanceCount();
            Console.WriteLine("Number of instance: " + currentInst);
            testLogFile.WriteLine("Number of instance: " + currentInst);


            DateTime experimentStartTime = DateTime.Now, nextSlotFinishTime;
            nextSlotFinishTime = experimentStartTime;


            Console.WriteLine("Experiment Start Time: " + DateTime.Now);
            testLogFile.WriteLine("Experiment Start Time: " + DateTime.Now);

            for (int j = instanceStart+1; j <= instanceEnd; j++)
            {

                nextSlotFinishTime = nextSlotFinishTime.Add(phaseDuration);

                TimeSpan sleepTime = nextSlotFinishTime.Subtract(scalingTime).Subtract(DateTime.Now);
                Thread.Sleep(sleepTime);

                Console.WriteLine("Slot: " + j + ", Waking up: " + DateTime.Now);
                testLogFile.WriteLine("Slot: " + j + ", Waking up: " + DateTime.Now);
                scaler.ScaleDeployment(j);

                Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));

                Console.WriteLine("Slot: " + j + ", start Time: " + DateTime.Now);
                testLogFile.WriteLine("Slot: " + j + ", start Time: " + DateTime.Now);
                currentInst = scaler.GetCurrentInstanceCount();
                Console.WriteLine("Number of instance: " + currentInst);
                testLogFile.WriteLine("Number of instance: " + currentInst);
                
            }
            
            testLogFile.Close();
        }

    }
}
