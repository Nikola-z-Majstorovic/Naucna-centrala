using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Web.Http;
using System.Web.Http.Filters;

namespace ScientificCentre.Base.Web.Http
{
    public interface IBaseActions
    {
        [HttpGet]
        [System.Web.Http.ActionName("GetAll")]
        HttpResponseMessage GetAll();

        [HttpGet]
        [System.Web.Http.ActionName("Get")]
        HttpResponseMessage Get(int objId);

        [HttpPost]
        [System.Web.Http.ActionName("Add")]
        HttpResponseMessage Create(dynamic model);

        [HttpPut]
        [System.Web.Http.ActionName("Update")]
        HttpResponseMessage Update(dynamic model);

        [HttpDelete]
        [System.Web.Http.ActionName("Delete")]
        HttpResponseMessage Delete(int objId);
    }
}
