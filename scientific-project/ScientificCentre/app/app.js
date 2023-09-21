
//Angular app
var scifiApp = angular.module('scifiApp', ['ngRoute', 'ngResource', 'ngAnimate', 'ngCookies', 'ngSanitize', 'angular-loading-bar',
    'ngClientPaginate', 'ngSectionHeader', 'ngBookingGrid', 'ngBookingSorter', 'ngTitle',
    'ngModelState', 'ui.bootstrap', 'tmh.dynamicLocale'
]);
scifiApp.constant('_', window._); //lodash :)
scifiApp.config(['$routeProvider', '$httpProvider', 'tmhDynamicLocaleProvider', 'cfpLoadingBarProvider', function ($routeProvider, $httpProvider, tmhDynamicLocaleProvider, cfpLoadingBarProvider) {
    tmhDynamicLocaleProvider.localeLocationPattern('/Scripts/i18n/angular-locale_{{locale}}.js');
    tmhDynamicLocaleProvider.defaultLocale('en');
    $httpProvider.interceptors.push('customHttpInterceptor');
    //#region Routes
    $routeProvider
        .when('/',
            {
                controller: 'homeCtrl',
                templateUrl: 'app/views/home.html',
                resolve: {}
            })
        .when('/login',
            {
                controller: 'loginCtrl',
                templateUrl: 'app/views/login.html',
                resolve: {}
            })
        .when('/register',
            {
                controller: 'registerCtrl',
                templateUrl: 'app/views/register.html',
                resolve: {}
            })
        .when('/magazines',
            {
                controller: 'magazinesCtrl',
                templateUrl: 'app/views/magazines.html',
                resolve: {}
            })    
        .when('/selectedMagazine/:magazineId',
            {
                controller: 'selectedMagazineCtrl',
                templateUrl: 'app/views/selectedMagazine.html',
                resolve: {}
            }) 
        .when('/selectedProject/:ProjectId',
            {
                controller: 'selectedMagazineCtrl',
                templateUrl: 'app/views/selectedMagazine.html',
                resolve: {}
            })
        .when('/sendProject',
            {
                controller: 'sendProjectCtrl',
                templateUrl: 'app/views/sendProject.html',
                resolve: {}
            })
        .when('/subscribe/:MagazineId',
            {
                controller: 'subscribeCtrl',
                templateUrl: 'app/views/subscribe.html',
                resolve: {}
            })
        .when('/settings/',
            {
                controller: 'settingsCtrl',
                templateUrl: 'app/views/settings.html',
                resolve: {}
            })
        .when('/authorSettings/',
            {
                controller: 'authorSettingsCtrl',
                templateUrl: 'app/views/authorSettings.html',
                resolve: {}
            })
        .when('/updateProject/:projectId',
            {
                controller: 'updateProjectCtrl',
                templateUrl: 'app/views/updateProject.html',
                resolve: {}
            })
        .when('/search/',
            {
                controller: 'searchCtrl',
                templateUrl: 'app/views/search.html',
                resolve: {}
            })
        .when('/searchList/',
                {
                    controller: 'searchListCtrl',
                    templateUrl: 'app/views/searchList.html',
                    resolve: {}
                })
        .otherwise({ redirectTo: '/' });
    //#endregion
}]);
scifiApp.factory('customHttpInterceptor', ['$q', '$rootScope', '$location', '$cookieStore', 'flashService', 'appService', function ($q, $rootScope, $location, $cookieStore, flashService, appService) {
    return {
        response: function (res) {
            //if (res.config) {
            //    if (res.config.url.startsWith($rootScope.getUrl()) && !res.config.url.endsWith('.html') && !res.config.url.startsWith('ui-grid')) {
            //        if ($rootScope.arrReq.length != 0) {
            //            $rootScope.arrReq.shift();
            //        }
            //        if ($rootScope.arrReq.length == 0) {
            //            if (res.config.ignoreLoadingBar != undefined) {
            //                if (res.config.ignoreLoadingBar) {

            //                }
            //                else {
            //                    appService.refreshScroll(true);
            //                }
            //            } else {
            //                appService.refreshScroll(true);
            //            }

            //        }
            //    }
            //}
            $rootScope.modelStateErrors = {};
            if (res.data.status) {
                if (res.data.status.code) {
                    if (res.data.status.code == 200) {
                        flashService.ok(res.data.status);
                    }
                }
            }
            return res;
        },
        //request: function (req) {
        //    if (req.url) {
        //        if (req.url.startsWith($rootScope.getUrl()) && !req.url.endsWith('.html') && !req.url.startsWith('ui-grid')) {
        //            $rootScope.arrReq.push(1);
        //        }
        //    }
        //    $rootScope.modelStateErrors = {};

        //    return req;
        //},
        responseError: function (res) {
            if ($rootScope.arrReq.length != 0) {
                $rootScope.arrReq.shift();
            }
            if ($rootScope.arrReq.length == 0) {
                appService.refreshScroll(true);
            }

            $rootScope.modelStateErrors = {};

            if (res.status) {
                if (res.status == 0) {
                    flashService.err({
                        code: 599,
                        title: 'offline',
                        messages: { message: 'You are not logged in' }
                    });
                    return $q.reject(res);
                }
                if (res.data) {
                    if (res.data.status) {
                        if (res.data.status.code == 401) {
                            flashService.err(res.data.status);
                            $location.path('/login');
                        }
                        $rootScope.modelStateErrors = res.data.status.errors;
                        res.data.status = {
                            errors: ['Error occured', ' Please correct your input and try again']
                        };
                        flashService.err(res.data.status);
                    }
                    else {
                        if (res.data.Message) {
                            res.data.status = {
                                code: 500,
                                message: res.data.Message,
                                messages: [],
                                errors: [res.data.Message, res.data.ExceptionMessage]
                            };
                            flashService.err(res.data.status);
                            $location.path('/login');
                        }
                    }
                }
            }
            else {
                flashService.err({ code: 406, title: 'Status error', messages: { message: 'res.status is undefined/null' } });
            }

            return $q.reject(res);
        }
    };
}]);
scifiApp.run(['$q', '$resource', '$rootScope', '$templateCache', '$cookieStore', '$window', '$timeout', '$locale', '$location', '$routeParams', 'appService', 'dataService', function ($q, $resource, $rootScope, $templateCache, $cookieStore, $window, $timeout, $locale, $location, $routeParams, appService, dataService) {
    //-----------------------------------------------------------------------------------------------------
    // Init all
    appService.init();
    //-----------------------------------------------------------------------------------------------------
    $rootScope.$on('loading', function (event, data) {
        $rootScope.loading = data.show;
    });
    //-----------------------------------------------------------------------------------------------------
    $rootScope.$on('$locationChangeStart', function () {
        $rootScope.modelStateErrors = {};
        $rootScope.route = $location.path();

        //if ($rootScope.loggedUser.UserId == '') {
        // $rootScope.changeView('/');
        //}
    });
    $rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
    });
    $rootScope.$on('cfpLoadingBar:loading', function (event, current, previous) {
        $rootScope.$broadcast('loading', { show: true });
    });
    $rootScope.$on('cfpLoadingBar:completed', function (event, current, previous) {
        $rootScope.$broadcast('loading', { show: false });
    });
    //cfpLoadingBar:loaded
    //-----------------------------------------------------------------------------------------------------
    $rootScope.$on('$viewContentLoaded', function () {
        appService.refreshScroll();
    });
    //-----------------------------------------------------------------------------------------------------
    $rootScope.logout = function () {
        $rootScope.changeView('/login');
        dataService.logout();
    };
    //-----------------------------------------------------------------------------------------------------


}]);
//---------------------------------------------------------------------------------------------------------