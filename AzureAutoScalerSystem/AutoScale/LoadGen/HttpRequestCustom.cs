/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.IO;
using System.Diagnostics;

namespace AutoScaler.LoadGen
{

        public class HttpRequestCustom
        {
            private HttpWebRequest request;
            private String url;
            private ResponseDetail respDetail;
            private int status;

            public int Status
            {
                get
                {
                    return status;
                }
                set
                {
                    status = value;
                }
            }

            public HttpRequestCustom(String url, ResponseDetail respDetail)
            {

                this.url = url;
                this.respDetail = respDetail;
            }

            public void start()
            {

            }

            public ResponseDetail GetResponse()
            {
                //ResponseDetail respDetail = new ResponseDetail();
                Stopwatch timer = new Stopwatch();
                timer.Start();

                request = (HttpWebRequest)WebRequest.Create(url);
                // Get the original response.
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                TimeSpan tsp = timer.Elapsed;


                timer.Restart();
                //this.Status = ((HttpWebResponse)response).StatusDescription;
                Console.WriteLine(response.StatusCode);
                // Get the stream containing all content returned by the requested server.
                Stream dataStream = response.GetResponseStream();

                // Open the stream using a StreamReader for easy access.
                StreamReader reader = new StreamReader(dataStream);

                // Read the content fu lly up to the end.
                string responseFromServer = reader.ReadToEnd();
                Console.WriteLine(responseFromServer);




                respDetail.responseTime = timer.Elapsed.TotalMilliseconds;



                // Clean up the streams.
                reader.Close();
                dataStream.Close();
                response.Close();
                respDetail.connectionTime = timer.Elapsed.TotalMilliseconds + tsp.TotalMilliseconds;



                return respDetail;
            }

        }


    }

