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
    public class GenerateRUBiSDelayTable
    {
        private String inputFilePath = "RUBiSOriginalDelayTable.csv";
        //private String outputFilePath = "runtimeDelay.csv";
        public GenerateRUBiSDelayTable()
        {

        }




        public void StartGeneratingTable()
        {


            StreamReader reader = new StreamReader(File.OpenRead(inputFilePath));
            StreamWriter output = new StreamWriter(SimulationSettings.runtimeDelayInputFile);
            //StreamWriter output = new StreamWriter("RUBiSDelaySmall.csv");
            //StreamReader reader = new StreamReader("Small2.csv");
            //StreamWriter output = new StreamWriter("SmallDelay.csv");
            //StreamReader reader = new StreamReader("Small.csv");

            if (!reader.EndOfStream)
            {
                reader.ReadLine();
            }


            String line;
            String[] values;
            int instanceCount;
            double delay;
            int throughputRate;
            double noOfClient = 0 ;

            //for (int i = 0; i < SimulationSettings.maxArrivalToConsider; i++)
            for (int i = 0; i < 23; i++)
            {
                output.Write("instanceCount,No of client,delay,Throughput,");
                //Console.Write("instanceCount,No of client,delay,Throughput,");
            }

            int count = 0;
            while (!reader.EndOfStream)
            {
                if (count % SimulationSettings.maxArrivalToConsider == 0)
                {
                    output.WriteLine("");
                    //Console.WriteLine("");

                }
                count++;
                line = reader.ReadLine();
                //Console.WriteLine(line);
                values = line.Split(',');

                noOfClient = Convert.ToDouble(values[4]);
                //Console.WriteLine(noOfClient);


                instanceCount = (int)Convert.ToDouble(values[3]);
                delay = Convert.ToDouble(values[5]);
                throughputRate = (int)Convert.ToDouble(values[6]);


                Console.WriteLine(instanceCount + "," + noOfClient + "," + delay + "," + throughputRate);
                output.Write(instanceCount + "," + noOfClient + "," + delay + "," + throughputRate + ",");



            }

            output.WriteLine("");
            reader.Close();
            output.Close();

        }


    }
}
