scifiApp.factory('appService', ['$rootScope', '$location', '$timeout', '$window', function ($rootScope, $location, $timeout, $window) {
    return {
        init: function () {
        
            //-----------------------------------------------------------------------------------------------------
            $rootScope.appService = this;
            //-----------------------------------------------------------------------------------------------------
            $rootScope.loadingMessage = '';
            //-----------------------------------------------------------------------------------------------------
            $rootScope.arrReq = [];
            //-----------------------------------------------------------------------------------------------------
            $rootScope._ = window._;
            //-----------------------------------------------------------------------------------------------------
            $rootScope.loggedUser = {
                UserId: '',
                UserName: '',
            };
            //-----------------------------------------------------------------------------------------------------
            $rootScope.CultureName = moment().locale();
            //-----------------------------------------------------------------------------------------------------
            // section icon/title init
            $rootScope.icon = 'fa-user';
            $rootScope.title = 'Login';
            $rootScope.mode = 'view';
            //-----------------------------------------------------------------------------------------------------
            $rootScope.enumTowns = [
                { name: 'Novi Sad', value: 9, countryId: 1 },
                { name: 'Kruševac', value: 10, countryId: 1 },
                { name: 'Beograd', value: 11, countryId: 1 },
                { name: 'Tirana', value: 12, countryId: 5 }
            ];
            $rootScope.enumCountries = [
                { name: 'Serbia', value: 1 },
                { name: 'Albania', value: 5 }
            ];
            $rootScope.enumAreas = [
                { name: 'Engeneering', value: 1 }
            ];

            $rootScope.enumMagazineName = [
                { name: 'MathReader', value: 1 },
                { name: 'BioMagazine', value: 2 },
                { name: 'SciTech', value: 3 },
                { name: 'PhysicMagazine', value: 4 },
                { name: 'WheaterReaders', value: 5 },
                { name: 'AstroMagazine', value: 6 },
                { name: 'LawMag', value: 7 }
            ];

            /*$rootScope.enumTaskTypes = [
                { name: 'Examine', value: 0 },                
                { name: 'Assign Editor', value: 1 },
                { name: 'Assign Reviewers', value: 2 },
                { name: 'Review', value: 3 },
                { name: 'Edit Project', value: 4 },
                { name: 'Send Project', value: 5 },
                { name: 'ConfirmProject', value: 6 }
            ];*/

            $rootScope.enumTaskTypes = [
                { name: 'Examine', value: 0 },
                { name: 'Assign Reviewers', value: 1 },
                { name: 'Review', value: 2 },
                { name: 'Edit Project', value: 3 },
                { name: 'Examine Updated Project With Reviews', value: 4 },
            ];
            

            $rootScope.enumRatings = [
                { name: 'Accept project', value: 0 },
                { name: 'Accept with correction', value: 1 },
                { name: 'Conditionaly accept', value: 2 },
                { name: 'Reject Project', value: 3 },
            ];
            //-----------------------------------------------------------------------------------------------------
            // Select/Options events
            //-----------------------------------------------------------------------------------------------------
            $rootScope.optionsChange = function (modelName, propertyName) {
                $rootScope[modelName][propertyName] = $rootScope[propertyName].value;
            }
            //-----------------------------------------------------------------------------------------------------
            $rootScope.getUrl = function () {
                return $rootScope.appService.getAppConf().App.Protocol + '' + $rootScope.appService.getAppConf().App.Host + ':' + $rootScope.appService.getAppConf().App.Port + '/' + $rootScope.appService.getAppConf().App.Api;
            };
            //-----------------------------------------------------------------------------------------------------
            $rootScope.replaceGuid = function () {
                if ($rootScope.loggedUser) {
                    if ($rootScope.loggedUser.UserId) {
                        return $rootScope.loggedUser.UserId.replace('0', '9');
                    } else { return ''; }
                } else { return ''; }
            };
            //-----------------------------------------------------------------------------------------------------
            $rootScope.changeView = function (view) {
                $location.path(view);
            };
            //-----------------------------------------------------------------------------------------------------
        },
        //Some lodash functions wrapped up
        lodashRemoveBy: function (array, propertyName, propertyValue) {
            return _.remove(array, [propertyName, propertyValue]);
        },
        lodashFindBy: function (array, propertyName, propertyValue) {
            return _.find(array, [propertyName, propertyValue]);
        },
        lodashFilterBy: function (array, propertyName, propertyValue) {
            return _.filter(array, [propertyName, propertyValue]);
        },
        lodashSortBy: function (array, propertyName) {
            return _.sortBy(array, [propertyName]);
        },
        findBy: function (array, propertyName, propertyValue) {
            if (array) {
                if (array.length != 0) {
                    for (var i = 0; i < array.length; i++) {
                        if (array[i][propertyName] === propertyValue) {
                            return array[i];
                        }
                    }
                }
            }
            return null;
        },
        //used when calling backend, transfering headers and data that will be used in sessions and booking grids
        getAppConf: function () {
            var format = '';
            var AppConf = {
                Header: this.getHeader(),
                App: {
                    Protocol: 'http://',
                    Host: $location.host(),
                    Port: $location.port(),
                    Api: 'api'
                },
                Section: {
                    Route: $location.path(),
                    Mode: 'view'
                },
                Grid: {
                    PageCount: 5,
                    ItemsPerPage: 5,
                    SortBy: '',
                    SortDirection: 'Asc'
                },

                User: {
                    UserId: $rootScope.loggedUser.UserId,
                    UserName: $rootScope.loggedUser.UserName,
                    FullName: $rootScope.loggedUser.FullName
                },
                Roles: $rootScope.loggedUser.Roles
            };
            if ($rootScope.loggedUser.UserId != '') {
                AppConf.User.UserId = $rootScope.loggedUser.UserId;
                AppConf.User.UserName = $rootScope.loggedUser.UserName;
                AppConf.User.FullName = $rootScope.loggedUser.FullName;
            }
            return AppConf;
        },
        getHeader: function () {
            if ($rootScope.loggedUser) {
                return {
                    UserId: $rootScope.loggedUser.UserId,
                }
            }
            else {
                return {
                    UserId: '',
                }
            }
        },
        refreshScroll: function (refreshOnly) {
            var that = this;
            $timeout(function () {
                var sclEl = $('.scroller');
                if (sclEl.length != 0) {
                    $(sclEl).scrollTop(0);
                }
            }, 300);
        }

    };
}]);
