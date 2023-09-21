scifiApp.controller('updateProjectCtrl', ['$rootScope', '$scope', 'dataService', 'appService', '$timeout', '$routeParams', function ($rootScope, $scope, dataService, appService, $timeout, $routeParams) {
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.icon = 'fa-home';
    $rootScope.title = 'Update Project';
    $rootScope.mode = 'view';
    //-----------------------------------------------------------------------------------------------------------   

    for (i = 0; i <= $rootScope.loggedUser.ProjectAuthors.length - 1; i++) {
        if ($rootScope.loggedUser.ProjectAuthors[i].ProjectId == $routeParams.projectId && $rootScope.loggedUser.ProjectAuthors[i].Role == "MainAuthor") {
            $scope.project = angular.copy($rootScope.loggedUser.ProjectAuthors[i].ScientificProject);
        }
    }   

    $scope.UpdateProject = function () {

        var files = document.getElementById('file').files;

        $scope.project.Magazines.push({ MagazineId: $scope.project.MagazineId });

        if (files.length > 0) {
            var f = files[0];

            var formD = new FormData();

            formD.append("file", f);

            dataService.uploadFile($scope.project).exec(formD).$promise.then(function (res, err) {
                //redirect author to his settings page
                $rootScope.loggedUser = res.data;
                $rootScope.changeView('/authorSettings');
            });
        } else {
            dataService.update('Projects', null, $scope.project, function (res) {
                //redirect author to his settings page
                $rootScope.loggedUser = res.data;
                $rootScope.changeView('/authorSettings');
            });
        }
       
       
    }
}]);

