/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using AutoScaler.AutoScale;
using System.IO;

namespace AutoScaler.Tests
{
    public class GenerateAdaptiveDelayTable
    {
        private String inputFilePath = "CombinedDelayResult.csv";
        //private String outputFilePath = "runtimeDelay.csv";
        public GenerateAdaptiveDelayTable()
        {

        }




        public void StartGeneratingLoad()
        {


            StreamReader reader = new StreamReader(File.OpenRead(inputFilePath));
            StreamWriter output = new StreamWriter(SimulationSettings.runtimeDelayInputFile);

            if (!reader.EndOfStream)
            {
                reader.ReadLine();
            }


            String line;
            String[] values;
            int instanceCount;
            double delay;
            double arrivalRate, prevArrival=0;
            Boolean lineOutputted = true;

            for (int i = 0; i < SimulationSettings.maxArrivalToConsider; i++)
            {
                output.Write("instanceCount,arrivalRate,delay,");
                Console.Write("instanceCount,arrivalRate,delay,");
            }

            while (!reader.EndOfStream)
            {
                line = reader.ReadLine();
                //Console.WriteLine(line);
                values = line.Split(',');

                arrivalRate = Convert.ToDouble(values[3]);

                //checking whether the arrival is 1;
                if (arrivalRate != 1 || !lineOutputted)
                {
                    instanceCount = (int)Convert.ToDouble(values[6]);
                    delay = Convert.ToDouble(values[4]);

                    Console.Write(instanceCount+ "," +arrivalRate+"," + delay);
                    output.Write(instanceCount + "," + arrivalRate + "," + delay+",");
                    lineOutputted = true;

                }

                if (arrivalRate == 1 && prevArrival == 1 && lineOutputted)
                {
                    output.WriteLine("");
                    Console.WriteLine("");
                    lineOutputted = false;
                    arrivalRate = 0;

                }



                prevArrival = arrivalRate;
            }


            reader.Close();
            output.Close();

        }


    }
}
