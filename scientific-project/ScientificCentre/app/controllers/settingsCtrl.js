scifiApp.controller('settingsCtrl', ['$rootScope', '$scope', 'dataService', 'appService', function ($rootScope, $scope, dataService, appService) {
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.icon = 'fa-home';
    $rootScope.title = 'Settings';
    $rootScope.mode = 'view';
    //-----------------------------------------------------------------------------------------------------------
    appService.refreshScroll();
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.query = "";
    //-----------------------------------------------------------------------------------------------------------
    $scope.selectedReviewers = [];
    $scope.showModalComment = false;
    $scope.showModalReviewers = false;

    $scope.getAllTasks = function () {
        dataService.getAll('Tasks', null, null, function (res) {            
            $scope.Tasks = res.data;
            console.log($scope.Tasks);
        });
    }
    dataService.getAll('Users', 'GetAllReviewers', null, function (res) {
        $scope.Reviewers = res.data;
    });

    $scope.getAllTasks();

    $scope.executeCommand = function (commandId, task) {
        $scope.selectedTask = task;
        switch (commandId) {
            case 1://asign editor
                //$scope.selectedProject = task.ScientificProject;                
                dataService.update('Tasks', 'AssignEditor', task, function (res) {
                    $scope.getAllTasks();
                });
                break;
            case 2://Assign reviewrs
                $scope.selectedProject = task.ScientificProject;
                $scope.showModalReviewers = true;
                break;
            case 3://Write review
                $scope.selectedProject = task.ScientificProject;
                $scope.showModalReview = true;

                $scope.review = {
                    ProjectId: $scope.selectedProject.ProjectId,
                    ReviewerId: $rootScope.loggedUser.UserId,
                    Approved: 1,
                    TaskId: $scope.selectedTask.TaskId,
                    AreaId: $scope.selectedProject.ScientificAreaId//,
                    //Rating: 
                    //    ReviewContent :
                }

                break;
            case 4://Accept project
                dataService.update('Projects', 'ApproveProject', task, function (res) {                  
                    var selectedtask = appService.lodashFindBy($scope.Tasks, 'TaskId', task.TaskId);
                    $scope.Tasks = _.without($scope.Tasks, selectedtask);
                    $scope.selectedTask = null;
                });
                break;
            case 5://Return project
                $scope.selectedProject = task.ScientificProject;
                $scope.showModalComment = true;
                $scope.selectedProject.Comment = "";
                //$scope.showCommentModal();
                break;
            case 6://Reject project
                dataService.update('Projects', 'DeleteProject', task, function (res) {
                    /* var foundTask = appService.lodashFindBy($scope.Tasks, 'TaskId', task.TaskId);
                     foundTask.Processed = true;*/
                    var selectedtask = appService.lodashFindBy($scope.Tasks, 'TaskId', task.TaskId);
                    $scope.Tasks = _.without($scope.Tasks, selectedtask);
                    $scope.selectedTask = null;
                });
                break;
            case 7://Send project again to previous reviewers
                dataService.update('Tasks', 'SendProjectToPreviousReviewers', task, function (res) {
                    var selectedtask = appService.lodashFindBy($scope.Tasks, 'TaskId', task.TaskId);
                    $scope.Tasks = _.without($scope.Tasks, selectedtask);
                    $scope.selectedTask = null;
                });
                break;
            default:
            
        }
    }

    $scope.exitModal = function () {
        $scope.showModalComment = false;
        $scope.showModalReviewers = false;
        $scope.showModalReview = false;
        $scope.showReviewCommentModal = false;
        $scope.selectedProject = null;

        $scope.selectedTask = null;


        $scope.selectedReviewers = [];
    }

    $scope.returnProjectToAuthor = function () {        

        dataService.update('Projects', 'CommentReturnProject', $scope.selectedProject, function (res) {
            var task = appService.lodashFindBy($scope.Tasks, 'TaskId', $scope.selectedTask.TaskId);

            $scope.Tasks = _.without($scope.Tasks, task);

            $scope.exitModal();
        });
      
    }
    $scope.completeWithoutReviewers = function () {
        dataService.update('Tasks', 'CompleteWithoutReviewers', $scope.selectedTask, function (res) {
            var task = appService.lodashFindBy($scope.Tasks, 'TaskId', $scope.selectedTask.TaskId);

            $scope.Tasks = _.without($scope.Tasks, task);

            $scope.exitModal();
        });
    }


    $scope.assignReviewers = function () {

        var selectedReviewerWorkers = [];
        for (var i = 0; i <= $scope.selectedReviewers.length - 1; i++) {
            var magazineWorkerTask = {
                TaskId: $scope.selectedTask.TaskId,
                ScientificCentreUsers: [{ UserId: $scope.selectedReviewers[i] }],
                ProjectId: $scope.selectedTask.ProjectId,
                StartedOnDate: moment(new Date()),
                TaskType: 2              
            }
            selectedReviewerWorkers.push(magazineWorkerTask);
        }

        if (selectedReviewerWorkers.length > 2) {
            console.log(selectedReviewerWorkers);
            dataService.update('Tasks', 'AssignReviewers', selectedReviewerWorkers, function (res) {
                var task = appService.lodashFindBy($scope.Tasks, 'TaskId', $scope.selectedTask.TaskId);

                $scope.Tasks = _.without($scope.Tasks, task);

                $scope.exitModal();
            });
        } else {
            alert("Please select at least two reviewers to proceed");
        }

    }
    $scope.forEditor = function () {
        if (!$scope.justEditor)
            $scope.justEditor = true;
        else {
            $scope.justEditor = false
        }

        $scope.review.OnlyForEditor = $scope.justEditor;
    }

    $scope.addReviewer = function (userId) {
        if (_.includes($scope.selectedReviewers, userId)) {
            $scope.selectedReviewers = _.without($scope.selectedReviewers, userId);//remove already selected Reviewer id
        } else {
            $scope.selectedReviewers.push(userId);//add Reviewer id to array
        }
    }
    $scope.getType = function (taskType) {
        return appService.lodashFindBy($rootScope.enumTaskTypes, 'value', Number(taskType));
    }
    $scope.sendReviewToEditor = function () {
        dataService.update('Tasks', 'SendReviewToEditor', $scope.review, function (res) {
            var task = appService.lodashFindBy($scope.Tasks, 'TaskId', $scope.selectedTask.TaskId);

            $scope.Tasks = _.without($scope.Tasks, task);

            $scope.exitModal();
        });
    }
    $scope.selectedProjectReviews = [];
    $scope.showReviewCommentModal = false;
    $scope.displayReviewCommentModal = function (ProjectReviews) {
        $scope.selectedProjectReviews = ProjectReviews; 
        $scope.showReviewCommentModal = true;
    }

    $scope.getRatingName = function (ratingId) {
        return appService.lodashFindBy($rootScope.enumRatings, 'value', ratingId);
    }
}]);
