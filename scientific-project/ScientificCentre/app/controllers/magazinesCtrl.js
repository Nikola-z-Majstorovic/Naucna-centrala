scifiApp.controller('magazinesCtrl', ['$rootScope', '$scope', '$timeout', '$window', 'dataService', 'appService', 'tmhDynamicLocale', function ($rootScope, $scope, $timeout, $window, dataService, appService, tmhDynamicLocale) {
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.icon = 'fa-user';
    $rootScope.title = 'Magazines';
    $rootScope.faicon = 'fa-booking-darkblue';
    $rootScope.mode = 'view';
    //-----------------------------------------------------------------------------------------------------------
    appService.refreshScroll();
    $rootScope.query = "";
    //-----------------------------------------------------------------------------------------------------------
    //Returns all magazines
    dataService.getAll('Magazines', null, null, function (res) {        
        $scope.Magazines = res.data;
    });
 

    $scope.navigateToMagazine = function (magazine) {
        
        if (magazine.Access == 2) {//closed access       
            dataService.get('Subscriptions', magazine.MagazineId, function (res) {//Checks subscription
                if (res.status.code == 200) {//if subscription is valid then redirect to selected magazine
                    $rootScope.changeView('/selectedMagazine/' + magazine.MagazineId);
                } else if (res.status.code == 403) {
                    $rootScope.changeView('/subscribe/' + magazine.MagazineId);
                }
            });
        } else {//open access
            $rootScope.changeView('/selectedMagazine/' + magazine.MagazineId);
        }
        
    }
    
}]);

