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
    public class WorkloadGen
    {
        protected List<double> workloadArrival;
        protected String inputFileName;
        //private int maxArrivalToConsider = 4320;
        protected int maxArrivalToConsider = 48;
        //protected double maxWorkloadValue = 1120;

        protected double maxWorkloadValue = 900;
        public int availableHours()
        {
            return maxArrivalToConsider;
        }
        public WorkloadGen(String inputFileName)
        {
            this.inputFileName = inputFileName;
            workloadArrival = new List<double>();
            readLoadGenFile();

        }

        public void readLoadGenFile()
        {
            try
            {
                String line;
                StreamReader reader = new StreamReader(File.OpenRead(inputFileName));

                for (int i = 0; !reader.EndOfStream && i < maxArrivalToConsider; i++)
                {
                    line = reader.ReadLine();
                    workloadArrival.Add(Convert.ToDouble(line) * maxWorkloadValue);

                }

                reader.Close();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex.ToString());
            }


            

        }

        public void printWorkload()
        {
            for (int j = 0; j < workloadArrival.Count(); j++)
            {
                Console.WriteLine(workloadArrival[j]);
            }
            Console.WriteLine("\nTotal Workload: "+workloadArrival.Count);
        }


        public void generateAzureLoadGenFile(int repeat, int emptySlot, string outputFileName)
        {
            StreamWriter loadgenFile = new System.IO.StreamWriter(outputFileName);
            int slotNo = 0;
            for (int i = 0; i < repeat+1; i++)
            {
                
                for (int j = 0; j < workloadArrival.Count(); j++)
                {

                    loadgenFile.WriteLine(slotNo + ",15,15,1,1," + workloadArrival[j]);
                    slotNo++;
                }

                for (int j = 0; j < emptySlot; j++)
                {
                    loadgenFile.WriteLine(slotNo + ",15,15,1,1,1");
                    slotNo++;
                }

            }
        
            loadgenFile.Flush();
            loadgenFile.Close();

        }


        public void generateRUBiSAzureLoadGenFile(int repeat, int emptySlotinVs, string outputFileName)
        {
            StreamWriter loadgenFile = new System.IO.StreamWriter(outputFileName);
            loadgenFile.WriteLine("Slot No, V slot no, No of Client");
            int slotNo = 0;
            for (int i = 0; i < repeat + 1; i++)
            {

                for (int j = 0; j < workloadArrival.Count(); j++)
                {

                    loadgenFile.WriteLine(slotNo + ","+j+"," + workloadArrival[j]);
                    slotNo++;
                }

                for (int j = 0; j < emptySlotinVs; j++)
                {
                    loadgenFile.WriteLine(slotNo + "," + workloadArrival.Count() + ",1" );
                    slotNo++;
                }

            }

            loadgenFile.Flush();
            loadgenFile.Close();

        }
        public void generateRUBiSReactSCGenFile(int repeat, int emptySlotinVs, string outputFileName)
        {
            StreamWriter loadgenFile = new System.IO.StreamWriter(outputFileName);
            loadgenFile.WriteLine("Slot No, V slot no, No of Client");
            int slotNo = 0;
            int avgPeriod = 4;
            for (int i = 0; i < repeat + 1; i++)
            {

                for (int j = 0; j < workloadArrival.Count(); )
                {
                    double sum = 0;
                    for (int k = 0; k < avgPeriod; k++)
                    {
                        sum = sum + workloadArrival[j + k];
                    }
                    double avg = sum / avgPeriod;

                    for (int k = 0; k < avgPeriod; k++)
                    {
                        loadgenFile.WriteLine(slotNo + "," + j + "," + avg);
                        slotNo++;
                    }


                    j = j + avgPeriod;
                }

                for (int j = 0; j < emptySlotinVs; j++)
                {
                    loadgenFile.WriteLine(slotNo + "," + workloadArrival.Count() + ",1");
                    slotNo++;
                }

            }

            loadgenFile.Flush();
            loadgenFile.Close();

        }

        public double[] getAllWorkloadArrival()
        {
            return workloadArrival.ToArray();
        }

        public double geActualtWorkloadArrival(int slotNo)
        {
                        double arrivalRate = 1;
                        if (slotNo < workloadArrival.Count())
                        {
                            arrivalRate = workloadArrival[slotNo];
                        }
                        return arrivalRate;
        }

        public double getWorkloadArrival(int slotNo)
        {
            //double arrivalRate = 1;
            //if (slotNo < workloadArrival.Count())
            //{
            //    arrivalRate = workloadArrival[slotNo];

            //    if (slotNo < SimulationSettings.repetativPatternLength)
            //    {
            //        arrivalRate = workloadArrival[slotNo];
            //    }
            //    else
            //    {
            //        double totalArrival = 0;
            //        int count = 0;
            //        for (int i = slotNo-SimulationSettings.repetativPatternLength; count <SimulationSettings.prevTimeSlotToConsider && i >= 0; i = i - SimulationSettings.repetativPatternLength)
            //        {
            //            totalArrival = totalArrival + workloadArrival[i];
            //            count++;
            //            //Console.WriteLine(workloadArrival[i]/maxWorkloadValue);
            //        }

            //        arrivalRate = totalArrival / count;
            //    }


            //}
            //return arrivalRate;
            //Console.WriteLine(geActualtWorkloadArrival(slotNo));
            return geActualtWorkloadArrival(slotNo);
        }
    }
}
