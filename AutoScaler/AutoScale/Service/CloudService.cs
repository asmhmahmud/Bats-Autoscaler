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

namespace AutoScaler.Service
{
    class CloudService
    {
        private const string ServiceEndpoint = "https://management.core.windows.net";
        private String publishSettingsFile;

        public CloudService(String publishSettingsFile)
        {
            this.publishSettingsFile = publishSettingsFile;
        }


        private static Binding WebHttpBinding()
        {
            var binding = new WebHttpBinding(WebHttpSecurityMode.Transport);
            binding.Security.Transport.ClientCredentialType = HttpClientCredentialType.Certificate;
            binding.ReaderQuotas.MaxStringContentLength = 67108864;

            return binding;
        }



        public IServiceManagement getManagementClient()
        {



            //Generate the certificate
            X509Store certificateStore = new X509Store(StoreName.My, StoreLocation.CurrentUser);
            certificateStore.Open(OpenFlags.ReadOnly);
            X509Certificate2Collection certs = certificateStore.Certificates.Find(
                X509FindType.FindByThumbprint,
                Settings.certThumbprint, false
                
                );
            //Console.WriteLine(Settings.certThumbprint);
            //Console.WriteLine(certs.Count+",  "+certs[0].Thumbprint);

            if (certs.Count == 0)
            {
                try
                {

                    Console.WriteLine("Attempting To Get Certificates....");


                    XDocument xdoc = XDocument.Load(publishSettingsFile);
                    //Console.WriteLine(xdoc.Descendants("PublishProfile").Single().Elements().ElementAt(0).Attribute("ManagementCertificate"));

                    
                    //String managementCertbase64string = xdoc.Descendants("PublishProfile").Single().Elements().ElementAt(0).Attribute("ManagementCertificate").Value;
                    String managementCertbase64string = xdoc.Descendants("PublishProfile").Single().Attribute("ManagementCertificate").Value;
              
                    //Console.WriteLine(managementCertbase64string);

                    X509Certificate2 importedCert = new X509Certificate2(
                        Convert.FromBase64String(managementCertbase64string));

                    string thumbprint = importedCert.Thumbprint;
                    //string subscriptionId = xdoc.Descendants("Subscription").First().Attribute("Id").Value;
                    Console.WriteLine("Thumbprint: " + thumbprint);
                    //Console.WriteLine("Subscription Id: " + subscriptionId);


                    X509Store store = new X509Store(StoreName.My, StoreLocation.CurrentUser);
                    store.Open(OpenFlags.ReadWrite);
                    store.Add(importedCert);
                    store.Close();

                    //Console.WriteLine("Couldn't find the certificate with thumbprint:" + certThumbprint);
                    //return;

                    certs = certificateStore.Certificates.Find(X509FindType.FindByThumbprint, Settings.certThumbprint, false);

                    if (certs.Count > 0)
                    {
                        Console.WriteLine("Successfully Got Certificate ....");
                    }
                    else
                    {
                        throw new Exception("No certificate in local certificate store");
                    }

                }
                catch (Exception ex)
                {
                    Console.WriteLine("Failed to get certificate ....");
                    Console.WriteLine(ex);
                    return null;
                }

            }

            certificateStore.Close();

            
            ////Generate the management client
            IServiceManagement managementClient = ServiceManagementHelper.CreateServiceManagementChannel(
                WebHttpBinding(), new Uri(ServiceEndpoint), certs[0]);


            return managementClient; 

        }
    }
}
