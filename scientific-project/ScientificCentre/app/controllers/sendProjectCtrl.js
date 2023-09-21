scifiApp.controller('sendProjectCtrl', ['$rootScope', '$scope', 'dataService', 'appService', '$timeout', function ($rootScope, $scope, dataService, appService, $timeout) {
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.icon = 'fa-home';
    $rootScope.title = 'Send Project';
    $rootScope.mode = 'view';
    //-----------------------------------------------------------------------------------------------------------
    appService.refreshScroll();
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.query = "";
    //-----------------------------------------------------------------------------------------------------------

    $scope.OtherAuthors = [];

    if ($rootScope.tmpProject) {
        $scope.project = $rootScope.tmpProject;
    }

    //-----------------------------------------------------------------------------------------------------------
    dataService.getAll('Users', 'GetAllAuthors', null, function (res) {
        $scope.allAuthors = res.data;
    });

    dataService.getAll('Magazines', null, null, function (res) {
        $scope.allMagazines = res.data;
    });

    dataService.getAll('Projects', 'GetAllScientificAreas', null, function (res) {
        $scope.allAreas = res.data;
    });

    $scope.checkMagazineAccessType = function (magazineId) {    
        var magazine = appService.lodashFindBy($scope.allMagazines, 'MagazineId', magazineId);
        if (magazine != null) {
           // if (magazine.Access == 1) {
            dataService.get('Subscriptions', magazine.MagazineId, function (res) {//Checks subscription
                    if (res.status.code == 200) {
                    } else if (res.status.code == 403) {
                        $rootScope.tmpProject = $scope.project;
                        $rootScope.tmpProject.MagazineId = null;
                        $rootScope.changeView('/subscribe/');
                    }
                });
           // }
        } 
    }


    $scope.addAuthor = function (userId) {
        if (_.includes($scope.OtherAuthors, userId)) {
            $scope.OtherAuthors = _.without($scope.OtherAuthors, userId);//remove already selected author id
        } else {
            $scope.OtherAuthors.push(userId);//add author id to array
        }  
    }

    $scope.SendProject = function () {

        $scope.project.ProjectAuthors = [];
        $scope.project.Magazines = [];
        for (var i = 0; i <= $scope.OtherAuthors.length - 1; i++) {
            var projectAuthor = {
                AuthorId: $scope.OtherAuthors[i],
               
                Role: 'Author'
            }
            $scope.project.ProjectAuthors.push(projectAuthor);
        }

        var files = document.getElementById('file').files;

        $scope.project.Magazines.push({ MagazineId: $scope.project.MagazineId });

        for (i = 0; i <= files.length - 1; i++) {

            var f = files[i];

            var formD = new FormData();

            formD.append("file", f);

            dataService.uploadFile($scope.project).exec(formD).$promise.then(function (res, err) {
                //redirect author to his settings page
                $rootScope.loggedUser = res.data;
                $rootScope.changeView('/authorSettings');
            });
        }
       
       /* 
        dataService.create('Projects', $scope.project, function (res) {
        });*/


    }

}]);