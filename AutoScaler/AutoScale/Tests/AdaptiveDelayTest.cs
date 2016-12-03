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
    
    class AdaptiveDelayTest
    {

        private Scaler scaler;
        private SimulationSettings simSettings;
        public AdaptiveDelayTest()
        {

            scaler = new Scaler();
            simSettings = new SimulationSettings();



        }

        public void StartGeneratingLoad()
        {
            DateTime experimentStartTime = DateTime.Now, nextSlotFinishTime;
            nextSlotFinishTime = experimentStartTime;

            StreamWriter testInputFile = new System.IO.StreamWriter("azureLoad.csv");
            

            

            int totalCount = 0;
            int instanceStart=11, instanceEnd=9;


            for (int j = instanceStart; j >= instanceEnd; j--)
            {
                testInputFile.WriteLine(totalCount + ",15,15,1,"+(j+1)+",1");
                totalCount++;
                testInputFile.WriteLine(totalCount + ",15,15,1," + (j + 1) + ",1");
                totalCount++;
                if (SimulationSettings.EnableSystem)
                {
                    nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);
                    nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);

                    Console.WriteLine("Scaling Instance: " + (j + 1));
                    Console.WriteLine("DateTime: " + DateTime.Now);

                    scaler.ScaleDeployment(j + 1);
                    Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));

                }
                else
                {
                    Console.WriteLine("Instance: " + (j + 1));
                }

                //Console.Write((j+1) + ": ");
                for (double i = 25; i < SimulationSettings.maxArrivalToConsider; i++)
                {

                    double arrival = (i+1) * ((j + 1) * SimulationSettings.serviceRatePerInstance) / SimulationSettings.maxArrivalToConsider;

                        Console.Write(arrival+ ", ");

                        testInputFile.WriteLine(totalCount + ",15,15,1,"+(j+1)+"," + arrival);

                        totalCount++;

                        //if ( i < SimulationSettings.serviceRatePerInstance/3)
                        //{
                        //    i = i + 2.5;
                        //}
                        //else if ( i < SimulationSettings.serviceRatePerInstance/2)
                        //{
                        //    i = i + 1.5;
                        //}
                        //else if (i < SimulationSettings.serviceRatePerInstance / 4)
                        //{
                        //    i = i + 0.5;
                        //}



                        if (SimulationSettings.EnableSystem)
                        {
                            Console.WriteLine("Arrival: " + arrival);
                            Console.WriteLine("DateTime: " + DateTime.Now);
                            nextSlotFinishTime = nextSlotFinishTime.Add(SimulationSettings.timeSlotLength);
                            Thread.Sleep(nextSlotFinishTime.Subtract(DateTime.Now));
                            
                        }


                }

                Console.Write("\n");

                //if (j == 17)
                //{
                //    j = 3;
                //}
                
            }
            Console.WriteLine("Total Count: "+totalCount);
            testInputFile.Close();
        }

    }
}
