scifiApp.controller('authorSettingsCtrl', ['$rootScope', '$scope', 'dataService', 'appService', function ($rootScope, $scope, dataService, appService) {
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.icon = 'fa-home';
    $rootScope.title = 'Settings Author';
    $rootScope.mode = 'view';
    //-----------------------------------------------------------------------------------------------------------

  
    //Collecting active and waiting for approval projects, just for displaying/hidding label in view
    $scope.activeProjects = [];
    $scope.waitingProjects = [];

    for (i = 0; i <= $rootScope.loggedUser.ProjectAuthors.length - 1; i++) {
        if (!$rootScope.loggedUser.ProjectAuthors[i].ScientificProject.Removed && !$rootScope.loggedUser.ProjectAuthors[i].ScientificProject.Approved) {
            $scope.waitingProjects.push($rootScope.loggedUser.ProjectAuthors[i]);
        }        
        if ($rootScope.loggedUser.ProjectAuthors[i].ScientificProject.Approved) {
            $scope.activeProjects.push($rootScope.loggedUser.ProjectAuthors[i]);
        }

    }
    //-----------------------------------------------------------------------------------------------------------
    $scope.projectExistsInTasks = function (projectId) {
       
       // var tasks = angular.copy(appService.lodashFilterBy($rootScope.loggedUser.Tasks, 'ProjectId', projectId));
        for (i = 0; i <= $rootScope.loggedUser.Tasks.length - 1; i++) {
            if ($rootScope.loggedUser.Tasks[i].ProjectId == projectId && !$rootScope.loggedUser.Tasks[i].Processed && !$rootScope.loggedUser.Tasks[i].Expired) {
                return true;
            }
        }

        return false;
       /* tasks = appService.lodashSortBy(tasks, 'StartedOnDate');
        console.log(tasks);
        var task = tasks[tasks.length - 1];//appService.lodashFindBy($rootScope.loggedUser.Tasks, 'ProjectId', projectId);
        console.log(task);
        if (task) {
            if (!task.Processed && !task.Expired) {
                return true;
            }
        } else {
            return false;
        }*/
    }
    $scope.selectedProjectReviews = [];
    $scope.showReviewCommentModal = false;
    $scope.displayReviewCommentModal = function (ProjectReviews) {
        $scope.selectedProjectReviews = ProjectReviews;
        console.log($scope.selectedProjectReviews);
        $scope.showReviewCommentModal = true;
    }

    $scope.exitModal = function () {
        $scope.showReviewCommentModal = false;
    }
}]);