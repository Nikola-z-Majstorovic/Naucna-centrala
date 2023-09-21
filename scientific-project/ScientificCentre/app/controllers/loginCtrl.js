scifiApp.controller('loginCtrl', ['$rootScope', '$scope', '$timeout', '$window', 'dataService', 'appService', 'tmhDynamicLocale', function ($rootScope, $scope, $timeout, $window, dataService, appService, tmhDynamicLocale) {
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.icon = 'fa-user';
    $rootScope.title = 'Login';
    $rootScope.faicon = 'fa-booking-darkblue';
    $rootScope.mode = 'view';
    //-----------------------------------------------------------------------------------------------------------
    appService.refreshScroll();
    //-----------------------------------------------------------------------------------------------------------
    $scope.login = function (loginuser) {
        //Logins user and creates session        
        dataService.login().exec({}, loginuser).$promise.then(function (res, err) {
            //dont login user if there is some issue like with the role for example
            if (res.status.message != 'Access Denied') {
                if (res.status.code == 200) {                    
                    //Everything is ok, login and redirect to home page
                    $rootScope.loggedUser = res.data;
                    //Save roles here
                    $rootScope.Roles = res.data.Roles;
                    moment.locale('en');

                    $rootScope.changeView('/');
                }
                else {
                    $rootScope.status = res.status;
                }
            } else {
                $rootScope.status = res.status;
            }
        });
    };

    //dataService.update('Users', 'ElasticIndexing',null , function (res) {
    //    $scope.getAllTasks();
    //});

    //-----------------------END OF LOGIN STUFF--------------------------------------------------------


    $rootScope.filteredProjects = null;

    dataService.getAll('Magazines', null, null, function (res) {
        console.log(res.data);
        $scope.Magazines = res.data;
    });

    dataService.getAll('Projects', 'GetAllScientificAreas', null, function (res) {
        $scope.allAreas = res.data;
    });

    $scope.search = function () {
        dataService.update('Users', 'SearchForProjects', $scope.project, function (res) {
            $rootScope.filteredProjects = res.data;
            $rootScope.changeView('/searchList')
        });
    }

}]);

