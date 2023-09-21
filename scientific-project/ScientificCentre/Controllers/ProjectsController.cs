using Newtonsoft.Json;
using ScientificCentre.Base.Data;
using ScientificCentre.Base.Web.Http;
using ScientificCentreData;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Mail;
using System.Web;
using System.Web.Http;
using iTextSharp.text.pdf;
using System.Text.RegularExpressions;
using System.Text;
using System.Reflection;
using ScientificCentreData.Extensions;

namespace ScientificCentre.Controllers
{
    public class ProjectsController : BaseApiController, IBaseActions
    {
        public HttpResponseMessage Create(dynamic model)
        {
            
            throw new NotImplementedException();

        }
        [HttpPut]
        public HttpResponseMessage ApproveProject(dynamic model)
        {
            Task task = MapJsonToModelObject<Task>(model, false);

            BaseRepository bs = new BaseRepository();

            bs.MarkTaskAsProcesedForTaskId(task.TaskId, true);

            ScientificProject scientificProject = bs.GetProject(task.ProjectId);

            int magazineId = bs.GetMagazineIdForWorkerId(GetUserId());

            Guid randomGuid = Guid.NewGuid();

            string firstPart = magazineId.ToString() + "/" + scientificProject.Subject + "/";
            string secondPart = randomGuid.ToString().Substring(0, 19 - firstPart.Length);

            scientificProject.Accepted = true;
            scientificProject.DOI = firstPart + secondPart;
            bs.UpdateScientificProject(scientificProject);

            SampleData sd = new SampleData();
            sd.AreaName = scientificProject.ScientificArea.Name;
            sd.Id = scientificProject.ProjectId;
            sd.ProjectContent = scientificProject.ProjectContent;
            sd.MagazineName = scientificProject.Magazines.First().Name;
            sd.ProjectKeywords = scientificProject.KeyWords;
            sd.ProjectName = scientificProject.Subject;

            string allAuthorsMerged = "";
            foreach(ProjectAuthor pa in scientificProject.ProjectAuthors)
            {
                allAuthorsMerged = allAuthorsMerged + pa.ScientificCentreUser.Name + " " + pa.ScientificCentreUser.Surname + "-";
            }

            allAuthorsMerged = allAuthorsMerged + "-" + scientificProject.NotRegisteredAutorsInfo;

            sd.ProjectAuthors = allAuthorsMerged;
            sd.DOI = scientificProject.DOI;
            //sd.ProjectContent = scientificProject.ProjectContent;
            sd.ProjectContent = System.IO.File.ReadAllText(HttpContext.Current.Server.MapPath(@"~\app\resources\css\Projects\107512-rf.pdf.txt"));// + scientificProject.PdfHtmlFileName.Replace(".html", ".txt")));

            //UsersController.Elasticsearch es = new UsersController.Elasticsearch();

            

            //es.AddNewIndexTest(sd);


            //Send email to author of approved project
            ScientificCentreUser author = bs.GetSelectedAuthor(scientificProject.ProjectId);
            ScientificCentreUser magazineEditor = bs.GetLogedUser(GetUserId());

            EmailController email = new EmailController();

            string emailMessage = "<p>Dear " + author.Name + " " + author.Surname +
                " Your project " + scientificProject.Subject + " has been accepted.</p><br>" +
                "<p>Project is examined by " + magazineEditor.Name + " " + magazineEditor.Surname + "</p><br>" +
                "<p>Kind regards,<br>Scientific Center Team</p>";

            email.SendMail(author.Email, emailMessage, "Your project " + scientificProject.Subject + " has been declined");

            ////////////////////////////////////////
          


            string[] messages = new string[] { "Project Successfully Approved", "Project Successfully Approved" };
            return Ok(true, HttpStatusCode.OK, "Success", messages);
        }
        [HttpPut]
        public HttpResponseMessage CommentReturnProject(dynamic model)
        {
            ScientificProject project = MapJsonToModelObject<ScientificProject>(model, false);
            BaseRepository bs = new BaseRepository();
            ProjectsEditor pe = new ProjectsEditor();

            if (project.ProjectsEditors.Count == 0)
            {//temp assign of MainEditor as project editor in order to avoid error when author is returning 
             //project to Main Editor even before Weak Editor Was asigned for this project examing
                
                pe.EditorId = GetUserId();
                pe.ProjectId = project.ProjectId;

                bs.AssignEditorForProject(pe);
            }
          
            project.Tasks = null;
            project.ProjectReviews = null;
            //project.ProjectAuthors = null;

            //update project with new comment
            bs.UpdateScientificProject(project);

           

            Magazine mz = bs.GetMagazineForProjectId(project.ProjectId);

            Guid mainEditorId = bs.GetMagazineMainEditor(mz.MagazineId);

            Guid loggedUserId = GetUserId();


            //Mark main editor task as done
            bs.MarkTaskAsProcesed(project.ProjectId, GetUserId(), true);

            ScientificCentreUser author = bs.GetSelectedAuthor(project.ProjectId);
            //create task for author so he can edit his project
            Task authorTask = new Task();
            authorTask.ScientificCentreUsers.Add(author);
            authorTask.ProjectId = project.ProjectId;
            authorTask.StartedOnDate = DateTime.Now;
            authorTask.ExpirationDate = DateTime.Now.AddDays(5);
            authorTask.TaskDescription = "Make required changes in project";
            authorTask.TaskType = 3;
            bs.CreateTask(authorTask);


            //send email to author
            EmailController email = new EmailController();

            ScientificCentreUser magazineEditor = bs.GetLogedUser(GetUserId());

            string emailMessage = "<p>Dear " + author.Name + " " + author.Surname +
                " Your project " + project.Subject + " has been return to you for additional modification.</p><br>" +
                "<p>Project is examined by " + magazineEditor.Name + " " + magazineEditor.Surname + "</p><br>" +
                "<p>Editor comment: " + project.Comment + "</p><br>" +
                "<p>If You have questions about details on comment for Your project, contact editor via his email addres: " + magazineEditor.Email + "</p><br>" +
                "<p>Kind regards,<br>Scientific Center Team</p>";

            email.SendMail(author.Email, emailMessage, "Your project " + project.Subject + " has been declined");

            string[] messages = new string[] { "Project Updated", "Successfully Added Comment" };
            return Ok(true, HttpStatusCode.OK, "Success", messages);
        }

