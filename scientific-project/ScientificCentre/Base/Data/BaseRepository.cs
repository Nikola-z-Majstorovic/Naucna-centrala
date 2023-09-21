using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Data;
using ScientificCentreData;

namespace ScientificCentre.Base.Data
{
    public class BaseRepository
    {
        #region Constructors
        public BaseRepository()
        {

        }
        #endregion


        #region Users
        public ProjectAuthor GetAuthorForProjectId(int ProjectId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;

                return context.ProjectAuthors.Include(pa => pa.ScientificProject).Include(pa => pa.ScientificCentreUser).Where(pa => pa.ProjectId == ProjectId && pa.Role == "MainAuthor").FirstOrDefault();
            }
        }

        public ScientificCentreUser GetUserForId(Guid userId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                //return context.ScientificCentreUsers.Include(u => u.UserInventories).Include(u => u.Tasks).Include(u => u.Subscriptions).Include(u => u.WorkerAreas.Select(wa =>wa.ScientificArea)).Include(u => u.Reviews).Include(u => u.ProjectAuthors.Select(pa => pa.ScientificProject)).Where(u => u.UserId == userId).Single();
                return context.ScientificCentreUsers.Include(u => u.UserInventories).Include(u => u.Tasks).Include(u => u.Subscriptions).Include(u => u.ProjectAuthors.Select(pa => pa.ScientificProject).Select(sp => sp.ProjectReviews)).Where(u => u.UserId == userId).FirstOrDefault();
            }
        }


  


        public void CreateUser(ScientificCentreUser user)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.ScientificCentreUsers.Attach(user);

                context.Entry(user).State = EntityState.Added;

                context.SaveChanges();
            }
        }

        public List<ScientificCentreUser> GetAllAuthors(Guid userId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;

                return context.ScientificCentreUsers.SqlQuery("SELECT SCU.* FROM ScientificCentreUsers SCU " +
                    "INNER JOIN aspnet_UsersInRoles AUR ON AUR.UserId = SCU.UserId " +
                    "INNER JOIN aspnet_Roles AR ON AR.RoleId = AUR.RoleId WHERE AR.RoleName = 'Author' AND SCU.UserId !='" + userId + "'").ToList();
            }
        }
        public ScientificCentreUser GetLogedUser(Guid userId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                return context.ScientificCentreUsers.Where(scu => scu.UserId == userId).First();
            }
        }
        public ScientificCentreUser GetSelectedAuthor(int ProjectId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                //   string sqlQuerry = "  select * from ProjectAuthors where ProjectId = '" + ProjectId  + "'";
                //       ProjectAuthor projectAuthor =  context.ProjectAuthors.SqlQuery(sqlQuerry).First();

                ProjectAuthor projectAuthor = context.ProjectAuthors.Where(pa => pa.ProjectId == ProjectId).First();

                string sqlQuerry = "  select * from ScientificCentreUsers where UserId = '" + projectAuthor.AuthorId + "'";

                return context.ScientificCentreUsers.SqlQuery(sqlQuerry).First();
            }
        }


        #endregion


        #region Magazines


        public Magazine GetMagazineForProjectId(int ProjectId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                return context.Magazines.Where(m => m.ScientificProjects.Any(a => a.ProjectId == ProjectId)).FirstOrDefault();
            }
        }
        public Magazine GetMagazineForId(int magazineId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                return context.Magazines.Where(m => m.MagazineId == magazineId).First();
            }
        }

        public int GetMagazineIdForWorkerId(Guid workerId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                return context.MagazineWorkers.Where(mw => mw.ReviewerEditorId == workerId).FirstOrDefault().MagazineId;
            }
        }

        public List<MagazineWorker> GetSelectedWorkersForMagazineId(int magazineId, string roleName)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                return context.MagazineWorkers.Include(mw => mw.ScientificCentreUser).Where(mw => mw.MagazineId == magazineId && mw.Role == roleName).ToList();
            }
        }

        public int CreateProject(ScientificProject scientificProject)
        {
            using (var context = new ScientificCentreEntities())
            {

                context.Configuration.ProxyCreationEnabled = false;

                context.ScientificProjects.Attach(scientificProject);

                context.Entry(scientificProject).State = EntityState.Added;

                context.SaveChanges();

                return scientificProject.ProjectId;

            }
        }
        public void AddAuthorForProject(ProjectAuthor projectAuthor)
        {
            using (var context = new ScientificCentreEntities())
            {

                context.ProjectAuthors.Attach(projectAuthor);

                context.Entry(projectAuthor).State = EntityState.Added;

                context.SaveChanges();
            }
        }

        public Guid GetMagazineMainEditor(int MagazineId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                return context.MagazineWorkers.Where(m => m.MagazineId == MagazineId && m.Role == "MainEditor").SingleOrDefault().ReviewerEditorId;

            }
        }

        public List<Magazine> GetAllMagazines()
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                return context.Magazines.Include(m => m.MagazineWorkers).Include(m => m.UserInventories).Include(m => m.ScientificProjects).ToList();
            }
        }


        public Magazine GetSelectedMagazine(int MagazineId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                return context.Magazines.Include(m => m.ScientificProjects.Select(sp => sp.ScientificArea)).Where(m => m.MagazineId == MagazineId).Single();
            }
        }
        #endregion

        #region Subscription
        public Subscription GetSubscriptionForUserId(Guid UserId, int MagazineId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                return context.Subscriptions.Where(s => s.UserId == UserId && s.SubscriptionMagazineId == MagazineId).FirstOrDefault();
            }
        }

        #endregion

        #region Projects 

        public List<ScientificArea> GetAllAreas()
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;

                return context.ScientificAreas.ToList();
            }
        }
        public ScientificProject GetProject(int ProjectId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;

                return context.ScientificProjects.Include(sp => sp.ScientificArea).Include(sp => sp.Magazines).Where(sp => sp.ProjectId == ProjectId).FirstOrDefault();
            }
        }

        public void UpdateScientificProject(ScientificProject scientificProject)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;
                context.Entry(scientificProject).State = EntityState.Detached;
                context.ScientificProjects.Attach(scientificProject);

                context.Entry(scientificProject).State = EntityState.Modified;

                context.SaveChanges();
            }
        }
        
        public void RemoveProject(int ScientificProjectId)
        {
            using (var context = new ScientificCentreEntities())
            {

                ScientificProject scientificProject = context.ScientificProjects.Where(sp => sp.ProjectId == ScientificProjectId).FirstOrDefault();
                scientificProject.Removed = true;
                context.Configuration.ProxyCreationEnabled = false;
                context.Entry(scientificProject).State = EntityState.Modified;
                context.SaveChanges();
            }
        }
        public void SaveImageForProject(ScientificProject scientificProject)
        {

            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;

                context.ScientificProjects.Attach(scientificProject);

                context.Entry(scientificProject).State = EntityState.Modified;

                context.SaveChanges();
            }
        }
        #endregion



        #region Tasks

        public int CreateTask(Task task)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;

                context.Tasks.Attach(task);

                context.Entry(task).State = EntityState.Added;

                context.SaveChanges();

                return task.TaskId;
            }
        }
        public void AssignEditorForProject(ProjectsEditor pe)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;

                context.ProjectsEditors.Attach(pe);

                context.Entry(pe).State = EntityState.Added;

                context.SaveChanges();

            }
        }

        public void DeleteProjectEditor(Guid UserId, int ProjectId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Database.ExecuteSqlCommand("DELETE FROM ProjectsEditor WHERE ProjectId ='" + ProjectId + "' AND EditorId='" + UserId + "'");
                context.SaveChanges();
            }
        }

        public Guid GetProjectEditorForProjectId(int ProjectId)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;

                return context.ProjectsEditors.Where(pe => pe.ProjectId == ProjectId).FirstOrDefault().EditorId;
            }
        }

        public List<Task> GetTasksForLogedUser(Guid userId)
        {
            using (var context = new ScientificCentreEntities())
            {

                //Those are tasks from authors
                List<Task> authorsExpiredTasks = context.Tasks.Include(t => t.ScientificProject).Include(t => t.ScientificCentreUsers).Where(t => t.ExpirationDate < DateTime.Now && t.Processed != true && t.Expired != true && t.TaskType == 3).ToList();
                foreach (Task tempTask in authorsExpiredTasks)
                {
                    ScientificProject tempProject = tempTask.ScientificProject;

                    ScientificCentreUser tempProjectAuthor = tempTask.ScientificCentreUsers.FirstOrDefault();

                    string messageBody = "<p>Your project " + tempProject.Subject + " has been decliend and marked as removed because You didnt make required changes on time.</p>" +
                    "<br><p>Scientific Centre Team</p>";
                    //Send email to author
                    Controllers.EmailController email = new Controllers.EmailController();
                    email.SendMail(tempProjectAuthor.Email, messageBody, "Removing Your Project - " + tempProject.Subject);

                    //Mark project as removed
                    context.ScientificProjects.SqlQuery("UPDATE ScientificProjects SET Removed = 1 WHERE ProjectId ='" + tempProject.ProjectId + "'");
                }

                //Those are tasks from workers
                List<Task> workersExpiredTasks = context.Tasks.Include(t => t.ScientificProject.ProjectAuthors.Select(pa => pa.ScientificCentreUser)).Include(t => t.ScientificCentreUsers).Where(t => t.ExpirationDate < DateTime.Now && t.Processed != true && t.Expired != true && t.TaskType != 3).ToList();

                int tempProjectId = 0;
                foreach (Task tempTask in workersExpiredTasks)
                {
                    if (tempProjectId != tempTask.ProjectId)
                    {
                        ScientificProject tempProject = tempTask.ScientificProject;

                        ProjectAuthor tempProjectAuthor = tempProject.ProjectAuthors.Where(pa => pa.Role == "Main Author").FirstOrDefault();

                        string messageBody = "<p>Your project " + tempProject.Subject + " has been decliend and marked as removed because one or more workers from magazine couldnt edit/review Your project on time.</p>" +
                            "<br><p>Scientific Centre Team</p>";
                        //Send email to author
                        if (tempProjectAuthor != null)
                        {
                            Controllers.EmailController email = new Controllers.EmailController();
                            email.SendMail(tempProjectAuthor.ScientificCentreUser.Email, messageBody, "Removing Your Project - " + tempProject.Subject);
                        }

                        //Mark project as removed
                        context.ScientificProjects.SqlQuery("UPDATE ScientificProjects SET Removed = 1 WHERE ProjectId ='" + tempProject.ProjectId + "'");

                        tempProjectId = tempTask.ProjectId;
                    }
                }


                //Set expired for every task that expired
                context.Database.ExecuteSqlCommand("UPDATE Tasks SET Expired = 1 WHERE ExpirationDate <= GETDATE() AND Processed != 1 AND Expired != 1");


                //string sqlQuerry = "SELECT T.* FROM TASKS T INNER JOIN dbo.ScientificProjects SP ON SP.ProjectId = T.ProjectId " +
                //    "INNER JOIN dbo.UserTasks UT ON UT.TaskId = T.TaskId INNER JOIN dbo.ScientificCentreUsers SCU ON SCU.UserId = UT.UserId WHERE SCU.UserId='" + userId + "'";
                context.Configuration.ProxyCreationEnabled = false;
                ScientificCentreUser user = context.ScientificCentreUsers.AsNoTracking().Include(u => u.Tasks.Select(sp => sp.ScientificProject).Select(sp => sp.ProjectAuthors.Select(pa => pa.ScientificCentreUser)))
                    .Include(u => u.Tasks.Select(sp => sp.ScientificProject).Select(sp => sp.ProjectReviews.Select(pa => pa.ScientificCentreUser)))
                    .Where(u => u.UserId == userId).FirstOrDefault();

                return user.Tasks.ToList();
                // string sqlQuerry =  "SELECT  T.* FROM dbo.Tasks T INNER JOIN dbo.UserTasks UT ON T.TaskId = UT.TaskId" +
                //" INNER JOIN dbo.ScientificCentreUsers SCU ON SCU.UserId = UT.UserId" +
                //   " INNER JOIN dbo.ScientificProjects SP ON SP.ProjectId = T.ProjectId" +
                // " WHERE UT.UserId = '" + userId +  " '";
                // context.Configuration.ProxyCreationEnabled = false;
                // return context.Tasks.SqlQuery(sqlQuerry).ToList();
            }
            //      context.Configuration.ProxyCreationEnabled = false;
            //     return context.Tasks.Include(t => t.ScientificProject).Include(t=>t.Select(t => t.ScientificCentreUsers.Where(scu => scu.UserId ==userId)).ToList()); //ToList()
            // return context.Tasks.Include(t => t.ScientificProject).Where(t=>t.ScientificCentreUsers.Select(scu => scu.UserId)==userId).ToList();
        }

        public void MarkTaskAsProcesed(int ProjectId, Guid userId, bool outcomeResult)
        {
            using (var context = new ScientificCentreEntities())
            {

                string sqlQuerry = "SELECT T.* FROM Tasks T INNER JOIN UserTasks UT ON UT.TaskId = T.TaskId " +
                                      "WHERE UserId = '" + userId + "' and T.ProjectId =  '" + ProjectId + "' and Processed != 1";
                context.Configuration.ProxyCreationEnabled = false;
                Task task = context.Tasks.SqlQuery(sqlQuerry).FirstOrDefault();

                task.Processed = true;
                task.OutcomeResult = outcomeResult;

                context.Tasks.Attach(task);

                context.Entry(task).State = EntityState.Modified;

                context.SaveChanges();

            }
        }
        public void MarkTaskAsProcesedForTaskId(int TaskId, bool outcomeResult)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;

                Task task = context.Tasks.Where(t => t.TaskId == TaskId).FirstOrDefault();

                task.Processed = true;
                task.OutcomeResult = outcomeResult;

                context.Tasks.Attach(task);

                context.Entry(task).State = EntityState.Modified;

                context.SaveChanges();
            }
        }
        public void CreateReviewForProject(ProjectReview projectReview)
        {
            using (var context = new ScientificCentreEntities())
            {
                context.Configuration.ProxyCreationEnabled = false;

                context.ProjectReviews.Attach(projectReview);

                context.Entry(projectReview).State = EntityState.Added;

                context.SaveChanges();

            }
        }
        public List<Task> CheckHowManyReviewsAreLeft(int ProjectId)
        {
            using (var context = new ScientificCentreEntities())
            {
                string sqlQuerry = "select * from Tasks where Processed = 0 and Expired = 0 and ProjectId= '" + ProjectId + "'";

                return context.Tasks.SqlQuery(sqlQuerry).ToList();
            }
        }
        #endregion
    }


    /*
    public List<BookingAgencyUser> GetAllUsers()
    {
        using (var context = new BookingAgencyEntities())
        {
            context.Configuration.ProxyCreationEnabled = false;

            //Guid adminUserId = new Guid("6C5DA31C-875E-498D-9B4B-51960B3A9EC2");//GetUserId();
            //return context.BookingAgencyUsers.Where(u => u.UserId != adminUserId).ToList();
            return context.BookingAgencyUsers.ToList();
        }
    }

    public List<BookingAgencyUser> GetAllApprovedAccomodationUsers()
    {
        using (var context = new BookingAgencyEntities())
        {
            context.Configuration.ProxyCreationEnabled = false;

            string sql = "SELECT BAU.* FROM BookingAgencyUsers BAU " +
                         "INNER JOIN aspnet_Membership AM ON AM.UserId = BAU.UserId " +
                         "INNER JOIN aspnet_UsersInRoles AUIR ON AUIR.UserId = BAU.UserId " +
                         "INNER JOIN aspnet_Roles AR ON AR.RoleId = AUIR.RoleId " +
                         "WHERE AM.IsApproved = 1 AND AR.RoleName = 'AccomodationUser'";

            return context.BookingAgencyUsers.SqlQuery(sql).ToList<BookingAgencyUser>();
        }
    }

    public Guid GetUserId()
    {
        if (HttpContext.Current.Session["UserId"] != null)
        {
            return new Guid(HttpContext.Current.Session["UserId"].ToString());
        }
        else
        {
            throw new Exception("Error!");
        }
    }

    public void UpdateUser(BookingAgencyUser user)
    {
        using (var context = new BookingAgencyEntities())
        {
            context.Configuration.ProxyCreationEnabled = false;
            var ExistingUser = context.BookingAgencyUsers.Where(u => u.UserId == user.UserId).Single();
            context.Entry(ExistingUser).CurrentValues.SetValues(user);
            context.SaveChanges();
        }
    }

    #endregion
*/


}