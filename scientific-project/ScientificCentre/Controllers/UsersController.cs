using System;
using System.Collections.Generic;
//using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Reflection;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.Security;
using System.Web.SessionState;
using Elasticsearch.Net;
using Nest;
using ScientificCentre.Base.Data;
using ScientificCentre.Base.Web.Http;
using ScientificCentreData;
using ScientificCentreData.Extensions;
using HttpGetAttribute = System.Web.Http.HttpGetAttribute;



namespace ScientificCentre.Controllers
{
    public class UsersController : BaseApiController, IBaseActions
    {
        //public ConnectionToEs _connectionToEs;

    public ConnectionToEs ESConnection = null;
    public ElasticClient ESClient = null;

    [System.Web.Http.HttpPut]
    [System.Web.Http.ActionName("ElasticIndexing")]
    public HttpResponseMessage ElasticIndexing(dynamic model)
    {

            ESConnection = new ConnectionToEs();
            ESClient = ESConnection.EsClient();

            SampleData testData = new SampleData() { Id = 502, MagazineName = "PhisicsReaderTest", ProjectName = "Test",
                ProjectKeywords = "T2", ProjectAuthors = "TestA2utor", AreaName = "Engeneering",
                ProjectContent = "The mean level of nausea was recorded by patients after PC.6 acupressure and also after pressure at a dummy point."};
         //   ESClient.CreateDocument(testData);
            ESClient.IndexAsync(testData, null);

            return Ok(false, HttpStatusCode.OK, "Access Denied");
    }
        [System.Web.Http.HttpPut]
        [System.Web.Http.ActionName("SearchForProjects")]
        public HttpResponseMessage SearchForProjects(dynamic model)
    {
            ProjectFilter filter = MapJsonToModelObject<ProjectFilter>(model, false);

            BaseRepository bs = new BaseRepository();

            string searchTerm = "";
            string searchField = "";

            if (!string.IsNullOrEmpty(filter.MagazineName))
            {
                searchTerm = searchTerm + "magazineName,";
                searchField = searchField + filter.MagazineName + ' ';// ',';
            }
            if (!string.IsNullOrEmpty(filter.ProjectName))
            {
                searchTerm = searchTerm + "projectName,";
                searchField = searchField + filter.ProjectName + ' ';// ',';
            }
            if (!string.IsNullOrEmpty(filter.ProjectContent))
            {
                searchTerm = searchTerm + "projectContent,";
                searchField = searchField + filter.ProjectContent + ' ';// ',';
            }
            if (!string.IsNullOrEmpty(filter.AuthorsName))
            {
                searchTerm = searchTerm + "projectAuthors,";
                searchField = searchField + filter.AuthorsName + ' ';// ',';
            }
            if (!string.IsNullOrEmpty(filter.ScientificArea))
            {
                searchTerm = searchTerm + "areaName,";
                searchField = searchField + filter.ScientificArea + ' '; //',';
            }
            if (!string.IsNullOrEmpty(filter.KeyWords))
            {
                searchTerm = searchTerm + "projectKeywords";
                searchField = searchField + filter.KeyWords;
            }



            string lastChar = searchTerm.Substring(searchTerm.Length - 1);

            if (lastChar == ",")
            {
                searchTerm = searchTerm.Remove(searchTerm.Length - 1);
            }


            List<SampleData> returnedResults = GetResult(searchTerm, searchField, filter.FillQuery, filter.FillAnalyzer);

            return Ok(false, HttpStatusCode.OK, "Access Denied");

        }