        public HttpResponseMessage Get(int objId)
        {
            throw new NotImplementedException();
        }

        public HttpResponseMessage GetAll()
        {
            throw new NotImplementedException();
        }
        public HttpResponseMessage GetAllScientificAreas(dynamic model)
        {
            if (ModelState.IsValid)
            {
                BaseRepository bs = new BaseRepository();
                List<ScientificArea> scientificAreas = bs.GetAllAreas();
                string[] messages = new string[] { "Successfully locations loaded", "Successfully locations loaded and listed" };
                return Ok(scientificAreas, HttpStatusCode.OK, "Successfully GetAll", messages);
            }
            else
            {
                return Error(HttpStatusCode.NotAcceptable, ModelState);
            }
        }
        [HttpPut]
        public HttpResponseMessage Update(dynamic model)
        {
            ScientificProject project = MapJsonToModelObject<ScientificProject>(model, false);

            BaseRepository bs = new BaseRepository();

            //update project
            bs.UpdateScientificProject(project);


            ScientificCentreUser authorUser = bs.GetUserForId(GetUserId());
            //mark task as completed for author because he finished editing his project
            bs.MarkTaskAsProcesed(project.ProjectId, authorUser.UserId, true);

            //return magazine 1st because we need magazine id to return magazine main editor
            // Magazine magazine = bs.GetMagazineForProjectId(project.ProjectId);

            //Guid editorId = bs.GetMagazineMainEditor(magazine.MagazineId);
            Guid editorId = bs.GetProjectEditorForProjectId(project.ProjectId);

            ScientificCentreUser editorUser = bs.GetUserForId(editorId);

            //Create new task for magazine editor so he can examine this project once again
            Task editorTask = new Task
            {
                ProjectId = project.ProjectId,
                StartedOnDate = DateTime.Now,
                ExpirationDate = DateTime.Now.AddDays(5),
                // TaskType = 1,
                TaskDescription = "Examine edited project"
            };

            if (project.ProjectReviews.Count > 0)
            {//Examine updated project with reviews
                editorTask.TaskType = 4;
            }
            else
            {//Just examine updated project
                editorTask.TaskType = 0;
            }

            editorTask.ScientificCentreUsers.Add(editorUser);

            bs.CreateTask(editorTask);

            //send email to main editor
            EmailController email = new EmailController();

            string emailMessage = "<p><p>Project " + project.Subject + " is edited by project Author: " + authorUser.Name +
                " " + authorUser.Surname + " - " + authorUser.Email +
                "<p>Please examine the project.</p>" +
                "<p>Kind regards, <br>Scientific Center Team</p>";

            email.SendMail(editorUser.Email, emailMessage, "Examine edited project " + project.Subject);
            //return fresh user with refreshed tasks and projects

           

          

            string[] messages = new string[] { "Project Updated", "Successfully Edited Project" };
            return Ok(bs.GetUserForId(authorUser.UserId), HttpStatusCode.OK, "Success", messages);
        }

