/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace AutoScaler.AutoScale
{
    public class RuntimeDelay
    {
        String inputFilePath;
        public double[,] runtimeDelayQueue=null;
        private int noOfColPerItemInDelayQueue = 3;
        private int throughPutIndex = 2;

        public static readonly int minimumDelay = 380;
        public static readonly int maxDelay = 50000;


        public RuntimeDelay(String inputFile)
        {
            this.inputFilePath = inputFile;
            runtimeDelayQueue = new double[SimulationSettings.maxInstanceCount, SimulationSettings.maxArrivalToConsider * noOfColPerItemInDelayQueue];
            generateRuntimeDelay();
        }
        
        private void generateRuntimeDelay()
        {
            try
            {
                StreamReader reader = new StreamReader(File.OpenRead(inputFilePath));

                if (!reader.EndOfStream)
                {
                    //skipping the headers
                    reader.ReadLine();

                }

                String line;
                String[] values;
                int noOfColinItemInInputFile = 4;


                for (int i = 0; i < runtimeDelayQueue.GetLength(0); i++)
                {
                    line = reader.ReadLine();
                    //Console.WriteLine(line);
                    values = line.Split(',');



                    //Console.WriteLine("Col: "+values.Count() / noOfColinItemInInputFile);
                    for (int j = 0; j < values.Count() / noOfColinItemInInputFile; j++)
                    {
                        if (values[j * noOfColinItemInInputFile + 1].Length > 0)
                        {
                            //Console.WriteLine(values[j * noOfColinItemInInputFile + 1]);
                            runtimeDelayQueue[i, j * noOfColPerItemInDelayQueue] = Convert.ToDouble(values[j * noOfColinItemInInputFile + 1]);
                            runtimeDelayQueue[i, j * noOfColPerItemInDelayQueue + 1] = Convert.ToDouble(values[j * noOfColinItemInInputFile + 2]);
                            runtimeDelayQueue[i, j * noOfColPerItemInDelayQueue + 2] = Convert.ToDouble(values[j * noOfColinItemInInputFile + 3]);
                        }

                    }

                }


                reader.Close();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex.ToString());
            }

        }

        public double getDelay(int instance, double arrivalRate)
        {
            double delay = double.MaxValue;
            int rowIndex =instance-1;
            int lastSmallestIndex=0;
            //Console.WriteLine(instance+","+arrivalRate);
            if (rowIndex >= 0 && rowIndex < SimulationSettings.maxInstanceCount)
            {

                for (int j = 0; j < runtimeDelayQueue.GetLength(1); j = j + noOfColPerItemInDelayQueue)
                {
                    //Console.WriteLine("Test");
                    if (arrivalRate >= runtimeDelayQueue[rowIndex, j] && runtimeDelayQueue[rowIndex, j] > 0)
                    {
                        lastSmallestIndex = j;
                        //Console.WriteLine("Test: " + lastSmallestIndex);
                    }
                    else if (arrivalRate < runtimeDelayQueue[rowIndex, j])
                    {
                        break;
                    }


                }
                if (arrivalRate < runtimeDelayQueue[rowIndex, 0])
                {
                    delay = runtimeDelayQueue[rowIndex, 1];
                    //delay = minimumDelay;
                }
                else if (arrivalRate == runtimeDelayQueue[rowIndex, lastSmallestIndex])
                {
                    delay = runtimeDelayQueue[rowIndex, lastSmallestIndex + 1];

                }
                else if (lastSmallestIndex + noOfColPerItemInDelayQueue >= runtimeDelayQueue.GetLength(1))
                {
                    //delay = double.MaxValue;
                    delay = maxDelay;
                }
                else
                {
                    double arrival_x1 = runtimeDelayQueue[rowIndex, lastSmallestIndex];
                    double arrival_x2 = runtimeDelayQueue[rowIndex, lastSmallestIndex + noOfColPerItemInDelayQueue];

                    double delay_y1 = runtimeDelayQueue[rowIndex, lastSmallestIndex + 1];
                    double delay_y2 = runtimeDelayQueue[rowIndex, lastSmallestIndex + noOfColPerItemInDelayQueue + 1];

                    delay = delay_y1 + (arrivalRate - arrival_x1) * (delay_y2 - delay_y1) / (arrival_x2 - arrival_x1);


                }

            }
            else
            {
                Console.WriteLine("Unknown Instance value: "+ instance);
                delay =  double.MaxValue;
            }
            //Console.WriteLine(
            //    "Small Index: " + lastSmallestIndex + 
            //    ",   Tot Col: " + runtimeDelayQueue.GetLength(1) + 
            //    ", Val: " + runtimeDelayQueue[rowIndex, lastSmallestIndex]
            //    );




            return delay * 0.001;
        }

        public double getThroughput(int instance, double arrivalRate)
        {
            double throughPut = 0;
            int rowIndex = instance - 1;
            int lastSmallestIndex = 0;
            //Console.WriteLine(instance+","+arrivalRate);
            if (rowIndex >= 0 && rowIndex < SimulationSettings.maxInstanceCount)
            {

                for (int j = 0; j < runtimeDelayQueue.GetLength(1); j = j + noOfColPerItemInDelayQueue)
                {
                    //Console.WriteLine("Test");
                    if (arrivalRate >= runtimeDelayQueue[rowIndex, j] && runtimeDelayQueue[rowIndex, j] > 0)
                    {
                        lastSmallestIndex = j;
                        //Console.WriteLine("Test: " + lastSmallestIndex);
                    }
                    else if (arrivalRate < runtimeDelayQueue[rowIndex, j])
                    {
                        break;
                    }


                }
                if (arrivalRate < runtimeDelayQueue[rowIndex, 0])
                {
                    throughPut = runtimeDelayQueue[rowIndex, throughPutIndex];
                    //delay = minimumDelay;
                }
                else if (arrivalRate == runtimeDelayQueue[rowIndex, lastSmallestIndex])
                {
                    throughPut = runtimeDelayQueue[rowIndex, lastSmallestIndex + throughPutIndex];

                }
                else if (lastSmallestIndex + noOfColPerItemInDelayQueue >= runtimeDelayQueue.GetLength(1))
                {
                    //delay = double.MaxValue;
                    throughPut = runtimeDelayQueue[rowIndex, lastSmallestIndex + throughPutIndex]; ;
                }
                else
                {
                    double arrival_x1 = runtimeDelayQueue[rowIndex, lastSmallestIndex];
                    double arrival_x2 = runtimeDelayQueue[rowIndex, lastSmallestIndex + noOfColPerItemInDelayQueue];

                    double th_y1 = runtimeDelayQueue[rowIndex, lastSmallestIndex + throughPutIndex];
                    double th_y2 = runtimeDelayQueue[rowIndex, lastSmallestIndex + noOfColPerItemInDelayQueue + throughPutIndex];

                    throughPut = th_y1 + (arrivalRate - arrival_x1) * (th_y2 - th_y1) / (arrival_x2 - arrival_x1);


                }

            }
            else
            {
                Console.WriteLine("Unknown Instance value: " + instance);
                throughPut = 0;
            }
            //Console.WriteLine(
            //    "Small Index: " + lastSmallestIndex + 
            //    ",   Tot Col: " + runtimeDelayQueue.GetLength(1) + 
            //    ", Val: " + runtimeDelayQueue[rowIndex, lastSmallestIndex]
            //    );




            return throughPut;
        }



        public void printDelayTable()
        {
            for (int i = 0; i < runtimeDelayQueue.GetLength(0); i++)
            {
                for (int j = 0; j < runtimeDelayQueue.GetLength(1); j++)
                {

                    Console.Write(runtimeDelayQueue[i, j] + ",");
                }
                Console.WriteLine("");

            }
        }


    }
}
