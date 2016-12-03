/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;
//using AutoScaler.AutoScale;
//using System.IO;

//namespace AutoScaler.Tests
//{
//    class delayQueueInstance
//    {
//        public double arrival;
//        public double delay;
//    }
//    public class GenerateDelayTable
//    {

//        private SimulationSettings simSettings;
//        private String inputFilePath = "Results_1374598504834.csv";
//        private double[] arrivals;

//        private double[,] runtimeDelayQueue;



//        public GenerateDelayTable()
//        {

//            simSettings = new SimulationSettings();
//            arrivals = simSettings.arrivals;

//            runtimeDelayQueue = new double[arrivals.Count(), SimulationSettings.maxInstanceCount];

//            for (int i = 0; i < runtimeDelayQueue.GetLength(0); i++)
//            {
//                for (int j = 0; j < runtimeDelayQueue.GetLength(1); j++)
//                {
//                    runtimeDelayQueue[i,j]=0;
//                }
//            }

//        }




//        public void StartGeneratingLoad()
//        {
//            DateTime experimentStartTime = DateTime.Now, nextSlotFinishTime;
//            nextSlotFinishTime = experimentStartTime;

//            StreamReader reader = new StreamReader(File.OpenRead(inputFilePath));
//            if (!reader.EndOfStream)
//            {
//                reader.ReadLine();
//            }


//            String line;
//            String[] values;
//            int instanceCount,arrivalIndex;
//            double delay;
//            double arrivalRate;
//            while (!reader.EndOfStream)
//            {
//                line = reader.ReadLine();
//                //Console.WriteLine(line);
//                values = line.Split(',');

//                arrivalRate = Convert.ToDouble(values[3]);

//                //checking whether the arrival is 1;
//                if (arrivalRate != 1)
//                {
//                    instanceCount = (int)Convert.ToDouble(values[6]);
//                    delay = Convert.ToDouble(values[4]);

//                    //Console.WriteLine(delay);

//                    arrivalIndex = Array.IndexOf(arrivals, arrivalRate);

//                    Console.WriteLine(arrivalIndex+", "+arrivalRate+", "+instanceCount);

//                    runtimeDelayQueue[arrivalIndex, instanceCount - 1] = delay;

//                    if (arrivalIndex>0 && runtimeDelayQueue[arrivalIndex - 1, instanceCount - 1] == 0)
//                    {
//                        generateIntermediateData(arrivalIndex, instanceCount - 1);

//                    }
                    


//                }

//            }


//            reader.Close();


//        }

//        private void generateIntermediateData(int row, int col)
//        {
//            int lastValueFoundIndex = 0;
//            for (int i = row-1; i > 0; i--)
//            {
//                if (runtimeDelayQueue[i, col] != 0)
//                {

//                    lastValueFoundIndex = i;
//                    break;
//                }
//            }

//            double ratio = (runtimeDelayQueue[row, col] - runtimeDelayQueue[lastValueFoundIndex, col])/(row-lastValueFoundIndex);
//            for (int i = row - 1,j=1; i > 0; i--,j++)
//            {
//                runtimeDelayQueue[i, col] = runtimeDelayQueue[row, col] + j * ratio;
//            }


//        }

//    }
//}
