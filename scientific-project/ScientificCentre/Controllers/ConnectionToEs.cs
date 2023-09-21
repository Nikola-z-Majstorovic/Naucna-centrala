using Elasticsearch.Net;
using Nest;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ScientificCentre.Controllers
{
    public class ConnectionToEs
    {

        public ElasticClient EsClient()
        {
            var nodes = new Uri[]
            {
                new Uri("http://localhost:9200/"),
            };

            var connectionPool = new StaticConnectionPool(nodes);
            var connectionSettings = new ConnectionSettings(connectionPool).DisableDirectStreaming();
            connectionSettings.DefaultIndex("sampledata"); //dodato 12/16/2019
            var elasticClient = new ElasticClient(connectionSettings);
            
            return elasticClient;
        }

    }
}