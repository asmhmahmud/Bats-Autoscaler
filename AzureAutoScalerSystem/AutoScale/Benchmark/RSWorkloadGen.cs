/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;
//using System.IO;
//using AutoScaler.AutoScale;

//namespace AutoScaler.Benchmark
//{
//    public class RSWorkloadGen:WorkloadGen
//    {
//        protected List<double> orgWorkloadArrival =new List<double>();
//        protected int avgPeriod = 4;
//        public RSWorkloadGen(String inputFileName):base(inputFileName)
//        {
//            //this.inputFileName = inputFileName;
//            //workloadArrival = new List<double>();
//            //readLoadGenFile();


//        }

//        public void  readLoadGenFile()
//        {
//            Console.WriteLine("Here");
//            try
//            {
//                String line;
//                StreamReader reader = new StreamReader(File.OpenRead(inputFileName));

//                for (int i = 0; !reader.EndOfStream && i < maxArrivalToConsider; i++)
//                {
//                    line = reader.ReadLine();
//                    orgWorkloadArrival.Add(Convert.ToDouble(line) * maxWorkloadValue);

//                }

//                reader.Close();
//            }
//            catch (Exception ex)
//            {
//                Console.WriteLine("Error: " + ex.ToString());
//            }

//            workloadArrival.Add(orgWorkloadArrival[0]);
//            for (int j = 0; j < orgWorkloadArrival.Count(); )
//            {
//                double sum = 0;
//                for (int k = 0; k < avgPeriod; k++)
//                {
//                    sum = sum + orgWorkloadArrival[j + k];
//                }
//                double avg = sum / avgPeriod;
//                Console.WriteLine(avg+"****");
//                for (int k = 0; k < avgPeriod; k++)
//                {
//                    workloadArrival.Add(orgWorkloadArrival[j + k]);
//                    if ((j + k) % SimulationSettings.repetativPatternLength == 0)
//                    {
//                        workloadArrival.Add(orgWorkloadArrival[j + k]);


//                    }

//                }


//                j = j + avgPeriod;
//            }


            

//        }

//        public void generateRUBiSAzureLoadGenFile(int repeat, int emptySlotinVs, string outputFileName)
//        {
//            StreamWriter loadgenFile = new System.IO.StreamWriter(outputFileName);
//            loadgenFile.WriteLine("Slot No, V slot no, No of Client");
//            int slotNo = 0;
//            int avgPeriod = 4;
//            for (int i = 0; i < repeat + 1; i++)
//            {

//                for (int j = 0; j < orgWorkloadArrival.Count(); )
//                {
//                    double sum = 0;
//                    for (int k = 0; k < avgPeriod; k++)
//                    {
//                        sum = sum + orgWorkloadArrival[j + k];
//                    }
//                    double avg = sum / avgPeriod;

//                    for (int k = 0; k < avgPeriod; k++)
//                    {
//                        loadgenFile.WriteLine(slotNo + "," + j + "," + avg);
//                        slotNo++;
//                    }


//                    j = j + avgPeriod;
//                }

//                for (int j = 0; j < emptySlotinVs; j++)
//                {
//                    loadgenFile.WriteLine(slotNo + "," + orgWorkloadArrival.Count() + ",1");
//                    slotNo++;
//                }

//            }

//            loadgenFile.Flush();
//            loadgenFile.Close();

//        }

//        protected List<double> workloadArrival;
//        protected String inputFileName;
//        //private int maxArrivalToConsider = 4320;
//        protected int maxArrivalToConsider = 48;
//        protected double maxWorkloadValue = 1120;

//        public int availableHours()
//        {
//            return maxArrivalToConsider;
//        }


//        public void printWorkload()
//        {
//            for (int j = 0; j < workloadArrival.Count(); j++)
//            {
//                Console.WriteLine(workloadArrival[j]);
//            }
//            Console.WriteLine("\nTotal Workload: "+workloadArrival.Count);
//        }


//        public double[] getAllWorkloadArrival()
//        {
//            return workloadArrival.ToArray();
//        }

//        public double geActualtWorkloadArrival(int slotNo)
//        {
//                        double arrivalRate = 1;
//                        if (slotNo < workloadArrival.Count())
//                        {
//                            arrivalRate = workloadArrival[slotNo];
//                        }
//                        return arrivalRate;
//        }

//        public double getWorkloadArrival(int slotNo)
//        {
//            //double arrivalRate = 1;
//            //if (slotNo < workloadArrival.Count())
//            //{
//            //    arrivalRate = workloadArrival[slotNo];

//            //    if (slotNo < SimulationSettings.repetativPatternLength)
//            //    {
//            //        arrivalRate = workloadArrival[slotNo];
//            //    }
//            //    else
//            //    {
//            //        double totalArrival = 0;
//            //        int count = 0;
//            //        for (int i = slotNo-SimulationSettings.repetativPatternLength; count <SimulationSettings.prevTimeSlotToConsider && i >= 0; i = i - SimulationSettings.repetativPatternLength)
//            //        {
//            //            totalArrival = totalArrival + workloadArrival[i];
//            //            count++;
//            //            //Console.WriteLine(workloadArrival[i]/maxWorkloadValue);
//            //        }

//            //        arrivalRate = totalArrival / count;
//            //    }


//            //}
//            //return arrivalRate;
//            return geActualtWorkloadArrival(slotNo);
//        }
//    }
//}
