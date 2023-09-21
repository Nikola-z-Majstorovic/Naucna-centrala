scifiApp.controller('subscribeCtrl', ['$rootScope', '$scope', '$timeout', '$window', 'dataService', 'appService', '$routeParams', function ($rootScope, $scope, $timeout, $window, dataService, appService, $routeParams) {
    //-----------------------------------------------------------------------------------------------------------
    $rootScope.icon = 'fa-user';
    $rootScope.title = 'Subscribe: ';
    $rootScope.faicon = 'fa-booking-darkblue';
    $rootScope.mode = 'view';
    //-----------------------------------------------------------------------------------------------------------
    appService.refreshScroll();
    $rootScope.query = "";
    //-----------------------------------------------------------------------------------------------------------

    $scope.choosePackage = function (packageId) {
        console.log($scope.packageSelected);

        /*
        var subscription = {
            SubscriptionMagazineId: Number($routeParams.MagazineId),
            SubibscriptionType: packageId
        };
        dataService.update('Subscriptions', 'PreparePrice', subscription, function (res) {
            if (res.status.code == 200) {
                $scope.price = res.data;
            }

            //call payment service
        });*/


        dataService.getAll('Subscriptions', 'PreparePrice', { objId: packageId }, function (res) {
            if (res.status.code == 200) {
                $scope.price = res.data;
            }
           
            //call payment service
        });
    }
}]);