        public List<SampleData> GetResult(string searchField, string searchQuery, bool matchAll, bool fillAnalyzer)
        {

            string[] fieldNamesSplit = searchField.Split(',');
            ESConnection = new ConnectionToEs();
            ESClient = ESConnection.EsClient();

            List<SampleData> datasend = new List<SampleData>();


            if (ESClient.Indices.Exists("sampledata").Exists)
            {
                //One field is sent
                if (fieldNamesSplit.Length == 1)
                {

                    // highlight one filed
                    ///////////////////////

                    //var responsedata = ESClient.Search<SampleData>(s => s
                    //        .Index("sampledata")
                    //        .Query(q => q
                    //            .Match(m => m
                    //                .Field(fieldNamesSplit[0])
                    //                .Query(searchQuery)//.Analyzer("serbian")
                    //            )
                    //            ).Highlight(h => h
                    //        .PreTags("<span style='background-color:yellow;'>")
                    //        .PostTags("</span>")
                    //        .Encoder(HighlighterEncoder.Html)
                    //        .Fields(
                    //           fs => fs
                    //                .Field("projectContent")

                    //            )
                    //        )
                    //);

                    //datasend = (from hits in responsedata.Hits
                    //            select hits.Source).ToList();


                    //int projectCounter = 0;

                    //if (datasend.Count() > 0)
                    //{
                    //    foreach (IHit<SampleData> sp in responsedata.Hits)
                    //    {

                    //        string projectName = sp.Source.ProjectName;
                    //        string highlighted = "";

                    //        foreach (var highTest in sp.Highlight)
                    //        {
                    //            if (highTest.Key == "projectContent")
                    //            {
                    //                foreach (string highlightSample in highTest.Value)
                    //                {
                    //                    //highlighted = highlighted + "|" + projectName + "} " + highlightSample;

                    //                    highlighted = highlighted + " " + highlightSample;
                    //                }
                    //            }

                    //        }


                    //        datasend[projectCounter].ProjectContent = highlighted;
                    //        projectCounter = projectCounter + 1;
                    //    }
                    //}


                    //string[] splitHighlight = highlighted.Split('|');

                    //datasend.First().ProjectContent = highlighted;

                    //   one filed
                    ////////////////////////////////
                    var responsedata = ESClient.Search<SampleData>(s => s
                                            .Index("sampledata")
                                            .Query(q => q
                                                .Match(m => m
                                                    .Field(fieldNamesSplit[0])
                                                    .Query(searchQuery)
                                                )
                                )
                        );
                    datasend = (from hits in responsedata.Hits
                                select hits.Source).ToList();

                    //var tttt = _connectionToEs.EsClient().Search<SampleData>(s => s
                    //       .Index("sampledata")
                    //        .Query(q => q
                    //            .MoreLikeThis(mlk => mlk.Fields(f => f.Field(p => p.ProjectContent)).Like(l => l.Document(d => d.Index("sampledata").Id(2))).MinTermFrequency(1).MaxQueryTerms(12))

                    //           )

                    //    );

                    //SampleData proba = new SampleData();

                    //proba.ProjectContent = "Satisfied conveying an dependent contented he gentleman";
                    //proba.MagazineName = "MathReaderTest";
                    /*  var tttt = _connectionToEs.EsClient().Search<SampleData>(s => s
                           .Index("sampledata")
                            .Query(q => q
                                .MoreLikeThis(mlk => mlk.Like(l => l.Document(d => d.Document(proba).Index("sampledata").Fields(f => f.Field(pf => pf.ProjectContent)))).MinTermFrequency(1).MaxQueryTerms(12))

                               )

                        );*/



                    /* var tttt = _connectionToEs.EsClient().Search<SampleData>(s => s
                          .Index("sampledata")
                           .Query(q => q
                               .MoreLikeThis(sn => sn
                                     .Fields(ff => ff
                                         .Field("projectContent")
                                     )
                                     .Like(l => l
                                         .Document(d => d.Document(proba).Index("sampledata"))                                          
                                     ).MinTermFrequency(1).MaxQueryTerms(15)
                                 )

                              )
                       );*/

                    //var tttt = _connectionToEs.EsClient().Search<SampleData>(s => s
                    //        .Index("sampledata")
                    //        .Query(q => q
                    //            .MoreLikeThis( 
                    //            mlk => mlk.Fields(f => f.Field(p => p.ProjectContent)))

                    //            )

                    //    );



                }

                if (!matchAll)
                {
                    if (fieldNamesSplit.Length == 2)
                    {
                        //var responsedata = _connectionToEs.EsClient().Search<SampleData>(s => s
                        //                        .Index("sampledata")
                        //                        .Query(q => q
                        //                            .Match(m => m
                        //                                .Field(fieldNamesSplit[0])
                        //                                .Field(fieldNamesSplit[1]).Query(searchQuery)
                        //                            )   
                        //            )
                        // );
                        var responsedata = ESClient.Search<SampleData>(s => s
                   .Index("sampledata")
                   .Query(q => q
                   .Bool(b => b
                       .Must(m1 =>
                       m1.Match(m => m.Field(fieldNamesSplit[0]).Query(searchQuery))
                       || m1.Match(m => m.Field(fieldNamesSplit[1]).Query(searchQuery))))
                                 )
                  );
                        datasend = (from hits in responsedata.Hits
                                    select hits.Source).ToList();
                    }
                    else if (fieldNamesSplit.Length == 3)
                    {
                        var responsedata = ESClient.Search<SampleData>(s => s
                     .Index("sampledata")
                     .Query(q => q
                     .Bool(b => b
                         .Must(m1 =>
                         m1.Match(m => m.Field(fieldNamesSplit[0]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[1]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[2]).Query(searchQuery))
                         ))
                             ));
                        datasend = (from hits in responsedata.Hits
                                    select hits.Source).ToList();
                    }
                    else if (fieldNamesSplit.Length == 4)
                    {

                        var responsedata = ESClient.Search<SampleData>(s => s
                     .Index("sampledata")
                     .Query(q => q
                     .Bool(b => b
                         .Must(m1 =>
                         m1.Match(m => m.Field(fieldNamesSplit[0]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[1]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[2]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[3]).Query(searchQuery))
                         ))
                                   ));
                        datasend = (from hits in responsedata.Hits
                                    select hits.Source).ToList();
                    }
                    else if (fieldNamesSplit.Length == 5)
                    {
                        var responsedata = ESClient.Search<SampleData>(s => s
                     .Index("sampledata")
                     .Query(q => q
                     .Bool(b => b
                         .Must(m1 =>
                         m1.Match(m => m.Field(fieldNamesSplit[0]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[1]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[2]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[3]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[4]).Query(searchQuery))
                         ))
                             ));
                        datasend = (from hits in responsedata.Hits
                                    select hits.Source).ToList();
                    }
                    else if (fieldNamesSplit.Length == 6)
                    {
                        var responsedata = ESClient.Search<SampleData>(s => s
                     .Index("sampledata")
                     .Query(q => q
                     .Bool(b => b
                         .Must(m1 =>
                         m1.Match(m => m.Field(fieldNamesSplit[0]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[1]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[2]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[3]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[4]).Query(searchQuery))
                         || m1.Match(m => m.Field(fieldNamesSplit[5]).Query(searchQuery))
                         ))
                             ));
                        datasend = (from hits in responsedata.Hits
                                    select hits.Source).ToList();
                    }

                }
                else
                {// MATCH ALL
                    if (fieldNamesSplit.Length == 2)
                    {

                        //var responsedata = _connectionToEs.EsClient().Search<SampleData>(s => s
                        //                        .Index("sampledata")
                        //                        .Query(q => q
                        //                    .DisMax(dm => dm
                        //                        .Queries(dq => dq
                        //                        .Bool(b => b
                        //                            .Must(m1 => m1.Match(m => m.Field(fieldNamesSplit[0]).Query("Test TestAutor")) && m1.Match(m => m.Field(fieldNamesSplit[1]).Query("TestAutor"))))

                        //                        )
                        //                    )

                        //                )

                        var responsedata = ESClient.Search<SampleData>(s => s
                        .Index("sampledata")
                        .Query(q => q
                        .Bool(b => b
                            .Must(m1 =>
                            m1.Match(m => m.Field(fieldNamesSplit[0]).Query(searchQuery))
                            && m1.Match(m => m.Field(fieldNamesSplit[1]).Query(searchQuery))))
                                      )
                       );
                        datasend = (from hits in responsedata.Hits
                                    select hits.Source).ToList();
                    }
                    else if (fieldNamesSplit.Length == 3)
                    {
                        var responsedata = ESClient.Search<SampleData>(s => s
                        .Index("sampledata")
                        .Query(q => q
                        .Bool(b => b
                            .Must(m1 =>
                            m1.Match(m => m.Field(fieldNamesSplit[0]).Query(searchQuery))
                            && m1.Match(m => m.Field(fieldNamesSplit[1]).Query(searchQuery))
                            && m1.Match(m => m.Field(fieldNamesSplit[2]).Query(searchQuery))))
                                      ));
                        datasend = (from hits in responsedata.Hits
                                    select hits.Source).ToList();
                    }
                    else if (fieldNamesSplit.Length == 4)
                    {
                        var responsedata = ESClient.Search<SampleData>(s => s
                     .Index("sampledata")
                     .Query(q => q
                     .Bool(b => b
                         .Must(m1 =>
                         m1.Match(m => m.Field(fieldNamesSplit[0]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[1]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[2]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[3]).Query(searchQuery))
                         ))
                                   ));
                        datasend = (from hits in responsedata.Hits
                                    select hits.Source).ToList();
                    }
                    else if (fieldNamesSplit.Length == 5)
                    {
                        var responsedata = ESClient.Search<SampleData>(s => s
                     .Index("sampledata")
                     .Query(q => q
                     .Bool(b => b
                         .Must(m1 =>
                         m1.Match(m => m.Field(fieldNamesSplit[0]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[1]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[2]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[3]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[4]).Query(searchQuery))
                         ))
                                   ));
                        datasend = (from hits in responsedata.Hits
                                    select hits.Source).ToList();
                    }
                    else if (fieldNamesSplit.Length == 6)
                    {
                        var responsedata = ESClient.Search<SampleData>(s => s
                     .Index("sampledata")
                     .Query(q => q
                     .Bool(b => b
                         .Must(m1 =>
                         m1.Match(m => m.Field(fieldNamesSplit[0]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[1]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[2]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[3]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[4]).Query(searchQuery))
                         && m1.Match(m => m.Field(fieldNamesSplit[5]).Query(searchQuery))
                         ))
                                   ));
                        datasend = (from hits in responsedata.Hits
                                    select hits.Source).ToList();
                    }
                }
            }
            return datasend;
        }

