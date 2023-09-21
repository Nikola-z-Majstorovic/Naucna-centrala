using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ScientificCentreData
{
    public class SampleData
    {
        public int Id { get; set; }//project id
        public string MagazineName { get; set; }
        public string ProjectName { get; set; } //subject
        public string ProjectKeywords { get; set; }
        public string ProjectAuthors { get; set; }
        public string ProjectContent { get; set; }
        public string AreaName { get; set; }
        public string DOI { get; set; }
    }
}