using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Reflection;
using System.Web.Http;
using Nest;
using ScientificCentre.Base.Data;
using ScientificCentre.Base.Web.Http;
using ScientificCentreData;

namespace ScientificCentre.Controllers
{
    public class TasksController : BaseApiController,IBaseActions
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
            throw new NotImplementedException();
        }

        public HttpResponseMessage GetAll()
        {
            if (ModelState.IsValid)
            {
                BaseRepository bs = new BaseRepository();
                List<Task> taskList = bs.GetTasksForLogedUser(GetUserId());
                string[] messages = new string[] { "Successfully tasks loaded", "Successfully tasks loaded and listed" };
                return Ok(taskList, HttpStatusCode.OK, "Successfully GetAll", messages);
            }
            else
            {
                return Error(HttpStatusCode.NotAcceptable, ModelState);
            }
        }
        [HttpPut]
        public HttpResponseMessage AssignEditor(dynamic model)
        {
            Task task = MapJsonToModelObject<Task>(model, false);

            if (ModelState.IsValid)
            {

                BaseRepository bs = new BaseRepository();
                //First set task as completed for logged user
                bs.MarkTaskAsProcesedForTaskId(task.TaskId, true);

                //Now select who will be editor for received project                
                Guid loggedUserId = GetUserId();
                List<MagazineWorker> workerList = bs.GetSelectedWorkersForMagazineId(bs.GetMagazineIdForWorkerId(loggedUserId), "Editor");

                ScientificProject project = bs.GetProject(task.ProjectId);

                List<MagazineWorker> filteredByAreaWorkers = new List<MagazineWorker>();

                foreach (MagazineWorker worker in workerList)
                {
                    if(worker.AreaId == project.ScientificAreaId)
                    {
                        filteredByAreaWorkers.Add(worker);
                    }
                }

                Task newTask = new Task
                {
                    ProjectId = project.ProjectId,
                    StartedOnDate = DateTime.Now,
                    ExpirationDate = DateTime.Now.AddDays(5),
                    TaskType = 1,
                    TaskDescription = "Assing Reviewers"
                };
                newTask.ProjectId = project.ProjectId;


                ProjectsEditor pe = new ProjectsEditor();

                if (filteredByAreaWorkers.Count > 0)
                {//we have some Editor workers with selected project Scientific Area

                    //first try to delete temp Main Editor as Editor from Project, if this is the case
                    bs.DeleteProjectEditor(GetUserId(), project.ProjectId);

                    Random r = new Random();
                    int rInt = r.Next(0, filteredByAreaWorkers.Count);

                    MagazineWorker editor = filteredByAreaWorkers[rInt];

                    //Create task for Editor, Assign Reviewers

                    ScientificCentreUser u = editor.ScientificCentreUser; //bs.GetUserForId(editor.ReviewerEditorId);
                    newTask.ScientificCentreUsers.Add(u);

                    bs.CreateTask(newTask);

                    //Add this editor to ProjectsEditor table
                    pe.ProjectId = newTask.ProjectId;
                    pe.ScientificCentreUser = u;
                    pe.EditorId = u.UserId;

                    bs.AssignEditorForProject(pe);

                }
                else
                {
                    //Create task for Main Editor because we dont have Editor workers, Assign Reviewers
                    ScientificCentreUser u = bs.GetLogedUser(loggedUserId);
                    newTask.ScientificCentreUsers.Add(u);

                    bs.CreateTask(newTask);

                    //Add this editor to ProjectsEditor table
                    
                    pe.ProjectId = newTask.ProjectId;
                    pe.ScientificCentreUser = u;
                    pe.EditorId = u.UserId;

                    bs.AssignEditorForProject(pe);

                }

                string[] messages = new string[] { "Task Successfully Completed", "Task Successfully Completed" };
                return Ok(true, HttpStatusCode.OK, "Successfully Update", messages);
            }
            else
            {
                return Error(HttpStatusCode.NotAcceptable, ModelState);
            }
        }
        [HttpPut]
        public HttpResponseMessage CompleteWithoutReviewers(dynamic model)
        {
            Task task = MapJsonToModelObject<Task>(model, false);

            if (ModelState.IsValid)
            {
                BaseRepository bs = new BaseRepository();
                bs.MarkTaskAsProcesedForTaskId(task.TaskId, false);

                
                //Guid mainEditorId = bs.GetMagazineMainEditor(bs.GetMagazineIdForWorkerId(GetUserId()));
                Guid editorId = bs.GetProjectEditorForProjectId(task.ProjectId);

                ScientificCentreUser editor = bs.GetUserForId(editorId);

                Task newTask = new Task
                {
                    ProjectId = task.ProjectId,
                    StartedOnDate = DateTime.Now,
                    ExpirationDate = DateTime.Now.AddDays(5),
                    TaskType = 1,
                    TaskDescription = "Assing Reviewers"
                };

                //Assign task to main editor
                newTask.ScientificCentreUsers.Add(editor);

                bs.CreateTask(newTask);


                string[] messages = new string[] { "Task Successfully Completed", "Task Successfully Completed" };
                return Ok(true, HttpStatusCode.OK, "Successfully Update", messages);
            }
            else
            {
                return Error(HttpStatusCode.NotAcceptable, ModelState);
            }
        }
        [HttpPut]

        public HttpResponseMessage AssignReviewers(dynamic model)
        {
            List<Task> selectedReviewersTasks = MapJsonToModelArrayObject<List<Task>>(model, false);

            if (ModelState.IsValid)
            {
                BaseRepository bs = new BaseRepository();
                //First complete task for Editor or Main Editor
                bs.MarkTaskAsProcesedForTaskId(selectedReviewersTasks[0].TaskId, true);

                foreach (Task task in selectedReviewersTasks)
                {
                    task.TaskId = 0;
                    task.ExpirationDate = task.StartedOnDate.AddDays(5);
                    task.TaskDescription = "Write review for project";
                    task.TaskType = 2;

                    bs.CreateTask(task);
                }


                string[] messages = new string[] { "Task Successfully Completed", "Reviewers Successfuly Assigned" };
                return Ok(true, HttpStatusCode.OK, "Successfully Update", messages);
            }
            else
            {
                return Error(HttpStatusCode.NotAcceptable, ModelState);
            }
        }
        [HttpPut]
        public HttpResponseMessage SendReviewToEditor(dynamic model)
        {
            ProjectReview projectReview = MapJsonToModelObject<ProjectReview>(model, false);
            projectReview.ReviewedOnDate = DateTime.Now;//setting review date
            if (ModelState.IsValid)
            {
                BaseRepository bs = new BaseRepository();

                Guid loggedUserId = GetUserId();

                bs.MarkTaskAsProcesedForTaskId(projectReview.TaskId, true);

                bs.CreateReviewForProject(projectReview);

                List<Task> leftTasks = bs.CheckHowManyReviewsAreLeft(projectReview.ProjectId);

                if(leftTasks.Count == 0)
                {//Because there are no reviewers left to write review, we create new task for Editor 
                 //so he can examin project onece more with reviews and comments and marks from reviewrs
                 //Guid mainEditorId = bs.GetMagazineMainEditor(bs.GetMagazineIdForWorkerId(GetUserId()));

                    Guid editorId = bs.GetProjectEditorForProjectId(projectReview.ProjectId);
                    
                    ScientificCentreUser editor = bs.GetUserForId(editorId);

                    Task newTask = new Task
                    {
                        ProjectId = projectReview.ProjectId,
                        StartedOnDate = DateTime.Now,
                        ExpirationDate = DateTime.Now.AddDays(5),
                        TaskType = 0,
                        TaskDescription = "Examine Project With Reviews and Comments"
                    };

                    //Assign task to main editor
                    newTask.ScientificCentreUsers.Add(editor);

                    bs.CreateTask(newTask);

                }

                string[] messages = new string[] { "Task Successfully Completed", "Review Successfuly Created" };

                return Ok(true, HttpStatusCode.OK, "Successfully Update", messages);
            }
            else
            {
                return Error(HttpStatusCode.NotAcceptable, ModelState);
            }
        }
        [HttpPut]
        public HttpResponseMessage SendProjectToPreviousReviewers(dynamic model)
        {
            Task task = MapJsonToModelObject<Task>(model, false);

            if (ModelState.IsValid)
            {
                BaseRepository bs = new BaseRepository();
                //First complete task for Editor or Main Editor
                bs.MarkTaskAsProcesedForTaskId(task.TaskId, true);

                ICollection<ProjectReview> projectReviews = task.ScientificProject.ProjectReviews;

                Guid tempUserId = new Guid();
                //go over each reviewer using ProjectReviews 
                foreach (ProjectReview pr in projectReviews)
                {
                    if(tempUserId != pr.ReviewerId) {
                        //Creating review task for each previous reviewer
                        Task newTask = new Task();
                        newTask.StartedOnDate = DateTime.Now;
                        newTask.ExpirationDate = task.StartedOnDate.AddDays(5);
                        newTask.TaskDescription = "Write review for project";
                        newTask.TaskType = 2;
                        newTask.ProjectId = task.ProjectId;

                        ScientificCentreUser reviewer = bs.GetUserForId(pr.ScientificCentreUser.UserId);

                        newTask.ScientificCentreUsers.Add(reviewer);

                        bs.CreateTask(newTask);

                        tempUserId = pr.ReviewerId;
                    }
                   
                }

                string[] messages = new string[] { "Task Successfully Completed", "Reviewers Successfuly Assigned" };
                return Ok(true, HttpStatusCode.OK, "Successfully Update", messages);
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