        [System.Web.Http.HttpPost]
    [System.Web.Http.ActionName("login")]
    public HttpResponseMessage Login(dynamic model)
    {
            {

                ScientificCentreUser user = MapJsonToModelObject<ScientificCentreUser>(model, false);

                if (ModelState.IsValid)
                {
                    BaseRepository bs = new BaseRepository();


                    bool isAuthenticared = Membership.ValidateUser(user.Username, user.Password);
                    MembershipUser mu = Membership.GetUser(user.Username);
                    //Roles.CreateRole("Admin");


                    if (isAuthenticared)
                    {

                        CreateUserSessions(user.Username, this.Request, Session);


                        FormsAuthentication.SetAuthCookie(user.Username, true);
                        Session.Timeout = Convert.ToInt32(FormsAuthentication.Timeout.TotalMinutes);
                        string[] messages = new string[] { "Successfully logged in", "Welcome to Scientific Centre Application" };


                        //-----------------------------------------------------
                        ScientificCentreUser lm = new ScientificCentreUser();
                        lm = ((ScientificCentreUser)Session["User"]);

                        //RolePrincipal r = (RolePrincipal)HttpContext.Current.User;
                        lm.Roles = Roles.GetRolesForUser(mu.UserName);

/*
                        if(lm.Roles.Any(role => role != "Author" && role != "Reader"))
                        {
                            lm.Tasks.ToList().ForEach(task =>
                               {
                                   if(task.ExpirationDate <= DateTime.Now)
                                   {//update task to be expired
                                       task.Expired = true;
                                       bs.UpdateTasktask;

                                   }
                               }

                              );
                               
                        }
                        */
                        //if (lm.Roles.Length == 0)
                        //{//This is for some users who doesnt have roles
                        //    string[] errors = new string[] { "Access Denied", "Your role does not allow Booking Agency access. Please contact your center administrator to get access to the Booking Agency." };
                        //    return Ok(false, HttpStatusCode.OK, "Access Denied", errors);
                        //}

                        Session["ln"] = lm;
                        //-----------------------------------------------------


                        return Ok(lm, HttpStatusCode.OK, "Success", messages);

                    }
                    else
                    {
                        string[] errors = new string[] { "Access Denied", "Wrong username or password" };
                        if (mu != null)
                        {
                            if (mu.IsApproved == false)
                            {
                                errors = new string[] { "Access Denied", "Your account has been disabled!" };
                                return Ok(false, HttpStatusCode.OK, "Access Denied", errors);
                            }
                        }
                        return Error(HttpStatusCode.Forbidden, "Access Denied", errors);
                    }
                }
                else
                {
                    return Error(HttpStatusCode.NotAcceptable, ModelState);
                }

            }
        }
        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("register")]
        public HttpResponseMessage Register(dynamic model)
        {

            ScientificCentreUser user = MapJsonToModelObject<ScientificCentreUser>(model, false);

            try
            {
                MembershipUser newUser = Membership.CreateUser(user.Username, user.Password, user.Email);

                

                if (newUser.UserName != null)
                {
                    //string role = "Author";
                    if (user.ReaderAuthorOther == 1)
                    {//Reader
                        //role = "Reader";
                        Roles.AddUserToRole(newUser.UserName, "Reader");
                    }
                    else
                    {//Author
                        Roles.AddUserToRole(newUser.UserName, "Author");
                    }
                    BaseRepository bs = new BaseRepository();
                    user.UserId = (Guid)newUser.ProviderUserKey;
                    bs.CreateUser(user);

                    string[] messages = new string[] { "Successfully Registered", "You can now login in Scientific Centre" };
                    return Ok(false, HttpStatusCode.OK, "Successfully Registered", messages);
                }
                else
                {
                    string[] errors = new string[] { "Registration Failed", "Please check your inputs or contact Administrator" };
                    return Ok(false, HttpStatusCode.OK, "Registration Failed", errors);
                }
            }
            catch (Exception ex)
            {
                string[] errors = new string[] { "Registration Failed", ex.Message };
                return Ok(false, HttpStatusCode.OK, "Registration Failed", errors);
            }

        }
        public void CreateUserSessions(string userName, HttpRequestMessage request, HttpSessionState session)
        {
            var user = Membership.GetUser(userName);
            MembershipUser ur = user;

            BaseRepository bs = new BaseRepository();

            var guid = new Guid(user.ProviderUserKey.ToString());
            var dbUser = bs.GetUserForId(guid);//   context.BookingAgencyUsers.FirstOrDefault(u => u.UserId == guid);


            string[] roles = Roles.GetRolesForUser(userName);

            session["User"] = dbUser;

            session["UserId"] = dbUser.UserId;


        }
        [System.Web.Http.HttpPost]
        [System.Web.Http.ActionName("logout")]
        public HttpResponseMessage Logout()
        {
            if (ModelState.IsValid)
            {
                FormsAuthentication.SignOut();
                Session.Abandon();

                HttpCookie currentUserCookie = HttpContext.Current.Request.Cookies[FormsAuthentication.FormsCookieName];
                currentUserCookie.Expires = DateTime.Now.AddYears(-1);
                currentUserCookie.Value = "";

                HttpContext.Current.Response.Cookies.Add(currentUserCookie);

                string[] messages = new string[] { "Successfully logged out", "See you soon" };
                return Ok(null, HttpStatusCode.OK, "Success", messages);
            }
            else
            {
                return Error(HttpStatusCode.NotAcceptable, ModelState);
            }
        }
        [System.Web.Http.HttpGet]
        public HttpResponseMessage GetAllAuthors() 
        {
            if (ModelState.IsValid)
            {
                BaseRepository bs = new BaseRepository();
                List<ScientificCentreUser> userList = bs.GetAllAuthors(GetUserId());
                string[] messages = new string[] { "Successfully authors loaded", "Successfully authors loaded and listed" };
                return Ok(userList, HttpStatusCode.OK, "Successfully GetAll", messages);
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
        [HttpGet]
        public HttpResponseMessage GetAllReviewers(dynamic model)
        {

                BaseRepository bs = new BaseRepository();


                int magazineId = bs.GetMagazineIdForWorkerId(GetUserId());

                List<MagazineWorker> reviewers = bs.GetSelectedWorkersForMagazineId(magazineId,"Reviewer");

                string[] messages = new string[] { "Successfully authors loaded", "Successfully authors loaded and listed" };
                return Ok(reviewers, HttpStatusCode.OK, "Successfully GetAll", messages);
  
        }
        public HttpResponseMessage Get(int objId)
        {

            throw new NotImplementedException();

        }

        public HttpResponseMessage GetAll()
        {
            throw new NotImplementedException();
        }

        public HttpResponseMessage Update(dynamic model)
        {
            throw new NotImplementedException();
        }

        public HttpResponseMessage Delete(int objId)
        {
            throw new NotImplementedException();
        }


    }
}