        public HttpResponseMessage Delete(int objId)
        {
            // BaseRepository bs = new BaseRepository();
            //bs.RemoveProject(objId);
            //bs.MarkTaskAsProcesed();

            string[] messages = new string[] { "Project Updated", "Successfully Added Comment" };
            return Ok(true, HttpStatusCode.OK, "Success", messages);
        }

        [HttpPut]
        public HttpResponseMessage DeleteProject(dynamic model)
        {
            Task task = MapJsonToModelObject<Task>(model, false);

            BaseRepository bs = new BaseRepository();

            bs.RemoveProject(task.ProjectId);

            bs.MarkTaskAsProcesed(task.ProjectId, GetUserId(), true);

            EmailController email = new EmailController();


            ProjectAuthor author = bs.GetAuthorForProjectId(task.ProjectId);

            ScientificCentreUser magazineEditor = bs.GetLogedUser(GetUserId());

            string emailMessage = "<p>Dear " + author.ScientificCentreUser.Name + " " + author.ScientificCentreUser.Surname +
                " Your project " + author.ScientificProject.Subject + " has been declined, becase it doesnt meet specific requirements.</p><br>" +
                "<p>Project is declined by magazine editor " + magazineEditor.Name + " " + magazineEditor.Surname + "</p><br>" +
                "<p>If You have questions about details on declining Your project, contact editor via his email addres: " + magazineEditor.Email + "</p><br>" +
                "<p>Kind regards,<br>Scientific Center Team</p>";

            email.SendMail(author.ScientificCentreUser.Email, emailMessage, "Your project " + author.ScientificProject.Subject + " has been declined");



            Magazine mz = bs.GetMagazineForProjectId(author.ScientificProject.ProjectId);

            Guid mainEditorId = bs.GetMagazineMainEditor(mz.MagazineId);

            Guid loggedUserId = GetUserId();

            
           

            string[] messages = new string[] { "Project Deleted", "Successfully Deleted Project" };
            return Ok(true, HttpStatusCode.OK, "Success", messages);
        }
        [System.Web.Http.HttpPost]
        public HttpResponseMessage UploadFiles()
        {

            ScientificProject project = JsonConvert.DeserializeObject<ScientificProject>(Session["Project"].ToString());
            BaseRepository bs = new BaseRepository();
            if (project.ProjectId != 0)
            {
              
                bs.UpdateScientificProject(project);

                ScientificCentreUser authorUser = bs.GetUserForId(GetUserId());
                //mark task as completed for author because he finished editing his project
                bs.MarkTaskAsProcesed(project.ProjectId, authorUser.UserId, true);

                //return magazine 1st because we need magazine id to return magazine main editor
                // Magazine magazine = bs.GetMagazineForProjectId(project.ProjectId);

                //Guid editorId = bs.GetMagazineMainEditor(magazine.MagazineId);
                Guid editorId = bs.GetProjectEditorForProjectId(project.ProjectId);

                ScientificCentreUser editorUser = bs.GetUserForId(editorId);

                //Create new task for magazine editor so he can examine this project once again
                Task editorTask = new Task
                {
                    ProjectId = project.ProjectId,
                    StartedOnDate = DateTime.Now,
                    ExpirationDate = DateTime.Now.AddDays(5),
                    // TaskType = 1,
                    TaskDescription = "Examine edited project"
                };

                if (project.ProjectReviews.Count > 0)
                {//Examine updated project with reviews
                    editorTask.TaskType = 4;
                }
                else
                {//Just examine updated project
                    editorTask.TaskType = 0;
                }

                editorTask.ScientificCentreUsers.Add(editorUser);

                bs.CreateTask(editorTask);

                var file = HttpContext.Current.Request.Files.Count > 0 ?
               HttpContext.Current.Request.Files[0] : null;

                //Check if author uploaded project if yes, then delete his previous pdf file and create new
                if (file != null && file.ContentLength > 0)
                {
                    System.IO.DirectoryInfo di = new DirectoryInfo(HttpContext.Current.Server.MapPath("~/app/resources/css/Projects"));

                    foreach (FileInfo fileInFiles in di.GetFiles())
                    {

                        if (fileInFiles.Name == project.FileName || fileInFiles.Name == project.PdfHtmlFileName)
                        {
                            fileInFiles.Delete();
                        }
                    }

                    var fileName = Path.GetFileName(file.FileName);

                    var pathToProject = Path.Combine("C:\\Users\\majst\\source\\repos\\ScientificCentre\\ScientificCentre\\app\\resources\\css\\Projects",
                        project.ProjectId.ToString() + fileName
                    );

                    file.SaveAs(pathToProject);

                    //Create pdf file
                    SautinSoft.PdfFocus pdffocus = new SautinSoft.PdfFocus();

                    pdffocus.OpenPdf(pathToProject);
               
                    string htmlFileLocation = "C:\\Users\\majst\\source\\repos\\ScientificCentre\\ScientificCentre\\app\\resources\\css\\Projects\\" + project.ProjectId.ToString() + fileName + ".html";
                    string txtFileLocation = "C:\\Users\\majst\\source\\repos\\ScientificCentre\\ScientificCentre\\app\\resources\\css\\Projects\\" + project.ProjectId.ToString() + fileName + ".txt";
                    if (pdffocus.PageCount > 0)
                    {

                        int result = pdffocus.ToHtml(htmlFileLocation);
                        result = pdffocus.ToText(txtFileLocation);
                        project.ProjectContent = pdffocus.ToText();
                    }


                    project.PdfHtmlFileName = project.ProjectId.ToString() + fileName.Replace(".pdf", ".html");
                    project.FileName = project.ProjectId.ToString() + fileName;
                    bs.SaveImageForProject(project);
                }


                //send email to main editor
                EmailController email = new EmailController();

                string emailMessage = "<p><p>Project " + project.Subject + " is edited by project Author: " + authorUser.Name +
                    " " + authorUser.Surname + " - " + authorUser.Email +
                    "<p>Please examine the project.</p>" +
                    "<p>Kind regards, <br>Scientific Center Team</p>";

                email.SendMail(editorUser.Email, emailMessage, "Examine edited project " + project.Subject);


                string[] messages = new string[] { "Project Updated", "Successfully Updated Project" };
                return Ok(bs.GetUserForId(GetUserId()), HttpStatusCode.OK, "Success", messages);
            }
            else
            {//this is new project


                project.SentOnDate = DateTime.Now;
                project.FileName = "Test";

                List<ProjectAuthor> tempProjAuthorList = new List<ProjectAuthor>();
                tempProjAuthorList = project.ProjectAuthors.ToList();

                project.ProjectAuthors = new List<ProjectAuthor>();
                project.ProjectId = bs.CreateProject(project);

                //inserting loged user as main author
                tempProjAuthorList.Add(new ProjectAuthor { ProjectId = project.ProjectId, AuthorId = GetUserId(), Role = "MainAuthor" });

                foreach (ProjectAuthor projAuthor in tempProjAuthorList)
                {
                    ProjectAuthor tempProjAuthor = projAuthor;
                    tempProjAuthor.ProjectId = project.ProjectId;
                    bs.AddAuthorForProject(projAuthor);
                }


                Guid magazineMainEditorId = bs.GetMagazineMainEditor(project.Magazines.First().MagazineId);

                Task task = new Task
                {
                    ProjectId = project.ProjectId,
                    StartedOnDate = DateTime.Now,
                    TaskDescription = "Review new project",
                    TaskType = 0,
                    ExpirationDate = DateTime.Now.AddDays(2)
                };

                ScientificCentreUser u = bs.GetLogedUser(magazineMainEditorId);
                task.ScientificCentreUsers.Add(u);

                task.TaskId = bs.CreateTask(task);


                var file = HttpContext.Current.Request.Files.Count > 0 ?
                HttpContext.Current.Request.Files[0] : null;


                if (file != null && file.ContentLength > 0)
                {
                    var fileName = Path.GetFileName(file.FileName);

                    var pathToProject = Path.Combine("C:\\Users\\majst\\source\\repos\\ScientificCentre\\ScientificCentre\\app\\resources\\css\\Projects",
                        project.ProjectId.ToString() + fileName
                    );

                    file.SaveAs(pathToProject);

                    //ExtractText(pathToProject, "outfile");

                    //Create pdf file
                    SautinSoft.PdfFocus pdffocus = new SautinSoft.PdfFocus();

                    pdffocus.OpenPdf(pathToProject);

                    //  string path = Path.Combine(Environment.CurrentDirectory, @"app\resources\css\Projects\");
                    string htmlFileLocation = "C:\\Users\\majst\\source\\repos\\ScientificCentre\\ScientificCentre\\app\\resources\\css\\Projects\\" + project.ProjectId.ToString() + fileName + ".html";                 
                    string txtFileLocation = "C:\\Users\\majst\\source\\repos\\ScientificCentre\\ScientificCentre\\app\\resources\\css\\Projects\\" + project.ProjectId.ToString() + fileName + ".txt";
                    if (pdffocus.PageCount > 0)
                    {
                        
                        int result = pdffocus.ToHtml(htmlFileLocation);
                        result = pdffocus.ToText(txtFileLocation);
                        project.ProjectContent = pdffocus.ToText();
                    }

                    project.PdfHtmlFileName = project.ProjectId.ToString() + fileName.Replace(".pdf", ".html");
                    project.FileName = project.ProjectId.ToString() + fileName;
                    bs.SaveImageForProject(project);

                }
                EmailController email = new EmailController();

                ScientificCentreUser authorUser = bs.GetUserForId(GetUserId());

                string emailMessage = "<p><p>Project " + project.Subject + " is edited by project Author: " + authorUser.Name +
                    " " + authorUser.Surname + " - " + authorUser.Email +
                    "<p>Please examine the project.</p>" +
                    "<p>Kind regards, <br>Scientific Center Team</p>";

                email.SendMail(u.Email, emailMessage, "Examine edited project " + project.Subject);

                string[] messages = new string[] { "Project Created", "Successfully Created Project" };
                return Ok(bs.GetUserForId(GetUserId()), HttpStatusCode.OK, "Success", messages);
            }
               
           
        }

