
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Reflection;
using System.Web.Http;

using ScientificCentre.Base.Data;
using ScientificCentre.Base.Web.Http;
using ScientificCentreData;

namespace ScientificCentre.Controllers
{
    public class SubscriptionsController : BaseApiController, IBaseActions
    {
        [HttpGet]
        public HttpResponseMessage PreparePrice(int objId)
        {
            if (ModelState.IsValid)
            {
                //BaseRepository bs = new BaseRepository();
                int basePrice = 30; //bs.getprice
                int sumToPay = 0;

                switch (objId)
                {
                    case 1://one month
                        sumToPay = basePrice;
                        break;
                    case 2://three month
                        sumToPay = (basePrice * 3) - 10;
                        break;
                    case 3://six month
                        sumToPay = (basePrice * 6) - 15;
                        break;

                }
                
                return Ok(sumToPay, HttpStatusCode.OK, "Successfully Get");
            }
            else
            {
                return Error(HttpStatusCode.NotAcceptable, ModelState);
            }
        }

        public HttpResponseMessage Create(dynamic model)
        {
            throw new NotImplementedException();
        }


        public HttpResponseMessage Get(int objId)
        {
            BaseRepository bs = new BaseRepository();

            int access = bs.GetMagazineForId(objId).Access;

         


            if (bs.GetMagazineForId(objId).Access == 1)
            {
                Subscription userSub = bs.GetSubscriptionForUserId(GetUserId(), objId);

              

                //user is without subscription
                if (userSub == null)
                {
                  
                    string[] errors = new string[] { "You are not subscribed", "Please subscribe" };
                    return Ok(false, HttpStatusCode.Forbidden, "Subscription Expired", errors);
                }
                else if (userSub.SubscriptionExpiryDate < DateTime.Now)
                {
                   

                    string[] errors = new string[] { "Subscription Expired", "Your subscription expired on: " + userSub.SubscriptionExpiryDate.ToString() + " Please renew subscription" };
                    return Ok(false, HttpStatusCode.Forbidden, "Subscription Expired", errors);
                }
               
            }

            string[] message = new string[] { "Magazine Selected", "Magazine projects are loaded successfuly" };
            return Ok(true, HttpStatusCode.OK, "Magazine Selected", message);
        }

        public HttpResponseMessage GetAll()
        {
            throw new NotImplementedException();
        }

        public HttpResponseMessage Update(dynamic model)
        {
            throw new NotImplementedException();
        }

      /*  [HttpPut]
        public HttpResponseMessage PreparePrice(dynamic model)
        {
            Subscription sub = MapJsonToModelObject<Subscription>(model, false);

            if (ModelState.IsValid)
            {
                //BaseRepository bs = new BaseRepository();
                int basePrice = 30; //bs.getprice
                int sumToPay = 0;

                switch (sub.sc)
                {
                    case 1://one month
                        sumToPay = basePrice;
                        break;
                    case 2://three month
                        sumToPay = (basePrice * 3) - 10;
                        break;
                    case 3://six month
                        sumToPay = (basePrice * 6) - 15;
                        break;

                }

                return Ok(sumToPay, HttpStatusCode.OK, "Successfully Get");
            }
            else
            {
                return Error(HttpStatusCode.NotAcceptable, ModelState);
            }
        }*/

        public HttpResponseMessage Delete(int objId)
        {
            throw new NotImplementedException();
        }
    }
}