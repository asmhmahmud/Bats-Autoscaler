/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Linq;
using Microsoft.Samples.WindowsAzure.ServiceManagement;
using System.Xml.Linq;
using AutoScaler.Service;

namespace AutoScaler.AutoScale
{
    class Scaler
    {
        IServiceManagement managementClient=null;

        public Scaler()
        {
            CloudService serviceConnection = new CloudService(Settings.publishSettingsFile);
            managementClient = serviceConnection.getManagementClient();
            if (managementClient == null)
            {
                throw new Exception("Failed to initiate management client");
            }
        }

        public int GetCurrentInstanceCount()
        {
            int instanceCount = 0;

            XDocument serviceConfiguration = getCurrentServiceConfiguration();
            //Console.WriteLine(serviceConfiguration);

            instanceCount = Int32.Parse( serviceConfiguration.Descendants().Elements().ElementAt(0).Elements().ElementAt(1).Attribute("count").Value);
            return instanceCount;

        }

        private XDocument getCurrentServiceConfiguration()
        {
            Deployment deployment = managementClient.GetDeploymentBySlot(Settings.subscriptionId, Settings.serviceName, Settings.slot);
            string configurationXml = ServiceManagementHelper.DecodeFromBase64String(deployment.Configuration);
            return XDocument.Parse(configurationXml);

        }

        public void ScaleDeployment(int instanceCount)
        {

            try
            {

                //Console.WriteLine(GetCurrentInstanceCount());

                Deployment deployment = managementClient.GetDeploymentBySlot(Settings.subscriptionId, Settings.serviceName, Settings.slot);


                if (deployment.Status.Equals(DeploymentStatus.Running, StringComparison.OrdinalIgnoreCase))
                {
                    XDocument serviceConfiguration = getCurrentServiceConfiguration();
                    int currentInstanceCount = Int32.Parse(serviceConfiguration.Descendants().Elements().ElementAt(0).Elements().ElementAt(1).Attribute("count").Value);

                    if (currentInstanceCount == instanceCount)
                    {
                        Console.WriteLine("Scale instance count is same as running instance count. ");
                        Console.WriteLine("Running instance count: " + currentInstanceCount + ", Scale Request will not be submitted");
                    }
                    else
                    {
                        Console.WriteLine("Running instance count: " + currentInstanceCount + ", Scaled instance count: " + instanceCount);
                        serviceConfiguration
                            .Descendants()
                            .Single(d => d.Name.LocalName == "Role" && d.Attributes().Single(a => a.Name.LocalName == "name").Value == Settings.roleName)
                            .Elements()
                            .Single(e => e.Name.LocalName == "Instances")
                            .Attributes()
                            .Single(a => a.Name.LocalName == "count").Value = instanceCount.ToString();

                        var changeConfigurationInput = new ChangeConfigurationInput();
                        changeConfigurationInput.Configuration = ServiceManagementHelper.EncodeToBase64String(serviceConfiguration.ToString(SaveOptions.DisableFormatting));

                        Console.WriteLine("Uploading new configuration...");

                        managementClient.ChangeConfigurationBySlot(Settings.subscriptionId, Settings.serviceName, Settings.slot, changeConfigurationInput);

                        Console.WriteLine("Finished.");
                    }


                }
                else
                {
                    Console.WriteLine("Deployment Status is not " + DeploymentStatus.Running + ", Current Status: " + deployment.Status);
                    Console.WriteLine("Scaling Request Will Ignored......");
                }


            }
            catch (Exception ex)
            {

                Console.WriteLine("Error While Scaling....");
                Console.WriteLine(ex.ToString());
            }

        }


    }
}
