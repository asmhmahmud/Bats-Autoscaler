/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AutoScaler.AutoScale
{
    class Settings
    {

        readonly public static String pcTableName = "WADPerformanceCountersTable";


        
        //rubisphpfive
        readonly public static String serviceName = "rubisphpfive";
        readonly public static String roleName = "phpwebrole";
        readonly public static String slot = "Production";
        readonly public static String publishSettingsFile = "phpfive.publishsettings";
        readonly public static String subscriptionId = "096f62ed-732f-485c-8274-85ad1bff6514";
        readonly public static String certThumbprint = "7B0CBB3F8D59681C5F17C9DF6F4408DDDF8AEFCB";
        readonly public static String pcConnString = "DefaultEndpointsProtocol=https;" +
            "AccountName=phpfivepc;" +
            "AccountKey=5qUCiitzVqlXVu4OfrRJa8S1AzH/rt0G5Soua8OSag2EGiDsEpxEJuL3IIPFIQrzuHhvfo/NsmXiLmka/N0fzw==";



        //rubisphpsix
        //readonly public static String serviceName = "rubisphpsix";
        //readonly public static String roleName = "phpwebrole";
        //readonly public static String slot = "Production";
        //readonly public static String publishSettingsFile = "phpsix.publishsettings";
        //readonly public static String subscriptionId = "f392531a-cac8-4026-bf66-3b4bdde859b3";
        //readonly public static String certThumbprint = "8A412384F34E0B06F070D172BE2D6DD21243479C";
        //readonly public static String pcConnString = "DefaultEndpointsProtocol=https;" +
        //    "AccountName=phpsixpc;" +
        //    "AccountKey=DZrHQk/u2otoh+LHgLlMCyodruGO6eo+nT+Lj8n2euzNho3F/7zH1pqL7Jfxgv00oZ+Dm3CrCVBHL0vCT9JJ4w==";






    }
}
