scifiApp.controller('selectedMagazineCtrl', ['$rootScope', '$scope', '$timeout', '$window', 'dataService', 'appService', '$routeParams', function ($rootScope, $scope, $timeout, $window, dataService, appService, $routeParams) {
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.icon = 'fa-user';
    $rootScope.title = 'Magazine: ';
    $rootScope.faicon = 'fa-booking-darkblue';
    $rootScope.mode = 'view';
    //-----------------------------------------------------------------------------------------------------------
    appService.refreshScroll();
    $rootScope.query = "";
    //-----------------------------------------------------------------------------------------------------------
    dataService.get('Magazines', Number($routeParams.magazineId), function (res) {
        $scope.selectedMagazine = res.data;
        $rootScope.title = $rootScope.title + $scope.selectedMagazine.Name;
    });

    $scope.getMagazineName = function (magazinId) {    
        return appService.lodashFindBy($rootScope.enumMagazineName, 'value', Number(magazinId));
    }


    $scope.buyProject = function(projectId) {
        //check if this project exists in user inventories

        if (true) {

        } else {
            $rootScope.changeView('/buyProject/' + projectId);
        }
    }
}]);