        [HttpPut]
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

            //UsersController.Elasticsearch es = new UsersController.Elasticsearch();


            string lastChar = searchTerm.Substring(searchTerm.Length - 1);

            if(lastChar == ",")
            {
                searchTerm = searchTerm.Remove(searchTerm.Length - 1);
            }

            //List<SampleData> returnedResults =  es.GetResult(searchTerm, searchField, filter.FillQuery, filter.FillAnalyzer);










            //Area Id
            //if (filter.PhrazeQuery == true)
            //{
            //    IEnumerable<SampleData> result = GoLucene.SearchDefault(searchField, searchTerm);
            //}
            //else
            //{
            //    IEnumerable<SampleData> result = GoLucene.Search(searchField, searchTerm);
            //}


            string[] messages = new string[] { "Project Search Approved", "Project Successfully Searched" };
            return Ok(null, HttpStatusCode.OK, "Success", messages);
        }




        /// BT = Beginning of a text object operator 
        /// ET = End of a text object operator
        /// Td move to the start of next line
        ///  5 Ts = superscript
        /// -5 Ts = subscript

        #region Fields

        #region _numberOfCharsToKeep
        /// <summary>
        /// The number of characters to keep, when extracting text.
        /// </summary>
        private static int _numberOfCharsToKeep = 15;
    
