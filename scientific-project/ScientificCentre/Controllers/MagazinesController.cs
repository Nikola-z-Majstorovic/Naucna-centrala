

using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using ScientificCentre.Base.Data;
using ScientificCentre.Base.Web.Http;
using ScientificCentreData;

namespace ScientificCentre.Controllers
{
    public class MagazinesController : BaseApiController, IBaseActions
    {
        public HttpResponseMessage Create(dynamic model)
        {
            throw new NotImplementedException();
        }

        public HttpResponseMessage Delete(int objId)
        {
            throw new NotImplementedException();
        }

        public HttpResponseMessage Get(int objId)
        {
            if (ModelState.IsValid)
            {
                BaseRepository bs = new BaseRepository();

                Magazine magazine = bs.GetSelectedMagazine(objId);
                return Ok(magazine, HttpStatusCode.OK, "Successfully Get");
            }
            else
            {
                return Error(HttpStatusCode.NotAcceptable, ModelState);
            }
        }

        public HttpResponseMessage GetAll()
        {
            if (ModelState.IsValid)
            {
                BaseRepository bs = new BaseRepository();
                List<Magazine> locationsList = bs.GetAllMagazines();
                string[] messages = new string[] { "Successfully magazines loaded", "Successfully magazines loaded and listed" };
                return Ok(locationsList, HttpStatusCode.OK, "Successfully GetAll", messages);
            }
            else
            {
                return Error(HttpStatusCode.NotAcceptable, ModelState);
            }
        }

        public HttpResponseMessage Update(dynamic model)
        {
            throw new NotImplementedException();
        }
    }
}