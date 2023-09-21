scifiApp.controller('registerCtrl', ['$rootScope', '$scope', '$timeout', '$window', 'dataService', 'appService', 'tmhDynamicLocale', function ($rootScope, $scope, $timeout, $window, dataService, appService, tmhDynamicLocale) {
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.icon = 'fa-user';
    $rootScope.title = 'Register';
    $rootScope.faicon = 'fa-booking-darkblue';
    $rootScope.mode = 'view';
    //-----------------------------------------------------------------------------------------------------------
    appService.refreshScroll();

    //-----------------------------------------------------------------------------------------------------------
    //Register user
    $scope.register = function (registeruser) {
        console.log(registeruser);
        dataService.register().exec({}, registeruser).$promise.then(function (res, err) {
            if (res.status.code == 200) {               
                 $rootScope.changeView('/login');
            }
        });
    }


    //-----------------------END OF LOGIN STUFF--------------------------------------------------------
    $scope.towns = [];
    $scope.updateTownList = function () {
        if ($scope.registeruser.Country) {
            $scope.towns = appService.lodashFilterBy($rootScope.enumTowns, 'countryId', $scope.registeruser.Country);
        } else {
            $scope.registeruser.TownId = null;
            $scope.towns = [];
        }        
    }
}]);