        #endregion

        #endregion

        #region ExtractText
        /// <summary>
        /// Extracts a text from a PDF file.
        /// </summary>
        /// <param name="inFileName">the full path to the pdf file.</param>
        /// <param name="outFileName">the output file name.</param>
        /// <returns>the extracted text</returns>
        public bool ExtractText(string inFileName, string outFileName)
        {
            StreamWriter outFile = null;
            try
            {
                // Create a reader for the given PDF file
                PdfReader reader = new PdfReader(inFileName);
                //outFile = File.CreateText(outFileName);
                outFile = new StreamWriter(outFileName, false, System.Text.Encoding.UTF8);

                Console.Write("Processing: ");

                int totalLen = 68;
                float charUnit = ((float)totalLen) / (float)reader.NumberOfPages;
                int totalWritten = 0;
                float curUnit = 0;

                for (int page = 1; page <= reader.NumberOfPages; page++)
                {
                    outFile.Write(ExtractTextFromPDFBytes(reader.GetPageContent(page)) + " ");

                    string testing = ExtractTextFromPDFBytes(reader.GetPageContent(page)) + " ";

 
           
                        var sb = new StringBuilder();
                        foreach (string s in testing.Split('\n'))
                        {
                            sb.AppendFormat("<p>{0}</p>", s);
                        }
                    testing = sb.ToString();
                    


                    // Write the progress.
                    if (charUnit >= 1.0f)
                    {
                        for (int i = 0; i < (int)charUnit; i++)
                        {
                            Console.Write("#");
                            totalWritten++;
                        }
                    }
                    else
                    {
                        curUnit += charUnit;
                        if (curUnit >= 1.0f)
                        {
                            for (int i = 0; i < (int)curUnit; i++)
                            {
                                Console.Write("#");
                                totalWritten++;
                            }
                            curUnit = 0;
                        }

                    }
                }

                if (totalWritten < totalLen)
                {
                    for (int i = 0; i < (totalLen - totalWritten); i++)
                    {
                        Console.Write("#");
                    }
                }
                return true;
            }
            catch
            {
                return false;
            }
            finally
            {
                if (outFile != null) outFile.Close();
            }
        }
        #endregion

