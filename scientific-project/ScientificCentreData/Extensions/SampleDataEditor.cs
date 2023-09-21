using Nest;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ScientificCentreData
{
   // [Nest.ElasticsearchType(RelationName = "eventelastic", IdProperty = "Id")]
    public class SampleDataEditor
    {
        [Nest.Number]
        public int Id { get; set; }
        
        public Guid EditorId { get; set; }
        public int MagazineId { get; set; }

        [Nest.GeoPoint]
        public LocationModel Location { get; set; }

    }

    [Nest.ElasticsearchType(RelationName = "Location", IdProperty = "Id")]
    public class LocationModel
    {
        public LocationModel(double lon, double lat)
        {
            Lon = lon;
            Lat = lat;
       
        }

        [Number(NumberType.Double, Name = "Lon")]
        public double Lon { get; set; }
        [Number(NumberType.Double, Name = "Lat")]
        public double Lat { get; set; }
    }
}