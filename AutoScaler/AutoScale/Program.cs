/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.ServiceModel;
using System.ServiceModel.Channels;
using System.Xml.Linq;
using Microsoft.Samples.WindowsAzure.ServiceManagement;
using AutoScaler.AutoScale;
using AutoScaler.LoadGen;
using System.Diagnostics;
using AutoScaler.Tests;
using AutoScaler.Benchmark;
using System.Windows.Forms.DataVisualization.Charting;
using AutoScaler.Budget;



namespace AutoScaler
{
    class Program
    {


        static void Main(string[] args)
        {
            try
            {
                RUBiSAutoScaleManager rbs = new RUBiSAutoScaleManager();
                rbs.StartAutoScaling();



                //rbs.test();
                //AutoScaleManager autoScMgr = new AutoScaleManager();
                //autoScMgr.StartAutoScaling();
                //autoScMgr.test();

                //PerfectHP perfHp = new PerfectHP();
                //perfHp.StartAutoScaling();
                //perfHp.test();

                //ReactSC rctSC = new ReactSC();
                //rctSC.StartAutoScaling();

                //DateTimeUtility dtUtil = new DateTimeUtility();
                //dtUtil.SleepUntillStartTime();
                //autoScMgr.test();


                //ClientGetAsync cg = new ClientGetAsync();
                //cg.start();
                //Console.WriteLine("Request Generated");



                //ResponseDetail rspDetail = new ResponseDetail();
                //HttpRequestCustom req = new HttpRequestCustom("http://localhost:39137/", rspDetail);
                //req.GetResponse();
                //Console.WriteLine("Connection Time: " + rspDetail.connectionTime);
                //Console.WriteLine("Response Time: " + rspDetail.responseTime);

               //System.Threading.Thread.Sleep(5000);



                //DelayTest dt = new DelayTest();
                //dt.StartGeneratingLoad();


                //AdaptiveDelayTest adt = new AdaptiveDelayTest();
                //adt.StartGeneratingLoad();


                //GenerateDelayTable gdt = new GenerateDelayTable();
                //gdt.StartGeneratingLoad();


                //GenerateAdaptiveDelayTable gadt = new GenerateAdaptiveDelayTable();
                //gadt.StartGeneratingLoad();


                //RuntimeDelay rrd = new RuntimeDelay(SimulationSettings.runtimeDelayInputFile);
                //Console.WriteLine(rrd.getDelay(1, 55));
                //rrd.printDelayTable();


                //WorkloadGen wg = new WorkloadGen(SimulationSettings.workloadGenInputFile);
                //Console.WriteLine(wg.getWorkloadArrival(47));
                //wg.generateRUBiSAzureLoadGenFile(20, SimulationSettings.emptySlotBetweenVs, "azureLoadmsr.csv");
                ////Console.WriteLine(wg.getWorkloadArrival(97)/200);

                //WorkloadGen rcwg = new WorkloadGen(SimulationSettings.workloadGenInputFile);
                //rcwg.generateRUBiSReactSCGenFile(20, SimulationSettings.emptySlotBetweenVs, "azureLoadmsr_sc.csv");
  

                //RUBiSRuntimeDelayTest rbTest = new RUBiSRuntimeDelayTest();
                //rbTest.startScaling();

                //GenerateRUBiSDelayTable rbDT = new GenerateRUBiSDelayTable();
                //rbDT.StartGeneratingTable();



                //optOfflineBench.test();

                //GraphData delayGraph = new GraphData();
                //delayGraph.generateGraphData("delayGraphData.csv");

                //Scaler scaler = new Scaler();
                //scaler.ScaleDeployment(12);

                //benchmarks
                //CBAS cbas = new CBAS();
                //cbas.StartAutoScaling();

                //OptOffline optOfflineBench = new OptOffline();
                //optOfflineBench.StartAutoScaling();

                //EqlSC eqlSC = new EqlSC();
                //eqlSC.StartAutoScaling();

                //ReactSC rctSC = new ReactSC();
                //rctSC.StartAutoScaling();

                //PerfOpt perfOpt = new PerfOpt();
                //perfOpt.StartAutoScaling();


                //user budget
                //RUBiSUserBud rbubud = new RUBiSUserBud();
                //rbubud.StartAutoScaling();

                //EqlSCBud equbud = new EqlSCBud();
                //equbud.StartAutoScaling();

                //OptOfflineBud optBud = new OptOfflineBud();
                //optBud.StartAutoScaling();


                //impv_bud_test
                //RUBiSImpvBud rbInd1 = new RUBiSImpvBud(0);
                //rbInd1.StartAutoScaling();

                //RUBiSImpvBud rbInd2 = new RUBiSImpvBud(3);
                //rbInd2.StartAutoScaling();

                //RUBiSImpvBud rbInd3 = new RUBiSImpvBud(5);
                //rbInd3.StartAutoScaling();

                //RUBiSAutoScaleManager rbs = new RUBiSAutoScaleManager();
                //rbs.StartAutoScaling();

            }
            catch (Exception ex)
            {

                Console.WriteLine(ex);

            }
        }

    }
}