        #region ExtractTextFromPDFBytes
        /// <summary>
        /// This method processes an uncompressed Adobe (text) object 
        /// and extracts text.
        /// </summary>
        /// <param name="input">uncompressed</param>
        /// <returns></returns>
        public string ExtractTextFromPDFBytes(byte[] input)
        {
            if (input == null || input.Length == 0) return "";

            try
            {
                string resultString = "";

                // Flag showing if we are we currently inside a text object
                bool inTextObject = false;

                // Flag showing if the next character is literal 
                // e.g. '\\' to get a '\' character or '\(' to get '('
                bool nextLiteral = false;

                // () Bracket nesting level. Text appears inside ()
                int bracketDepth = 0;

                // Keep previous chars to get extract numbers etc.:
                char[] previousCharacters = new char[_numberOfCharsToKeep];
                for (int j = 0; j < _numberOfCharsToKeep; j++) previousCharacters[j] = ' ';


                for (int i = 0; i < input.Length; i++)
                {
                    char c = (char)input[i];
                    if (input[i] == 213)
                        c = "'".ToCharArray()[0];

                    if (inTextObject)
                    {
                        // Position the text
                        if (bracketDepth == 0)
                        {
                            if (CheckToken(new string[] { "TD", "Td" }, previousCharacters))
                            {
                                resultString += "\n\r";
                            }
                            else
                            {
                                if (CheckToken(new string[] { "'", "T*", "\"" }, previousCharacters))
                                {
                                    resultString += "\n";
                                }
                                else
                                {
                                    if (CheckToken(new string[] { "Tj" }, previousCharacters))
                                    {
                                        resultString += " ";
                                    }
                                }
                            }
                        }

                        // End of a text object, also go to a new line.
                        if (bracketDepth == 0 &&
                            CheckToken(new string[] { "ET" }, previousCharacters))
                        {

                            inTextObject = false;
                            resultString += " ";
                        }
                        else
                        {
                            // Start outputting text
                            if ((c == '(') && (bracketDepth == 0) && (!nextLiteral))
                            {
                                bracketDepth = 1;
                            }
                            else
                            {
                                // Stop outputting text
                                if ((c == ')') && (bracketDepth == 1) && (!nextLiteral))
                                {
                                    bracketDepth = 0;
                                }
                                else
                                {
                                    // Just a normal text character:
                                    if (bracketDepth == 1)
                                    {
                                        // Only print out next character no matter what. 
                                        // Do not interpret.
                                        if (c == '\\' && !nextLiteral)
                                        {
                                            resultString += c.ToString();
                                            nextLiteral = true;
                                        }
                                        else
                                        {
                                            if (((c >= ' ') && (c <= '~')) ||
                                                ((c >= 128) && (c < 255)))
                                            {
                                                resultString += c.ToString();
                                            }

                                            nextLiteral = false;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Store the recent characters for 
                    // when we have to go back for a checking
                    for (int j = 0; j < _numberOfCharsToKeep - 1; j++)
                    {
                        previousCharacters[j] = previousCharacters[j + 1];
                    }
                    previousCharacters[_numberOfCharsToKeep - 1] = c;

                    // Start of a text object
                    if (!inTextObject && CheckToken(new string[] { "BT" }, previousCharacters))
                    {
                        inTextObject = true;
                    }
                }

                return CleanupContent(resultString);
            }
            catch
            {
                return "";
            }
        }

        private string CleanupContent(string text)
        {
            string[] patterns = { @"\\\(", @"\\\)", @"\\226", @"\\222", @"\\223", @"\\224", @"\\340", @"\\342", @"\\344", @"\\300", @"\\302", @"\\304", @"\\351", @"\\350", @"\\352", @"\\353", @"\\311", @"\\310", @"\\312", @"\\313", @"\\362", @"\\364", @"\\366", @"\\322", @"\\324", @"\\326", @"\\354", @"\\356", @"\\357", @"\\314", @"\\316", @"\\317", @"\\347", @"\\307", @"\\371", @"\\373", @"\\374", @"\\331", @"\\333", @"\\334", @"\\256", @"\\231", @"\\253", @"\\273", @"\\251", @"\\221" };
            string[] replace = { "(", ")", "-", "'", "\"", "\"", "à", "â", "ä", "À", "Â", "Ä", "é", "è", "ê", "ë", "É", "È", "Ê", "Ë", "ò", "ô", "ö", "Ò", "Ô", "Ö", "ì", "î", "ï", "Ì", "Î", "Ï", "ç", "Ç", "ù", "û", "ü", "Ù", "Û", "Ü", "®", "™", "«", "»", "©", "'" };

            for (int i = 0; i < patterns.Length; i++)
            {
                string regExPattern = patterns[i];
                Regex regex = new Regex(regExPattern, RegexOptions.IgnoreCase);
                text = regex.Replace(text, replace[i]);
            }

            return text;
        }

        #endregion

        #region CheckToken
        /// <summary>
        /// Check if a certain 2 character token just came along (e.g. BT)
        /// </summary>
        /// <param name="tokens">the searched token</param>
        /// <param name="recent">the recent character array</param>
        /// <returns></returns>
        private bool CheckToken(string[] tokens, char[] recent)
        {
            foreach (string token in tokens)
            {
                if ((recent[_numberOfCharsToKeep - 3] == token[0]) &&
                    (recent[_numberOfCharsToKeep - 2] == token[1]) &&
                    ((recent[_numberOfCharsToKeep - 1] == ' ') ||
                    (recent[_numberOfCharsToKeep - 1] == 0x0d) ||
                    (recent[_numberOfCharsToKeep - 1] == 0x0a)) &&
                    ((recent[_numberOfCharsToKeep - 4] == ' ') ||
                    (recent[_numberOfCharsToKeep - 4] == 0x0d) ||
                    (recent[_numberOfCharsToKeep - 4] == 0x0a))
                    )
                {
                    return true;
                }
            }
            return false;
        }
        #endregion




























    }
}