using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ScientificCentreData.Extensions
{
    public class ProjectFilter
    {
        public bool FillQuery { get; set; }

        public bool PhrazeQuery { get; set; }
        /// <summary>
        /// ///////////////////
        /// </summary>
        public string MagazineName { get; set; }

        public string ProjectName { get; set; } // subject

        public string KeyWords { get; set; }

        public string AuthorsName { get; set; }

        public string ProjectContent { get; set; }

        public string ScientificArea { get; set; }

        public bool FillAnalyzer { get; set; }
    }
}
