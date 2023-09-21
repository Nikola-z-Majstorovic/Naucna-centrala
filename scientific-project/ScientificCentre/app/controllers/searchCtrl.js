scifiApp.controller('searchCtrl', ['$rootScope', '$scope', 'dataService', 'appService', function ($rootScope, $scope, dataService, appService) {
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.icon = 'fa-home';
    $rootScope.title = 'Search';
    $rootScope.mode = 'view';
    //-----------------------------------------------------------------------------------------------------------


    $rootScope.filteredProjects = null;

    dataService.getAll('Magazines', null, null, function (res) {
        console.log(res.data);
        $scope.Magazines = res.data;
    });

    dataService.getAll('Projects', 'GetAllScientificAreas', null, function (res) {
        $scope.allAreas = res.data;
    });

    $scope.search = function () {
        dataService.update('Projects', 'SearchForProjects', $scope.project, function (res) {
            $rootScope.filteredProjects = res.data;
            $rootScope.changeView('/searchList' )
        });
    }
}]);