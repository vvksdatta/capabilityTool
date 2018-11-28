(function () {
  var app = angular.module('appCtrl', ['ui.router','ngMessages', 'ngStorage','rzModule', 'ui.bootstrap', 'ngAnimate', 'data-table', 'ncy-angular-breadcrumb','base64', 'ngSanitize', 'dndLists', 'ngDragDrop', 'ngMaterial', 'chart.js']);
  app.service('authInterceptor', function($q) {
    var service = this;
    service.responseError = function(response) {
      if (response.status == "401"){
        window.location = "/#/login";
      }
      return $q.reject(response);
    };
  });
  app.service('dataService', function($http) {
    this.getData = function(encoded) {
      return  $http({
        method: 'GET',
        url: '/api/auth/login',
        headers: {'Authorization': 'Basic ' + encoded }
      }).success(function(response){
        return response;
      }).error(function(){
        alert("error");
        return null ;
      });
    }
  });
  app.service('newDomains', function () {
    var domains = [];
    return {
      getAddedDomains: function () {
        return domains;
      },
      setAddedDomains: function(value) {
        domains.push(value);
      },
      resetDomains: function() {
        domains = [];
      }
    };
  });
  app.service('newAssets', function () {
    var assets = [];
    return {
      getAddedAssets: function () {
        return assets;
      },
      setAddedAssets: function(value) {
        assets.push(value);
      },
      resetAssets: function() {
        assets = [];
      }
    };
  });
  app.service('newEnvironments', function () {
    var environments = [];
    return {
      getAddedEnvironments: function () {
        return environments;
      },
      setAddedEnvironments: function(value) {
        environments.push(value);
      },
      resetEnvironments: function() {
        environments = [];
      }
    };
  });
  app.service('sprintNav', function () {
    var sprint = {};
    sprint.projectId = '';
    sprint.sprintId = '';
    return {
      getSprintNav: function () {
        return sprint;
      },
      setSprintNav: function(projectId, sprintId) {
        sprint.projectId = projectId;
        sprint.sprintId = sprintId;
      },
    }
  });
  app.factory('alertFactory',['$rootScope',
  function($rootScope) {
    var alertService = {};
    $rootScope.alerts = [];
    alertService.addAuto = function(type, msg, delay) {
      var alert = {'type': type, 'msg': msg};
      $rootScope.alerts.push(alert);
      if (!delay ) {
        delay = 2500; // default delay is 2500ms
      }
      window.setTimeout(function() {
        var index = $rootScope.alerts.indexOf(alert);
        if (index > -1) {
          $rootScope.alerts.splice(index, 1);
          $rootScope.$apply(); // refresh GUI
        }
      }, delay);
    };
    alertService.closeAlert = function(index) {
      //remove the alert from the array to avoid showing previous alerts
      $rootScope.alerts.splice(index, 1);
    };
    return alertService;
  }]);
  app.factory('GetProgrammingSkillsService', function ($http, $q) {
    return {
      getCountry: function(str) {
        // the $http API is based on the deferred/promise APIs exposed by the $q service. So it returns a promise for us by default
        var url = "/api/skills/searchSkill";
        return $http.put(url,str)
        .then(function(response) {
          return response.data;
        }, function(response) {
          // something went wrong
          return $q.reject(response.data);
        });
      }
    };
  });
  app.factory('GetSprintDomainsService', function ($http, $q) {
    return {
      getDomains: function(str) {
        // the $http API is based on the deferred/promise APIs exposed by the $q service
        // so it returns a promise for us by default
        var url = "/api/sprints/searchDomain";
        return $http.put(url,str)
        .then(function(response) {
          return response.data;
        }, function(response) {
          // something went wrong
          return $q.reject(response.data);
        });
      }
    };
  });
  app.factory('GetSprintAssetsService', function ($http, $q) {
    return {
      getAssets: function(str) {
        // the $http API is based on the deferred/promise APIs exposed by the $q service
        // so it returns a promise for us by default
        var url = "/api/sprints/searchAsset";
        return $http.put(url,str)
        .then(function(response) {
          return response.data;
        }, function(response) {
          // something went wrong
          return $q.reject(response.data);
        });
      }
    };
  });
  app.factory('GetSprintEnvironmentsService', function ($http, $q) {
    return {
      getEnvironments: function(str) {
        // the $http API is based on the deferred/promise APIs exposed by the $q service
        // so it returns a promise for us by default
        var url = "/api/sprints/searchEnvironment";
        return $http.put(url,str)
        .then(function(response) {
          return response.data;
        }, function(response) {
          // something went wrong
          return $q.reject(response.data);
        });
      }
    };
  });
  app.factory('GetSprintRequirementsService', function ($http, $q) {
    return {
      getRequirements: function(str) {
        var url = "/api/sprints/searchRequirement";
        return $http.put(url,str)
        .then(function(response) {
          return response.data;
        }, function(response) {
          // something went wrong
          return $q.reject(response.data);
        });
      }
    };
  });
  app.directive('userCard', function () {
    return {
      restrict: 'E',
      templateUrl: 'people/userCard.tmpl.html',
      scope: {
        name: '@',
        theme: '@'
      },
      controller: function ($scope) {
        $scope.theme = $scope.theme || 'default';
      }
    }
  });
  app.config(['$urlRouterProvider', '$stateProvider','$locationProvider', '$provide', '$httpProvider', '$mdThemingProvider','$mdIconProvider','$mdAriaProvider', function($urlRouterProvider, $stateProvider, $locationProvider, $provide, $httpProvider, $mdThemingProvider, $mdIconProvider,$mdAriaProvider) {
    $mdAriaProvider.disableWarnings();
    $mdThemingProvider.theme('default');
    $mdIconProvider.iconSet('person', 'photos/user.svg', 24);
    $mdIconProvider.defaultIconSet('photos/user.svg', 24);
    $httpProvider.interceptors.push('authInterceptor');
    $urlRouterProvider.otherwise('/');
    $stateProvider
    .state('home', {
      url:'/',
      templateUrl: 'home.html',
      ncyBreadcrumb: {
        label: 'Home'
      }
    })
    .state('login', {
      url:'/login',
      templateUrl: 'login/login.html',
      controller: 'LoginIndexController',
      controllerAs: 'vm',
    })
    .state('manageUser', {
      url:'/manageUser',
      templateUrl: 'user/manageUser.html',
      controller: 'manageUserCtrl',
      ncyBreadcrumb: {
        label: 'Manage user'
      }
    })
    .state('manageUser.myDetails', {
      url:'/myDetails',
      params: {
        userId: null
      },
      templateUrl: 'user/userPage.html',
      controller: 'userPageCtrl',
      ncyBreadcrumb: {
        label: 'My details'
      }
    })
    .state('manageUser.createNewUser', {
      url:'/createNewUser',
      templateUrl: 'user/createNewUser.html',
      controller: 'createNewUserCtrl',
      ncyBreadcrumb: {
        label: 'Create new user'
      }
    })
    .state('manageUser.manageUserAccounts', {
      url:'/manageUsers',
      params: {
        userId: null
      },
      templateUrl: 'user/manageUserAccounts.html',
      controller: 'manageUsersCtrl',
      ncyBreadcrumb: {
        label: 'Manage user accounts'
      }
    })
    .state('summaries', {
      url:'/summaries',
      templateUrl: 'summaries/summaries.html',
      ncyBreadcrumb: {
        label: 'Summary'
      }
    })
    .state('summaries.projects', {
      url:'/projects',
      templateUrl: 'summaries/summaries.projects.html',
      controller: 'projectSummaryCtrl',
      ncyBreadcrumb: {
        label: 'Projects'
      }
    })
    .state('summaries.sprints', {
      url:'/sprints',
      templateUrl: 'summaries/summaries.sprints.html',
      controller: 'sprintSummaryCtrl',
      ncyBreadcrumb: {
        label: 'Sprints'
      }
    })
    .state('summaries.people', {
      url:'/people',
      templateUrl: 'summaries/summaries.people.html',
      controller: 'peopleSummaryCtrl',
      ncyBreadcrumb: {
        label: 'People'
      }
    })
    .state('management', {
      url:'/management',
      templateUrl: 'management/management.html',
      ncyBreadcrumb: {
        label: 'Management'
      }
    })
    .state('management.projects', {
      url:'/projects',
      templateUrl: 'project/projects.html',
      ncyBreadcrumb: {
        skip : true
      }
    })
    .state('management.people', {
      url:'/people',
      templateUrl: 'people/people.html',
      //controller: 'peopleCtrl',
      ncyBreadcrumb: {
        skip : true
      }
    })
    .state('management.projects.projectsTable', {
      url:'/summary',
      templateUrl: 'project/projectsTable.html',
      controller: 'projectManagementCtrl',
      ncyBreadcrumb: {
        label: 'Summary of projects'
      }
    })
    .state('management.people.peopleTable', {
      url:'/summary',
      templateUrl: 'people/peopleTable.html',
      controller: 'peopleManagementCtrl',
      ncyBreadcrumb: {
        label: 'Summary of people'
      }
    })
    .state('management.projects.addProject', {
      url:'/addProject',
      templateUrl: 'project/addProject.html',
      //controller: 'addProjectCtrl',
      ncyBreadcrumb: {
        skip : true
      }
    })
    .state('management.people.addPerson', {
      url:'/addPerson',
      params: {
        personId: null
      },
      templateUrl: 'people/addPerson.html',
      //controller: 'peopleCtrl',
      ncyBreadcrumb: {
        label: 'Add person'
      }
    })
    .state('management.people.editPerson', {
      url:'/editPerson',
      templateUrl: 'people/editPerson.html',
      controller: 'programmingskills',
      ncyBreadcrumb: {
        label: 'Edit person'
      }
    })
    .state('management.projects.addProject.newProject', {
      url:'/newProject',
      templateUrl: 'project/newProject.html',
      controller: 'addProjectCtrl',
      ncyBreadcrumb: {
        label: 'Add new project'
      }
    })
    .state('management.people.addPerson.newPerson', {
      url:'/newPerson',
      templateUrl: 'people/newPerson.html',
      controller: 'peopleCtrl',
      ncyBreadcrumb: {
        label: 'Personal details'
      }
    })
    .state('management.people.editPerson.person', {
      url:'/:personId',
      templateUrl: 'people/existingPerson.html',
      controller: 'peopleEditCtrl',
      ncyBreadcrumb: {
        label: 'Personal details'
      }
    })
    .state('management.people.addPerson.capabilities', {
      url:'/addCapabilities/:personId',
      templateUrl: 'people/capabilities.html',
      controller: 'capabilityCtrl',
      ncyBreadcrumb: {
        label: 'Capabilities'
      }
    })
    .state('management.people.editPerson.editCapabilities', {
      url:'/editCapabilities/:personId',
      templateUrl: 'people/editCapabilities.html',
      controller: 'editCapabilityCtrl',
      ncyBreadcrumb: {
        label: 'Capabilities'
      }
    })
    .state('management.people.addPerson.programmingSkills', {
      url:'/programmingSkills/:personId',
      templateUrl: 'people/programmingSkills.html',
      controller: 'programmingskills',
      ncyBreadcrumb: {
        label: 'Programming skills'
      }
    })
    .state('management.people.editPerson.programmingSkills', {
      url:'/programmingSkills/:personId',
      templateUrl: 'people/editProgrammingSkills.html',
      controller: 'editProgrammingskills',
      ncyBreadcrumb: {
        label: 'Programming skills'
      }
    })
    .state('management.people.addPerson.newPerson.details', {
      url:'/newPerson/:action',
      templateUrl: 'people/newPerson.html',
      controller: 'peopleCtrl',
      ncyBreadcrumb: {
        label: 'Add person'
      }
    })
    .state('management.projects.addProject.participants', {
      url:'/:projectId/participants',
      templateUrl: 'project/participants.html',
      controller: 'projectParticipants',
      ncyBreadcrumb: {
        label: 'Add project participants'
      }
    })
    .state('management.projects.editProject', {
      url:'/editProject/:projectId',
      controller: 'editProject',
      templateUrl: 'project/editProject.html',
      ncyBreadcrumb: {
        label: 'Edit project',
      }
    })
    .state('management.projects.editProjectParticipants', {
      url:'/:projectId/editProjectParticipants',
      controller: 'editProjectParticipants',
      templateUrl: 'project/editProjectParticipants.html',
      ncyBreadcrumb: {
        label: 'Edit project participants',
      }
    })
    .state('management.sprints', {
      url:'/sprints',
      templateUrl: 'sprints/sprints.html',
      ncyBreadcrumb: {
        skip : true
      }
    })
    .state('management.sprints.sprintsTable', {
      url:'/summary',
      templateUrl: 'sprints/sprintsTable.html',
      controller: 'sprintsTableCtrl',
      ncyBreadcrumb: {
        label: 'Summary of sprints'
      }
    })
    .state('management.sprints.addSprint', {
      url:'/addSprint',
      templateUrl: 'sprints/addSprint.html',
      ncyBreadcrumb: {
        label: 'Add sprint'
      }
    })
    .state('management.sprints.addSprint.newSprint', {
      url:'/newSprint',
      templateUrl: 'sprints/newSprint.html',
      controller: 'newSprintCtrl',
      ncyBreadcrumb: {
        label: 'Sprint details'
      }
    })
    .state('management.sprints.addSprint.companyFactors', {
      url:'/addCompnayDrivenFactors/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
      },
      templateUrl: 'sprints/compnayFactors.html',
      controller: 'companyFactorsCtrl',
      ncyBreadcrumb: {
        label: 'Compnay driven factors'
      }
    })
    .state('management.sprints.addSprint.sprintRequirements', {
      url:'/addSprintRequirements/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
      },
      templateUrl: 'sprints/sprintRequirements.html',
      controller: 'sprintRequirementsCtrl',
      ncyBreadcrumb: {
        label: 'Sprint requirements'
      }
    })
    .state('management.sprints.addSprint.sprintRoles', {
      url:'/addSprintRoles/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
      },
      templateUrl: 'sprints/sprintRoles.html',
      controller: 'sprintRolesCtrl',
      ncyBreadcrumb: {
        label: 'Sprint roles'
      }
    }).state('management.sprints.editSprint', {
      url:'/editSprint',
      templateUrl: 'sprints/editSprint.html',
      ncyBreadcrumb: {
        label: 'Edit sprint'
      }
    })
    .state('management.sprints.editSprint.existingSprint', {
      url:'/existingSprint/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
      },
      templateUrl: 'sprints/existingSprint.html',
      controller: 'existingSprintCtrl',
      ncyBreadcrumb: {
        label: 'Sprint details'
      }
    })
    .state('management.sprints.editSprint.companyFactors', {
      url:'/editCompnayDrivenFactors/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
      },
      templateUrl: 'sprints/editCompnayFactors.html',
      controller: 'editCompanyFactorsCtrl',
      ncyBreadcrumb: {
        label: 'Compnay driven factors'
      }
    })
    .state('management.sprints.editSprint.sprintRequirements', {
      url:'/editSprintRequirements/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
      },
      templateUrl: 'sprints/editSprintRequirements.html',
      controller: 'editSprintRequirementsCtrl',
      ncyBreadcrumb: {
        label: 'Sprint requirements'
      }
    })
    .state('management.sprints.editSprint.sprintRoles', {
      url:'/editSprintRoles/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
      },
      templateUrl: 'sprints/editsprintRoles.html',
      controller: 'sprintRolesCtrl',
      ncyBreadcrumb: {
        label: 'Sprint roles'
      }
    })
    .state('management.sprints.editSprint.sprintParticipants', {
      url:'/sprintParticipants/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
      },
      templateUrl: 'sprints/sprintParticipants.html',
      controller: 'editPeopleToSprintCtrl',
      ncyBreadcrumb: {
        label: 'Sprint participants'
      }
    })
    .state('management.sprints.editSprint.peopleToIssues', {
      url:'/peopleToIssues/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
      },
      templateUrl: 'sprints/editPeopleToIssues.html',
      controller: 'peopleToIssuesCtrl',
      ncyBreadcrumb: {
        label: 'People to issues'
      }
    })
    .state('management.sprints.editSprint.questionnaire', {
      url:'/questionnaire/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
      },
      templateUrl: 'sprints/questionnaire.html',
      controller: 'editSprintQuestionnaireCtrl',
      ncyBreadcrumb: {
        label: 'Post completion questionnaire'
      }
    })
    .state('management.sprints.peopleToSprint', {
      url:'/peopleToSprint/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
        selectedRoles: null,
      },
      templateUrl: 'sprints/peopleToSprint.html',
      controller: 'peopleToSprintCtrl',
      ncyBreadcrumb: {
        label: 'People to sprint'
      }
    })
    .state('management.sprints.peopleToIssues', {
      url:'/peopleToIssues/:sprintId/:projectId',
      params: {
        projectId: null,
        sprintId: null,
      },
      templateUrl: 'sprints/peopleToIssues.html',
      controller: 'peopleToIssuesCtrl',
      ncyBreadcrumb: {
        label: 'People to issues'
      }
    })
  }]);
  app.run(run);
  function run($rootScope, $http, $location, $localStorage) {
    // keep user logged in after page refresh
    if ($localStorage.currentUser) {
      $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
    }
    $rootScope.location = $location;
    // redirect to login page if not logged in and trying to access a restricted page
    $rootScope.$on('$locationChangeStart', function (event, next, current) {
      var publicPages = ['/login'];
      var restrictedPage = publicPages.indexOf($location.path()) === -1;
      if (restrictedPage && !$localStorage.currentUser) {
        $location.path('/login');
      }
    });
  }
  app.controller('manageUserCtrl', function($scope, $state, $location, $http, alertFactory, $q, dataService,  $localStorage, $stateParams, $log, $timeout) {
    $scope.currentUserId = $localStorage.currentUser.userId;
    if($scope.currentUserId != null){
      $http.get('/api/people/getUserName/'+$scope.currentUserId).then(function(response)
      {
        $scope.newUserName = response.data.personName;
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching user name";
        alertFactory.addAuto('danger', $string, optionalDelay);
      })
    }
    var tabClasses;
    function initTabs() {
      tabClasses = ["","","",""];
    }
    $scope.getTabClass = function (tabNum) {
      return tabClasses[tabNum];
    };
    $scope.getTabPaneClass = function (tabNum) {
      return "tab-pane " + tabClasses[tabNum];
    }
    $scope.setActiveTab = function (tabNum) {
      initTabs();
      tabClasses[tabNum] = "active";
    };
    initTabs();
  });
  app.controller('manageUsersCtrl', function($state, $http, $q ,$scope, dataService, alertFactory, $location, $mdDialog, $localStorage) {
    $scope.currentUserId = $localStorage.currentUser.userId;
    $http.get('/api/people/getUsersList').then(function(response) {
      $scope.usersList = response.data;
    })
    .catch(function(response, status) {
      var optionalDelay = 5000;
      var $string = "Error in fetching list of users";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
    $scope.removeUser =function(ev, user){
      var confirm = $mdDialog.confirm()
      .title('Would you like to remove the user '+user.userFirstName +'?')
      .textContent('This person will no longer be able to access the portal')
      .ariaLabel('')
      .targetEvent(ev)
      .ok('Proceed!')
      .cancel('Cancel');
      $mdDialog.show(confirm).then(function() {
        for (var i =  $scope.usersList.length - 1; i >= 0; i--) {
          if ($scope.usersList[i].userId == user.userId) {
            $http.delete('/api/people/deleteUser/'+user.userId).then(function(response) {
              var optionalDelay = 5000;
              var $string = "Deleted the user "+ user.userFirstName;
              alertFactory.addAuto('success', $string, optionalDelay);
            })
            .catch(function(response, status) {
              var optionalDelay = 5000;
              var $string = "Error in removing the user "+ user.userFirstName;
              alertFactory.addAuto('danger', $string, optionalDelay);
              $scope.usersList.push(user);
            });
            $scope.usersList.splice(i, 1);
          }
        }
      });
    };
  });
  app.controller('createNewUserCtrl', function($base64, $state, $http, $q ,$scope, dataService, alertFactory, $location) {
    $scope.addUser = function(register) {
      $http.put('/api/auth/register', register).then(function(response) {
        var $string = response.data;
        var optionalDelay = 3000;
        alertFactory.addAuto('success', $string.success, optionalDelay);
        $state.go("manageUser.myDetails");
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Email ID or user name already registered! Use another one";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
    }
  });
  app.controller('userPageCtrl', function($scope,  $base64, $state, $location, $http, alertFactory, $q, dataService,  $localStorage, $stateParams, $log, $timeout) {
    if($stateParams.userId){
      var currentUserId = $stateParams.userId;
    }else{
      var currentUserId = $localStorage.currentUser.userId;
    }
    if(currentUserId != null){
      $http.get('/api/people/getUserDetailsbyId/'+currentUserId).then(function(response)
      {
        $scope.userDetails = response.data;
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching user details";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
    }
    $scope.updateUserDetails = function(userDetails){
      $http.put('/api/people/updateUserDetails/',userDetails).then(function(response)
      {
        var optionalDelay = 5000;
        var $string = "Successfully updated the details of user "+ userDetails.userFirstName;
        $state.go('home');
        alertFactory.addAuto('success', $string, optionalDelay);
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching user details";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
    };
    $scope.updateUserPassword = function(form2, userPasswordChange){
      if(userPasswordChange.newPassword != userPasswordChange.newPasswordRepeat){
        var optionalDelay = 5000;
        var $string = "The new passwords aren't the same";
        alertFactory.addAuto('danger', $string, optionalDelay);
        form2.newPasswordRepeat.$invalid = true;
        form2.newPassword.$invalid = true;
        return form2;
      } else if(userPasswordChange.newPassword == userPasswordChange.currentPassword || userPasswordChange.newPasswordRepeat == userPasswordChange.currentPassword){
        var optionalDelay = 5000;
        var $string = "Your new password can't be same as the current one";
        alertFactory.addAuto('danger', $string, optionalDelay);
        form2.newPasswordRepeat.$invalid = true;
        form2.newPassword.$invalid = true;
        form2.currentPassword.$invalid = true;
        return form2;
      }
      else {
        userPasswordChange.userId = $scope.userDetails.userId
        userPasswordChange.userName = $scope.userDetails.userName;
        var encoded = $base64.encode(JSON.stringify(userPasswordChange));
        $http.put('/api/people/updateUserPassword/',encoded).then(function(response)
        {
          var optionalDelay = 5000;
          var $string = "Successfully updated the password";
          $scope.readOnly = true;
          $scope.changePassword = false;
          $scope.userPasswordChange = {};
          form2.$setPristine();
          alertFactory.addAuto('success', $string, optionalDelay);
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "";
          if(!response.data){
            $string = "Error in updating the user details";
          }else{
            $string = response.data.error;
            form2.currentPassword.$invalid = true;
          }
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      }
    };
  });
  app.controller('projectParticipants', function TodoCtrl($scope, $element,$log, $state, $stateParams, $filter, $location, $http, alertFactory, $base64, $q, dataService, alertFactory, $mdDialog ) {
    var currentProject = $stateParams;
    $scope.showRemove = true;
    $scope.removeSelected = {};
    $scope.addBack = function(showRemove){
      $scope.showRemove = true;
      showRemove = true;
      return showRemove;
    }
    $scope.removeProjectParticipants = function(){
      var currentPeopleList = $scope.peopleList;
      var clearSelected  = $scope.list2;
      var remove = $scope.removeSelected;
      $http.get('/api/people/summary').then(function(response)
      {
        var people = response.data;
        $scope.peopleListCopy= people;
      }).then(function(){
        var list =  $scope.peopleListCopy;
        var splicelist =[];
        if($scope.list2){
          angular.forEach($scope.list2, function(value3, key3) {
            if(value3.personId && $scope.removeSelected[value3.personId] == true ){
              for (var k =  list.length - 1; k >= 0; k--){
                if(list[k].personId == value3.personId ){
                  $scope.peopleList.push(list[k]);
                  //clearSelected[key].splice(key3,1);
                  var tmp ={};
                  tmp.personId = value3.personId;
                  tmp.personName = value3.personName;
                  splicelist.push(tmp);
                  remove[list[k].personId] =false;
                  //$log.debug('Hello ' +list3[key3].personId + list[k].personName + '!');
                  //$log.debug('Hello ' +list3[key3].personId+ " asdasd "+key3+ "ff"+ '!');
                }
              };
            }
          });
        }
        $scope.countSelected = 0;
        angular.forEach(splicelist, function(value2, key2) {
          //delete $scope.list2[key][value2];
          //$scope.list2[key][value2] = 1;
          // $scope.showRemove = false;
          angular.forEach($scope.list2, function(value3, key3) {
            if(value2.personId == value3.personId && value2.personName == value3.personName ){
              //$log.debug('Hello ' +key3+ '!');
              clearSelected.splice(key3,1);
            }
          })
          //$scope.list2[key].remove(0,1,2,3,4);
        })
        $scope.countSelected = 	$scope.countSelected + clearSelected.length;
        if($scope.countSelected == 0){
          $scope.showRemove = false;
        }
        //return $scope.removeSelected = {};
      }).then(function(){
        $scope.peopleList = currentPeopleList;
        $scope.list2 = clearSelected;
        $scope.removeSelected =remove;
        //$scope.drop[$scope.selectedRole] = true;
      });
    };
    $http.get('/api/roles/rolesOfPeople').then(function(response)
    {
      var rolesOfPeople = response.data;
      $scope.rolesOfPeople= rolesOfPeople;
    })
    .catch(function(response, status) {
      //	$scope.loading = false;
      var optionalDelay = 5000;
      var $string = "Error in fetching roles of people";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
    $http.get('/api/roles/getRoles').then(function(response)
    {
      var roles = response.data;
      var rolesList = roles;
      $scope.rolesList= rolesList;
    })
    .catch(function(response, status) {
      //	$scope.loading = false;
      var optionalDelay = 5000;
      var $string = "Error in fetching roles";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
    $http.get('/api/people/summary').then(function(response)
    {
      var people = response.data;
      var peopleList = people;
      $scope.peopleList= peopleList;
    }).then(function(){
      $http.get('/api/projects/' + currentProject.projectId).then(function(response)
      {
        var finalProject =   response.data;
        var  projectStartDate = $filter('date')(finalProject.projectStartDate, 'yyyy/MM/dd');
        var  projectEndDate = $filter('date')(finalProject.projectEndDate, 'yyyy/MM/dd');
        finalProject.projectStartDate = projectStartDate;
        finalProject.projectEndDate = projectEndDate;
        finalProject.projectLeaderName = null;
        if($scope.peopleList.length != 0){
          angular.forEach($scope.peopleList, function(value, key) {
            if (value.personId == finalProject.projectLeader) {
              finalProject.projectLeaderName = value.personName;
            }
          });
        }
        $scope.finalProject= finalProject;
      })
      .catch(function(response, status) {
        //	$scope.loading = false;
        var optionalDelay = 5000;
        var $string = "Error in fetching project  details";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
    })
    .catch(function(response, status) {
      //	$scope.loading = false;
      var optionalDelay = 5000;
      var $string = "Error in fetching list of people";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
    $scope.list2 = [];
    //	$scope.demo = [{ "personId": 1, "personName": "sai datta Admin", "projects": [ { "projectId": 1, "projectName": "Project1", "sprints": [ { "sprintId": 4, "sprintName": "sprintlog", "issues": [ { "issueId": 7 }, { "issueId": 6 } ], "numberofIssues": 2 }, { "sprintId": 8, "sprintName": "demosprint", "issues": [ { "issueId": 0 }, { "issueId": 1 } ], "numberofIssues": 2 } ] } ], "roleId": "3", "jqyoui_pos": 1 } ];
    $scope.filterPeople = function() {
      roleName= $scope.selectedRole;
    }
    $scope.addProjectParticipants =function(participants){
      var ee = [];
      angular.forEach(participants, function(value, key) {
        var txt = "{\"personId\":"+value.personId+",\"roleId\":"+value.roleId+"}";
        var txt2 = angular.fromJson(txt);
        this.push(txt2);
      },ee );
      $scope.ree = angular.bind(this,ee);
      //$scope.jsn = angular.fromJson($scope.ree);
      //$scope.wee = allocatedPeople;
      $http.put('/api/projects/setProjectParticipants/'+currentProject.projectId,$scope.ree ).then(function(response) {
        $state.go("management.projects.projectsTable");
        var $string = "Successfully added the project participants";
        var optionalDelay = 3000;
        alertFactory.addAuto('success', $string, optionalDelay);
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = {};
        if(response.data.message !=null){
          $string = response.data.message;
        }else {
          $string = "Participants already added to the project";
        }
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
    };
    $scope.clearParticipants =function(ev, participants){
      var confirm = $mdDialog.confirm()
      .title('Would you like to clear all the project participants?')
      .textContent('This will also clear the project participants on Redmine')
      .ariaLabel('')
      .targetEvent(ev)
      .ok('Proceed!')
      .cancel('Cancel');
      $mdDialog.show(confirm).then(function() {
        angular.forEach(participants, function(value, key) {
          this.push(value);
        },$scope.peopleList);
        $scope.list2= [];
      });
    };
    $scope.manageProject = function(project) {
      $state.go("management.projects.editProject",project );
    };
  });
  (function () {
    'use strict';
    angular
    .module('appCtrl')
    .factory('AuthenticationService', Service);
    function Service($http, $localStorage, $base64, alertFactory, $window) {
      var service = {};
      service.Login = Login;
      service.Logout = Logout;
      return service;
      function Login(username, password, callback) {
        var encoded = $base64.encode(username + ':' + password);
        // $http.post('/api/authenticate', { username: username, password: password })   dnZrc2RhdHRhQGdtYWlsLmNvbTpwYXNzd29yZDEyMw==
        $http({
          method: 'GET',
          url: '/api/auth/login',
          headers: { 'X-Requested-With' : 'XMLHttpRequest', 'Authorization': 'Basic ' + encoded}
        })
        .then(function (response) {
          // login successful if there's a token in the response
          if (response.data.token) {
            // store username and token in local storage to keep user logged in between page refreshes
            $localStorage.currentUser = { userFirstName: response.data.userFirstName, userId: response.data.userId, token: response.data.token };
            // add jwt token to auth header for all requests made by the $http service
            $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
            // execute callback with true to indicate successful login
            callback(true);
          } else {
            //$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
            // execute callback with false to indicate failed login
            callback(false);
          }
        }, function errorCallback(response) {
          if (response.status = '401'){
            callback(false);
          }
          // called asynchronously if an error occurs
          // or server returns response with an error status.
        });
      }
      function Logout() {
        // remove user from local storage and clear http auth header
        delete $localStorage.currentUser;
        $http.defaults.headers.common.Authorization = '';
      }
    }
  })();
  app.controller('NavController', function($state, $http, alertFactory, $q ,$scope, $localStorage, $location, $window) {
    if($localStorage.currentUser){
      $scope.userFirstName = $localStorage.currentUser.userFirstName;
      $scope.currentUserId = $localStorage.currentUser.userId;
    }
    $scope.synchronize = function() {
      $scope.loading = true;
      $http.get('/api/redmine').then(function(response) {
        $scope.loading = false;
        var $string = "Hurray! Successfully synchronized with Redmine!";
        var optionalDelay = 3000;
        $state.reload();
        alertFactory.addAuto('success', $string, optionalDelay);
      })
      .catch(function(response, status) {
        $scope.loading = false;
        var optionalDelay = 5000;
        var $string = "Error in synchronizing with redmine";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
    }
  });
  app.controller('LoginIndexController', function($base64, $state, $http, $q ,$scope, dataService, alertFactory, $location, AuthenticationService) {
    var vm = this;
    vm.login = login;
    initController();
    function initController() {
      // reset login status
      AuthenticationService.Logout();
    };
    function login() {
      vm.loading = true;
      AuthenticationService.Login(vm.username, vm.password, function (result) {
        if (result === true) {
          var optionalDelay = 2000;
          var $string = "Login successful!";
          alertFactory.addAuto('success', $string, optionalDelay);
          $location.path('/');
        } else {
          vm.username = null;
          vm.password = null;
          $scope.form.$setPristine();
          $scope.form.$setUntouched();
          $scope.form.$rollbackViewValue();
          //vm.error = 'Username or password is incorrect';
          var optionalDelay = 5000;
          var $string = "Username or password is incorrect!";
          alertFactory.addAuto('Danger', $string, optionalDelay);
          vm.loading = false;
        }
      });
    };
  });
  app.filter('capitalize', function() {
    return function(text) {
      return (!!text) ? text.charAt(0).toUpperCase() + text.substr(1).toLowerCase() : '';
    }
  });
  function RootCtrl($rootScope, $location, alertService) {
    $rootScope.changeView = function(view) {
      $location.path(view);
    }
    // root binding for alertService
    $rootScope.closeAlert = alertService.closeAlert;
  }
  RootCtrl.$inject = ['$scope', '$location', 'alertService'];
  app.controller('sprintsTableCtrl', function($scope, $state, $location, $http, alertFactory, $base64, $q, dataService, alertFactory,  $localStorage) {
    refreshSprintsSummary();
    $scope.manageSprint = function(sprint) {
      $state.go("management.sprints.editSprint.existingSprint",sprint );
    };
    function refreshSprintsSummary() {
      $http.get('/api/sprints/summary').then(function(response) {
        $scope.sprints= response.data;
      });
    }
  });
  app.controller('sprintSummaryCtrl', function($scope, $state, $location, $http, alertFactory, $base64, $q, dataService, alertFactory,  $localStorage) {
    $http.get('/api/sprints/summary').then(function(response) {
      $scope.sprints= response.data;
    }).catch(function(response, status) {
      var optionalDelay = 5000;
      var $string = "Error in fetching summary of sprints";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
  });
  app.controller('projectSummaryCtrl', function($scope, $state, $location, $http, alertFactory, $base64, $q, dataService, alertFactory,  $localStorage) {
    var parentProjectsList = [];
    $http.get('/api/projects/summary').then(function(response) {
      $scope.projects = response.data;
    }).then(function(){
      $http.get('/api/projects/getAllProjectsNested').then(function(response)
      {
        var ProjectsHierarchy = response.data;
        $scope.ProjectsHierarchy = ProjectsHierarchy;
      }). then(function(){
        $scope.getParentOf = function(project, parentProjectDetails){
          angular.forEach($scope.projects, function(value, key) {
            if(value.projectId == project.parentProjectId){
              parentProjectDetails.projectId = value.projectId;
              parentProjectDetails.projectName = value.projectName;
              parentProjectDetails.parentProjectId = value.parentProjectId;
            }
          });
        }
        function existsInArray( arr, item ) {
          for( var i = 0; i < arr.length; i++ )
          if( arr[ i ].projectId === item.projectId ) return true;
          return false;
        }
        $scope.getParentProjects = function(project){
          angular.forEach($scope.ProjectsHierarchy, function(value, key) {
            if(value.projectId == project.projectId){
              return;
            }
          });
          var projectsList = [];
          projectsList.push(project);
          var currentProject = project;
          while(!existsInArray( $scope.ProjectsHierarchy, currentProject ) ){
            var parentProjectDetails = {};
            $scope.getParentOf(currentProject, parentProjectDetails);
            projectsList.push(parentProjectDetails);
            if(parentProjectDetails.parentProjectId != null){
              currentProject = parentProjectDetails;
            }else{
              break;
            }
          }
          return 	projectsList;
        }
      }).then(function(){
        angular.forEach($scope.projects, function(value,key){
          parentProjectsList[value.projectId] = $scope.getParentProjects(value);
        });
        $scope.parentProjectsList = parentProjectsList;
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching list of projects";
        alertFactory.addAuto('danger', $string, optionalDelay);
      })
    }) .catch(function(response, status) {
      var optionalDelay = 5000;
      var $string = "Error in fetching summary of projects";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
  });
  app.controller('peopleSummaryCtrl', function($scope, $state, $location, $http, alertFactory, $base64, $q, dataService, alertFactory,  $localStorage) {
    $http.get('/api/people/summary').then(function(response) {
      $scope.people= response.data;
    }).catch(function(response, status) {
      var optionalDelay = 5000;
      var $string = "Error in fetching summary of people";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
  });
  app.controller('projectManagementCtrl', function($scope, $state, $location, $http, alertFactory, $base64, $q, dataService, alertFactory,  $localStorage, $log) {
    var parentProjectsList = [];
    $http.get('/api/projects/summary').then(function(response) {
      $scope.projects = response.data;
    }).then(function(){
      $http.get('/api/projects/getAllProjectsNested').then(function(response)
      {
        var ProjectsHierarchy = response.data;
        $scope.ProjectsHierarchy = ProjectsHierarchy;
      }). then(function(){
        $scope.getParentOf = function(project, parentProjectDetails){
          angular.forEach($scope.projects, function(value, key) {
            if(value.projectId == project.parentProjectId){
              parentProjectDetails.projectId = value.projectId;
              parentProjectDetails.projectName = value.projectName;
              parentProjectDetails.parentProjectId = value.parentProjectId;
            }
          });
        }
        function existsInArray( arr, item ) {
          for( var i = 0; i < arr.length; i++ )
          if( arr[ i ].projectId === item.projectId ) return true;
          return false;
        }
        $scope.getParentProjects = function(project){
          angular.forEach($scope.ProjectsHierarchy, function(value, key) {
            if(value.projectId == project.projectId){
              return;
            }
          });
          var projectsList = [];
          projectsList.push(project);
          var currentProject = project;
          while(!existsInArray( $scope.ProjectsHierarchy, currentProject ) ){
            var parentProjectDetails = {};
            $scope.getParentOf(currentProject, parentProjectDetails);
            projectsList.push(parentProjectDetails);
            if(parentProjectDetails.parentProjectId != null){
              currentProject = parentProjectDetails;
            }else{
              break;
            }
          }
          return 	projectsList;
        }
      }).then(function(){
        angular.forEach($scope.projects, function(value,key){
          parentProjectsList[value.projectId] = $scope.getParentProjects(value);
        });
        $scope.parentProjectsList = parentProjectsList;
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching list of projects";
        alertFactory.addAuto('danger', $string, optionalDelay);
      })
    }) .catch(function(response, status) {
      var optionalDelay = 5000;
      var $string = "Error in fetching summary of projects";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
    $scope.manageProject = function(project) {
      $state.go("management.projects.editProject",project );
    }
    $scope.manageProjectWithId = function(id) {
      var project = {};
      project.projectId= id;
      $state.go("management.projects.editProject",project );
    }
  });
  app.controller('addProjectCtrl', function($scope, $state, $location, $http, alertFactory, $base64, $q, dataService, alertFactory,  $localStorage) {
    $scope.formatProjects = function(projectsList, currentProject, indent){
      for(var i=0; i<currentProject.projectList.length; i++){
        var project = {};
        project.projectId = currentProject.projectList[i].projectId;
        project.projectName = " - ".repeat(indent)+currentProject.projectList[i].projectName;
        projectsList.push(project);
        var updatedProject = currentProject.projectList[i];
        if(updatedProject.projectList !=null && updatedProject.projectList.length !=0){
          indent++;
          $scope.formatProjects(projectsList, updatedProject, indent);
          indent--;
        }
      }
    };
    $http.get('/api/projects/getAllProjectsNested').then(function(response)
    {
      var projects = response.data;
      var projectsList = [];
      angular.forEach(projects, function(value, key) {
        var project = {};
        project.projectId = value.projectId;
        project.projectName = value.projectName;
        projectsList.push(project);
        if(value.projectList !=null && value.projectList.length !=0){
          var indent = 1;
          $scope.formatProjects(projectsList, value, indent);
        }
      });
      $scope.projectsList = projectsList;
    })
    .catch(function(response, status) {
      var optionalDelay = 5000;
      var $string = "Error in fetching list of projects";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
    $http.get('/api/people/getAllPeople').then(function(response)
    {
      $scope.peopleList = response.data;
    }).catch(function(response, status) {
      var optionalDelay = 5000;
      var $string = "Error in fetching list of people";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
    $scope.projectStartDate = new Date();
    $scope.minDate = new Date(
      $scope.projectStartDate.getFullYear(),
      $scope.projectStartDate.getMonth(),
      $scope.projectStartDate.getDate()
    );
    $scope.maxDate = new Date(
      $scope.projectStartDate.getFullYear(),
      $scope.projectStartDate.getMonth() + 3,
      $scope.projectStartDate.getDate()
    );
    $scope.minDate2 = $scope.projectStartDate;
    $scope.onlyWeekdaysPredicate = function(date) {
      var day = date.getDay();
      return day === 1 || day === 2 || day  ==3 || day == 4 || day ==5;
    };
    $scope.createProject = function(project) {
      project.projectUpdatedBy = $localStorage.currentUser.userFirstName;
      $http.put('/api/projects/newproject',project).then(function(response) {
        var createdProject = response.data;
        $state.go("management.projects.addProject.participants",createdProject);
        var $string = "Successfully created a new project!";
        var optionalDelay = 3000;
        alertFactory.addAuto('success', $string, optionalDelay);
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in creating a new project";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
    };
  });
  app.controller('peopleEditCtrl', function($scope, $state, $location, $http, alertFactory, $base64, $q, dataService, alertFactory,  $localStorage, $stateParams, $log, $window) {
    var currentPerson = $stateParams;
    $scope.userId = currentPerson.personId;
    var tabClasses;
    function initTabs() {
      tabClasses = ["","","",""];
    }
    $scope.getTabClass = function (tabNum) {
      return tabClasses[tabNum];
    };
    $scope.getTabPaneClass = function (tabNum) {
      return "tab-pane " + tabClasses[tabNum];
    }
    $scope.setActiveTab = function (tabNum) {
      initTabs();
      tabClasses[tabNum] = "active";
    };
    initTabs();
    //$scope.setActiveTab(2);
    $scope.formData = {};
    if($scope.userId != null){
      $http.get('/api/roles/getRoles').then(function(response)
      {
        $scope.rolesList= response.data;
      })
      $http.get('/api/people/getPersonNameRoles/'+$scope.userId).then(function(response)
      {
        var person = {};
        person.firstName = response.data.firstName;
        person.lastName = response.data.lastName;
        person.emailID = response.data.emailID;
        person.personId =  $scope.userId;
        $scope.formData.selectedRoles = {};
        $scope.person = person;
        var roles = response.data.roles;
        if(roles != null){
          for (var i =  roles.length - 1; i >= 0; i--) {
            if(roles[i].roleName != null){
              $scope.formData.selectedRoles[roles[i].roleName] = true;
            }
          }
        }
      }).then(function(){
        $scope.someSelected = function (object) {
          return Object.keys(object).some(function (key) {
            return object[key];
          });
        }
      });
    }
    $scope.addPerson = function(person, readOnly){
      if(readOnly != true){
        var newRoleName = $scope.formData.selectedRoles ;
        var ee =[];
        for (var i =  $scope.rolesList.length - 1; i >= 0; i--) {
          angular.forEach($scope.formData.selectedRoles, function(value2, key2) {
            if ($scope.rolesList[i].roleName == key2) {
              if (value2 == true) {
                var txt = "{\"roleId\":"+$scope.rolesList[i].roleId+",\"roleName\":\""+$scope.rolesList[i].roleName+"\"}";
                var txt2 = angular.fromJson(txt);
                this.push(txt2);
              }
            }
          },ee );
        }
        person.roles = angular.bind(this,ee);
        //$scope.newPerson = person;
        $http.put('/api/people/updatePerson',person).then(function(response) {
          var updatedPerson = response.data;
          $state.go("management.people.editPerson.editCapabilities", updatedPerson);
          var $string = "Successfully updated " +person.firstName;
          var optionalDelay = 3000;
          alertFactory.addAuto('success', $string, optionalDelay);
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "Error in updating person. "+ response.data.errors[0]+"!";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      }
      else{
        $state.go("management.people.editPerson.editCapabilities", person);
      }
    }
  });
  app.controller('editSprintQuestionnaireCtrl', function TodoCtrl($scope, $state, $filter, $http, $q, dataService, alertFactory, $localStorage, $stateParams, $log, $window ) {
    $scope.projectId = $stateParams.projectId;
    $scope.sprintId = $stateParams.sprintId;
    $http.get('/api/sprints/getQuestionnaire/'+$scope.sprintId+'/'+$scope.projectId).then(function(response)
    {
      if(response.data.length!=0){
        $scope.questionnaire= response.data[0];
        $scope.lastUpdate = $filter('date')($scope.questionnaire.lastUpdate, 'yyyy/MM/dd');
      }
    }).then(function(){
      $http.get('api/sprints/existingSprintDetails/'+$scope.sprintId+'/'+$scope.projectId).then(function(response)
      {
        $scope.sprintDetails = response.data;
      })
    }).then(function(){
      $scope.$broadcast('rzSliderForceRender');
      //cope.questionnaire.question1Option  = 'Highly underestimated';
      $scope.q1 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Highly underestimated', legend: 'Highly underestimated'},
            {value: 'Underestimated', legend: 'Underestimated'},
            {value: 'Accurately estimated', legend: 'Accurately estimated'},
            {value: 'Overestimated', legend: 'Overestimated'},
            {value: 'Highly overestimated', legend: 'Highly overestimated'}
          ]
        }
      };
      $scope.q2 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Superficial', legend: 'Superficial'},
            {value: 'Satisfactory', legend: 'Satisfactory'},
            {value: 'Good', legend: 'Good'},
            {value: 'Excellent', legend: 'Excellent'},
            {value: 'Perfect', legend: 'Perfect'}
          ]
        }
      };
      $scope.q3 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Not accurate', legend: 'Not accurate'},
            {value: 'Superficial', legend: 'Superficial'},
            {value: 'Satisfactory', legend: 'Satisfactory'},
            {value: 'Good', legend: 'Good'},
            {value: 'Highly accurate', legend: 'Highly accurate'},
          ]
        }
      };
      $scope.q4 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'Low', legend: 'Low'},
            {value: 'Medium', legend: 'Medium'},
            {value: 'High', legend: 'High'},
            {value: 'Very high', legend: 'Very high'}
          ]
        }
      };
      $scope.q5 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'Low', legend: 'Low'},
            {value: 'Medium', legend: 'Medium'},
            {value: 'High', legend: 'High'},
            {value: 'Very high', legend: 'Very high'}
          ]
        }
      };
      $scope.q6 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Very Low', legend: 'Very Low'},
            {value: 'Low', legend: 'Low'},
            {value: 'Medium', legend: 'Medium'},
            {value: 'High', legend: 'High'},
            {value: 'Very high', legend: 'Very high'}
          ]
        }
      };
      $scope.q7 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Very Low', legend: 'Very Low'},
            {value: 'Low', legend: 'Low'},
            {value: 'Medium', legend: 'Medium'},
            {value: 'High', legend: 'High'},
            {value: 'Very high', legend: 'Very high'}
          ]
        }
      };
      $scope.q8 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Uncertain', legend: 'Uncertain'},
            {value: 'Non-problematic', legend: 'Non-problematic'},
            {value: 'Slightly problematic', legend: 'Slightly problematic'},
            {value: 'Significantly problematic', legend: 'Significantly problematic'},
            {value: 'Highly problematic', legend: 'Highly problematic'}
          ]
        }
      };
      $scope.saveQuestionnaire = function(questionnaire){
        questionnaire.updatedBy = $localStorage.currentUser.userFirstName;
        questionnaire.projectId = $scope.projectId ;
        questionnaire.sprintId = $scope.sprintId;
        $log.debug("sfgasdfgsdfg");
        $http.post('/api/sprints/updateQuestionnaire',questionnaire).then(function(response)
        {
          var optionalDelay = 3000;
          var $string = "Updated the questionnaire";
          $state.go("management.sprints.editSprint.existingSprint",response.data);
          alertFactory.addAuto('success', $string, optionalDelay);
        })
        .catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in updating the questionnaire";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      }
    });
    angular.element(document).ready(function () {
      $scope.$broadcast('rzSliderForceRender');
    });
  });
  app.controller('peopleToSprintCtrl', function($scope, $state, $timeout, $filter, $http, $q, dataService, alertFactory, $localStorage, $stateParams, $log, $window, $mdDialog) {
    $scope.projectId = $stateParams.projectId;
    $scope.sprintId = $stateParams.sprintId;
    if($stateParams.selectedRoles){
      $scope.selectedRoles = $stateParams.selectedRoles;
    }
    $scope.list2 = [];
    $scope.drop = [];
    $scope.removeSelected = {};
    $scope.showRemove = false;
    $scope.numberOfParticipants =0;
    $scope.formData1 = {};
    $scope.formData1.selectedRoles = {};
    $scope.someSelected = function (object) {
      return Object.keys(object).some(function (key) {
        return object[key];
      });
    }
    $http.get('/api/projects/getNumberofParticipants/'+$scope.projectId).then(function(response)
    {
      $scope.projectRolesList= response.data;
    }).catch(function(response, status) {
      //	$scope.loading = false;
      var optionalDelay = 5000;
      var $string = "Error in fetching roles";
      alertFactory.addAuto('danger', $string, optionalDelay);
    }).then(function(){
      $scope.clearForm = function(formName){
        formName.$setPristine();
        $scope.formData1.selectedRoles = {};
        $scope.maxError=false;
      }
      $scope.change= function(form1, role, maxNumber){
        $scope.maxError  = null;
        var keepGoing = true;
        angular.forEach(form1,function(value,key){
          if(key==role && keepGoing){
            angular.forEach(value.$error,function(value2,key2){
              //if(key2=="min" && value2==true ){
              //return	$scope.minError ="Participants value can't be negative";
              //}
              if(key2=="max" && value2==true){
                keepGoing = false;
                return $scope.maxError ="Number of "+role+"s can't be more than "+maxNumber;
                //$log.debug("max is "+JSON.stringify(value2));
              }
            })
          }
        });
        //var tmp = form1.$name+'.'+role
        //$log.debug(JSON.stringify(tmp));
      }
      $scope.increment = function(val,max){
        if($scope.formData1.selectedRoles[val] == null){
          $scope.formData1.selectedRoles[val] = 0;
        }
        if($scope.formData1.selectedRoles[val] < max){
          $scope.formData1.selectedRoles[val] = ($scope.formData1.selectedRoles[val] +1);
        }
      }
      $scope.decrement = function(val,max){
        if($scope.formData1.selectedRoles[val] != null && $scope.formData1.selectedRoles[val] > 0 ){
          if($scope.formData1.selectedRoles[val] <= max){
            $scope.formData1.selectedRoles[val] = ($scope.formData1.selectedRoles[val] -1);
          }
        }
        if($scope.formData1.selectedRoles[val] == null){
          $scope.formData1.selectedRoles[val] = max;
        }
      }
      $scope.createSprintRoles =function(formName){
        angular.forEach($scope.formData1.selectedRoles, function(val,key){
          $scope.selectedRoles[key] = ($scope.selectedRoles[key] +val);
        });
        $scope.clearForm(formName);
      }
    });
    $http.put('/api/projects/rolesOfPeopleInProject', $scope.projectId ).then(function(response)
    {
      var rolesOfPeople = response.data;
      $scope.rolesOfPeople= rolesOfPeople;
    }).then (function(){
      $http.put('/api/people/summaryOfPeopleInProject', $scope.projectId ).then(function(response)
      {
        var people = response.data;
        $scope.peopleList= people;
      })
      .then( function(){
        if($scope.selectedRoles){
          angular.forEach($scope.selectedRoles, function(value,key){
            $scope.list2[key] = [];
            $scope.drop[key] = false;
          });
          $http.get('/api/sprints/getSprintParticipants/'+$scope.sprintId+'/'+$scope.projectId).then(function(response)
          {
            $scope.sprintParticipants= response.data;
            angular.forEach($scope.sprintParticipants, function(val,key){
              $scope.list2[val.roleName] = [];
              for(var i = 0; i < $scope.peopleList.length; i++ ){
                if( $scope.peopleList[i].personId == val.personId ){
                  $log.debug("person is"+ $scope.peopleList[i].personName);
                  $scope.peopleList.splice(i,1);
                  i =0;
                }
              };
            });
            angular.forEach($scope.sprintParticipants, function(val,key){
              $scope.selectedRoles[val.roleName] = ($scope.selectedRoles[val.roleName] +1);
              $scope.list2[val.roleName].push(val);
            });
            angular.forEach($scope.sprintParticipants, function(val,key){
              $scope.limitEntry($scope.list2[val.roleName].length,key,val.roleName);
            });
          });
        }
        else if(!$scope.selectedRoles){
          $http.get('/api/sprints/getSprintParticipants/'+$scope.sprintId+'/'+$scope.projectId).then(function(response)
          {
            if(response.data.length==0){
              var optionalDelay = 7000;
              var $string = "Redirected due to no sprint participants or page refresh.Do not refresh while selecting people";
              $state.go('management.sprints.editSprint.existingSprint', {projectId: $scope.projectId , sprintId: $scope.sprintId});
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
            else{
              $scope.sprintParticipants= response.data;
              $scope.selectedRoles = {};
              angular.forEach($scope.sprintParticipants, function(val,key){
                $scope.selectedRoles[val.roleName] = 0;
                $scope.list2[val.roleName] = [];
                $scope.drop[val.roleName] = true;
                for(var i = 0; i < $scope.peopleList.length; i++ ){
                  if( $scope.peopleList[i].personId == val.personId ){
                    $log.debug("person is"+ $scope.peopleList[i].personName);
                    $scope.peopleList.splice(i,1);
                    i =0;
                  }
                };
              });
              angular.forEach($scope.sprintParticipants, function(val,key){
                $scope.selectedRoles[val.roleName] = ($scope.selectedRoles[val.roleName] +1);
                $scope.list2[val.roleName].push(val);
              });
              angular.forEach($scope.sprintParticipants, function(val,key){
                $scope.limitEntry($scope.list2[val.roleName].length,key,val.roleName);
              });
            }
          });
        }
        $scope.limitEntry = function(length,value,key){
          $scope.showRemove = true;
          $scope.numberOfParticipants = $scope.numberOfParticipants +	length;
          if((length+1)>value){
            return $scope.drop[key] = false;
          }
        }
        $http.get('/api/roles/getRoles').then(function(response)
        {
          $scope.rolesList= response.data;
        })
        .catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching list of roles";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
        $http.get('/api/capabilities/getCapabilitiesList').then(function(response)
        {
          $scope.allCapabilitiesList = response.data;
        }).catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching list of capabilities";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
        $http.get('/api/skills/getProgrammingSkillsList').then(function(response)
        {
          $scope.allProgrammingSkillsList = response.data;
        }).catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching list of programming skills";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
        $scope.manageSprint = function(sprint) {
          $state.go("management.sprints.editSprint.existingSprint",sprint );
        };
        $scope.setGraph = function(person){
          $scope.showOverviewGraph = true;
          $scope.programmingSkillsOverviewGraph = false;
          $scope.programmingSkillsTimelineGraph = false;
          $scope.capabilitiesTimelineGraph = false;
          $scope.capabilitiesCompareGraph = false;
          $scope.skillsCompareGraph =false;
          $scope.selectedSkill = null;
          $scope.selectedCapability = null;
          return person.personId;
        }
        $scope.setProgrammingSkillsOverviewGraph = function(person){
          $scope.showOverviewGraph = false;
          $scope.programmingSkillsOverviewGraph = true;
          $scope.capabilitiesTimelineGraph = false;
          $scope.programmingSkillsTimelineGraph = false;
          $scope.capabilitiesCompareGraph = false;
          $scope.skillsCompareGraph =false;
          $scope.selectedSkill = null;
          $scope.selectedCapability = null;
          return person.personId;
        }
        $scope.showProgrammingSkillsOverview = function(person){
          var personProgrammingskills = [];
          $http.get('/api/skills/getProgrammingSkllsOfPerson/'+person.personId).then(function(response)
          {
            if(response.data.length !=0){
              personProgrammingskills = response.data;
              $scope.setProgrammingSkillsOverviewGraph(person);
            }
            else{
              if( $scope.programmingSkillsOverviewGraph == true){
                $scope.programmingSkillsOverviewGraph = false;
              }
              $scope.showOverviewGraph = false;
              $scope.programmingSkillsTimelineGraph = false;
              $scope.capabilitiesTimelineGraph = false;
              var optionalDelay = 5000;
              var $string = person.personName+"'s programming skills haven't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          }).then(function(){
            $scope.personSelected = person;
            $scope.labels = [];
            //$scope.series = ['Series A'];
            $scope.data = [];
            $scope.colors = [];
            $scope.skillOverviewColors = [];
            //$scope.personSelected = person;
            $scope.skillOverviewOptions =  {
              maintainAspectRatio: false,
              tooltips: {
                callbacks: {
                  label: function(tooltipItem, data) {
                    var label = null;
                    if(tooltipItem.xLabel=='0'){
                      label = 'Undefined';
                    }
                    if(tooltipItem.xLabel=='25'){
                      label = 'Average';
                    }
                    if(tooltipItem.xLabel=='50'){
                      label = 'Good';
                    }
                    if(tooltipItem.xLabel=='75'){
                      label = 'High';
                    }
                    if(tooltipItem.xLabel=='100'){
                      label = 'Very High';
                    }
                    //return tooltipItem.yLabel + ' : ' + label;
                    return "Proficiency" + ' : ' + label;
                  },
                }
              },
              legend: {
                display: false,
                position: "bottom",
                onHover: function(event, legendItem) {
                  document.getElementById("bar").style.cursor = 'pointer';
                },
              },
              scales: {
                xAxes: [{  ticks: {min: 0, max:100}, scaleLabel: {
                  display: true,
                }}],
              }
            }
            angular.forEach(personProgrammingskills, function(value,key){
              if(value.proficiency == "Undefined" ){
              }
              else if(value.proficiency == "Average"){
                $scope.data.push('25');
                $scope.skillOverviewColors.push('#fdbb84');
                $scope.labels.push(value.skillName);
              }
              else if(value.proficiency == "Good" ){
                $scope.data.push('50');
                $scope.skillOverviewColors.push('#de9226');
                $scope.labels.push(value.skillName);
              }
              else if(value.proficiency == "High"){
                $scope.data.push('75');
                $scope.skillOverviewColors.push('#a3eca3');
                $scope.labels.push(value.skillName);
              }
              else {
                $scope.data.push('100');
                $scope.skillOverviewColors.push('#1b7c1b');
                $scope.labels.push(value.skillName);
              }
              var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
              var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
              $scope.capabilitiesLastUpdatedDate  = skillUpdatedAgo +" on "+skillLastUpdatedDate;
              $scope.capabilitiesLastUpdatedBy = value.updatedBy;
            })
          });
        }
        $scope.setProgrammingSkillsTimelineGraph = function(person){
          $scope.showOverviewGraph = false;
          $scope.programmingSkillsOverviewGraph = false;
          $scope.capabilitiesTimelineGraph = false;
          $scope.capabilitiesCompareGraph = false;
          $scope.skillsCompareGraph =false;
          $scope.programmingSkillsTimelineGraph = true;
          $scope.selectedCapability = null;
          return person.personId;
        }
        $scope.programmingSkillsTimelineDiv = function(person){
          $http.get('/api/skills/getProgrammingSkllsRecorded/'+person.personId).then(function(response)
          {
            $scope.programmingSkillsList = response.data;
          }).then(function(){
            $scope.showOverviewGraph = false;
            $scope.programmingSkillsOverviewGraph = false;
            $scope.programmingSkillsTimelineGraph = true;
            $scope.capabilitiesTimelineGraph = false;
            if($scope.selectedSkill == null){
              $scope.lineLabels = [];
              $scope.lineSeries = [];
              $scope.lineData = [];
              $scope.dataValues = [];
            }
            if($scope.programmingSkillsList.length ==0){
              if($scope.programmingSkillsTimelineGraph == true){
                $scope.programmingSkillsTimelineGraph = false;
              }
              $scope.programmingSkillsOverviewGraph = false;
              $scope.showOverviewGraph = false;
              $scope.capabilitiesTimelineGraph = false;
              var optionalDelay = 5000;
              var $string = person.personName+"'s programming skills haven't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          });
        }
        $scope.showProgrammingSkillsTimeline = function(person, skill){
          var personProgrammingskills = [];
          $http.get('/api/skills/getProgrammingSkllsTimeline/'+person.personId+'/'+skill.skillId).then(function(response)
          {
            if(response.data.length !=0){
              personProgrammingskills = response.data;
              $scope.setProgrammingSkillsTimelineGraph(person);
            }
            else{
              if( $scope.programmingSkillsTimelineGraph == true){
                $scope.programmingSkillsTimelineGraph = false;
              }
              $scope.programmingSkillsOverviewGraph = false;
              $scope.showOverviewGraph = false;
              $scope.capabilitiesTimelineGraph = false;
              $scope.skillsCompareGraph =false;
            }
          }).then(function(){
            $scope.selectedSkill = skill.skillName;
            $scope.personSelected = person;
            $scope.lineLabels = [];
            $scope.lineSeries = [];
            $scope.lineData = [];
            $scope.dataValues = [];
            $scope.lineColors = ['#46BFBD'];
            $scope.lineSeries.push(skill.skillName);
            $scope.skillsoptions =  {
              maintainAspectRatio: false,
              tooltips: {
                callbacks: {
                  label: function(tooltipItem, data) {
                    var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
                    var label = null;
                    if(tooltipItem.yLabel=='0'){
                      label = 'Undefined';
                    }
                    if(tooltipItem.yLabel=='25'){
                      label = 'Average';
                    }
                    if(tooltipItem.yLabel=='50'){
                      label = 'Good';
                    }
                    if(tooltipItem.yLabel=='75'){
                      label = 'High';
                    }
                    if(tooltipItem.yLabel=='100'){
                      label = 'Very High';
                    }
                    return datasetLabel + ' : ' + label;
                  },
                  title: function(array, data) {
                    var value1 = null;
                    angular.forEach(array,function(value,key){
                      value.xLabel = moment(value.xLabel).format('MMM Do YYYY');
                      value1 = value;
                    });
                    return value1.xLabel;
                  },
                }
              },elements: {
                line: {
                  tension: 0
                }
              },
              legend: {
                display: false,
              },
              scales: {
                yAxes: [{id: 'y-axis-1', type: 'linear', position: 'left', ticks: {min: 0, max:100}, scaleLabel: {
                  display: true,
                  labelString: "Proficiency in "+skill.skillName,
                  //labelString: "Proficiency",
                }}],
                xAxes: [{
                  type: 'time',
                  display: true,
                  scaleLabel: {
                    display: false,
                  }
                }],
              }
            };
            //$scope.personSelected = person;
            angular.forEach(personProgrammingskills, function(value,key){
              var dateLabel = new Date(value.lastUpdate);
              $scope.lineLabels.push(dateLabel);
              if(value.proficiency == "Undefined" ){
              }
              else if(value.proficiency == "Average"){
                $scope.dataValues.push('25');
                //$scope.lineColors.push('#46BFBD');
              }
              else if(value.proficiency == "Good" ){
                $scope.dataValues.push('50');
                //$scope.colors.push('#97bbcd');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              else if(value.proficiency == "High"){
                $scope.dataValues.push('75');
                //$scope.colors.push('#FDB45C');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              else {
                $scope.dataValues.push('100');
                //$scope.colors.push('#46BFBD');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
              var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
              $scope.skillLastUpdatedDate = skillUpdatedAgo +" on "+skillLastUpdatedDate;
            })
            $scope.lineData.push($scope.dataValues);
          });
        }
        $scope.setCapabilitiesTimeline = function(person){
          $scope.showOverviewGraph = false;
          $scope.programmingSkillsOverviewGraph = false;
          $scope.programmingSkillsTimelineGraph = false;
          $scope.capabilitiesCompareGraph = false;
          $scope.skillsCompareGraph =false;
          $scope.capabilitiesTimelineGraph = true;
          $scope.selectedSkill = null;
          return person.personId;
        }
        $scope.capabilitiesTimelineDiv = function(person){
          $http.get('/api/capabilities/getCapabilitiesList').then(function(response)
          {
            $scope.capabilitiesList = response.data;
          }).then(function(){
            $scope.showOverviewGraph = false;
            $scope.skillsCompareGraph =false;
            $scope.programmingSkillsTimelineGraph = false;
            $scope.capabilitiesCompareGraph = false;
            $scope.programmingSkillsCompareGraph = false;
            $scope.capabilitiesTimelineGraph = true;
            $scope.programmingSkillsOverviewGraph = false;
            if($scope.selectedCapability == null){
              $scope.capabilitylineLabels = [];
              $scope.capabilitylineSeries = [];
              $scope.capabilitylineData = [];
              $scope.capabilitydataValues = [];
              $scope.capabilityMeasures = null;
            }
            if($scope.capabilitiesList.length ==0){
              $scope.capabilitiesTimelineGraph  = false;
              var optionalDelay = 5000;
              var $string = "List of capabilities haven't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          });
        }
        $scope.showCapabilitiesTimeline = function(person, capability){
          var personCapabilities = [];
          $http.get('/api/capabilities/getCapabilitiesTimeline/'+person.personId+'/'+capability.capabilityId).then(function(response)
          {
            if(response.data.length !=0){
              personCapabilities = response.data;
              $scope.setCapabilitiesTimeline(person);
            }
            else{
              if( $scope.capabilitiesTimelineGraph == true){
                $scope.capabilitiesTimelineGraph = false;
              }
              $scope.programmingSkillsOverviewGraph = false;
              $scope.showOverviewGraph = false;
              $scope.programmingSkillsTimelineGraph = false;
              var optionalDelay = 5000;
              var $string = person.personName+"'s capabilities haven't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          }).then(function(){
            $http.get('/api/capabilities/getCapabilityMeasures/'+capability.capabilityId).then(function(response)
            {
              $scope.capabilityMeasures = response.data;
            });
          }).then(function(){
            $scope.selectedCapability = capability;
            $scope.personSelected = person;
            $scope.capabilitylineLabels = [];
            $scope.capabilitylineSeries = [];
            $scope.capabilitylineData = [];
            $scope.capabilitydataValues = [];
            $scope.capabilitylineColors = ['#46BFBD'];
            $scope.capabilitylineSeries.push(capability.capabilityName);
            $scope.capabilityoptions =  {
              maintainAspectRatio: false,
              tooltips: {
                callbacks: {
                  label: function(tooltipItem, data) {
                    var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
                    var label = null;
                    if(tooltipItem.yLabel=='0'){
                      label = 'Superficial';
                    }
                    if(tooltipItem.yLabel=='25'){
                      label = 'Satisfactory';
                    }
                    if(tooltipItem.yLabel=='50'){
                      label = 'Good';
                    }
                    if(tooltipItem.yLabel=='75'){
                      label = 'Excellent';
                    }
                    if(tooltipItem.yLabel=='100'){
                      label = 'Perfect';
                    }
                    return datasetLabel + ' : ' + label;
                  },
                  title: function(array, data) {
                    var value1 = null;
                    angular.forEach(array,function(value,key){
                      value.xLabel = moment(value.xLabel).format('MMM Do YYYY');
                      value1 = value;
                    });
                    return value1.xLabel;
                  },
                }
              },elements: {
                line: {
                  tension: 0
                }
              },
              legend: {
                display: false,
              },
              scales: {
                yAxes: [{id: 'y-axis-1', type: 'linear', position: 'left', ticks: {min: 0, max:100}, scaleLabel: {
                  display: true,
                  //labelString: "Proficiency in "+capability.capabilityName,
                  labelString: "Proficiency",
                }}],
                xAxes: [{
                  type: 'time',
                  display: true,
                  scaleLabel: {
                    display: false,
                  }
                }],
              }
            };
            //$scope.personSelected = person;
            angular.forEach(personCapabilities, function(value,key){
              var dateLabel = new Date(value.lastUpdate);
              $scope.capabilitylineLabels.push(dateLabel);
              if(value.proficiency == "Undefined" || value.proficiency == "Superficial" || value.proficiency == "No match" ){
                $scope.capabilitydataValues.push('0');
              }
              else if(value.proficiency == "Satisfactory" || value.proficiency == "Acceptable" || value.proficiency == "Average"){
                $scope.capabilitydataValues.push('25');
                //$scope.lineColors.push('#46BFBD');
              }
              else if(value.proficiency == "Good" || value.proficiency == "Average match" ){
                $scope.capabilitydataValues.push('50');
                //$scope.colors.push('#97bbcd');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              else if(value.proficiency == "Excellent" || value.proficiency == "Good match" || value.proficiency == "High"){
                $scope.capabilitydataValues.push('75');
                //$scope.colors.push('#FDB45C');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              else {
                $scope.capabilitydataValues.push('100');
                //$scope.colors.push('#46BFBD');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
              var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
              $scope.capabilityLastUpdatedDate = skillUpdatedAgo +" on "+skillLastUpdatedDate;
            })
            $scope.capabilitylineData.push($scope.capabilitydataValues);
          });
        }
        $scope.setProgrammingSkillsCompare = function(){
          //$scope.programmingskillsCompareGraph = true;
          $scope.skillsCompareGraph = true;
          $scope.showOverviewGraph = false;
          $scope.programmingSkillsOverviewGraph = false;
          $scope.programmingSkillsTimelineGraph = false;
          $scope.capabilitiesTimelineGraph = false;
          $scope.capabilitiesCompareGraph = false;
          //$scope.selectedSkill = null;
        }
        $scope.showCompareProgrammingSkills = function(selected, skill){
          //var personCapabilities = [];
          var people = [];
          angular.forEach(selected, function(value,key){
            if(value == true){
              var samplePerson = {};
              samplePerson.personId = key;
              people.push(samplePerson);
            }
          })
          var programmingSkillsOfPeople = {};
          programmingSkillsOfPeople.skillId = skill.skillId;
          programmingSkillsOfPeople.people = people;
          $http.put('/api/skills/getProgrammingSkillValueOfPeople',programmingSkillsOfPeople).then(function(response)
          {
            $scope.programmingSkillsCompareValues = response.data;
          }).then(function(){
            if($scope.programmingSkillsCompareValues.length!=0){
              //$log.debug('Hello dump length' +response.length+ '!');
              //var capability.personName = details.personName;
              //var capability.proficiency = details.proficiency;
              //$log.debug('Hello length is' +$scope.programmingSkillsCompareValues.length+ '!');
              //personCapabilities.push(response);
              $scope.setProgrammingSkillsCompare();
            }
            else{
              if( $scope.skillsCompareGraph == true){
                $scope.skillsCompareGraph =false;
              }
              $scope.programmingSkillsOverviewGraph = false;
              $scope.capabilitiesTimelineGraph = false;
              $scope.showOverviewGraph = false;
              $scope.capabilitiesCompareGraph = false;
            }
          }).then(function(){
            $scope.selectedSkill = skill;
            //$log.debug('Hello val is' +$scope.programmingskillsCompareGraph+ '!');
            //$scope.personSelected = person;
            $scope.colors=[];
            $scope.skillComparelineLabels = [];
            $scope.skillComparelineSeries = [];
            $scope.skillCompareColors = [];
            $scope.skillComparelineData = [];
            //$scope.capabilityComparelineColors = ['#46BFBD','#FDB45C'];
            $scope.skillCompareoptions =  {
              maintainAspectRatio: false,
              tooltips: {
                callbacks: {
                  label: function(tooltipItem, data) {
                    var label = null;
                    if(tooltipItem.xLabel=='0'){
                      label = 'Undefined';
                    }
                    if(tooltipItem.xLabel=='25'){
                      label = 'Average';
                    }
                    if(tooltipItem.xLabel=='50'){
                      label = 'Good';
                    }
                    if(tooltipItem.xLabel=='75'){
                      label = 'High';
                    }
                    if(tooltipItem.xLabel=='100'){
                      label = 'Very High';
                    }
                    //return tooltipItem.yLabel + ' : ' + label;
                    return tooltipItem.yLabel + ' : ' + label;
                  },
                }
              },
              legend: {
                display: false,
                position: "bottom",
                onHover: function(event, legendItem) {
                  document.getElementById("bar").style.cursor = 'pointer';
                },
              },
              scales: {
                xAxes: [{  ticks: {min: 0, max:100}, scaleLabel: {
                  display: true,
                }}],
              }
            }
            $scope.skillComparelineSeries.push(skill.skillName);
            angular.forEach($scope.programmingSkillsCompareValues, function(value,key){
              if(value.proficiency !=""){
                var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
                var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
                $scope.skillLastUpdatedDate = skillUpdatedAgo +" on "+skillLastUpdatedDate;
                $scope.skilllastUpdatedBy = value.updatedBy;
                $scope.skillComparelineLabels.push(value.personName);
              }
              if(value.proficiency == "Undefined" || value.proficiency == "Superficial" || value.proficiency == "No match" ){
                $scope.skillComparelineData.push('0');
                $scope.skillCompareColors.push('#e34a33');
              }
              else if(value.proficiency == "Satisfactory" || value.proficiency == "Acceptable" || value.proficiency == "Average"){
                $scope.skillComparelineData.push('25');
                $scope.skillCompareColors.push('#fdbb84');
              }
              else if(value.proficiency == "Good" || value.proficiency == "Average match" ){
                $scope.skillComparelineData.push('50');
                $scope.skillCompareColors.push('#de9226');
              }
              else if(value.proficiency == "Excellent" || value.proficiency == "Good match" || value.proficiency == "High"){
                $scope.skillComparelineData.push('75');
                $scope.skillCompareColors.push('#a3eca3');
              }
              else if(value.proficiency == "Perfect" || value.proficiency == "Excellent match" || value.proficiency == "Outstanding" || value.proficiency == "Very high"){
                $scope.skillComparelineData.push('100');
                $scope.skillCompareColors.push('#1b7c1b');
              }
              else{
                //$scope.capabilityComparedataValues.push('0');
                var optionalDelay = 5000;
                var $string = value.personName+"'s programming skills haven't been updated!";
                alertFactory.addAuto('danger', $string, optionalDelay);
              }
              // $scope.capabilityLastUpdatedDate = $filter('date')(value.lastUpdate, 'yyyy/MM/dd');
              //$scope.skillComparelineData.push($scope.skillComparedataValues);
            })
          }).then(function(){
            if($scope.skillComparelineData.length ==0){
              $scope.skillsCompareGraph =false;
              var optionalDelay = 5000;
              var $string = "The skill '"+skill.skillName+"' for the selected people hasn't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          })
        }
        $scope.setCapabilitiesCompare = function(){
          $scope.showOverviewGraph = false;
          $scope.programmingSkillsOverviewGraph = false;
          $scope.programmingSkillsTimelineGraph = false;
          $scope.capabilitiesTimelineGraph = false;
          $scope.skillsCompareGraph =false;
          $scope.capabilitiesCompareGraph = true;
          $scope.selectedSkill = null;
        }
        $scope.showCompareCapabilities = function(selected, capability){
          //var personCapabilities = [];
          var people = [];
          $http.get('/api/capabilities/getCapabilityMeasures/'+capability.capabilityId).then(function(response)
          {
            $scope.capabilityCompareMeasures = response.data;
          }).then(function(){
            angular.forEach(selected, function(value,key){
              if(value == true){
                var samplePerson = {};
                samplePerson.personId = key;
                people.push(samplePerson);
              }
            })
          }).then(function(){
            var capabilityOfPeople = {};
            capabilityOfPeople.capabilityId = capability.capabilityId;
            capabilityOfPeople.people = people;
            //var txt2 = angular.fromJson(txt);
            $scope.capabilityOfPeople =capabilityOfPeople;
            //$log.debug('Hello ' +capabilityOfPeople.people.toString()+ '!');
            $http.put('/api/capabilities/getCapabilityValueOfPeople',capabilityOfPeople).then(function(response)
            {
              $scope.capabilityCompareValues = response.data;
            }).then(function(){
              if($scope.capabilityCompareValues.length!=0){
                //$log.debug('Hello dump length' +response.length+ '!');
                //var capability.personName = details.personName;
                //var capability.proficiency = details.proficiency;
                //$log.debug('Hello ' +response.personName+response.proficiency+ '!');
                //personCapabilities.push(response);
                $scope.setCapabilitiesCompare();
              }
              else{
                if( $scope.capabilitiesCompareGraph == true){
                  $scope.capabilitiesCompareGraph =false;
                }
                $scope.programmingSkillsOverviewGraph = false;
                $scope.capabilitiesTimelineGraph = false;
                $scope.showOverviewGraph = false;
                $scope.programmingSkillsTimelineGraph = false;
                $scope.skillsCompareGraph =false;
                var personName = null;
                $http.get('/api/people/getPersonName/'+key).then(function(response)
                {
                  personName = response.data.PersonName;
                }).then(function(){
                  var optionalDelay = 5000;
                  var $string = personName+"'s capabilities haven't been updated!";
                  alertFactory.addAuto('danger', $string, optionalDelay);
                });
              }
              $scope.selectedCapability = capability;
              $scope.measures = [];
              //$scope.personSelected = person;
              $scope.capabilityComparelineLabels = [];
              $scope.capabilityComparelineSeries = [];
              $scope.colors = [];
              $scope.capabilityComparelineColors = [];
              $scope.capabilityComparelineData = [];
              //$scope.capabilityComparelineColors = ['#46BFBD'];
              $scope.measures['Commitment'] = ["Superficial", "Satisfactory", "Good" ,"Excellent","Perfect"];
              $scope.measures['Domain knowledge'] = ["Superficial", "Satisfactory", "Good" ,"Excellent","Perfect"];
              $scope.measures["Person's own interest"] = ["Undefined","No match","Average match","Good match","Excellent match"];
              $scope.measures["Previous deliverables' quality"] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
              $scope.measures['Previous projects performance'] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
              $scope.measures['Programming experience'] = ["Undefined","Average","Good","High","Very high"];
              $scope.measures['Programming language knowledge'] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
              $scope.measures['Understanding software security'] = ["Undefined","Average","Good","High","Very high"];
              $scope.capabilityCompareoptions =  {
                maintainAspectRatio: false,
                tooltips: {
                  callbacks: {
                    label: function(tooltipItem, data) {
                      //$log.debug("ylab"+tooltipItem.yLabel+"xlab"+tooltipItem.xLabel);
                      var label = null;
                      if(tooltipItem.xLabel=='0'){
                        var measures = $scope.measures[capability.capabilityName];
                        label = measures[0];
                      }
                      if(tooltipItem.xLabel=='25'){
                        var measures = $scope.measures[capability.capabilityName];
                        label = measures[1];
                      }
                      if(tooltipItem.xLabel=='50'){
                        var measures = $scope.measures[capability.capabilityName];
                        label = measures[2];
                      }
                      if(tooltipItem.xLabel=='75'){
                        var measures = $scope.measures[capability.capabilityName];
                        label = measures[3];
                      }
                      if(tooltipItem.xLabel=='100'){
                        var measures = $scope.measures[capability.capabilityName];
                        label = measures[4];
                      }
                      //return tooltipItem.yLabel + ' : ' + label;
                      return tooltipItem.yLabel + ' : ' + label;
                    },
                  }
                },
                legend: {
                  display: false,
                  position: "bottom",
                  onHover: function(event, legendItem) {
                    document.getElementById("bar").style.cursor = 'pointer';
                  },
                },
                scales: {
                  xAxes: [{  ticks: {min: 0, max:100}, scaleLabel: {
                    display: true,
                  }}],
                }
              }
              $scope.capabilityComparelineSeries.push(capability.capabilityName);
              angular.forEach($scope.capabilityCompareValues, function(value,key){
                //$scope.capabilityComparedataValues = [];
                if(value.proficiency !=""){
                  $scope.capabilityComparelineLabels.push(value.personName);
                  var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
                  var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
                  $scope.capabilitiesLastUpdatedDate = skillUpdatedAgo +" on "+skillLastUpdatedDate;
                  $scope.capabilitylastUpdatedBy = value.updatedBy;
                }
                if(value.proficiency == "Undefined" || value.proficiency == "Superficial" || value.proficiency == "No match" ){
                  $scope.capabilityComparelineData.push('0');
                  $scope.capabilityComparelineColors.push('#e34a33');
                }
                else if(value.proficiency == "Satisfactory" || value.proficiency == "Acceptable" || value.proficiency == "Average"){
                  $scope.capabilityComparelineData.push('25');
                  $scope.capabilityComparelineColors.push('#fdbb84');
                }
                else if(value.proficiency == "Good" || value.proficiency == "Average match" ){
                  $scope.capabilityComparelineData.push('50');
                  $scope.capabilityComparelineColors.push('#de9226');
                }
                else if(value.proficiency == "Excellent" || value.proficiency == "Good match" || value.proficiency == "High"){
                  $scope.capabilityComparelineData.push('75');
                  $scope.capabilityComparelineColors.push('#a3eca3');
                }
                else if(value.proficiency == "Perfect" || value.proficiency == "Excellent match" || value.proficiency == "Outstanding" || value.proficiency == "Very high"){
                  $scope.capabilityComparelineData.push('100');
                  $scope.capabilityComparelineColors.push('#1b7c1b');
                }
                else{
                  //$scope.capabilityComparelineData.push('0');
                  //$scope.capabilityComparelineColors.push('#e34a33');
                }
              })
            }).then(function(){
              if($scope.capabilityComparelineData.length ==0){
                $scope.capabilitiesCompareGraph =false;
                var optionalDelay = 5000;
                var $string = "The capabilities of the selected people haven't been updated!";
                alertFactory.addAuto('danger', $string, optionalDelay);
              }
            })
          })
        }
        $scope.showGraph = function(person){
          var personCapabilities = [];
          $http.get('/api/capabilities/getCapabilityValuesOfPerson/'+person.personId).then(function(response)
          {
            if(response.data.length !=0){
              personCapabilities = response.data;
              $scope.setGraph(person);
            }
            else{
              if( $scope.showOverviewGraph == true){
                $scope.showOverviewGraph = false;
              }
              $scope.capabilitiesTimelineGraph = false;
              $scope.programmingSkillsOverviewGraph = false;
              $scope.programmingSkillsTimelineGraph = false;
              $scope.skillsCompareGraph =false;
              var optionalDelay = 5000;
              var $string = person.personName+"'s capabilities haven't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          }).then(function(){
            $scope.personSelected = person;
            $scope.labels = [];
            $scope.series = ['Series A'];
            $scope.data = [];
            $scope.colors = [];
            $scope.measures = [];
            // angular.forEach(personCapabilities, function(value,key){
            //$scope.measures[value.capabilityName] = [];
            //});
            $scope.measures['Commitment'] = ["Superficial", "Satisfactory", "Good" ,"Excellent","Perfect"];
            $scope.measures['Domain knowledge'] = ["Superficial", "Satisfactory", "Good" ,"Excellent","Perfect"];
            $scope.measures["Person's own interest"] = ["Undefined","No match","Average match","Good match","Excellent match"];
            $scope.measures["Previous deliverables' quality"] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
            $scope.measures['Previous projects performance'] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
            $scope.measures['Programming experience'] = ["Undefined","Average","Good","High","Very high"];
            $scope.measures['Programming language knowledge'] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
            $scope.measures['Understanding software security'] = ["Undefined","Average","Good","High","Very high"];
            $scope.capabilitiesOverviewOptions =  {
              maintainAspectRatio: false,
              tooltips: {
                callbacks: {
                  label: function(tooltipItem, data) {
                    var label = null;
                    if(tooltipItem.xLabel=='0'){
                      var measures = $scope.measures[tooltipItem.yLabel];
                      label = measures[0];
                    }
                    if(tooltipItem.xLabel=='25'){
                      var measures = $scope.measures[tooltipItem.yLabel];
                      label = measures[1];
                    }
                    if(tooltipItem.xLabel=='50'){
                      var measures = $scope.measures[tooltipItem.yLabel];
                      label = measures[2];
                    }
                    if(tooltipItem.xLabel=='75'){
                      var measures = $scope.measures[tooltipItem.yLabel];
                      label = measures[3];
                    }
                    if(tooltipItem.xLabel=='100'){
                      var measures = $scope.measures[tooltipItem.yLabel];
                      label = measures[4];
                    }
                    //return tooltipItem.yLabel + ' : ' + label;
                    return "Proficiency" + ' : ' + label;
                  },
                }
              },
              legend: {
                display: false,
                position: "bottom",
                onHover: function(event, legendItem) {
                  document.getElementById("bar").style.cursor = 'pointer';
                },
              },
              scales: {
                xAxes: [{  ticks: {min: 0, max:100}, scaleLabel: {
                  display: true,
                }}],
              }
            }
            //$scope.personSelected = person;
            angular.forEach(personCapabilities, function(value,key){
              //$scope.labels.push(value.capabilityName);
              //$scope.colors.push('#46BFBD');
              if(value.proficiency == "Undefined" || value.proficiency == "Superficial" || value.proficiency == "No match"){
                $scope.data.push('0');
                $scope.colors.push('#e34a33');
                $scope.labels.push(value.capabilityName);
              }
              else if(value.proficiency == "Satisfactory" || value.proficiency == "Acceptable" || value.proficiency == "Average"){
                $scope.data.push('25');
                $scope.colors.push('#fdbb84');
                $scope.labels.push(value.capabilityName);
              }
              else if(value.proficiency == "Good" || value.proficiency == "Average match"){
                $scope.data.push('50');
                $scope.colors.push('#de9226');
                $scope.labels.push(value.capabilityName);
              }
              else if(value.proficiency == "Excellent" || value.proficiency == "Good match" || value.proficiency == "High"){
                $scope.data.push('75');
                $scope.colors.push('#a3eca3');
                $scope.labels.push(value.capabilityName);
              }
              else {
                $scope.data.push('100');
                $scope.colors.push('#1b7c1b');
                $scope.labels.push(value.capabilityName);
              }
              var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
              var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
              $scope.capabilitiesLastUpdatedDate = skillUpdatedAgo +" on "+skillLastUpdatedDate;
              $scope.capabilitiesLastUpdatedBy = value.updatedBy;
            })
          });
        }
        $scope.filterPeople = function(selectedRole) {
          $scope.isAllSelected = false;
          $scope.selectedRole = selectedRole;
          //$scope.drop[selectedRole] = true;
          angular.forEach($scope.selectedRoles, function(value,key){
            if(key == selectedRole){
              $scope.drop[selectedRole] = true;
            }
            else{
              $scope.drop[key] = false;
            }
          });
          angular.forEach($scope.selectedRoles, function(value, key) {
            if($scope.list2[key].length == value){
              $scope.drop[key] = false;
            }
          });
          var peoplelength =$scope.peopleList.length;
          var list1 =  $scope.peopleList;
          for (var i =  peoplelength - 1; i >= 0; i--) {
            angular.forEach($scope.list2, function(value2, key2) {
              if ($scope.peopleList[i].personId == value2.personId) {
                list1.splice(i, 1);
              }
            });
          }
          $scope.show = true;
          return $scope.peopleList = list1;
        }
        Array.prototype.remove = function(from, to) {
          var rest = this.slice((to || from) + 1 || this.length);
          this.length = from < 0 ? this.length + from : from;
          return this.push.apply(this, rest);
        };
        var sprint = {};
        sprint.projectId = $scope.projectId;
        sprint.sprintId = $scope.sprintId;
        $http.put('/api/sprints/sprintBriefSummary',sprint).then(function(response)
        {
          $scope.sprints= response.data;
          var  sprintStartDate = $filter('date')($scope.sprints.sprintStartDate, 'yyyy/MM/dd');
          var  sprintEndDate = $filter('date')($scope.sprints.sprintEndDate, 'yyyy/MM/dd');
          $scope.sprints.sprintStartDate = sprintStartDate;
          $scope.sprints.sprintEndDate =sprintEndDate;
        })
        .catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching sprint summary details";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
        $scope.formData = {};
        $scope.formData.selected = {};
        $scope.toggleAll = function() {
          var toggleStatus = !$scope.isAllSelected;
          angular.forEach($scope.peopleList, function(itm){
            if(itm.roleName==$scope.selectedRole){
              $scope.formData.selected[itm.personId] = toggleStatus;
            }
          });
          if($scope.formData.selected !={}){
            var count = 0;
            angular.forEach($scope.formData.selected, function(value,key){
              $log.debug('Hello ' +value+key+ '!');
              if(value == true){
                count = count+1;
              }
            });
            $scope.selectedForComparison = count;
            if($scope.selectedForComparison <2){
              if($scope.capabilitiesCompareGraph == true){
                $scope.capabilitiesCompareGraph = false;
              }
            }
          }
        }
        $scope.optionToggled = function(){
          $scope.isAllSelected = $scope.peopleList.every(function(itm){ return $scope.formData.selected[itm.personId]; })
          if($scope.formData.selected !={}){
            var count = 0;
            angular.forEach($scope.formData.selected, function(value,key){
              $log.debug('Hello ' +value+key+ '!');
              if(value == true){
                count = count+1;
              }
            });
            $scope.selectedForComparison = count;
            if($scope.selectedForComparison <2){
              if($scope.capabilitiesCompareGraph == true){
                $scope.capabilitiesCompareGraph = false;
              }
            }
          }
        }
        $scope.clearParticipants =function(ev,participants){
          var confirm = $mdDialog.confirm()
          .title('Would you like to clear all the sprint participants?')
          .textContent('This will also clear the sprint participants on Redmine. Issues allocated to people will be cleared')
          .ariaLabel('')
          .targetEvent(ev)
          .ok('Proceed!')
          .cancel('Cancel');
          $mdDialog.show(confirm).then(function() {
            angular.forEach(participants, function(value) {
              this.push(value);
            },$scope.peopleList);
            $scope.list2= [];
            var sprint = {};
            sprint.projectId = $scope.projectId;
            sprint.sprintId = $scope.sprintId;
            $http.delete('/api/sprints/deleteSprintParticipants/'+$scope.sprintId+"/"+$scope.projectId).then(function(response) {
              var $string = "Cleared sprint participants!";
              var optionalDelay = 3000;
              //alertFactory.addAuto('success', $string, optionalDelay);
            })
            .catch(function(response, status) {
              var optionalDelay = 5000;
              var $string = "Error in deleting sprint participants";
              alertFactory.addAuto('danger', $string, optionalDelay);
            });
          });
        };
        $scope.updateSprintParticipants = function(){
          var listOfParticipants = [];
          angular.forEach($scope.selectedRoles, function(value, key) {
            if($scope.list2[key]){
              var list3 = $scope.list2[key];
              angular.forEach(list3, function(value3, key3) {
                if(list3[key3].personId){
                  if(list3[key3].personId && list3[key3].roleName ){
                    //clearSelected[key].splice(key3,1);
                    var tmp ={};
                    tmp.personId = list3[key3].personId;
                    tmp.personName = list3[key3].personName;
                    //tmp.roleName = list3[key3].roleName;
                    listOfParticipants.push(tmp);
                  }
                }
              });
            }
          });
          //$scope.listOfParticipants = listOfParticipants;
          var sprintDetails = {};
          sprintDetails.projectId = $scope.projectId;
          sprintDetails.sprintId = $scope.sprintId;
          sprintDetails.sprintParticipants = listOfParticipants;
          $http.put('/api/sprints/updateSprintParticipants',sprintDetails).then(function(response) {
            $state.go("management.sprints.peopleToIssues",sprintDetails);
            var $string = "Successfully added the sprint participants";
            var optionalDelay = 3000;
            alertFactory.addAuto('success', $string, optionalDelay);
          })
          .catch(function(response, status) {
            var optionalDelay = 5000;
            var $string = "Error in adding sprint participants";
            alertFactory.addAuto('danger', $string, optionalDelay);
          })
        };
        $scope.saveSprintParticipants = function(){
          var listOfParticipants = [];
          angular.forEach($scope.selectedRoles, function(value, key) {
            if($scope.list2[key]){
              var list3 = $scope.list2[key];
              angular.forEach(list3, function(value3, key3) {
                if(list3[key3].personId){
                  if(list3[key3].personId && list3[key3].roleName ){
                    //clearSelected[key].splice(key3,1);
                    var tmp ={};
                    tmp.personId = list3[key3].personId;
                    tmp.personName = list3[key3].personName;
                    //tmp.roleName = list3[key3].roleName;
                    listOfParticipants.push(tmp);
                  }
                }
              });
            }
          });
          //$scope.listOfParticipants = listOfParticipants;
          var sprintDetails = {};
          sprintDetails.projectId = $scope.projectId;
          sprintDetails.sprintId = $scope.sprintId;
          sprintDetails.sprintParticipants = listOfParticipants;
          $http.put('/api/sprints/updateSprintParticipants',sprintDetails).then(function(response) {
            $state.go("management.sprints.peopleToIssues",sprintDetails);
            var $string = "Successfully added the sprint participants";
            var optionalDelay = 3000;
            alertFactory.addAuto('success', $string, optionalDelay);
          })
          .catch(function(response, status) {
            var optionalDelay = 5000;
            var $string = "Error in adding sprint participants";
            alertFactory.addAuto('danger', $string, optionalDelay);
          })
        };
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching list of people";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
    })
    .catch(function(response, status) {
      //	$scope.loading = false;
      var optionalDelay = 5000;
      var $string = "Error in fetching roles of people";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
    $scope.removeSprintParticipants = function(){
      var currentPeopleList = $scope.peopleList;
      var clearSelected  = $scope.list2;
      var remove = $scope.removeSelected;
      $http.put('/api/people/summaryOfPeopleInProject', $scope.projectId ).then(function(response)
      {
        var people = response.data;
        $scope.peopleListCopy= people;
      }).then(function(){
        var list =  $scope.peopleListCopy;
        var splicelist =[];
        angular.forEach($scope.selectedRoles, function(value, key) {
          splicelist[key] = [];
          if($scope.list2[key]){
            var list3 = $scope.list2[key];
            angular.forEach(list3, function(value3, key3) {
              if(list3[key3].personId && $scope.removeSelected[list3[key3].personId] == true ){
                for (var k =  list.length - 1; k >= 0; k--){
                  if(list[k].personId == list3[key3].personId ){
                    if($scope.drop[list3[key3].roleName] == false){
                      $scope.drop[list3[key3].roleName] = true;
                    }
                    currentPeopleList.push(list[k]);
                    //clearSelected[key].splice(key3,1);
                    var tmp ={};
                    tmp.personId = list3[key3].personId;
                    tmp.personName = list3[key3].personName;
                    splicelist[key].push(tmp);
                    remove[list[k].personId] =false;
                    //$log.debug('Hello ' +list3[key3].personId + list[k].personName + '!');
                    //$log.debug('Hello ' +list3[key3].personId+ " asdasd "+key3+ "ff"+ '!');
                  }
                };
              }
            });
          }
        });
        $scope.countSelected = 0;
        angular.forEach($scope.selectedRoles, function(value, key) {
          angular.forEach(splicelist[key], function(value2, key2) {
            //delete $scope.list2[key][value2];
            //$scope.list2[key][value2] = 1;
            // $scope.showRemove = false;
            angular.forEach($scope.list2[key], function(value3, key3) {
              if(value2.personId == value3.personId && value2.personName == value3.personName ){
                //$log.debug('Hello ' +key3+ '!');
                clearSelected[key].splice(key3,1);
              }
            })
            //$scope.list2[key].remove(0,1,2,3,4);
          })
          $scope.countSelected = 	$scope.countSelected + clearSelected[key].length;
        });
        angular.forEach($scope.selectedRoles, function(value, key) {
          if($scope.list2[key].length == value){
            $scope.drop[key] = false;
          }
          if($scope.list2[key].length < value && $scope.selectedRole == key){
            $scope.drop[key] = true;
          }else {
            $scope.drop[key] = false;
          }
        });
        if($scope.countSelected == 0){
          $scope.showRemove = false;
        }
        //return $scope.removeSelected = {};
        $log.debug('Hello ' +$scope.countSelected+ '!');
      }).then(function(){
        $scope.peopleList = currentPeopleList;
        $scope.list2 = clearSelected;
        $scope.removeSelected =remove;
      });
    };
  });
  app.controller('editPeopleToSprintCtrl', function($scope, $state, $timeout, $filter, $http, $q, dataService, alertFactory, $localStorage, $stateParams, $log, $window, $mdDialog) {
    $scope.projectId = $stateParams.projectId;
    $scope.sprintId = $stateParams.sprintId;
    $scope.selectedRoles = {};
    $scope.list2 = [];
    $scope.drop = [];
    $scope.removeSelected = {};
    $scope.showRemove = false;
    $scope.numberOfParticipants =0;
    $scope.formData1 = {};
    $scope.formData1.selectedRoles = {};
    $scope.someSelected = function (object) {
      return Object.keys(object).some(function (key) {
        return object[key];
      });
    };
    var sprint = {};
    sprint.projectId = $scope.projectId;
    sprint.sprintId = $scope.sprintId;
    $http.put('api/sprints/sprintBriefSummary',sprint).then(function(response)
    {
      $scope.sprintDetails = response.data;
    })
    .catch(function(response, status) {
      var optionalDelay = 5000;
      var $string = "Error in fetching sprint details";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
    $http.get('/api/projects/getNumberofParticipants/'+$scope.projectId).then(function(response)
    {
      $scope.projectRolesList= response.data;
    }).catch(function(response, status) {
      //	$scope.loading = false;
      var optionalDelay = 5000;
      var $string = "Error in fetching roles";
      alertFactory.addAuto('danger', $string, optionalDelay);
    }).then(function(){
      $scope.clearForm = function(formName){
        formName.$setPristine();
        $scope.formData1.selectedRoles = {};
        $scope.maxError=false;
      }
      $scope.change= function(form1, role, maxNumber){
        $scope.maxError  = null;
        var keepGoing = true;
        angular.forEach(form1,function(value,key){
          if(key==role && keepGoing){
            angular.forEach(value.$error,function(value2,key2){
              //if(key2=="min" && value2==true ){
              //return	$scope.minError ="Participants value can't be negative";
              //}
              if(key2=="max" && value2==true){
                keepGoing = false;
                return $scope.maxError ="Number of "+role+"s can't be more than "+maxNumber;
                //$log.debug("max is "+JSON.stringify(value2));
              }
            })
          }
        });
        //var tmp = form1.$name+'.'+role
        //$log.debug(JSON.stringify(tmp));
      }
      $scope.increment = function(val,max){
        if($scope.formData1.selectedRoles[val] == null){
          $scope.formData1.selectedRoles[val] = 0;
        }
        if($scope.formData1.selectedRoles[val] < max){
          $scope.formData1.selectedRoles[val] = ($scope.formData1.selectedRoles[val] +1);
        }
      }
      $scope.decrement = function(val,max){
        if($scope.formData1.selectedRoles[val] != null && $scope.formData1.selectedRoles[val] > 0 ){
          if($scope.formData1.selectedRoles[val] <= max){
            $scope.formData1.selectedRoles[val] = ($scope.formData1.selectedRoles[val] -1);
          }
        }
        if($scope.formData1.selectedRoles[val] == null){
          $scope.formData1.selectedRoles[val] = max;
        }
      }
      $scope.createSprintRoles =function(formName){
        angular.forEach($scope.formData1.selectedRoles, function(val,key){
          $scope.selectedRoles[key] = ($scope.selectedRoles[key] +val);
          $scope.limitEntry($scope.list2[key].length,$scope.selectedRoles[key],key);
          angular.forEach($scope.projectRolesList, function(key2,val2){
            if(key2.roleName == key){
              key2.numberOfPeople = key2.numberOfPeople -val;
            }
          });
        });
        $scope.clearForm(formName);
      }
    });
    $http.put('/api/projects/rolesOfPeopleInProject', $scope.projectId ).then(function(response)
    {
      var rolesOfPeople = response.data;
      $scope.rolesOfPeople= rolesOfPeople;
      angular.forEach( $scope.rolesOfPeople, function(val,key){
        $scope.selectedRoles[val.roleName] = 0;
      });
    }).then (function(){
      $http.put('/api/people/summaryOfPeopleInProject', $scope.projectId ).then(function(response)
      {
        var people = response.data;
        $scope.peopleList= people;
      })
      .then( function(){
        if($scope.selectedRoles){
          angular.forEach($scope.selectedRoles, function(value,key){
            $scope.list2[key] = [];
            $scope.drop[key] = false;
          });
          $http.get('/api/sprints/getSprintParticipants/'+$scope.sprintId+'/'+$scope.projectId).then(function(response)
          {
            $scope.sprintParticipants= response.data;
            angular.forEach($scope.sprintParticipants, function(val,key){
              $scope.list2[val.roleName] = [];
              angular.forEach($scope.projectRolesList, function(key2,val2){
                if(key2.roleName == val.roleName){
                  key2.numberOfPeople = key2.numberOfPeople -1;
                }
              });
              for(var i = 0; i < $scope.peopleList.length; i++ ){
                if( $scope.peopleList[i].personId == val.personId ){
                  $log.debug("person is"+ $scope.peopleList[i].personName);
                  $scope.peopleList.splice(i,1);
                  i =0;
                }
              };
            });
            angular.forEach($scope.sprintParticipants, function(val,key){
              $scope.selectedRoles[val.roleName] = ($scope.selectedRoles[val.roleName] +1);
              $scope.list2[val.roleName].push(val);
            });
            angular.forEach($scope.sprintParticipants, function(val,key){
              $scope.limitEntry($scope.list2[val.roleName].length,key,val.roleName);
            });
          });
        }
        else if(!$scope.selectedRoles){
          $http.get('/api/sprints/getSprintParticipants/'+$scope.sprintId+'/'+$scope.projectId).then(function(response)
          {
            if(response.data.length==0){
              var optionalDelay = 7000;
              var $string = "No sprint participant exists!";
              // $state.go('management.sprints.editSprint.existingSprint', {projectId: $scope.projectId , sprintId: $scope.sprintId});
              alertFactory.addAuto('info', $string, optionalDelay);
            }
            else{
              $scope.sprintParticipants= response.data;
              $scope.selectedRoles = {};
              angular.forEach($scope.sprintParticipants, function(val,key){
                $scope.selectedRoles[val.roleName] = 0;
                $scope.list2[val.roleName] = [];
                $scope.drop[val.roleName] = true;
                for(var i = 0; i < $scope.peopleList.length; i++ ){
                  if( $scope.peopleList[i].personId == val.personId ){
                    $log.debug("person is"+ $scope.peopleList[i].personName);
                    $scope.peopleList.splice(i,1);
                    i =0;
                  }
                };
              });
              angular.forEach($scope.sprintParticipants, function(val,key){
                $scope.selectedRoles[val.roleName] = ($scope.selectedRoles[val.roleName] +1);
                $scope.list2[val.roleName].push(val);
              });
              angular.forEach($scope.sprintParticipants, function(val,key){
                $scope.limitEntry($scope.list2[val.roleName].length,key,val.roleName);
              });
            }
          });
        }
        $scope.limitEntry = function(length,value,key){
          $scope.showRemove = true;
          $scope.numberOfParticipants = $scope.numberOfParticipants +	length;
          if((length+1)>value){
            return $scope.drop[key] = false;
          }
          else if((length+1)<=value && $scope.selectedRole == key) {
            return $scope.drop[key] = true;
          }
        }
        $http.get('/api/roles/getRoles').then(function(response)
        {
          $scope.rolesList= response.data;
        })
        .catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching list of roles";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
        $http.get('/api/capabilities/getCapabilitiesList').then(function(response)
        {
          $scope.allCapabilitiesList = response.data;
        }).catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching list of capabilities";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
        $http.get('/api/skills/getProgrammingSkillsList').then(function(response)
        {
          $scope.allProgrammingSkillsList = response.data;
        }).catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching list of programming skills";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
        $scope.manageSprint = function(sprint) {
          $state.go("management.sprints.editSprint.existingSprint",sprint );
        };
        $scope.setGraph = function(person){
          $scope.showOverviewGraph = true;
          $scope.programmingSkillsOverviewGraph = false;
          $scope.programmingSkillsTimelineGraph = false;
          $scope.capabilitiesTimelineGraph = false;
          $scope.capabilitiesCompareGraph = false;
          $scope.skillsCompareGraph =false;
          $scope.selectedSkill = null;
          $scope.selectedCapability = null;
          return person.personId;
        }
        $scope.setProgrammingSkillsOverviewGraph = function(person){
          $scope.showOverviewGraph = false;
          $scope.programmingSkillsOverviewGraph = true;
          $scope.capabilitiesTimelineGraph = false;
          $scope.programmingSkillsTimelineGraph = false;
          $scope.capabilitiesCompareGraph = false;
          $scope.skillsCompareGraph =false;
          $scope.selectedSkill = null;
          $scope.selectedCapability = null;
          return person.personId;
        }
        $scope.showProgrammingSkillsOverview = function(person){
          var personProgrammingskills = [];
          $http.get('/api/skills/getProgrammingSkllsOfPerson/'+person.personId).then(function(response)
          {
            if(response.data.length !=0){
              personProgrammingskills = response.data;
              $scope.setProgrammingSkillsOverviewGraph(person);
            }
            else{
              if( $scope.programmingSkillsOverviewGraph == true){
                $scope.programmingSkillsOverviewGraph = false;
              }
              $scope.showOverviewGraph = false;
              $scope.programmingSkillsTimelineGraph = false;
              $scope.capabilitiesTimelineGraph = false;
              var optionalDelay = 5000;
              var $string = person.personName+"'s programming skills haven't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          }).then(function(){
            $scope.personSelected = person;
            $scope.labels = [];
            //$scope.series = ['Series A'];
            $scope.data = [];
            $scope.colors = [];
            $scope.skillOverviewColors = [];
            //$scope.personSelected = person;
            $scope.skillOverviewOptions =  {
              maintainAspectRatio: false,
              tooltips: {
                callbacks: {
                  label: function(tooltipItem, data) {
                    var label = null;
                    if(tooltipItem.xLabel=='0'){
                      label = 'Undefined';
                    }
                    if(tooltipItem.xLabel=='25'){
                      label = 'Average';
                    }
                    if(tooltipItem.xLabel=='50'){
                      label = 'Good';
                    }
                    if(tooltipItem.xLabel=='75'){
                      label = 'High';
                    }
                    if(tooltipItem.xLabel=='100'){
                      label = 'Very High';
                    }
                    //return tooltipItem.yLabel + ' : ' + label;
                    return "Proficiency" + ' : ' + label;
                  },
                }
              },
              legend: {
                display: false,
                position: "bottom",
                onHover: function(event, legendItem) {
                  document.getElementById("bar").style.cursor = 'pointer';
                },
              },
              scales: {
                xAxes: [{  ticks: {min: 0, max:100}, scaleLabel: {
                  display: true,
                }}],
              }
            }
            angular.forEach(personProgrammingskills, function(value,key){
              if(value.proficiency == "Undefined" ){
              }
              else if(value.proficiency == "Average"){
                $scope.data.push('25');
                $scope.skillOverviewColors.push('#fdbb84');
                $scope.labels.push(value.skillName);
              }
              else if(value.proficiency == "Good" ){
                $scope.data.push('50');
                $scope.skillOverviewColors.push('#de9226');
                $scope.labels.push(value.skillName);
              }
              else if(value.proficiency == "High"){
                $scope.data.push('75');
                $scope.skillOverviewColors.push('#a3eca3');
                $scope.labels.push(value.skillName);
              }
              else {
                $scope.data.push('100');
                $scope.skillOverviewColors.push('#1b7c1b');
                $scope.labels.push(value.skillName);
              }
              var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
              var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
              $scope.capabilitiesLastUpdatedDate  = skillUpdatedAgo +" on "+skillLastUpdatedDate;
              $scope.capabilitiesLastUpdatedBy = value.updatedBy;
            })
          });
        }
        $scope.setProgrammingSkillsTimelineGraph = function(person){
          $scope.showOverviewGraph = false;
          $scope.programmingSkillsOverviewGraph = false;
          $scope.capabilitiesTimelineGraph = false;
          $scope.capabilitiesCompareGraph = false;
          $scope.skillsCompareGraph =false;
          $scope.programmingSkillsTimelineGraph = true;
          $scope.selectedCapability = null;
          return person.personId;
        }
        $scope.programmingSkillsTimelineDiv = function(person){
          $http.get('/api/skills/getProgrammingSkllsRecorded/'+person.personId).then(function(response)
          {
            $scope.programmingSkillsList = response.data;
          }).then(function(){
            $scope.showOverviewGraph = false;
            $scope.programmingSkillsOverviewGraph = false;
            $scope.programmingSkillsTimelineGraph = true;
            $scope.capabilitiesTimelineGraph = false;
            if($scope.selectedSkill == null){
              $scope.lineLabels = [];
              $scope.lineSeries = [];
              $scope.lineData = [];
              $scope.dataValues = [];
            }
            if($scope.programmingSkillsList.length ==0){
              if($scope.programmingSkillsTimelineGraph == true){
                $scope.programmingSkillsTimelineGraph = false;
              }
              $scope.programmingSkillsOverviewGraph = false;
              $scope.showOverviewGraph = false;
              $scope.capabilitiesTimelineGraph = false;
              var optionalDelay = 5000;
              var $string = person.personName+"'s programming skills haven't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          });
        }
        $scope.showProgrammingSkillsTimeline = function(person, skill){
          var personProgrammingskills = [];
          $http.get('/api/skills/getProgrammingSkllsTimeline/'+person.personId+'/'+skill.skillId).then(function(response)
          {
            if(response.data.length !=0){
              personProgrammingskills = response.data;
              $scope.setProgrammingSkillsTimelineGraph(person);
            }
            else{
              if( $scope.programmingSkillsTimelineGraph == true){
                $scope.programmingSkillsTimelineGraph = false;
              }
              $scope.programmingSkillsOverviewGraph = false;
              $scope.showOverviewGraph = false;
              $scope.capabilitiesTimelineGraph = false;
              $scope.skillsCompareGraph =false;
            }
          }).then(function(){
            $scope.selectedSkill = skill.skillName;
            $scope.personSelected = person;
            $scope.lineLabels = [];
            $scope.lineSeries = [];
            $scope.lineData = [];
            $scope.dataValues = [];
            $scope.lineColors = ['#46BFBD'];
            $scope.lineSeries.push(skill.skillName);
            $scope.skillsoptions =  {
              maintainAspectRatio: false,
              tooltips: {
                callbacks: {
                  label: function(tooltipItem, data) {
                    var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
                    var label = null;
                    if(tooltipItem.yLabel=='0'){
                      label = 'Undefined';
                    }
                    if(tooltipItem.yLabel=='25'){
                      label = 'Average';
                    }
                    if(tooltipItem.yLabel=='50'){
                      label = 'Good';
                    }
                    if(tooltipItem.yLabel=='75'){
                      label = 'High';
                    }
                    if(tooltipItem.yLabel=='100'){
                      label = 'Very High';
                    }
                    return datasetLabel + ' : ' + label;
                  },
                  title: function(array, data) {
                    var value1 = null;
                    angular.forEach(array,function(value,key){
                      value.xLabel = moment(value.xLabel).format('MMM Do YYYY');
                      value1 = value;
                    });
                    return value1.xLabel;
                  },
                }
              },elements: {
                line: {
                  tension: 0
                }
              },
              legend: {
                display: false,
              },
              scales: {
                yAxes: [{id: 'y-axis-1', type: 'linear', position: 'left', ticks: {min: 0, max:100}, scaleLabel: {
                  display: true,
                  labelString: "Proficiency in "+skill.skillName,
                  //labelString: "Proficiency",
                }}],
                xAxes: [{
                  type: 'time',
                  display: true,
                  scaleLabel: {
                    display: false,
                  }
                }],
              }
            };
            //$scope.personSelected = person;
            angular.forEach(personProgrammingskills, function(value,key){
              var dateLabel = new Date(value.lastUpdate);
              $scope.lineLabels.push(dateLabel);
              if(value.proficiency == "Undefined" ){
              }
              else if(value.proficiency == "Average"){
                $scope.dataValues.push('25');
                //$scope.lineColors.push('#46BFBD');
              }
              else if(value.proficiency == "Good" ){
                $scope.dataValues.push('50');
                //$scope.colors.push('#97bbcd');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              else if(value.proficiency == "High"){
                $scope.dataValues.push('75');
                //$scope.colors.push('#FDB45C');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              else {
                $scope.dataValues.push('100');
                //$scope.colors.push('#46BFBD');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
              var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
              $scope.skillLastUpdatedDate = skillUpdatedAgo +" on "+skillLastUpdatedDate;
            })
            $scope.lineData.push($scope.dataValues);
          });
        }
        $scope.setCapabilitiesTimeline = function(person){
          $scope.showOverviewGraph = false;
          $scope.programmingSkillsOverviewGraph = false;
          $scope.programmingSkillsTimelineGraph = false;
          $scope.capabilitiesCompareGraph = false;
          $scope.skillsCompareGraph =false;
          $scope.capabilitiesTimelineGraph = true;
          $scope.selectedSkill = null;
          return person.personId;
        }
        $scope.capabilitiesTimelineDiv = function(person){
          $http.get('/api/capabilities/getCapabilitiesList').then(function(response)
          {
            $scope.capabilitiesList = response.data;
          }).then(function(){
            $scope.showOverviewGraph = false;
            $scope.skillsCompareGraph =false;
            $scope.programmingSkillsTimelineGraph = false;
            $scope.capabilitiesCompareGraph = false;
            $scope.programmingSkillsCompareGraph = false;
            $scope.capabilitiesTimelineGraph = true;
            $scope.programmingSkillsOverviewGraph = false;
            if($scope.selectedCapability == null){
              $scope.capabilitylineLabels = [];
              $scope.capabilitylineSeries = [];
              $scope.capabilitylineData = [];
              $scope.capabilitydataValues = [];
              $scope.capabilityMeasures = null;
            }
            if($scope.capabilitiesList.length ==0){
              $scope.capabilitiesTimelineGraph  = false;
              var optionalDelay = 5000;
              var $string = "List of capabilities haven't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          });
        }
        $scope.showCapabilitiesTimeline = function(person, capability){
          var personCapabilities = [];
          $http.get('/api/capabilities/getCapabilitiesTimeline/'+person.personId+'/'+capability.capabilityId).then(function(response)
          {
            if(response.data.length !=0){
              personCapabilities = response.data;
              $scope.setCapabilitiesTimeline(person);
            }
            else{
              if( $scope.capabilitiesTimelineGraph == true){
                $scope.capabilitiesTimelineGraph = false;
              }
              $scope.programmingSkillsOverviewGraph = false;
              $scope.showOverviewGraph = false;
              $scope.programmingSkillsTimelineGraph = false;
              var optionalDelay = 5000;
              var $string = person.personName+"'s capabilities haven't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          }).then(function(){
            $http.get('/api/capabilities/getCapabilityMeasures/'+capability.capabilityId).then(function(response)
            {
              $scope.capabilityMeasures = response.data;
            });
          }).then(function(){
            $scope.selectedCapability = capability;
            $scope.personSelected = person;
            $scope.capabilitylineLabels = [];
            $scope.capabilitylineSeries = [];
            $scope.capabilitylineData = [];
            $scope.capabilitydataValues = [];
            $scope.capabilitylineColors = ['#46BFBD'];
            $scope.capabilitylineSeries.push(capability.capabilityName);
            $scope.capabilityoptions =  {
              maintainAspectRatio: false,
              tooltips: {
                callbacks: {
                  label: function(tooltipItem, data) {
                    var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
                    var label = null;
                    if(tooltipItem.yLabel=='0'){
                      label = 'Superficial';
                    }
                    if(tooltipItem.yLabel=='25'){
                      label = 'Satisfactory';
                    }
                    if(tooltipItem.yLabel=='50'){
                      label = 'Good';
                    }
                    if(tooltipItem.yLabel=='75'){
                      label = 'Excellent';
                    }
                    if(tooltipItem.yLabel=='100'){
                      label = 'Perfect';
                    }
                    return datasetLabel + ' : ' + label;
                  },
                  title: function(array, data) {
                    var value1 = null;
                    angular.forEach(array,function(value,key){
                      value.xLabel = moment(value.xLabel).format('MMM Do YYYY');
                      value1 = value;
                    });
                    return value1.xLabel;
                  },
                }
              },elements: {
                line: {
                  tension: 0
                }
              },
              legend: {
                display: false,
              },
              scales: {
                yAxes: [{id: 'y-axis-1', type: 'linear', position: 'left', ticks: {min: 0, max:100}, scaleLabel: {
                  display: true,
                  //labelString: "Proficiency in "+capability.capabilityName,
                  labelString: "Proficiency",
                }}],
                xAxes: [{
                  type: 'time',
                  display: true,
                  scaleLabel: {
                    display: false,
                  }
                }],
              }
            };
            //$scope.personSelected = person;
            angular.forEach(personCapabilities, function(value,key){
              var dateLabel = new Date(value.lastUpdate);
              $scope.capabilitylineLabels.push(dateLabel);
              if(value.proficiency == "Undefined" || value.proficiency == "Superficial" || value.proficiency == "No match" ){
                $scope.capabilitydataValues.push('0');
              }
              else if(value.proficiency == "Satisfactory" || value.proficiency == "Acceptable" || value.proficiency == "Average"){
                $scope.capabilitydataValues.push('25');
                //$scope.lineColors.push('#46BFBD');
              }
              else if(value.proficiency == "Good" || value.proficiency == "Average match" ){
                $scope.capabilitydataValues.push('50');
                //$scope.colors.push('#97bbcd');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              else if(value.proficiency == "Excellent" || value.proficiency == "Good match" || value.proficiency == "High"){
                $scope.capabilitydataValues.push('75');
                //$scope.colors.push('#FDB45C');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              else {
                $scope.capabilitydataValues.push('100');
                //$scope.colors.push('#46BFBD');
                //$scope.labels.push(value.skillName);
                // $scope.lineColors.push('#46BFBD');
              }
              var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
              var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
              $scope.capabilityLastUpdatedDate = skillUpdatedAgo +" on "+skillLastUpdatedDate;
            })
            $scope.capabilitylineData.push($scope.capabilitydataValues);
          });
        }
        $scope.setProgrammingSkillsCompare = function(){
          //$scope.programmingskillsCompareGraph = true;
          $scope.skillsCompareGraph = true;
          $scope.showOverviewGraph = false;
          $scope.programmingSkillsOverviewGraph = false;
          $scope.programmingSkillsTimelineGraph = false;
          $scope.capabilitiesTimelineGraph = false;
          $scope.capabilitiesCompareGraph = false;
          //$scope.selectedSkill = null;
        }
        $scope.showCompareProgrammingSkills = function(selected, skill){
          //var personCapabilities = [];
          var people = [];
          angular.forEach(selected, function(value,key){
            if(value == true){
              var samplePerson = {};
              samplePerson.personId = key;
              people.push(samplePerson);
            }
          })
          var programmingSkillsOfPeople = {};
          programmingSkillsOfPeople.skillId = skill.skillId;
          programmingSkillsOfPeople.people = people;
          $http.put('/api/skills/getProgrammingSkillValueOfPeople',programmingSkillsOfPeople).then(function(response)
          {
            $scope.programmingSkillsCompareValues = response.data;
          }).then(function(){
            if($scope.programmingSkillsCompareValues.length!=0){
              //$log.debug('Hello dump length' +response.length+ '!');
              //var capability.personName = details.personName;
              //var capability.proficiency = details.proficiency;
              //$log.debug('Hello length is' +$scope.programmingSkillsCompareValues.length+ '!');
              //personCapabilities.push(response);
              $scope.setProgrammingSkillsCompare();
            }
            else{
              if( $scope.skillsCompareGraph == true){
                $scope.skillsCompareGraph =false;
              }
              $scope.programmingSkillsOverviewGraph = false;
              $scope.capabilitiesTimelineGraph = false;
              $scope.showOverviewGraph = false;
              $scope.capabilitiesCompareGraph = false;
            }
          }).then(function(){
            $scope.selectedSkill = skill;
            //$log.debug('Hello val is' +$scope.programmingskillsCompareGraph+ '!');
            //$scope.personSelected = person;
            $scope.colors=[];
            $scope.skillComparelineLabels = [];
            $scope.skillComparelineSeries = [];
            $scope.skillCompareColors = [];
            $scope.skillComparelineData = [];
            //$scope.capabilityComparelineColors = ['#46BFBD','#FDB45C'];
            $scope.skillCompareoptions =  {
              maintainAspectRatio: false,
              tooltips: {
                callbacks: {
                  label: function(tooltipItem, data) {
                    var label = null;
                    if(tooltipItem.xLabel=='0'){
                      label = 'Undefined';
                    }
                    if(tooltipItem.xLabel=='25'){
                      label = 'Average';
                    }
                    if(tooltipItem.xLabel=='50'){
                      label = 'Good';
                    }
                    if(tooltipItem.xLabel=='75'){
                      label = 'High';
                    }
                    if(tooltipItem.xLabel=='100'){
                      label = 'Very High';
                    }
                    //return tooltipItem.yLabel + ' : ' + label;
                    return tooltipItem.yLabel + ' : ' + label;
                  },
                }
              },
              legend: {
                display: false,
                position: "bottom",
                onHover: function(event, legendItem) {
                  document.getElementById("bar").style.cursor = 'pointer';
                },
              },
              scales: {
                xAxes: [{  ticks: {min: 0, max:100}, scaleLabel: {
                  display: true,
                }}],
              }
            }
            $scope.skillComparelineSeries.push(skill.skillName);
            angular.forEach($scope.programmingSkillsCompareValues, function(value,key){
              if(value.proficiency !=""){
                var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
                var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
                $scope.skillLastUpdatedDate = skillUpdatedAgo +" on "+skillLastUpdatedDate;
                $scope.skilllastUpdatedBy = value.updatedBy;
                $scope.skillComparelineLabels.push(value.personName);
              }
              if(value.proficiency == "Undefined" || value.proficiency == "Superficial" || value.proficiency == "No match" ){
                $scope.skillComparelineData.push('0');
                $scope.skillCompareColors.push('#e34a33');
              }
              else if(value.proficiency == "Satisfactory" || value.proficiency == "Acceptable" || value.proficiency == "Average"){
                $scope.skillComparelineData.push('25');
                $scope.skillCompareColors.push('#fdbb84');
              }
              else if(value.proficiency == "Good" || value.proficiency == "Average match" ){
                $scope.skillComparelineData.push('50');
                $scope.skillCompareColors.push('#de9226');
              }
              else if(value.proficiency == "Excellent" || value.proficiency == "Good match" || value.proficiency == "High"){
                $scope.skillComparelineData.push('75');
                $scope.skillCompareColors.push('#a3eca3');
              }
              else if(value.proficiency == "Perfect" || value.proficiency == "Excellent match" || value.proficiency == "Outstanding" || value.proficiency == "Very high"){
                $scope.skillComparelineData.push('100');
                $scope.skillCompareColors.push('#1b7c1b');
              }
              else{
                //$scope.capabilityComparedataValues.push('0');
                var optionalDelay = 5000;
                var $string = value.personName+"'s programming skills haven't been updated!";
                alertFactory.addAuto('danger', $string, optionalDelay);
              }
              // $scope.capabilityLastUpdatedDate = $filter('date')(value.lastUpdate, 'yyyy/MM/dd');
              //$scope.skillComparelineData.push($scope.skillComparedataValues);
            })
          }).then(function(){
            if($scope.skillComparelineData.length ==0){
              $scope.skillsCompareGraph =false;
              var optionalDelay = 5000;
              var $string = "The skill '"+skill.skillName+"' for the selected people hasn't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          })
        }
        $scope.setCapabilitiesCompare = function(){
          $scope.showOverviewGraph = false;
          $scope.programmingSkillsOverviewGraph = false;
          $scope.programmingSkillsTimelineGraph = false;
          $scope.capabilitiesTimelineGraph = false;
          $scope.skillsCompareGraph =false;
          $scope.capabilitiesCompareGraph = true;
          $scope.selectedSkill = null;
        }
        $scope.showCompareCapabilities = function(selected, capability){
          //var personCapabilities = [];
          var people = [];
          $http.get('/api/capabilities/getCapabilityMeasures/'+capability.capabilityId).then(function(response)
          {
            $scope.capabilityCompareMeasures = response.data;
          }).then(function(){
            angular.forEach(selected, function(value,key){
              if(value == true){
                var samplePerson = {};
                samplePerson.personId = key;
                people.push(samplePerson);
              }
            })
          }).then(function(){
            var capabilityOfPeople = {};
            capabilityOfPeople.capabilityId = capability.capabilityId;
            capabilityOfPeople.people = people;
            //var txt2 = angular.fromJson(txt);
            $scope.capabilityOfPeople =capabilityOfPeople;
            //$log.debug('Hello ' +capabilityOfPeople.people.toString()+ '!');
            $http.put('/api/capabilities/getCapabilityValueOfPeople',capabilityOfPeople).then(function(response)
            {
              $scope.capabilityCompareValues = response.data;
            }).then(function(){
              if($scope.capabilityCompareValues.length!=0){
                //$log.debug('Hello dump length' +response.length+ '!');
                //var capability.personName = details.personName;
                //var capability.proficiency = details.proficiency;
                //$log.debug('Hello ' +response.personName+response.proficiency+ '!');
                //personCapabilities.push(response);
                $scope.setCapabilitiesCompare();
              }
              else{
                if( $scope.capabilitiesCompareGraph == true){
                  $scope.capabilitiesCompareGraph =false;
                }
                $scope.programmingSkillsOverviewGraph = false;
                $scope.capabilitiesTimelineGraph = false;
                $scope.showOverviewGraph = false;
                $scope.programmingSkillsTimelineGraph = false;
                $scope.skillsCompareGraph =false;
                var personName = null;
                $http.get('/api/people/getPersonName/'+key).then(function(response)
                {
                  personName = response.data.PersonName;
                }).then(function(){
                  var optionalDelay = 5000;
                  var $string = personName+"'s capabilities haven't been updated!";
                  alertFactory.addAuto('danger', $string, optionalDelay);
                });
              }
              $scope.selectedCapability = capability;
              $scope.measures = [];
              //$scope.personSelected = person;
              $scope.capabilityComparelineLabels = [];
              $scope.capabilityComparelineSeries = [];
              $scope.colors = [];
              $scope.capabilityComparelineColors = [];
              $scope.capabilityComparelineData = [];
              //$scope.capabilityComparelineColors = ['#46BFBD'];
              $scope.measures['Commitment'] = ["Superficial", "Satisfactory", "Good" ,"Excellent","Perfect"];
              $scope.measures['Domain knowledge'] = ["Superficial", "Satisfactory", "Good" ,"Excellent","Perfect"];
              $scope.measures["Person's own interest"] = ["Undefined","No match","Average match","Good match","Excellent match"];
              $scope.measures["Previous deliverables' quality"] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
              $scope.measures['Previous projects performance'] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
              $scope.measures['Programming experience'] = ["Undefined","Average","Good","High","Very high"];
              $scope.measures['Programming language knowledge'] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
              $scope.measures['Understanding software security'] = ["Undefined","Average","Good","High","Very high"];
              $scope.capabilityCompareoptions =  {
                maintainAspectRatio: false,
                tooltips: {
                  callbacks: {
                    label: function(tooltipItem, data) {
                      //$log.debug("ylab"+tooltipItem.yLabel+"xlab"+tooltipItem.xLabel);
                      var label = null;
                      if(tooltipItem.xLabel=='0'){
                        var measures = $scope.measures[capability.capabilityName];
                        label = measures[0];
                      }
                      if(tooltipItem.xLabel=='25'){
                        var measures = $scope.measures[capability.capabilityName];
                        label = measures[1];
                      }
                      if(tooltipItem.xLabel=='50'){
                        var measures = $scope.measures[capability.capabilityName];
                        label = measures[2];
                      }
                      if(tooltipItem.xLabel=='75'){
                        var measures = $scope.measures[capability.capabilityName];
                        label = measures[3];
                      }
                      if(tooltipItem.xLabel=='100'){
                        var measures = $scope.measures[capability.capabilityName];
                        label = measures[4];
                      }
                      //return tooltipItem.yLabel + ' : ' + label;
                      return tooltipItem.yLabel + ' : ' + label;
                    },
                  }
                },
                legend: {
                  display: false,
                  position: "bottom",
                  onHover: function(event, legendItem) {
                    document.getElementById("bar").style.cursor = 'pointer';
                  },
                },
                scales: {
                  xAxes: [{  ticks: {min: 0, max:100}, scaleLabel: {
                    display: true,
                  }}],
                }
              }
              $scope.capabilityComparelineSeries.push(capability.capabilityName);
              angular.forEach($scope.capabilityCompareValues, function(value,key){
                //$scope.capabilityComparedataValues = [];
                if(value.proficiency !=""){
                  $scope.capabilityComparelineLabels.push(value.personName);
                  var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
                  var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
                  $scope.capabilitiesLastUpdatedDate = skillUpdatedAgo +" on "+skillLastUpdatedDate;
                  $scope.capabilitylastUpdatedBy = value.updatedBy;
                }
                if(value.proficiency == "Undefined" || value.proficiency == "Superficial" || value.proficiency == "No match" ){
                  $scope.capabilityComparelineData.push('0');
                  $scope.capabilityComparelineColors.push('#e34a33');
                }
                else if(value.proficiency == "Satisfactory" || value.proficiency == "Acceptable" || value.proficiency == "Average"){
                  $scope.capabilityComparelineData.push('25');
                  $scope.capabilityComparelineColors.push('#fdbb84');
                }
                else if(value.proficiency == "Good" || value.proficiency == "Average match" ){
                  $scope.capabilityComparelineData.push('50');
                  $scope.capabilityComparelineColors.push('#de9226');
                }
                else if(value.proficiency == "Excellent" || value.proficiency == "Good match" || value.proficiency == "High"){
                  $scope.capabilityComparelineData.push('75');
                  $scope.capabilityComparelineColors.push('#a3eca3');
                }
                else if(value.proficiency == "Perfect" || value.proficiency == "Excellent match" || value.proficiency == "Outstanding" || value.proficiency == "Very high"){
                  $scope.capabilityComparelineData.push('100');
                  $scope.capabilityComparelineColors.push('#1b7c1b');
                }
                else{
                  //$scope.capabilityComparelineData.push('0');
                  //$scope.capabilityComparelineColors.push('#e34a33');
                }
              })
            }).then(function(){
              if($scope.capabilityComparelineData.length ==0){
                $scope.capabilitiesCompareGraph =false;
                var optionalDelay = 5000;
                var $string = "The capabilities of the selected people haven't been updated!";
                alertFactory.addAuto('danger', $string, optionalDelay);
              }
            })
          })
        }
        $scope.showGraph = function(person){
          var personCapabilities = [];
          $http.get('/api/capabilities/getCapabilityValuesOfPerson/'+person.personId).then(function(response)
          {
            if(response.data.length !=0){
              personCapabilities = response.data;
              $scope.setGraph(person);
            }
            else{
              if( $scope.showOverviewGraph == true){
                $scope.showOverviewGraph = false;
              }
              $scope.capabilitiesTimelineGraph = false;
              $scope.programmingSkillsOverviewGraph = false;
              $scope.programmingSkillsTimelineGraph = false;
              $scope.skillsCompareGraph =false;
              var optionalDelay = 5000;
              var $string = person.personName+"'s capabilities haven't been updated!";
              alertFactory.addAuto('danger', $string, optionalDelay);
            }
          }).then(function(){
            $scope.personSelected = person;
            $scope.labels = [];
            $scope.series = ['Series A'];
            $scope.data = [];
            $scope.colors = [];
            $scope.measures = [];
            // angular.forEach(personCapabilities, function(value,key){
            //$scope.measures[value.capabilityName] = [];
            //});
            $scope.measures['Commitment'] = ["Superficial", "Satisfactory", "Good" ,"Excellent","Perfect"];
            $scope.measures['Domain knowledge'] = ["Superficial", "Satisfactory", "Good" ,"Excellent","Perfect"];
            $scope.measures["Person's own interest"] = ["Undefined","No match","Average match","Good match","Excellent match"];
            $scope.measures["Previous deliverables' quality"] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
            $scope.measures['Previous projects performance'] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
            $scope.measures['Programming experience'] = ["Undefined","Average","Good","High","Very high"];
            $scope.measures['Programming language knowledge'] = ["Undefined","Acceptable","Good","Excellent","Outstanding"];
            $scope.measures['Understanding software security'] = ["Undefined","Average","Good","High","Very high"];
            $scope.capabilitiesOverviewOptions =  {
              maintainAspectRatio: false,
              tooltips: {
                callbacks: {
                  label: function(tooltipItem, data) {
                    var label = null;
                    if(tooltipItem.xLabel=='0'){
                      var measures = $scope.measures[tooltipItem.yLabel];
                      label = measures[0];
                    }
                    if(tooltipItem.xLabel=='25'){
                      var measures = $scope.measures[tooltipItem.yLabel];
                      label = measures[1];
                    }
                    if(tooltipItem.xLabel=='50'){
                      var measures = $scope.measures[tooltipItem.yLabel];
                      label = measures[2];
                    }
                    if(tooltipItem.xLabel=='75'){
                      var measures = $scope.measures[tooltipItem.yLabel];
                      label = measures[3];
                    }
                    if(tooltipItem.xLabel=='100'){
                      var measures = $scope.measures[tooltipItem.yLabel];
                      label = measures[4];
                    }
                    //return tooltipItem.yLabel + ' : ' + label;
                    return "Proficiency" + ' : ' + label;
                  },
                }
              },
              legend: {
                display: false,
                position: "bottom",
                onHover: function(event, legendItem) {
                  document.getElementById("bar").style.cursor = 'pointer';
                },
              },
              scales: {
                xAxes: [{  ticks: {min: 0, max:100}, scaleLabel: {
                  display: true,
                }}],
              }
            }
            //$scope.personSelected = person;
            angular.forEach(personCapabilities, function(value,key){
              //$scope.labels.push(value.capabilityName);
              //$scope.colors.push('#46BFBD');
              if(value.proficiency == "Undefined" || value.proficiency == "Superficial" || value.proficiency == "No match"){
                $scope.data.push('0');
                $scope.colors.push('#e34a33');
                $scope.labels.push(value.capabilityName);
              }
              else if(value.proficiency == "Satisfactory" || value.proficiency == "Acceptable" || value.proficiency == "Average"){
                $scope.data.push('25');
                $scope.colors.push('#fdbb84');
                $scope.labels.push(value.capabilityName);
              }
              else if(value.proficiency == "Good" || value.proficiency == "Average match"){
                $scope.data.push('50');
                $scope.colors.push('#de9226');
                $scope.labels.push(value.capabilityName);
              }
              else if(value.proficiency == "Excellent" || value.proficiency == "Good match" || value.proficiency == "High"){
                $scope.data.push('75');
                $scope.colors.push('#a3eca3');
                $scope.labels.push(value.capabilityName);
              }
              else {
                $scope.data.push('100');
                $scope.colors.push('#1b7c1b');
                $scope.labels.push(value.capabilityName);
              }
              var skillLastUpdatedDate = moment(value.lastUpdate).format('MMM Do YYYY');
              var skillUpdatedAgo = moment(value.lastUpdate).fromNow();
              $scope.capabilitiesLastUpdatedDate = skillUpdatedAgo +" on "+skillLastUpdatedDate;
              $scope.capabilitiesLastUpdatedBy = value.updatedBy;
            })
          });
        }
        $scope.filterPeople = function(selectedRole) {
          $scope.isAllSelected = false;
          $scope.selectedRole = selectedRole;
          //$scope.drop[selectedRole] = true;
          angular.forEach($scope.selectedRoles, function(value,key){
            if(key == selectedRole){
              $scope.drop[selectedRole] = true;
            }
            else{
              $scope.drop[key] = false;
            }
          });
          angular.forEach($scope.selectedRoles, function(value, key) {
            if($scope.list2[key].length == value){
              $scope.drop[key] = false;
            }
          });
          var peoplelength =$scope.peopleList.length;
          var list1 =  $scope.peopleList;
          for (var i =  peoplelength - 1; i >= 0; i--) {
            angular.forEach($scope.list2, function(value2, key2) {
              if ($scope.peopleList[i].personId == value2.personId) {
                list1.splice(i, 1);
              }
            });
          }
          $scope.show = true;
          return $scope.peopleList = list1;
        }
        Array.prototype.remove = function(from, to) {
          var rest = this.slice((to || from) + 1 || this.length);
          this.length = from < 0 ? this.length + from : from;
          return this.push.apply(this, rest);
        };
        var sprint = {};
        sprint.projectId = $scope.projectId;
        sprint.sprintId = $scope.sprintId;
        $http.put('/api/sprints/sprintBriefSummary',sprint).then(function(response)
        {
          $scope.sprints= response.data;
          var  sprintStartDate = $filter('date')($scope.sprints.sprintStartDate, 'yyyy/MM/dd');
          var  sprintEndDate = $filter('date')($scope.sprints.sprintEndDate, 'yyyy/MM/dd');
          $scope.sprints.sprintStartDate = sprintStartDate;
          $scope.sprints.sprintEndDate =sprintEndDate;
        })
        .catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching sprint summary details";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
        $scope.formData = {};
        $scope.formData.selected = {};
        $scope.toggleAll = function() {
          var toggleStatus = !$scope.isAllSelected;
          angular.forEach($scope.peopleList, function(itm){
            if(itm.roleName==$scope.selectedRole){
              $scope.formData.selected[itm.personId] = toggleStatus;
            }
          });
          if($scope.formData.selected !={}){
            var count = 0;
            angular.forEach($scope.formData.selected, function(value,key){
              $log.debug('Hello ' +value+key+ '!');
              if(value == true){
                count = count+1;
              }
            });
            $scope.selectedForComparison = count;
            if($scope.selectedForComparison <2){
              if($scope.capabilitiesCompareGraph == true){
                $scope.capabilitiesCompareGraph = false;
              }
            }
          }
        }
        $scope.optionToggled = function(){
          $scope.isAllSelected = $scope.peopleList.every(function(itm){ return $scope.formData.selected[itm.personId]; })
          if($scope.formData.selected !={}){
            var count = 0;
            angular.forEach($scope.formData.selected, function(value,key){
              $log.debug('Hello ' +value+key+ '!');
              if(value == true){
                count = count+1;
              }
            });
            $scope.selectedForComparison = count;
            if($scope.selectedForComparison <2){
              if($scope.capabilitiesCompareGraph == true){
                $scope.capabilitiesCompareGraph = false;
              }
            }
          }
        }
        $scope.clearParticipants =function(ev,participants){
          var confirm = $mdDialog.confirm()
          .title('Would you like to clear all the sprint participants?')
          .textContent('This will also clear the sprint participants on Redmine. Issues allocated to people will be cleared')
          .ariaLabel('')
          .targetEvent(ev)
          .ok('Proceed!')
          .cancel('Cancel');
          $mdDialog.show(confirm).then(function() {
            angular.forEach(participants, function(value) {
              this.push(value);
            },$scope.peopleList);
            $scope.list2= [];
            var sprint = {};
            sprint.projectId = $scope.projectId;
            sprint.sprintId = $scope.sprintId;
            $http.delete('/api/sprints/deleteSprintParticipants/'+$scope.sprintId+"/"+$scope.projectId).then(function(response) {
              var $string = "Cleared sprint participants!";
              var optionalDelay = 3000;
              //alertFactory.addAuto('success', $string, optionalDelay);
            })
            .catch(function(response, status) {
              var optionalDelay = 5000;
              var $string = "Error in deleting sprint participants";
              alertFactory.addAuto('danger', $string, optionalDelay);
            });
          });
        };
        $scope.updateSprintParticipants = function(){
          var listOfParticipants = [];
          angular.forEach($scope.selectedRoles, function(value, key) {
            if($scope.list2[key]){
              var list3 = $scope.list2[key];
              angular.forEach(list3, function(value3, key3) {
                if(list3[key3].personId){
                  if(list3[key3].personId && list3[key3].roleName ){
                    //clearSelected[key].splice(key3,1);
                    var tmp ={};
                    tmp.personId = list3[key3].personId;
                    tmp.personName = list3[key3].personName;
                    //tmp.roleName = list3[key3].roleName;
                    listOfParticipants.push(tmp);
                  }
                }
              });
            }
          });
          //$scope.listOfParticipants = listOfParticipants;
          var sprintDetails = {};
          sprintDetails.projectId = $scope.projectId;
          sprintDetails.sprintId = $scope.sprintId;
          sprintDetails.sprintParticipants = listOfParticipants;
          $http.put('/api/sprints/updateSprintParticipants',sprintDetails).then(function(response) {
            $state.go("management.sprints.editSprint.peopleToIssues",sprintDetails);
            var $string = "Successfully updated the sprint participants";
            var optionalDelay = 3000;
            alertFactory.addAuto('success', $string, optionalDelay);
          })
          .catch(function(response, status) {
            var optionalDelay = 5000;
            var $string = "Error in adding sprint participants";
            alertFactory.addAuto('danger', $string, optionalDelay);
          })
        };
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching list of people";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
    })
    .catch(function(response, status) {
      //	$scope.loading = false;
      var optionalDelay = 5000;
      var $string = "Error in fetching roles of people";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
    $scope.removeSprintParticipants = function(){
      var currentPeopleList = $scope.peopleList;
      var clearSelected  = $scope.list2;
      var remove = $scope.removeSelected;
      $http.put('/api/people/summaryOfPeopleInProject', $scope.projectId ).then(function(response)
      {
        var people = response.data;
        $scope.peopleListCopy= people;
      }).then(function(){
        var list =  $scope.peopleListCopy;
        var splicelist =[];
        angular.forEach($scope.selectedRoles, function(value, key) {
          splicelist[key] = [];
          if($scope.list2[key]){
            var list3 = $scope.list2[key];
            angular.forEach(list3, function(value3, key3) {
              if(list3[key3].personId && $scope.removeSelected[list3[key3].personId] == true ){
                for (var k =  list.length - 1; k >= 0; k--){
                  if(list[k].personId == list3[key3].personId ){
                    if($scope.drop[list3[key3].roleName] == false){
                      $scope.drop[list3[key3].roleName] = true;
                    }
                    currentPeopleList.push(list[k]);
                    //clearSelected[key].splice(key3,1);
                    var tmp ={};
                    tmp.personId = list3[key3].personId;
                    tmp.personName = list3[key3].personName;
                    splicelist[key].push(tmp);
                    remove[list[k].personId] =false;
                    //$log.debug('Hello ' +list3[key3].personId + list[k].personName + '!');
                    //$log.debug('Hello ' +list3[key3].personId+ " asdasd "+key3+ "ff"+ '!');
                  }
                };
              }
            });
          }
        });
        $scope.countSelected = 0;
        angular.forEach($scope.selectedRoles, function(value, key) {
          angular.forEach(splicelist[key], function(value2, key2) {
            //delete $scope.list2[key][value2];
            //$scope.list2[key][value2] = 1;
            // $scope.showRemove = false;
            angular.forEach($scope.list2[key], function(value3, key3) {
              if(value2.personId == value3.personId && value2.personName == value3.personName ){
                //$log.debug('Hello ' +key3+ '!');
                clearSelected[key].splice(key3,1);
              }
            })
          })
          $scope.countSelected = 	$scope.countSelected + clearSelected[key].length;
        });
        angular.forEach($scope.selectedRoles, function(value, key) {
          if($scope.list2[key].length == value){
            $scope.drop[key] = false;
          }
          if($scope.list2[key].length < value && $scope.selectedRole == key){
            $scope.drop[key] = true;
          }else {
            $scope.drop[key] = false;
          }
        });
        if($scope.countSelected == 0){
          $scope.showRemove = false;
        }
        //return $scope.removeSelected = {};
      }).then(function(){
        $scope.peopleList = currentPeopleList;
        $scope.list2 = clearSelected;
        $scope.removeSelected =remove;
      });
    };
  });
  app.controller('peopleToIssuesCtrl', function($scope, $state, $timeout, $http,$filter, $q, dataService, alertFactory,  $localStorage, $stateParams, $log, $window, $mdDialog) {
    $scope.projectId = $stateParams.projectId;
    $scope.sprintId = $stateParams.sprintId;
    $scope.drop = [];
    $scope.list2 = [];
    $scope.selectedSecurityLevel = [];
    $scope.issueDetails = [];
    $scope.selectedSecurityRisk = [];
    $scope.removeSelected = {};
    $http.get('/api/sprints/getSprintParticipants/'+$scope.sprintId+"/"+$scope.projectId).then(function(response)
    {
      $scope.peopleList = response.data;
    })
    .catch(function(response, status) {
      var optionalDelay = 5000;
      var $string = "Error in fetching list of sprint participants";
      alertFactory.addAuto('danger', $string, optionalDelay);
    });
    $http.get('api/issues/getSpecialIssuesInSprint/'+$scope.sprintId+"/"+$scope.projectId).then(function(response)
    {
      $scope.specialIssues = response.data;
    }).then(function(){
      angular.forEach($scope.specialIssues, function(value,key){
        $scope.list2[value.issueId] = [];
        $scope.selectedSecurityLevel[value.issueId] = value.securityLevel;
        $scope.selectedSecurityRisk[value.issueId] = value.securityRiskAnalysis;
        $scope.issueDetails[value.issueId] = false;
        $scope.drop[value.issueId] = true;
        angular.forEach($scope.peopleList, function(value2,key2){
          if(value2.personId == value.personId){
            var assignedIssue = {};
            assignedIssue.personId = value2.personId;
            assignedIssue.personName =  value2.personName;
            assignedIssue.issueId = value.issueId;
            $scope.list2[value.issueId].push(assignedIssue);
            if($scope.drop[value.issueId] == true){
              $scope.drop[value.issueId] = false;
            }
          }
        });
        value.issueStartDate = $filter('date')(value.issueStartDate, 'yyyy/MM/dd');
        value.issueDueDate = $filter('date')(value.issueDueDate, 'yyyy/MM/dd');
        if(value.issueEstimatedTime == null){
          value.issueEstimatedTime = '--';
        }
        if(value.issueDueDate == null){
          value.issueDueDate = '--';
        }
        if(value.issueDescription == ""){
          value.issueDescription = '--';
        }
        //$scope.list2[key].length = value;
      });
      $scope.showDetails = function(issueId){
        angular.forEach($scope.specialIssues, function(value,key){
          if(value.issueId!=issueId && $scope.issueDetails[value.issueId] == true){
            $scope.issueDetails[value.issueId] = false;
          }
        });
        $scope.issueDetails[issueId] = !$scope.issueDetails[issueId];
      }
      $scope.showRemove = false;
      $scope.numberOfParticipants =0;
      $scope.limitEntry = function(length,id,person){
        var initialPeopleList = [];
        $http.get('/api/sprints/getSprintParticipants/'+$scope.sprintId+"/"+$scope.projectId).then(function(response)
        {
          initialPeopleList   = response.data;
          $scope.peopleList = response.data;
        }).then(function(){
          //$log.debug('Hello im here ' +person.personName+ '!');
          $scope.showRemove = true;
          $scope.numberOfParticipants = $scope.numberOfParticipants +	length;
          person.issueId = id;
          angular.forEach($scope.specialIssues, function(value,key){
            var list3 = $scope.list2[value.issueId];
            angular.forEach(list3, function(value3, key3) {
              if(list3[key3].issueId == id && $scope.peopleList.length != initialPeopleList.length){
                var tmp ={};
                tmp.personId = list3[key3].personId;
                tmp.personName = list3[key3].personName;
                tmp.roleName = list3[key3].roleName;
                $scope.peopleList.push(tmp);
              }
            });
          });
          if((length+1)>1){
            return $scope.drop[id] = false;
          }
        });
      };
      $scope.removePeopleToIssues = function(ev){
        var count = 0;
        angular.forEach($scope.specialIssues, function(value, key) {
          if($scope.removeSelected[value.issueId] == true ){
            count = count +1;
          }
        });
        if(count == 0 ){
          var optionalDelay = 4000;
          var $string = "Select atleast one assignee";
          alertFactory.addAuto('warning', $string, optionalDelay);
          return null;
        }
        var clearSelected  = $scope.list2;
        var remove = $scope.removeSelected;
        var splicelist =[];
        var confirm = $mdDialog.confirm()
        .title('Would you like to remove the people allocated to issues?')
        .textContent('Add another person to replace existing assignee on Redmine')
        .ariaLabel('')
        .targetEvent(ev)
        .ok('Proceed!')
        .cancel('Cancel');
        $mdDialog.show(confirm).then(function() {
          angular.forEach($scope.specialIssues, function(value, key) {
            splicelist[value.issueId] = [];
            if($scope.list2[value.issueId]){
              var list3 = $scope.list2[value.issueId];
              angular.forEach(list3, function(value3, key3) {
                //$log.debug('Hello ' +value3.personName+ '!');
                if(list3[key3].personId && $scope.removeSelected[list3[key3].issueId] == true ){
                  if($scope.drop[list3[key3].issueId] == false){
                    $scope.drop[list3[key3].issueId] = true;
                  }
                  var tmp ={};
                  tmp.personId = list3[key3].personId;
                  tmp.personName = list3[key3].personName;
                  splicelist[value.issueId].push(tmp);
                  remove[value.issueId] =false;
                }
              });
            }
          });
          $scope.countSelected = 0;
          angular.forEach($scope.specialIssues, function(value, key) {
            angular.forEach(splicelist[value.issueId], function(value2, key2) {
              angular.forEach($scope.list2[value.issueId], function(value3, key3) {
                if(value2.personId == value3.personId && value2.personName == value3.personName ){
                  //$log.debug('Hello ' +key3+ '!');
                  clearSelected[value.issueId].splice(key3,1);
                  //$scope.selectedSecurityLevel[value.issueId]=null;
                  //$scope.selectedSecurityRisk[value.issueId] =null;
                }
              })
            })
            $scope.countSelected = 	$scope.countSelected + clearSelected[value.issueId].length;
          });
          if($scope.countSelected == 0){
            $scope.showRemove = false;
          }
          $scope.list2 = clearSelected;
          $scope.removeSelected =remove;
        });
      };
      $scope.savePeopleToIssues = function(){
        var listOfIssuesAllocated = [];
        angular.forEach($scope.specialIssues, function(value, key) {
          if($scope.list2[value.issueId]){
            var list3 = $scope.list2[value.issueId];
            //$log.debug('Hello ' +list3.length+ '!');
            angular.forEach(list3, function(value3, key3) {
              if(list3[key3].personId){
                if(list3[key3].personId && list3[key3].issueId ){
                  //clearSelected[key].splice(key3,1);
                  var tmp ={};
                  tmp.personId = list3[key3].personId;
                  tmp.issueId = list3[key3].issueId;
                  tmp.securityLevel = $scope.selectedSecurityLevel[value.issueId];
                  tmp.securityRiskAnalysis = $scope.selectedSecurityRisk[value.issueId];
                  //tmp.roleName = list3[key3].roleName;
                  listOfIssuesAllocated.push(tmp);
                }
              }
            });
          }
        });
        var issueDetails = {};
        issueDetails.projectId = $scope.projectId;
        issueDetails.sprintId = $scope.sprintId;
        issueDetails.issuesAllocated = listOfIssuesAllocated;
        $http.post('/api/issues/updateAllocatedIssues',issueDetails).then(function(response) {
          $state.go("management.sprints.sprintsTable");
          var $string = "Successfully added people to issues";
          var optionalDelay = 3000;
          alertFactory.addAuto('success', $string, optionalDelay);
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "Error in adding people to issues";
          alertFactory.addAuto('danger', $string, optionalDelay);
        })
      };
    })
  });
  app.controller('sprintRolesCtrl', function($scope, $state, $timeout, $http, $q, dataService, alertFactory,  $localStorage, $stateParams, $log, $window, $mdDialog) {
    $scope.projectId = $stateParams.projectId;
    $scope.sprintId = $stateParams.sprintId;
    var sprint = {};
    sprint.projectId = $scope.projectId;
    sprint.sprintId = $scope.sprintId;
    if($scope.sprintId != null){
      $http.put('api/sprints/sprintBriefSummary',sprint).then(function(response)
      {
        $scope.sprintDetails = response.data;
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching sprint details";
        alertFactory.addAuto('danger', $string, optionalDelay);
      })
    }
    $http.get('/api/projects/getNumberofParticipants/'+$scope.projectId).then(function(response)
    {
      $scope.rolesList= response.data;
    }).catch(function(response, status) {
      //	$scope.loading = false;
      var optionalDelay = 5000;
      var $string = "Error in fetching roles";
      alertFactory.addAuto('danger', $string, optionalDelay);
    }).then(function(){
      $scope.clearForm = function(formName){
        formName.$setPristine();
        $scope.formData.selectedRoles = {};
        $scope.maxError=false;
      }
      $scope.change= function(form1, role, maxNumber){
        $scope.maxError  = null;
        var keepGoing = true;
        angular.forEach(form1,function(value,key){
          if(key==role && keepGoing){
            angular.forEach(value.$error,function(value2,key2){
              //if(key2=="min" && value2==true ){
              //return	$scope.minError ="Participants value can't be negative";
              //}
              if(key2=="max" && value2==true){
                keepGoing = false;
                return $scope.maxError ="Number of "+role+"s can't be more than "+maxNumber;
                //$log.debug("max is "+JSON.stringify(value2));
              }
            })
          }
        });
        //var tmp = form1.$name+'.'+role
        //$log.debug(JSON.stringify(tmp));
      }
      $scope.formData = {};
      $scope.formData.selectedRoles = {};
      $scope.someSelected = function (object) {
        return Object.keys(object).some(function (key) {
          return object[key];
        });
      }
      $scope.increment = function(val,max){
        if($scope.formData.selectedRoles[val] == null){
          $scope.formData.selectedRoles[val] = 0;
        }
        if($scope.formData.selectedRoles[val] < max){
          $scope.formData.selectedRoles[val] = ($scope.formData.selectedRoles[val] +1);
        }
      }
      $scope.decrement = function(val,max){
        if($scope.formData.selectedRoles[val] != null && $scope.formData.selectedRoles[val] > 0 ){
          if($scope.formData.selectedRoles[val] <= max){
            $scope.formData.selectedRoles[val] = ($scope.formData.selectedRoles[val] -1);
          }
        }
        if($scope.formData.selectedRoles[val] == null){
          $scope.formData.selectedRoles[val] = max;
        }
      }
      $scope.createSprintRoles =function(){
        $state.go('management.sprints.peopleToSprint',{projectId : $scope.projectId, sprintId : $scope.sprintId, selectedRoles : $scope.formData.selectedRoles});
      }
    });
    $scope.manageProject = function(project) {
      $state.go("management.projects.editProject",project );
    }
    $scope.manageSprint = function(sprint) {
      $state.go("management.sprints.editSprint.existingSprint",sprint );
    }
  });
  app.controller('editSprintRequirementsCtrl', function($scope, $state, $timeout, sprintNav,$http, alertFactory, $q, dataService, alertFactory,  $localStorage, $stateParams, $log, $window, $mdDialog,GetSprintRequirementsService) {
    $scope.projectId = $stateParams.projectId;
    $scope.sprintId = $stateParams.sprintId;
    sprintNav.setSprintNav($stateParams.projectId, $stateParams.sprintId);
    $http.get('/api/sprints/getRequirementsSelectedInSprint/'+$stateParams.sprintId+'/'+$stateParams.projectId).then(function(response) {
      $scope.existingRequirementsOfSprint = response.data;
    })
    .then(function(){
      var tabClasses;
      function initTabs() {
        tabClasses = ["","","",""];
      }
      $scope.getTabClass = function (tabNum) {
        return tabClasses[tabNum];
      };
      $scope.getTabPaneClass = function (tabNum) {
        return "tab-pane " + tabClasses[tabNum];
      }
      $scope.setActiveTab = function (tabNum) {
        initTabs();
        tabClasses[tabNum] = "active";
      };
      initTabs();
      $scope.sl = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'Average', legend: 'Average'},
            {value: 'Good', legend: 'Good'},
            {value: 'High', legend: 'High'},
            {value: 'Very high', legend: 'Very high'}
          ]
        }
      }
      $scope.hideSearch = true;
      var tabs = [
        { title: 'Instructions', content: "You can select or add multiple requirements and indicate the level of requirement. If you added a new requirement, add a description."}
      ],
      selected = null,
      previous = null;
      $scope.tabs = tabs;
      $scope.removeTab = function (tab) {
        var index = tabs.indexOf(tab);
        tabs.splice(index, 1);
        if($scope.tabs.length == 0){
          $scope.tabs.push({ title: 'Instructions',content: "You can select or add multiple requirements and indicate the level of requirement. If you added a new requirement, add a description.", disabled: false});
          return  $scope.hideSearch= true;
        }
      };
      $scope.addTab = function (title, view) {
        for (var i =  tabs.length - 1; i >= 0; i--) {
          if(tabs[i].title == title){
            var optionalDelay = 5000;
            var $string = "You already selected the requirement "+ title;
            alertFactory.addAuto('warning', $string, optionalDelay);
            return;
          }
        }
        tabs.push({ title: title, content: view, disabled: false});
        if($scope.tabs.length >= 2){
          if(tabs[0].title == 'Instructions'){
            $scope.tabs.splice(0,1);
          }
        }
      };
      angular.isUndefinedOrNull = function(val) {
        return angular.isUndefined(val) || val === null
      }
      $scope.viewRequirements = function(){
        angular.forEach($scope.existingRequirementsOfSprint, function(value, key) {
          if(value.sprintRequirementId){
            $scope.addTab(value.sprintRequirementName,value.sprintRequirementDescription);
          }
        });
        angular.forEach($scope.tabs, function(value, key) {
          angular.forEach($scope.existingRequirementsOfSprint, function(value2, key2) {
            if(value.title == value2.sprintRequirementName && value.content == value2.sprintRequirementDescription ){
              value.requirementLevel = value2.requirementLevel;
              value.comment = value2.outcome;
            }
          });
        });
        $scope.hideSearch = false;
      };
      //$scope.selectedIndex = 1;
      $scope.removeTabs = function (ev) {
        var confirm = $mdDialog.confirm()
        .title("Would you like to clear all the requirements? ")
        .textContent('This will only clear all the tabs selected on this page.')
        .ariaLabel('')
        .targetEvent(ev)
        .ok('Proceed!')
        .cancel('Cancel');
        $mdDialog.show(confirm).then(function() {
          for (var i =  $scope.tabs.length - 1; i >= 0; i--) {
            $scope.removeTab($scope.tabs[i]);
          }
        });
      };
      $scope.searchText = "";
      $scope.selectedItem = [];
      $scope.isDisabled = false;
      $scope.noCache = true;
      $scope.searchTextChange = function (str) {
        return  GetSprintRequirementsService.getRequirements(str);
      }
      $scope.newState = function(ev,req) {
        var confirm = $mdDialog.confirm()
        .title("Would you like to add the requirement '"+req+"'?")
        .textContent('This will also add the requirement to the catalogue of requirements')
        .ariaLabel('')
        .targetEvent(ev)
        .ok('Proceed!')
        .cancel('Cancel');
        $mdDialog.show(confirm).then(function() {
          $http.post('/api/sprints/insertRequirement',req).then(function(response) {
            var $string = "Added requirement to the catalogue of requirements!";
            $scope.searchTextChange();
            $scope.searchTextChange(req);
            $scope.searchText = req;
            var txt = "{\"value\":\""+req+"\",\"display\":\""+req+"\"}";
            $scope.selectedItem = angular.fromJson(txt);
            // $scope.selectedItem= programmingSkillEntered.value;
            var optionalDelay = 3000;
            alertFactory.addAuto('success', $string, optionalDelay);
          })
          .catch(function(response, status) {
            var optionalDelay = 5000;
            var $string = "Error in adding the requirement to the catalogue of requirements";
            alertFactory.addAuto('danger', $string, optionalDelay);
          });
        });
      }
      $scope.saveRequirements = function(tabs){
        var RequirementsEntered = [];
        angular.forEach(tabs, function(value, key) {
          var requirements = {};
          requirements.projectId = $scope.projectId;
          requirements.sprintId = $scope.sprintId;
          requirements.sprintRequirementName = value.title;
          requirements.requirementLevel = value.requirementLevel;
          requirements.sprintRequirementDescription = value.description;
          requirements.outcome = value.comment;
          requirements.updatedBy = $localStorage.currentUser.userFirstName;
          RequirementsEntered.push(requirements);
        });
        $scope.requirements =RequirementsEntered ;
        $http.post('/api/sprints/updateRequirementsSelection',$scope.requirements).then(function(response)
        {
          var optionalDelay = 3000;
          var $string = "Sprint requirements updated!";
          $state.go("management.sprints.editSprint.sprintParticipants", response.data);
          alertFactory.addAuto('success', $string, optionalDelay);
        })
        .catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 3000;
          var $string = "Error in adding sprint requirements";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      }
      $scope.clearRequirements = function(){
        //$state.go("management.people.addPerson.capabilities",$scope.userId);
      }
      //var sprint = sprintNav.getSprintNav();
      //$scope.projectId = sprint.projectId;
      //$scope.sprintId = sprint.sprintId;
    })
  });
  app.controller('sprintRequirementsCtrl', function($scope, $state, $timeout, sprintNav,$http, alertFactory, $q, dataService, alertFactory,  $localStorage, $stateParams, $log, $window, $mdDialog,GetSprintRequirementsService) {
    //$scope.projectId = $stateParams.projectId;
    //$scope.sprintId = $stateParams.sprintId;
    sprintNav.setSprintNav($stateParams.projectId, $stateParams.sprintId);
    var tabClasses;
    function initTabs() {
      tabClasses = ["","","",""];
    }
    $scope.getTabClass = function (tabNum) {
      return tabClasses[tabNum];
    };
    $scope.getTabPaneClass = function (tabNum) {
      return "tab-pane " + tabClasses[tabNum];
    }
    $scope.setActiveTab = function (tabNum) {
      initTabs();
      tabClasses[tabNum] = "active";
    };
    initTabs();
    $scope.sl = {
      options: {
        hideLimitLabels: true,
        showTicks: true,
        showSelectionBar: true,
        hidePointerLabels : true,
        stepsArray: [
          {value: 'Undefined', legend: 'Undefined'},
          {value: 'Average', legend: 'Average'},
          {value: 'Good', legend: 'Good'},
          {value: 'High', legend: 'High'},
          {value: 'Very high', legend: 'Very high'}
        ]
      }
    }
    var tabs = [
      { title: 'Instructions', content: "You can select or add multiple requirements and indicate the level of requirement. If you added a new requirement, add a description."}
    ],
    selected = null,
    previous = null;
    $scope.tabs = tabs;
    $scope.selectedIndex = 1;
    $scope.removeTab = function (tab) {
      var index = tabs.indexOf(tab);
      tabs.splice(index, 1);
      if($scope.tabs.length == 0){
        $scope.tabs.push({ title: 'Instructions',content: "You can select or add multiple requirements and indicate the level of requirement. If you added a new requirement, add a description.", disabled: false});
      }
    };
    $scope.removeTabs = function (ev) {
      var confirm = $mdDialog.confirm()
      .title("Would you like to clear all the requirements? ")
      .textContent('This will only clear all the tabs selected on this page.')
      .ariaLabel('')
      .targetEvent(ev)
      .ok('Proceed!')
      .cancel('Cancel');
      $mdDialog.show(confirm).then(function() {
        for (var i =  $scope.tabs.length - 1; i >= 0; i--) {
          $scope.removeTab($scope.tabs[i]);
        }
      });
    };
    $scope.searchText = "";
    $scope.selectedItem = [];
    $scope.isDisabled = false;
    $scope.noCache = true;
    //$scope.searchTextChange("");
    $scope.searchTextChange = function (str) {
      return  GetSprintRequirementsService.getRequirements(str);
    }
    $scope.newState = function(ev,req) {
      var confirm = $mdDialog.confirm()
      .title("Would you like to add the requirement '"+req+"'?")
      .textContent('This will also add the requirement to the catalogue of requirements')
      .ariaLabel('')
      .targetEvent(ev)
      .ok('Proceed!')
      .cancel('Cancel');
      $mdDialog.show(confirm).then(function() {
        $http.post('/api/sprints/insertRequirement',req).then(function(response) {
          var $string = "Added requirement to the catalogue of requirements!";
          $scope.searchTextChange();
          $scope.searchTextChange(req);
          $scope.searchText = req;
          var txt = "{\"value\":\""+req+"\",\"display\":\""+req+"\"}";
          $scope.selectedItem = angular.fromJson(txt);
          // $scope.selectedItem= programmingSkillEntered.value;
          var optionalDelay = 3000;
          alertFactory.addAuto('success', $string, optionalDelay);
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "Error in adding the requirement to the catalogue of requirements";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      });
    }
    $scope.saveRequirements = function(tabs){
      var RequirementsEntered = [];
      angular.forEach(tabs, function(value, key) {
        var requirements = {};
        requirements.projectId = $scope.projectId;
        requirements.sprintId = $scope.sprintId;
        requirements.sprintRequirementName = value.title;
        requirements.requirementLevel = value.requirementLevel;
        requirements.sprintRequirementDescription = value.description;
        requirements.updatedBy = $localStorage.currentUser.userFirstName;
        RequirementsEntered.push(requirements);
      });
      $scope.requirements =RequirementsEntered ;
      $http.put('/api/sprints/insertRequirementsSelection',$scope.requirements).then(function(response)
      {
        var optionalDelay = 3000;
        var $string = "Sprint requirements saved!";
        $state.go("management.sprints.addSprint.sprintRoles", response.data);
        alertFactory.addAuto('success', $string, optionalDelay);
      })
      .catch(function(response, status) {
        //	$scope.loading = false;
        var optionalDelay = 3000;
        var $string = "Error in adding sprint requirements";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
    }
    $scope.clearRequirements = function(){
      //$state.go("management.people.addPerson.capabilities",$scope.userId);
    }
    $scope.addTab = function (title, view) {
      for (var i =  tabs.length - 1; i >= 0; i--) {
        if(tabs[i].title == title){
          var optionalDelay = 5000;
          var $string = "You already selected the requirement "+ title;
          alertFactory.addAuto('warning', $string, optionalDelay);
          return;
        }
      }
      tabs.push({ title: title, content: view, disabled: false});
      if($scope.tabs.length >= 2){
        if(tabs[0].title == 'Instructions'){
          $scope.tabs.splice(0,1);
        }
      }
    }
    var sprint = sprintNav.getSprintNav();
    $scope.projectId = sprint.projectId;
    $scope.sprintId = sprint.sprintId;
  });
  app.controller('editCompanyFactorsCtrl', function($scope, $state, $timeout, $http, alertFactory, $q, dataService, $localStorage, $stateParams, $log, $window, $mdDialog, newEnvironments,sprintNav, GetSprintEnvironmentsService) {
    sprintNav.setSprintNav($stateParams.projectId, $stateParams.sprintId);
    var sprint = sprintNav.getSprintNav();
    $scope.projectId = sprint.projectId;
    $scope.sprintId = sprint.sprintId;
    $scope.formData = {};
    $scope.formData.selectedEnvironments = {};
    $scope.someSelected = function (object) {
      return Object.keys(object).some(function (key) {
        return object[key];
      });
    }
    var tabClasses;
    function initTabs() {
      tabClasses = ["","","",""];
    }
    $scope.getTabClass = function (tabNum) {
      return tabClasses[tabNum];
    };
    $scope.getTabPaneClass = function (tabNum) {
      return "tab-pane " + tabClasses[tabNum];
    }
    $scope.setActiveTab = function (tabNum) {
      initTabs();
      tabClasses[tabNum] = "active";
    };
    initTabs();
    $http.get('/api/sprints/getAllSprintDevelopmentEnvironments').then(function(response)
    {
      $scope.developmentEnvironmentsList = response.data;
    })
    .catch(function(response, status) {
      var optionalDelay = 5000;
      var $string = "Error in fetching list of development environments";
      alertFactory.addAuto('danger', $string, optionalDelay);
    })
    $http.get('/api/sprints/getCompanyDrivenFactors/'+$scope.sprintId+'/'+$scope.projectId).then(function(response)
    {
      $scope.listOfCompanyDrivenFactorsForSprint = response.data;
    }).then(function(){
      $scope.$broadcast('rzSliderForceRender');
      $scope.companyFactorsSlider = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'Low', legend: 'Low'},
            {value: 'Medium', legend: 'Medium'},
            {value: 'High', legend: 'High'},
            {value: 'Very high', legend: 'Very high'}
          ]
        }
      };
      if($scope.listOfCompanyDrivenFactorsForSprint.coachingPotential != null &&
        $scope.listOfCompanyDrivenFactorsForSprint.teamKnowledgeDiversity != null){
          $scope.slider1 = $scope.listOfCompanyDrivenFactorsForSprint.coachingPotential;
          $scope.slider2 = $scope.listOfCompanyDrivenFactorsForSprint.teamKnowledgeDiversity;
        }
        angular.forEach($scope.listOfCompanyDrivenFactorsForSprint.developmentEnvironments, function(value, key) {
          $scope.formData.selectedEnvironments[value.envName] = true;
        });
      })
      $scope.addComapnyDrivenFactors = function(){
        var factors = {};
        $http.get('/api/sprints/getAllSprintDevelopmentEnvironments').then(function(response)
        {
          $scope.updatedDevelopmentEnvironmentsList = response.data;
        }).then(function(){
          var de =[];
          for (var i = $scope.updatedDevelopmentEnvironmentsList.length - 1; i >= 0; i--) {
            angular.forEach($scope.formData.selectedEnvironments, function(value2, key2) {
              if ($scope.updatedDevelopmentEnvironmentsList[i].envName == key2) {
                if (value2 == true) {
                  var txt = "{\"envId\":"+$scope.updatedDevelopmentEnvironmentsList[i].envId+",\"envName\":\""+$scope.updatedDevelopmentEnvironmentsList[i].envName+"\"}";
                  var txt2 = angular.fromJson(txt);
                  this.push(txt2);
                }
              }
            },de );
          }
          factors.developmentEnvironments = angular.bind(this,de);
          factors.projectId = $scope.projectId;
          factors.sprintId = $scope.sprintId;
          factors.coachingPotential = $scope.slider1;
          factors.teamKnowledgeDiversity = $scope.slider2;
          factors.updatedBy = $localStorage.currentUser.userFirstName;
          $scope.factors= factors;
        }).then (function(){
          $http.post('/api/sprints/insertCompanyDrivenFactors',$scope.factors).then(function(response)
          {
            var optionalDelay = 3000;
            var $string = "Updated company driven factors of the sprint";
            newEnvironments.resetEnvironments();
            $scope.extraEnvironmentsList =[];
            alertFactory.addAuto('success', $string, optionalDelay);
            $state.go("management.sprints.editSprint.sprintRequirements", response.data, {reload: true} );
          }).catch(function(response, status) {
            var optionalDelay = 5000;
            var $string = "Error in adding company driven factors to the sprint. "+ response.data.error;
            alertFactory.addAuto('danger', $string, optionalDelay);
          })
        })
      };
      $scope.openDialog =function($event){
        $mdDialog.show({
          controller: function DialogCtrl3 ($timeout, $q, $scope, $mdDialog, GetSprintEnvironmentsService, $http, alertFactory, $state, newEnvironments) {
            // list of `state` value/display objects
            $scope.searchText = "";
            $scope.selectedItem = [];
            $scope.isDisabled = false;
            $scope.noCache = true;
            $scope.extraEnvironmentsList = null;
            $scope.cancel = function($event) {
              $mdDialog.cancel();
            };
            $scope.finish = function($event) {
              $mdDialog.hide();
              //$state.go("management.sprints.addSprint.newSprint");
            };
            $scope.searchTextChange = function (str) {
              return  GetSprintEnvironmentsService.getEnvironments(str);
            }
            $scope.newState = function(envi) {
              $http.post('/api/sprints/insertEnvironment',envi).then(function(response) {
                var $string = "Added "+envi+" to the catalogue of enironments!";
                //$scope.searchTextChange();
                var addedEnvironment = {};
                addedEnvironment.envId = 0;
                addedEnvironment.envName = envi;
                //$scope.extraDomainsList = addedDomain;
                newEnvironments.setAddedEnvironments(addedEnvironment);
                var optionalDelay = 2000;
                $mdDialog.hide();
                alertFactory.addAuto('success', $string, optionalDelay);
              })
              .catch(function(response, status) {
                var optionalDelay = 5000;
                var $string = "Error in adding"+envi+" to the list of environments";
                alertFactory.addAuto('danger', $string, optionalDelay);
              });
            }
          },
          templateUrl: 'sprints/dialog3.tmpl.html',
          //parent: angular.element(document.getElementById('extraEnvironments')),
          parent: angular.element(document.body),
          targetEvent: $event,
          clickOutsideToClose:true
        })
      }
      if (newEnvironments.getAddedEnvironments() != []){
        $scope.extraEnvironmentsList = newEnvironments.getAddedEnvironments();
      }
      angular.element(document).ready(function () {
        $scope.$broadcast('rzSliderForceRender');
      });
    });
    app.controller('companyFactorsCtrl', function($scope, $state, $timeout, $http, alertFactory, $q, dataService, $localStorage, $stateParams, $log, $window, $mdDialog, newEnvironments,sprintNav, GetSprintEnvironmentsService) {
      sprintNav.setSprintNav($stateParams.projectId, $stateParams.sprintId);
      var sprint = sprintNav.getSprintNav();
      $scope.projectId = sprint.projectId;
      $scope.sprintId = sprint.sprintId;
      $scope.formData = {};
      $scope.formData.selectedEnvironments = {};
      $scope.someSelected = function (object) {
        return Object.keys(object).some(function (key) {
          return object[key];
        });
      }
      var tabClasses;
      function initTabs() {
        tabClasses = ["","","",""];
      }
      $scope.getTabClass = function (tabNum) {
        return tabClasses[tabNum];
      };
      $scope.getTabPaneClass = function (tabNum) {
        return "tab-pane " + tabClasses[tabNum];
      }
      $scope.setActiveTab = function (tabNum) {
        initTabs();
        tabClasses[tabNum] = "active";
      };
      initTabs();
      $http.get('/api/sprints/getAllSprintDevelopmentEnvironments').then(function(response)
      {
        $scope.developmentEnvironmentsList = response.data;
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching list of development environments";
        alertFactory.addAuto('danger', $string, optionalDelay);
      })
      $scope.$broadcast('rzSliderForceRender');
      $scope.companyFactorsSlider = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'Low', legend: 'Low'},
            {value: 'Medium', legend: 'Medium'},
            {value: 'High', legend: 'High'},
            {value: 'Very high', legend: 'Very high'}
          ]
        }
      };
      $scope.addComapnyDrivenFactors = function(){
        var factors = {};
        $http.get('/api/sprints/getAllSprintDevelopmentEnvironments').then(function(response)
        {
          $scope.updatedDevelopmentEnvironmentsList = response.data;
        }).then(function(){
          var de =[];
          for (var i = $scope.updatedDevelopmentEnvironmentsList.length - 1; i >= 0; i--) {
            angular.forEach($scope.formData.selectedEnvironments, function(value2, key2) {
              if ($scope.updatedDevelopmentEnvironmentsList[i].envName == key2) {
                if (value2 == true) {
                  var txt = "{\"envId\":"+$scope.updatedDevelopmentEnvironmentsList[i].envId+",\"envName\":\""+$scope.updatedDevelopmentEnvironmentsList[i].envName+"\"}";
                  var txt2 = angular.fromJson(txt);
                  this.push(txt2);
                }
              }
            },de );
          }
          factors.developmentEnvironments = angular.bind(this,de);
          factors.projectId = $scope.projectId;
          factors.sprintId = $scope.sprintId;
          factors.coachingPotential = $scope.slider1;
          factors.teamKnowledgeDiversity = $scope.slider2;
          factors.updatedBy = $localStorage.currentUser.userFirstName;
          $scope.factors= factors;
        }).then (function(){
          $http.post('/api/sprints/insertCompanyDrivenFactors',$scope.factors).then(function(response)
          {
            var optionalDelay = 3000;
            var $string = "Added company driven factors to the sprint";
            newEnvironments.resetEnvironments();
            $scope.extraEnvironmentsList =[];
            alertFactory.addAuto('success', $string, optionalDelay);
            $state.go("management.sprints.addSprint.sprintRequirements", response.data, {reload: true} );
          })
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "Error in adding company driven factors to the sprint. "+ response.data.error;
          alertFactory.addAuto('danger', $string, optionalDelay);
        })
      };
      $scope.openDialog =function($event){
        $mdDialog.show({
          controller: function DialogCtrl3 ($timeout, $q, $scope, $mdDialog, GetSprintEnvironmentsService, $http, alertFactory, $state, newEnvironments) {
            // list of `state` value/display objects
            $scope.searchText = "";
            $scope.selectedItem = [];
            $scope.isDisabled = false;
            $scope.noCache = true;
            $scope.extraEnvironmentsList = null;
            $scope.cancel = function($event) {
              $mdDialog.cancel();
            };
            $scope.finish = function($event) {
              $mdDialog.hide();
              //$state.go("management.sprints.addSprint.newSprint");
            };
            $scope.searchTextChange = function (str) {
              return  GetSprintEnvironmentsService.getEnvironments(str);
            }
            $scope.newState = function(envi) {
              $http.post('/api/sprints/insertEnvironment',envi).then(function(response) {
                var $string = "Added "+envi+" to the catalogue of enironments!";
                //$scope.searchTextChange();
                var addedEnvironment = {};
                addedEnvironment.envId = 0;
                addedEnvironment.envName = envi;
                //$scope.extraDomainsList = addedDomain;
                newEnvironments.setAddedEnvironments(addedEnvironment);
                var optionalDelay = 2000;
                $mdDialog.hide();
                alertFactory.addAuto('success', $string, optionalDelay);
              })
              .catch(function(response, status) {
                var optionalDelay = 5000;
                var $string = "Error in adding"+envi+" to the list of environments";
                alertFactory.addAuto('danger', $string, optionalDelay);
              });
            }
          },
          templateUrl: 'sprints/dialog3.tmpl.html',
          //parent: angular.element(document.getElementById('extraEnvironments')),
          parent: angular.element(document.body),
          targetEvent: $event,
          clickOutsideToClose:true
        })
      }
      if (newEnvironments.getAddedEnvironments() != []){
        $scope.extraEnvironmentsList = newEnvironments.getAddedEnvironments();
      }
    });
    app.controller('existingSprintCtrl', function($scope, $filter, $state, $timeout,$location, $http, alertFactory, $base64, $q, dataService, newDomains, alertFactory,  $localStorage, $stateParams, $log, $window, $mdDialog, GetSprintDomainsService, GetSprintAssetsService, newAssets) {
      $scope.projectId = $stateParams.projectId;
      $scope.sprintId = $stateParams.sprintId;
      $scope.checkFinished = function(sprint){
        if($scope.sprintStatus == 'false'){
          var tmp = {};
          tmp.projectId = $scope.projectId;
          tmp.sprintId = $scope.sprintId;
          $state.go("management.sprints.editSprint.questionnaire",tmp);
        }
      }
      $scope.formatProjects = function(projectsList, currentProject, indent){
        for(var i=0; i<currentProject.projectList.length; i++){
          var project = {};
          project.projectId = currentProject.projectList[i].projectId;
          project.projectName = " - ".repeat(indent)+currentProject.projectList[i].projectName;
          projectsList.push(project);
          var updatedProject = currentProject.projectList[i];
          if(updatedProject.projectList !=null && updatedProject.projectList.length !=0){
            indent++;
            $scope.formatProjects(projectsList, updatedProject, indent);
            indent--;
          }
        }
      };
      $http.get('/api/projects/getAllProjectsNested').then(function(response)
      {
        var projects = response.data;
        var projectsList = [];
        angular.forEach(projects, function(value, key) {
          var project = {};
          project.projectId = value.projectId;
          project.projectName = value.projectName;
          projectsList.push(project);
          if(value.projectList !=null && value.projectList.length !=0){
            var indent = 1;
            $scope.formatProjects(projectsList, value, indent);
          }
        });
        $scope.projectsList = projectsList;
      }).then(function(){
        $http.get('/api/sprints/getAllSprintDomains').then(function(response)
        {
          $scope.domainsList= response.data;
        })
        .catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching updated list of domains";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      }).then(function(){
        $http.get('/api/sprints/getAllSprintAssets').then(function(response)
        {
          $scope.assetsList= response.data;
        })
        .catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching updated list of assets";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      }).then(function(){
        $http.get('/api/sprints/existingSprintDetails/'+$scope.sprintId+'/'+$scope.projectId).then(function(response)
        {
          $scope.sprint= response.data;
        }).then(function(){
          $http.get('/api/sprints/getSprintParticipants/'+$scope.sprintId+'/'+$scope.projectId).then(function(response)
          {
            $scope.sprintParticipants= response.data;
            angular.forEach($scope.sprintParticipants, function(value, key) {
              if(value.roleName == null){
                $scope.displayInfo = true;
              }
            });
            if($scope.sprintParticipants.length==0){
              $scope.displayAddButton = true;
            }
          });
        }).then(function(){
          var existingSprint = $scope.sprint;
          $scope.sprintStartDate = new Date(existingSprint.sprintStartDate);
          if(existingSprint.sprintStartDate == null){
            $scope.sprintStartDate = new Date();
          }
          existingSprint.sprintStartDate = $filter('date')(existingSprint.sprintStartDate, 'yyyy/MM/dd');
          existingSprint.sprintEndDate = $filter('date')(existingSprint.sprintEndDate, 'yyyy/MM/dd');
          if(existingSprint.sprintStatus == 'open'){
            $scope.sprint.sprintStatus = 'true';
          }else{
            $scope.sprint.sprintStatus = 'false';
          }
          var tabClasses;
          function initTabs() {
            tabClasses = ["","","",""];
          }
          $scope.getTabClass = function (tabNum) {
            return tabClasses[tabNum];
          };
          $scope.getTabPaneClass = function (tabNum) {
            return "tab-pane " + tabClasses[tabNum];
          }
          $scope.setActiveTab = function (tabNum) {
            initTabs();
            tabClasses[tabNum] = "active";
          };
          initTabs();
          $scope.minDate = new Date(
            $scope.sprintStartDate.getFullYear(),
            $scope.sprintStartDate.getMonth(),
            $scope.sprintStartDate.getDate()
          );
          $scope.maxDate = new Date(
            $scope.sprintStartDate.getFullYear(),
            $scope.sprintStartDate.getMonth() + 3,
            $scope.sprintStartDate.getDate()
          );
          $scope.onlyWeekdaysPredicate = function(date) {
            var day = date.getDay();
            return day === 1 || day === 2 || day  ==3 || day == 4 || day ==5;
          };
          $scope.formData = {};
          $scope.formData2 = {};
          $scope.formData.selectedDomains = {};
          $scope.formData2.selectedAssets = {};
          angular.forEach($scope.sprint.kindOfAssets, function(value, key) {
            $scope.formData2.selectedAssets[value.assetName] = true;
          });
          angular.forEach($scope.sprint.applicationDomain, function(value, key) {
            $scope.formData.selectedDomains[value.domainName] = true;
          });
          $scope.someSelected = function (object) {
            return Object.keys(object).some(function (key) {
              return object[key];
            });
          }
          $scope.clearSprint =function(ev, sprint){
            var confirm = $mdDialog.confirm()
            .title('Would you like to clear all the sprint details?')
            .textContent('This will only clear the sprint details on this page. The sprint on Redmine remains unaffected.')
            .ariaLabel('')
            .targetEvent(ev)
            .ok('Proceed!')
            .cancel('Cancel');
            $mdDialog.show(confirm).then(function() {
              $scope.sprint={};
              $scope.form.$setPristine();
              $scope.formData.selectedDomains=[];
              $scope.formData2.selectedAssets =[];
            });
          }
          $scope.openDialog =function openDialog($event){
            $mdDialog.show({
              controller: function DialogCtrl ($timeout, $q, $scope, $mdDialog, GetSprintDomainsService, $http, alertFactory, $state, newDomains) {
                // list of `state` value/display objects
                $scope.searchText = "";
                $scope.selectedItem = [];
                $scope.isDisabled = false;
                $scope.noCache = true;
                $scope.extraDomainsList = null;
                $scope.cancel = function($event) {
                  $mdDialog.cancel();
                };
                $scope.finish = function($event) {
                  $mdDialog.hide();
                  //$state.go("management.sprints.addSprint.newSprint");
                };
                $scope.searchTextChange = function (str) {
                  return  GetSprintDomainsService.getDomains(str);
                }
                $scope.newState = function(domain) {
                  $http.post('/api/sprints/insertDomain',domain).then(function(response) {
                    var $string = "Added "+ domain +" to the catalogue of domains!";
                    //$scope.searchTextChange();
                    //$scope.searchTextChange(domain);
                    //$scope.searchText = domain;
                    // $scope.selectedItem= programmingSkillEntered.value;
                    var addedDomain = {};
                    addedDomain.domainId = 0;
                    addedDomain.domainName = domain;
                    //$scope.extraDomainsList = addedDomain;
                    newDomains.setAddedDomains(addedDomain);
                    var optionalDelay = 2000;
                    $mdDialog.hide();
                    alertFactory.addAuto('success', $string, optionalDelay);
                  })
                  .catch(function(response, status) {
                    var optionalDelay = 5000;
                    var $string = "Error in adding domain to the list of domains";
                    alertFactory.addAuto('danger', $string, optionalDelay);
                  });
                }
              } ,
              templateUrl: 'sprints/dialog.tmpl.html',
              parent: angular.element(document.body),
              //parent: angular.element(document.getElementById('extras')),
              targetEvent: $event,
              clickOutsideToClose:true,
            })
            //
          }
          $scope.openDialog2 =function($event){
            $mdDialog.show({
              controller: function DialogCtrl2 ($timeout, $q, $scope, $mdDialog, GetSprintAssetsService, $http, alertFactory, $state, newAssets) {
                // list of `state` value/display objects
                $scope.searchText = "";
                $scope.selectedItem = [];
                $scope.isDisabled = false;
                $scope.noCache = true;
                $scope.extraAssetsList = null;
                $scope.cancel = function($event) {
                  $mdDialog.cancel();
                };
                $scope.finish = function($event) {
                  $mdDialog.hide();
                };
                $scope.searchTextChange = function (str) {
                  return  GetSprintAssetsService.getAssets(str);
                }
                $scope.newState = function(asset) {
                  $http.post('/api/sprints/insertAsset',asset).then(function(response) {
                    var $string = "Added "+asset+" to the catalogue of assets!";
                    $scope.searchTextChange();
                    var addedAsset = {};
                    addedAsset.assetId = 0;
                    addedAsset.assetName = asset;
                    //$scope.extraDomainsList = addedDomain;
                    newAssets.setAddedAssets(addedAsset);
                    var optionalDelay = 2000;
                    $mdDialog.hide();
                    alertFactory.addAuto('success', $string, optionalDelay);
                  })
                  .catch(function(response, status) {
                    var optionalDelay = 5000;
                    var $string = "Error in adding asset to the list of assets";
                    alertFactory.addAuto('danger', $string, optionalDelay);
                  });
                }
              },
              templateUrl: 'sprints/dialog2.tmpl.html',
              //parent: angular.element(document.getElementById('extras')),
              parent: angular.element(document.body),
              targetEvent: $event,
              clickOutsideToClose:true
            })
          }
          if (newDomains.getAddedDomains() != []){
            $scope.extraDomainsList = newDomains.getAddedDomains();
          }
          if (newAssets.getAddedAssets() != []){
            $scope.extraAssetsList = newAssets.getAddedAssets();
          }
          $scope.latestassets = function(){
            $http.get('/api/sprints/getAllSprintAssets').then(function(response)
            {
              $scope.latestassetsList= response.data;
            })
          };
          $scope.updateSprint = function(sprint){
            //var newDomainNames = $scope.formData.selectedDomains ;
            //var newAssetNames = $scope.formData.selectedAssets ;
            var createSprint = sprint;
            $http.get('/api/sprints/getAllSprintDomainsAssets').then(function(response)
            {
              var list = response.data;
              $scope.latestdomainsList = list.latestdomainsList;
              $scope.latestassetsList = list.latestassetsList ;
            }).then(function(){
              var ee =[];
              for (var i =  $scope.latestdomainsList.length - 1; i >= 0; i--) {
                angular.forEach($scope.formData.selectedDomains, function(value2, key2) {
                  if ($scope.latestdomainsList[i].domainName == key2) {
                    if (value2 == true) {
                      var txt = "{\"domainId\":"+$scope.latestdomainsList[i].domainId+",\"domainName\":\""+$scope.latestdomainsList[i].domainName+"\"}";
                      var txt2 = angular.fromJson(txt);
                      this.push(txt2);
                    }
                  }
                },ee );
              }
              createSprint.applicationDomain = angular.bind(this,ee);
            }).then(function(){
              var an =[];
              for (var i =  $scope.latestassetsList.length - 1; i >= 0; i--) {
                angular.forEach($scope.formData2.selectedAssets, function(value2, key2) {
                  if ($scope.latestassetsList[i].assetName == key2) {
                    if (value2 == true) {
                      var txt = "{\"assetId\":"+$scope.latestassetsList[i].assetId+",\"assetName\":\""+$scope.latestassetsList[i].assetName+"\"}";
                      var txt2 = angular.fromJson(txt);
                      this.push(txt2);
                    }
                  }
                },an );
              }
              createSprint.kindOfAssets = angular.bind(this,an);
              createSprint.sprintUpdatedBy = $localStorage.currentUser.userFirstName;
              createSprint.sprintStartDate = new Date(sprint.sprintStartDate);
              createSprint.sprintEndDate = new Date(sprint.sprintEndDate);
              //if($scope.sprintStatus == true){
              //createSprint.sprintStatus = 'true';
              //	}
              //else if($scope.sprintStatus == false){
              //createSprint.sprintStatus = 'false';
              //}
              $scope.createSprint = createSprint;
            }).then(function(){
              $http.post('/api/sprints/updateSprint',$scope.createSprint).then(function(response) {
                var createdSprint = response.data;
                $state.go("management.sprints.editSprint.companyFactors", createdSprint);
                var $string = "Successfully updated the sprint " +sprint.sprintName;
                var optionalDelay = 3000;
                alertFactory.addAuto('success', $string, optionalDelay);
                newDomains.resetDomains();
                newAssets.resetAssets();
                $scope.extraDomainsList=[];
                $scope.extraAssetsList =[];
              }) .catch(function(response, status) {
                var optionalDelay = 5000;
                var $string = {};
                if(response.data.message !=null){
                  $string = response.data.message;
                  if($string = "Forbidden. Please check whether the user has proper permissions"){
                    $string = $string+". Perhaps, the project is closed!"
                  }
                }else {
                  $string = "Error in updating the sprint.";
                }
                alertFactory.addAuto('danger', $string, optionalDelay);
              });
            })
          }
          $scope.editSprintParticipants  = function(){
            var sprintDetails = {};
            sprintDetails.projectId = $scope.projectId;
            sprintDetails.sprintId = $scope.sprintId;
            $state.go("management.sprints.editSprint.sprintParticipants",sprintDetails);
          }
        });
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching list of projects";
        alertFactory.addAuto('danger', $string, optionalDelay);
      })
    });
    app.controller('newSprintCtrl', function($scope, $state, $timeout,$location, $http, alertFactory, $base64, $q, dataService, newDomains, alertFactory,  $localStorage, $stateParams, $log, $window, $mdDialog, GetSprintDomainsService, GetSprintAssetsService, newAssets) {
      $scope.sprintStartDate = new Date();
      $scope.minDate = new Date(
        $scope.sprintStartDate.getFullYear(),
        $scope.sprintStartDate.getMonth(),
        $scope.sprintStartDate.getDate()
      );
      $scope.maxDate = new Date(
        $scope.sprintStartDate.getFullYear(),
        $scope.sprintStartDate.getMonth() + 3,
        $scope.sprintStartDate.getDate()
      );
      $scope.minDate2 = $scope.sprintStartDate;
      $scope.onlyWeekdaysPredicate = function(date) {
        var day = date.getDay();
        return day === 1 || day === 2 || day  ==3 || day == 4 || day ==5;
      };
      $scope.formData = {};
      $scope.formData2 = {};
      $scope.formData.selectedDomains = {};
      $scope.formData2.selectedAssets = {};
      $scope.someSelected = function (object) {
        return Object.keys(object).some(function (key) {
          return object[key];
        });
      }
      var tabClasses;
      function initTabs() {
        tabClasses = ["","","",""];
      }
      $scope.getTabClass = function (tabNum) {
        return tabClasses[tabNum];
      };
      $scope.getTabPaneClass = function (tabNum) {
        return "tab-pane " + tabClasses[tabNum];
      }
      $scope.setActiveTab = function (tabNum) {
        initTabs();
        tabClasses[tabNum] = "active";
      };
      initTabs();
      $scope.formatProjects = function(projectsList, currentProject, indent){
        for(var i=0; i<currentProject.projectList.length; i++){
          var project = {};
          project.projectId = currentProject.projectList[i].projectId;
          project.projectName = " - ".repeat(indent)+currentProject.projectList[i].projectName;
          projectsList.push(project);
          var updatedProject = currentProject.projectList[i];
          if(updatedProject.projectList !=null && updatedProject.projectList.length !=0){
            indent++;
            $scope.formatProjects(projectsList, updatedProject, indent);
            indent--;
          }
        }
      };
      $http.get('/api/projects/getAllProjectsNested').then(function(response)
      {
        var projects = response.data;
        var projectsList = [];
        angular.forEach(projects, function(value, key) {
          var project = {};
          project.projectId = value.projectId;
          project.projectName = value.projectName;
          projectsList.push(project);
          if(value.projectList !=null && value.projectList.length !=0){
            var indent = 1;
            $scope.formatProjects(projectsList, value, indent);
          }
        });
        $scope.projectsList = projectsList;
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching list of projects";
        alertFactory.addAuto('danger', $string, optionalDelay);
      })
      $http.get('/api/sprints/getAllSprintDomains').then(function(response)
      {
        $scope.domainsList= response.data;
      })
      .catch(function(response, status) {
        //	$scope.loading = false;
        var optionalDelay = 5000;
        var $string = "Error in fetching updated list of domains";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
      $http.get('/api/sprints/getAllSprintAssets').then(function(response)
      {
        $scope.assetsList= response.data;
      })
      .catch(function(response, status) {
        //	$scope.loading = false;
        var optionalDelay = 5000;
        var $string = "Error in fetching updated list of assets";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
      $scope.createSprint = function(sprint){
        //var newDomainNames = $scope.formData.selectedDomains ;
        //var newAssetNames = $scope.formData.selectedAssets ;
        var createSprint = sprint;
        $http.get('/api/sprints/getAllSprintDomainsAssets').then(function(response)
        {
          var list = response.data;
          $scope.latestdomainsList = list.latestdomainsList;
          $scope.latestassetsList = list.latestassetsList ;
        }).then(function(){
          var ee =[];
          for (var i =  $scope.latestdomainsList.length - 1; i >= 0; i--) {
            angular.forEach($scope.formData.selectedDomains, function(value2, key2) {
              if ($scope.latestdomainsList[i].domainName == key2) {
                if (value2 == true) {
                  var txt = "{\"domainId\":"+$scope.latestdomainsList[i].domainId+",\"domainName\":\""+$scope.latestdomainsList[i].domainName+"\"}";
                  var txt2 = angular.fromJson(txt);
                  this.push(txt2);
                }
              }
            },ee );
          }
          createSprint.applicationDomain = angular.bind(this,ee);
        }).then(function(){
          var an =[];
          for (var i =  $scope.latestassetsList.length - 1; i >= 0; i--) {
            angular.forEach($scope.formData2.selectedAssets, function(value2, key2) {
              if ($scope.latestassetsList[i].assetName == key2) {
                if (value2 == true) {
                  var txt = "{\"assetId\":"+$scope.latestassetsList[i].assetId+",\"assetName\":\""+$scope.latestassetsList[i].assetName+"\"}";
                  var txt2 = angular.fromJson(txt);
                  this.push(txt2);
                }
              }
            },an );
          }
          createSprint.kindOfAssets = angular.bind(this,an);
          createSprint.sprintUpdatedBy = $localStorage.currentUser.userFirstName;
          $scope.createSprint = createSprint;
        }).then(function(){
          $http.put('/api/sprints/addNewSprint',$scope.createSprint).then(function(response) {
            var createdSprint = response.data;
            $state.go("management.sprints.addSprint.companyFactors", createdSprint);
            var $string = "Successfully added the sprint " +sprint.sprintName;
            var optionalDelay = 3000;
            alertFactory.addAuto('success', $string, optionalDelay);
            newDomains.resetDomains();
            newAssets.resetAssets();
            $scope.extraDomainsList=[];
            $scope.extraAssetsList =[];
          })
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "Error in adding the sprint.";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      }
      $scope.openDialog =function openDialog($event){
        $mdDialog.show({
          controller: function DialogCtrl ($timeout, $q, $scope, $mdDialog, GetSprintDomainsService, $http, alertFactory, $state, newDomains) {
            // list of `state` value/display objects
            $scope.searchText = "";
            $scope.selectedItem = [];
            $scope.isDisabled = false;
            $scope.noCache = true;
            $scope.extraDomainsList = null;
            $scope.cancel = function($event) {
              $mdDialog.cancel();
            };
            $scope.finish = function($event) {
              $mdDialog.hide();
              //$state.go("management.sprints.addSprint.newSprint");
            };
            $scope.searchTextChange = function (str) {
              return  GetSprintDomainsService.getDomains(str);
            }
            $scope.newState = function(domain) {
              $http.post('/api/sprints/insertDomain',domain).then(function(response) {
                var $string = "Added "+ domain +" to the catalogue of domains!";
                //$scope.searchTextChange();
                //$scope.searchTextChange(domain);
                //$scope.searchText = domain;
                // $scope.selectedItem= programmingSkillEntered.value;
                var addedDomain = {};
                addedDomain.domainId = 0;
                addedDomain.domainName = domain;
                //$scope.extraDomainsList = addedDomain;
                newDomains.setAddedDomains(addedDomain);
                var optionalDelay = 2000;
                $mdDialog.hide();
                alertFactory.addAuto('success', $string, optionalDelay);
              })
              .catch(function(response, status) {
                var optionalDelay = 5000;
                var $string = "Error in adding domain to the list of domains";
                alertFactory.addAuto('danger', $string, optionalDelay);
              });
            }
          } ,
          templateUrl: 'sprints/dialog.tmpl.html',
          parent: angular.element(document.body),
          //parent: angular.element(document.getElementById('extras')),
          targetEvent: $event,
          clickOutsideToClose:true,
        })
        //
      }
      $scope.openDialog2 =function($event){
        $mdDialog.show({
          controller: function DialogCtrl2 ($timeout, $q, $scope, $mdDialog, GetSprintAssetsService, $http, alertFactory, $state, newAssets) {
            // list of `state` value/display objects
            $scope.searchText = "";
            $scope.selectedItem = [];
            $scope.isDisabled = false;
            $scope.noCache = true;
            $scope.extraAssetsList = null;
            $scope.cancel = function($event) {
              $mdDialog.cancel();
            };
            $scope.finish = function($event) {
              $mdDialog.hide();
            };
            $scope.searchTextChange = function (str) {
              return  GetSprintAssetsService.getAssets(str);
            }
            $scope.newState = function(asset) {
              $http.post('/api/sprints/insertAsset',asset).then(function(response) {
                var $string = "Added "+asset+" to the catalogue of assets!";
                $scope.searchTextChange();
                var addedAsset = {};
                addedAsset.assetId = 0;
                addedAsset.assetName = asset;
                //$scope.extraDomainsList = addedDomain;
                newAssets.setAddedAssets(addedAsset);
                var optionalDelay = 2000;
                $mdDialog.hide();
                alertFactory.addAuto('success', $string, optionalDelay);
              })
              .catch(function(response, status) {
                var optionalDelay = 5000;
                var $string = "Error in adding asset to the list of assets";
                alertFactory.addAuto('danger', $string, optionalDelay);
              });
            }
          },
          templateUrl: 'sprints/dialog2.tmpl.html',
          parent: angular.element(document.body),
          targetEvent: $event,
          clickOutsideToClose:true
        })
      }
      if (newDomains.getAddedDomains() != []){
        $scope.extraDomainsList = newDomains.getAddedDomains();
      }
      if (newAssets.getAddedAssets() != []){
        $scope.extraAssetsList = newAssets.getAddedAssets();
      }
    });
    app.controller('peopleManagementCtrl', function($scope, $state, $location, $http, alertFactory, $base64, $q, dataService, alertFactory,  $localStorage, $stateParams, $log, $window) {
      $http.get('/api/people/summary').then(function(response) {
        $scope.people= response.data;
      });
      $scope.managePerson = function(person) {
        $state.go("management.people.editPerson.person",person );
      }
    });
    app.controller('peopleCtrl', function($scope, $state, $location, $http, alertFactory, $base64, $q, dataService, alertFactory,  $localStorage, $stateParams, $log, $window) {
      $scope.formData = {};
      $scope.formData.selectedRoles = {};
      $scope.someSelected = function (object) {
        return Object.keys(object).some(function (key) {
          return object[key];
        });
      }
      var tabClasses;
      function initTabs() {
        tabClasses = ["","","",""];
      }
      $scope.getTabClass = function (tabNum) {
        return tabClasses[tabNum];
      };
      $scope.getTabPaneClass = function (tabNum) {
        return "tab-pane " + tabClasses[tabNum];
      }
      $scope.setActiveTab = function (tabNum) {
        initTabs();
        tabClasses[tabNum] = "active";
      };
      initTabs();
      //$scope.setActiveTab(2);
      $scope.activateTab = function(){
        $scope.setActiveTab(2);
      }
      $scope.addPerson = function(person){
        var newRoleName = $scope.formData.selectedRoles ;
        var ee =[];
        for (var i =  $scope.rolesList.length - 1; i >= 0; i--) {
          angular.forEach($scope.formData.selectedRoles, function(value2, key2) {
            if ($scope.rolesList[i].roleName == key2) {
              if (value2 == true) {
                var tmp = {};
                tmp.roleId = $scope.rolesList[i].roleId;
                tmp.roleName = $scope.rolesList[i].roleName;
                this.push(tmp);
              }
            }
          },ee );
        }
        person.roles = angular.bind(this,ee);
        $http.put('/api/people/addPerson',person).then(function(response) {
          var createdPerson = response.data;
          $scope.userId = createdPerson.personId;
          $state.go("management.people.addPerson.capabilities", createdPerson );
          var $string = "Successfully added " +person.firstName;
          var optionalDelay = 3000;
          alertFactory.addAuto('success', $string, optionalDelay);
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "Error in adding person. "+ response.data.errors[0]+"!";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      }
      $scope.currentProject= null;
      $http.get('/api/roles/getRoles').then(function(response)
      {
        var roles = response.data;
        var rolesList = roles;
        $scope.rolesList= rolesList;
      })
      .catch(function(response, status) {
        //	$scope.loading = false;
        var optionalDelay = 5000;
        var $string = "Error in fetching roles";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
    });
    app.controller('capabilityCtrl', function($scope, $state, $location, $http, alertFactory, $base64, $q, dataService, alertFactory,  $localStorage, $stateParams, $log, $window) {
      $scope.userId2 = $stateParams.personId;
      if($scope.userId2 != ''){
        $http.get('/api/people/getPersonName/'+$scope.userId2).then(function(response)
        {
          $scope.newUserName = response.data.personName;
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "Error in updating capability details";
          alertFactory.addAuto('danger', $string, optionalDelay);
        })
      }
      var tabClasses;
      function initTabs() {
        tabClasses = ["","","",""];
      }
      $scope.getTabClass = function (tabNum) {
        return tabClasses[tabNum];
      };
      $scope.getTabPaneClass = function (tabNum) {
        return "tab-pane " + tabClasses[tabNum];
      }
      $scope.setActiveTab = function (tabNum) {
        initTabs();
        tabClasses[tabNum] = "active";
      };
      initTabs();
      //$scope.setActiveTab(2);
      $scope.slider1 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Superficial', legend: 'Superficial'},
            {value: 'Satisfactory', legend: 'Satisfactory'},
            {value: 'Good', legend: 'Good'},
            {value: 'Excellent', legend: 'Excellent'},
            {value: 'Perfect', legend: 'Perfect'}
          ]
        }
      };
      $scope.slider2 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Superficial', legend: 'Superficial'},
            {value: 'Satisfactory', legend: 'Satisfactory'},
            {value: 'Good', legend: 'Good'},
            {value: 'Excellent', legend: 'Excellent'},
            {value: 'Perfect', legend: 'Perfect'}
          ]
        }
      };
      $scope.slider3 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'No match', legend: 'No match'},
            {value: 'Average match', legend: 'Average match'},
            {value: 'Good match', legend: 'Good match'},
            {value: 'Excellent match', legend: 'Excellent match'}
          ]
        }
      };
      $scope.slider4 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'Acceptable', legend: 'Acceptable'},
            {value: 'Good', legend: 'Good'},
            {value: 'Excellent', legend: 'Excellent'},
            {value: 'Outstanding', legend: 'Outstanding'}
          ]
        }
      };
      $scope.slider5 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'Acceptable', legend: 'Acceptable'},
            {value: 'Good', legend: 'Good'},
            {value: 'Excellent', legend: 'Excellent'},
            {value: 'Outstanding', legend: 'Outstanding'}
          ]
        }
      };
      $scope.slider6 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'Average', legend: 'Average'},
            {value: 'Good', legend: 'Good'},
            {value: 'High', legend: 'High'},
            {value: 'Very high', legend: 'Very high'}
          ]
        }
      };
      $scope.slider7 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'Acceptable', legend: 'Acceptable'},
            {value: 'Good', legend: 'Good'},
            {value: 'Excellent', legend: 'Excellent'},
            {value: 'Outstanding', legend: 'Outstanding'}
          ]
        }
      };
      $scope.slider8 = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'Average', legend: 'Average'},
            {value: 'Good', legend: 'Good'},
            {value: 'High', legend: 'High'},
            {value: 'Very high', legend: 'Very high'}
          ]
        }
      };
      if($scope.userId2 != ''){
        $http.get('/api/capabilities/getCapabilitiesOfPerson/'+$scope.userId2).then(function(response)
        {
          if(response.data != null){
            for (var i =  response.data.length - 1; i >= 0; i--) {
              if(response.data[i].capabilityId ==1){
                $scope.slider1.value = response.data[i].proficiency;
              }
              if(response.data[i].capabilityId ==2){
                $scope.slider2.value = response.data[i].proficiency;
              }if(response.data[i].capabilityId ==3){
                $scope.slider3.value = response.data[i].proficiency;
              }if(response.data[i].capabilityId ==4){
                $scope.slider4.value = response.data[i].proficiency;
              }if(response.data[i].capabilityId ==5){
                $scope.slider5.value = response.data[i].proficiency;
              }if(response.data[i].capabilityId ==6){
                $scope.slider6.value = response.data[i].proficiency;
              }if(response.data[i].capabilityId ==7){
                $scope.slider7.value = response.data[i].proficiency;
              }if(response.data[i].capabilityId ==8){
                $scope.slider8.value = response.data[i].proficiency;
              }
            }
          }
        })
      }
      $scope.addCapabilities =function(){
        var newPerson =[];
        $scope.person = {};
        $scope.person.commitment = $scope.slider1.value;
        $scope.person.domainKnowledge = $scope.slider2.value;
        $scope.person.ownInterest = $scope.slider3.value;
        $scope.person.prevDelQuality = $scope.slider4.value;
        $scope.person.prevProjectPerf = $scope.slider5.value;
        $scope.person.prgmExperience = $scope.slider6.value;
        $scope.person.prgmLanKnowledge = $scope.slider7.value;
        $scope.person.undrSoftSec = $scope.slider8.value;
        $scope.person.updatedBy = $localStorage.currentUser.userFirstName;
        var currentPerson = $stateParams;
        //		    $scope.person.updatedBy = $localStorage.currentUser.userFirstName;
        newPerson.push($scope.person);
        $scope.newPerson = newPerson;
        $http.post('/api/capabilities/insertCapabilities/'+currentPerson.personId,newPerson).then(function(response)
        {
          var optionalDelay = 5000;
          var $string = "Successfully updated capability details";
          alertFactory.addAuto('success', $string, optionalDelay);
          // $state.go('management.people.addPerson.programmingSkills',currentPerson);
          $state.go("management.people.addPerson",{personId : currentPerson.personId},{reload: true}).then(function(){
            $state.go("management.people.addPerson.programmingSkills",currentPerson)
          });
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "Error in updating capability details";
          alertFactory.addAuto('danger', $string, optionalDelay);
        })
      };
      $scope.clearCapabilities =function(){
        $scope.slider1.value = "Superficial";
        $scope.slider2.value = "Superficial";
        $scope.slider3.value = "Undefined";
        $scope.slider4.value = "Undefined";
        $scope.slider5.value = "Undefined";
        $scope.slider6.value = "Undefined";
        $scope.slider7.value = "Undefined";
        $scope.slider8.value = "Undefined";
      };
    });
    app.controller('editCapabilityCtrl', function($scope, $state, $location, $http, alertFactory, $base64, $q, dataService, alertFactory,  $localStorage, $stateParams, $log, $window) {
      $scope.userId2 = $stateParams.personId;
      var tabClasses;
      function initTabs() {
        tabClasses = ["","","",""];
      }
      $scope.getTabClass = function (tabNum) {
        return tabClasses[tabNum];
      };
      $scope.getTabPaneClass = function (tabNum) {
        return "tab-pane " + tabClasses[tabNum];
      }
      $scope.setActiveTab = function (tabNum) {
        initTabs();
        tabClasses[tabNum] = "active";
      };
      initTabs();
      //$scope.setActiveTab(2);
      if($scope.userId2 != ''){
        $http.get('/api/capabilities/getCapabilitiesOfPerson/'+$scope.userId2).then(function(response)
        {
          $scope.assessedCapabilities = response.data;
        }).then(function(){
          $scope.slider1 = {
            options: {
              hideLimitLabels: true,
              showTicks: true,
              showSelectionBar: true,
              hidePointerLabels : true,
              stepsArray: [
                {value: 'Superficial', legend: 'Superficial'},
                {value: 'Satisfactory', legend: 'Satisfactory'},
                {value: 'Good', legend: 'Good'},
                {value: 'Excellent', legend: 'Excellent'},
                {value: 'Perfect', legend: 'Perfect'}
              ]
            }
          };
          $scope.slider2 = {
            options: {
              hideLimitLabels: true,
              showTicks: true,
              showSelectionBar: true,
              hidePointerLabels : true,
              stepsArray: [
                {value: 'Superficial', legend: 'Superficial'},
                {value: 'Satisfactory', legend: 'Satisfactory'},
                {value: 'Good', legend: 'Good'},
                {value: 'Excellent', legend: 'Excellent'},
                {value: 'Perfect', legend: 'Perfect'}
              ]
            }
          };
          $scope.slider3 = {
            options: {
              hideLimitLabels: true,
              showTicks: true,
              showSelectionBar: true,
              hidePointerLabels : true,
              stepsArray: [
                {value: 'Undefined', legend: 'Undefined'},
                {value: 'No match', legend: 'No match'},
                {value: 'Average match', legend: 'Average match'},
                {value: 'Good match', legend: 'Good match'},
                {value: 'Excellent match', legend: 'Excellent match'}
              ]
            }
          };
          $scope.slider4 = {
            options: {
              hideLimitLabels: true,
              showTicks: true,
              showSelectionBar: true,
              hidePointerLabels : true,
              stepsArray: [
                {value: 'Undefined', legend: 'Undefined'},
                {value: 'Acceptable', legend: 'Acceptable'},
                {value: 'Good', legend: 'Good'},
                {value: 'Excellent', legend: 'Excellent'},
                {value: 'Outstanding', legend: 'Outstanding'}
              ]
            }
          };
          $scope.slider5 = {
            options: {
              hideLimitLabels: true,
              showTicks: true,
              showSelectionBar: true,
              hidePointerLabels : true,
              stepsArray: [
                {value: 'Undefined', legend: 'Undefined'},
                {value: 'Acceptable', legend: 'Acceptable'},
                {value: 'Good', legend: 'Good'},
                {value: 'Excellent', legend: 'Excellent'},
                {value: 'Outstanding', legend: 'Outstanding'}
              ]
            }
          };
          $scope.slider6 = {
            options: {
              hideLimitLabels: true,
              showTicks: true,
              showSelectionBar: true,
              hidePointerLabels : true,
              stepsArray: [
                {value: 'Undefined', legend: 'Undefined'},
                {value: 'Average', legend: 'Average'},
                {value: 'Good', legend: 'Good'},
                {value: 'High', legend: 'High'},
                {value: 'Very high', legend: 'Very high'}
              ]
            }
          };
          $scope.slider7 = {
            options: {
              hideLimitLabels: true,
              showTicks: true,
              showSelectionBar: true,
              hidePointerLabels : true,
              stepsArray: [
                {value: 'Undefined', legend: 'Undefined'},
                {value: 'Acceptable', legend: 'Acceptable'},
                {value: 'Good', legend: 'Good'},
                {value: 'Excellent', legend: 'Excellent'},
                {value: 'Outstanding', legend: 'Outstanding'}
              ]
            }
          };
          $scope.slider8 = {
            options: {
              hideLimitLabels: true,
              showTicks: true,
              showSelectionBar: true,
              hidePointerLabels : true,
              stepsArray: [
                {value: 'Undefined', legend: 'Undefined'},
                {value: 'Average', legend: 'Average'},
                {value: 'Good', legend: 'Good'},
                {value: 'High', legend: 'High'},
                {value: 'Very high', legend: 'Very high'}
              ]
            }
          };
          if(  $scope.assessedCapabilities != null){
            for (var i =   $scope.assessedCapabilities.length - 1; i >= 0; i--) {
              if( $scope.assessedCapabilities[i].capabilityId ==1){
                $scope.slider1.value =  $scope.assessedCapabilities[i].proficiency;
              }
              if( $scope.assessedCapabilities[i].capabilityId ==2){
                $scope.slider2.value = $scope.assessedCapabilities[i].proficiency;
              }if( $scope.assessedCapabilities[i].capabilityId ==3){
                $scope.slider3.value =  $scope.assessedCapabilities[i].proficiency;
              }if( $scope.assessedCapabilities[i].capabilityId ==4){
                $scope.slider4.value =  $scope.assessedCapabilities[i].proficiency;
              }if( $scope.assessedCapabilities[i].capabilityId ==5){
                $scope.slider5.value =  $scope.assessedCapabilities[i].proficiency;
              }if( $scope.assessedCapabilities[i].capabilityId ==6){
                $scope.slider6.value =  $scope.assessedCapabilities[i].proficiency;
              }if( $scope.assessedCapabilities[i].capabilityId ==7){
                $scope.slider7.value =  $scope.assessedCapabilities[i].proficiency;
              }if( $scope.assessedCapabilities[i].capabilityId ==8){
                $scope.slider8.value =  $scope.assessedCapabilities[i].proficiency;
              }
            }
          }
          $scope.updateCapabilities =function(){
            var newPerson =[];
            $scope.person = {};
            $scope.person.commitment = $scope.slider1.value;
            $scope.person.domainKnowledge = $scope.slider2.value;
            $scope.person.ownInterest = $scope.slider3.value;
            $scope.person.prevDelQuality = $scope.slider4.value;
            $scope.person.prevProjectPerf = $scope.slider5.value;
            $scope.person.prgmExperience = $scope.slider6.value;
            $scope.person.prgmLanKnowledge = $scope.slider7.value;
            $scope.person.undrSoftSec = $scope.slider8.value;
            $scope.person.updatedBy = $localStorage.currentUser.userFirstName;
            var currentPerson = $stateParams;
            //		    $scope.person.updatedBy = $localStorage.currentUser.userFirstName;
            newPerson.push($scope.person);
            $scope.newPerson = newPerson;
            $http.post('/api/capabilities/insertCapabilities/'+currentPerson.personId,newPerson).then(function(response)
            {
              var optionalDelay = 5000;
              var $string = "Successfully updated capability details";
              alertFactory.addAuto('success', $string, optionalDelay);
              // $state.go('management.people.addPerson.programmingSkills',currentPerson);
              $state.go("management.people.addPerson",{personId : currentPerson.personId},{reload: true}).then(function(){
                $state.go("management.people.editPerson.programmingSkills",currentPerson)
              });
            })
            .catch(function(response, status) {
              var optionalDelay = 5000;
              var $string = "Error in updating capability details";
              alertFactory.addAuto('danger', $string, optionalDelay);
            })
          };
          $scope.clearCapabilities =function(){
            $scope.slider1.value = "Superficial";
            $scope.slider2.value = "Superficial";
            $scope.slider3.value = "Undefined";
            $scope.slider4.value = "Undefined";
            $scope.slider5.value = "Undefined";
            $scope.slider6.value = "Undefined";
            $scope.slider7.value = "Undefined";
            $scope.slider8.value = "Undefined";
          };
        });
      }
    });
    app.controller('editProgrammingskills', function($scope, $state, $location, $http, alertFactory, $q, dataService, alertFactory,  $localStorage, $stateParams, $log, $timeout, GetProgrammingSkillsService, $mdDialog) {
      //$state.go($state.current, currentPerson, {reload: true});
      $scope.userId = $stateParams.personId;
      if($scope.userId != null){
        $http.get('/api/people/getPersonName/'+$scope.userId).then(function(response)
        {
          $scope.newUserName = response.data.personName;
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "Error in updating capability details";
          alertFactory.addAuto('danger', $string, optionalDelay);
        })
      }
      $http.get('api/skills/getProgrammingSkllsOfPerson/'+$scope.userId).then(function(response) {
        $scope.existingSkills = response.data;
      }).then(function(){
        $scope.sl = {
          options: {
            hideLimitLabels: true,
            showTicks: true,
            showSelectionBar: true,
            hidePointerLabels : true,
            stepsArray: [
              {value: 'Undefined', legend: 'Undefined'},
              {value: 'Average', legend: 'Average'},
              {value: 'Good', legend: 'Good'},
              {value: 'High', legend: 'High'},
              {value: 'Very high', legend: 'Very high'}
            ]
          }
        }
        $scope.hideSearch = true;
        var tabs = [
          { title: 'Instructions', content: "Select or add a skill and rate the proficiency. You can add multiple skills"}
        ],
        selected = null,
        previous = null;
        $scope.tabs = tabs;
        $scope.selectedIndex = 1;
        $scope.removeTab = function (tab) {
          var index = tabs.indexOf(tab);
          tabs.splice(index, 1);
          if($scope.tabs.length == 0){
            $scope.tabs.push({ title: 'Instructions', content: "Select or add a skill and rate the proficiency. You can add multiple skills", disabled: false});
            return  $scope.hideSearch= true;
          }
        };
        $scope.viewSkills = function(){
          angular.forEach($scope.existingSkills, function(value, key) {
            if(value.skillName){
              $scope.addTab(value.skillName,value.skillName);
            }
          });
          angular.forEach($scope.tabs, function(value, key) {
            angular.forEach($scope.existingSkills, function(value2, key2) {
              if(value.title == value2.skillName && value.content == value2.skillName ){
                value.proficiency = value2.proficiency;
                //value.comment = value2.outcome;
              }
            });
          });
          $scope.hideSearch = false;
        };
        $scope.searchText = "";
        $scope.Person = [];
        $scope.selectedItem = [];
        $scope.isDisabled = false;
        $scope.noCache = true;
        $scope.searchTextChange = function (str) {
          return  GetProgrammingSkillsService.getCountry(str);
        }
        $scope.newState = function(ev,skill) {
          var confirm = $mdDialog.confirm()
          .title("Would you like to add the skill '"+skill+"'?")
          .textContent('This will also also add the skill to the skills database')
          .ariaLabel('')
          .targetEvent(ev)
          .ok('Proceed!')
          .cancel('Cancel');
          $mdDialog.show(confirm).then(function() {
            $http.post('/api/skills/insertSkill',skill).then(function(response) {
              var $string = "Added skill to skills database!";
              $scope.searchTextChange();
              $scope.searchTextChange(skill);
              $scope.searchText = skill;
              var txt = "{\"value\":\""+skill+"\",\"display\":\""+skill+"\"}";
              $scope.selectedItem = angular.fromJson(txt);
              // $scope.selectedItem= programmingSkillEntered.value;
              var optionalDelay = 3000;
              alertFactory.addAuto('success', $string, optionalDelay);
            })
            .catch(function(response, status) {
              var optionalDelay = 5000;
              var $string = "Error in adding skill to skills database";
              alertFactory.addAuto('danger', $string, optionalDelay);
            });
          });
        }
        $scope.saveSkills = function(tabs){
          $scope.userId = $stateParams.personId;
          var programmingSkillsEntered = [];
          angular.forEach(tabs, function(value, key) {
            var personSkills = {};
            personSkills.skillName = value.title;
            personSkills.proficiency = value.proficiency;
            personSkills.updatedBy = $localStorage.currentUser.userFirstName;
            programmingSkillsEntered.push(personSkills);
          });
          $scope.skills =programmingSkillsEntered ;
          $http.post('/api/skills/insertSkillsAssessment/'+$scope.userId, $scope.skills).then(function(response)
          {
            var optionalDelay = 3000;
            var $string = "Programming skills saved!";
            $state.go("management.people.peopleTable");
            alertFactory.addAuto('success', $string, optionalDelay);
          })
          .catch(function(response, status) {
            //	$scope.loading = false;
            var optionalDelay = 3000;
            var $string = "Error in adding skills";
            alertFactory.addAuto('danger', $string, optionalDelay);
          });
        }
        $scope.clearSkills = function(ev){
          var confirm = $mdDialog.confirm()
          .title("Would you like to clear all the programming skills? ")
          .textContent('This will only clear all the tabs selected on this page.')
          .ariaLabel('')
          .targetEvent(ev)
          .ok('Proceed!')
          .cancel('Cancel');
          $mdDialog.show(confirm).then(function() {
            for (var i =  $scope.tabs.length - 1; i >= 0; i--) {
              $scope.removeTab($scope.tabs[i]);
            }
          });
        };
        $scope.addTab = function (title, view) {
          for (var i =  tabs.length - 1; i >= 0; i--) {
            if(tabs[i].title == title){
              var optionalDelay = 5000;
              var $string = "You already selected the skill "+ title;
              alertFactory.addAuto('warning', $string, optionalDelay);
              return;
            }
          }
          tabs.push({ title: title, content: view, disabled: false});
          if($scope.tabs.length >= 2){
            if(tabs[0].title == 'Instructions'){
              $scope.tabs.splice(0,1);
            }
          }
        };
      });
    });
    app.controller('programmingskills', function($scope, $state, $location, $http, alertFactory, $q, dataService, alertFactory,  $localStorage, $stateParams, $log, $timeout, GetProgrammingSkillsService, $mdDialog) {
      //$state.go($state.current, currentPerson, {reload: true});
      $scope.userId = $stateParams.personId;
      if($scope.userId != null){
        $http.get('/api/people/getPersonName/'+$scope.userId).then(function(response)
        {
          $scope.newUserName = response.data.personName;
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "Error in updating capability details";
          alertFactory.addAuto('danger', $string, optionalDelay);
        })
      }
      $scope.sl = {
        options: {
          hideLimitLabels: true,
          showTicks: true,
          showSelectionBar: true,
          hidePointerLabels : true,
          stepsArray: [
            {value: 'Undefined', legend: 'Undefined'},
            {value: 'Average', legend: 'Average'},
            {value: 'Good', legend: 'Good'},
            {value: 'High', legend: 'High'},
            {value: 'Very high', legend: 'Very high'}
          ]
        }
      }
      var tabs = [
        { title: 'Instructions', content: "Select or add a skill and rate the proficiency. You can add multiple skills"}
      ],
      selected = null,
      previous = null;
      $scope.tabs = tabs;
      $scope.selectedIndex = 1;
      $scope.removeTab = function (tab) {
        var index = tabs.indexOf(tab);
        tabs.splice(index, 1);
        if($scope.tabs.length == 0){
          $scope.tabs.push({ title: 'Instructions', content: "Select or add a skill and rate the proficiency. You can add multiple skills", disabled: false});
        }
      };
      $scope.searchText = "";
      $scope.Person = [];
      $scope.selectedItem = [];
      $scope.isDisabled = false;
      $scope.noCache = true;
      $scope.searchTextChange = function (str) {
        return  GetProgrammingSkillsService.getCountry(str);
      }
      $scope.newState = function(ev,skill) {
        var confirm = $mdDialog.confirm()
        .title("Would you like to add the skill '"+skill+"'?")
        .textContent('This will also also add the skill to the skills database')
        .ariaLabel('')
        .targetEvent(ev)
        .ok('Proceed!')
        .cancel('Cancel');
        $mdDialog.show(confirm).then(function() {
          $http.post('/api/skills/insertSkill',skill).then(function(response) {
            var $string = "Added skill to skills database!";
            $scope.searchTextChange();
            $scope.searchTextChange(skill);
            $scope.searchText = skill;
            var txt = "{\"value\":\""+skill+"\",\"display\":\""+skill+"\"}";
            $scope.selectedItem = angular.fromJson(txt);
            // $scope.selectedItem= programmingSkillEntered.value;
            var optionalDelay = 3000;
            alertFactory.addAuto('success', $string, optionalDelay);
          })
          .catch(function(response, status) {
            var optionalDelay = 5000;
            var $string = "Error in adding skill to skills database";
            alertFactory.addAuto('danger', $string, optionalDelay);
          });
        });
      }
      $scope.saveSkills = function(tabs){
        $scope.userId = $stateParams.personId;
        var programmingSkillsEntered = [];
        angular.forEach(tabs, function(value, key) {
          var personSkills = {};
          personSkills.skillName = value.title;
          personSkills.proficiency = value.proficiency;
          personSkills.updatedBy = $localStorage.currentUser.userFirstName;
          programmingSkillsEntered.push(personSkills);
        });
        $scope.skills =programmingSkillsEntered ;
        $http.post('/api/skills/insertSkillsAssessment/'+$scope.userId, $scope.skills).then(function(response)
        {
          var optionalDelay = 3000;
          var $string = "Programming skills saved!";
          $state.go("management.people.peopleTable");
          alertFactory.addAuto('success', $string, optionalDelay);
        })
        .catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 3000;
          var $string = "Error in adding skills";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      }
      $scope.clearSkills = function(ev){
        var confirm = $mdDialog.confirm()
        .title("Would you like to clear all the programming skills? ")
        .textContent('This will only clear all the tabs selected on this page.')
        .ariaLabel('')
        .targetEvent(ev)
        .ok('Proceed!')
        .cancel('Cancel');
        $mdDialog.show(confirm).then(function() {
          for (var i =  $scope.tabs.length - 1; i >= 0; i--) {
            $scope.removeTab($scope.tabs[i]);
          }
        });
      };
      $scope.addTab = function (title, view) {
        for (var i =  tabs.length - 1; i >= 0; i--) {
          if(tabs[i].title == title){
            var optionalDelay = 5000;
            var $string = "You already selected the skill "+ title;
            alertFactory.addAuto('warning', $string, optionalDelay);
            return;
          }
        }
        tabs.push({ title: title, content: view, disabled: false});
        if($scope.tabs.length >= 2){
          if(tabs[0].title == 'Instructions'){
            $scope.tabs.splice(0,1);
          }
        }
      };
    });
    app.controller('editProjectParticipants', function TodoCtrl($scope, $element,$log, $state, $stateParams, $filter, $location, $http, alertFactory, $base64, $q, dataService, alertFactory, $mdDialog ) {
      var currentProject = $stateParams;
      $scope.showRemove = true;
      $scope.removeSelected = {};
      $scope.addBack = function(showRemove){
        $scope.showRemove = true;
        showRemove = true;
        return showRemove;
      }
      $scope.removeProjectParticipants = function(){
        $scope.filterPeople();
        var currentPeopleList = $scope.peopleList;
        var clearSelected  = $scope.list2;
        var remove = $scope.removeSelected;
        $http.get('/api/people/summary').then(function(response)
        {
          var people = response.data;
          $scope.peopleListCopy= people;
        }).then(function(){
          var list =  $scope.peopleListCopy;
          var splicelist =[];
          if($scope.list2){
            angular.forEach($scope.list2, function(value3, key3) {
              if(value3.personId && $scope.removeSelected[value3.personId] == true ){
                for (var k =  list.length - 1; k >= 0; k--){
                  if(list[k].personId == value3.personId){
                    $scope.peopleList.push(list[k]);
                    $log.debug('Hello ' +$scope.peopleList.length+ '!');
                    //clearSelected[key].splice(key3,1);
                    var tmp ={};
                    tmp.personId = value3.personId;
                    tmp.personName = value3.personName;
                    tmp.roleid = value3.roleId;
                    tmp.roleName = value3.roleName;
                    splicelist.push(tmp);
                    remove[list[k].personId] =false;
                  }
                };
              }
            });
          }
          $scope.countSelected = 0;
          angular.forEach(splicelist, function(value2, key2) {
            //delete $scope.list2[key][value2];
            //$scope.list2[key][value2] = 1;
            // $scope.showRemove = false;
            angular.forEach($scope.list2, function(value3, key3) {
              if(value2.personId == value3.personId && value2.personName == value3.personName ){
                //$log.debug('Hello ' +key3+ '!');
                clearSelected.splice(key3,1);
              }
            })
            //$scope.list2[key].remove(0,1,2,3,4);
          })
          $scope.countSelected = 	$scope.countSelected + clearSelected.length;
          if($scope.countSelected == 0){
            $scope.showRemove = false;
          }
          //return $scope.removeSelected = {};
        }).then(function(){
          $scope.peopleList = currentPeopleList;
          $scope.list2 = clearSelected;
          $scope.removeSelected =remove;
          //$scope.drop[$scope.selectedRole] = true;
        });
      };
      $http.get('/api/roles/rolesOfPeople').then(function(response)
      {
        var rolesOfPeople = response.data;
        $scope.rolesOfPeople= rolesOfPeople;
      })
      .catch(function(response, status) {
        //	$scope.loading = false;
        var optionalDelay = 5000;
        var $string = "Error in fetching roles of people";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
      $http.get('/api/projects/participants/' +currentProject.projectId).then(function(response)
      {
        var projectParticipants = response.data;
        $scope.list2 = projectParticipants;
      })
      .catch(function(response, status) {
        //	$scope.loading = false;
        var optionalDelay = 5000;
        var $string = "Error in fetching project participants details";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
      $http.get('/api/people/summary').then(function(response)
      {
        var people = response.data;
        var peopleList = people;
        $scope.peopleList= peopleList;
      }).then(function(){
        $http.get('/api/projects/' + currentProject.projectId).then(function(response)
        {
          var CurrentProject = response.data;
          var finalProject = CurrentProject;
          var  projectStartDate = $filter('date')(CurrentProject.projectStartDate, 'yyyy/MM/dd');
          var  projectEndDate = $filter('date')(CurrentProject.projectEndDate, 'yyyy/MM/dd');
          finalProject.projectStartDate = projectStartDate;
          finalProject.projectEndDate = projectEndDate;
          finalProject.projectLeaderName = null;
          if($scope.peopleList.length != 0){
            angular.forEach($scope.peopleList, function(value, key) {
              if (value.personId == finalProject.projectLeader) {
                finalProject.projectLeaderName = value.personName;
              }
            });
          }
          $scope.finalProject= finalProject;
        })
        .catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching project  details";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      })
      .catch(function(response, status) {
        //	$scope.loading = false;
        var optionalDelay = 5000;
        var $string = "Error in fetching list of people";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
      $http.get('/api/roles/getRoles').then(function(response)
      {
        var roles = response.data;
        var rolesList = roles;
        $scope.rolesList= rolesList;
      })
      .catch(function(response, status) {
        //	$scope.loading = false;
        var optionalDelay = 5000;
        var $string = "Error in fetching roles";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
      $scope.list2 = [];
      //	$scope.demo = [{ "personId": 1, "personName": "sai datta Admin", "projects": [ { "projectId": 1, "projectName": "Project1", "sprints": [ { "sprintId": 4, "sprintName": "sprintlog", "issues": [ { "issueId": 7 }, { "issueId": 6 } ], "numberofIssues": 2 }, { "sprintId": 8, "sprintName": "demosprint", "issues": [ { "issueId": 0 }, { "issueId": 1 } ], "numberofIssues": 2 } ] } ], "roleId": "3", "jqyoui_pos": 1 } ];
      $scope.filterPeople = function() {
        //roleName= $scope.selectedRole;
        var peoplelength =$scope.peopleList.length;
        var list1 =  $scope.peopleList;
        for (var i =  peoplelength - 1; i >= 0; i--) {
          angular.forEach($scope.list2, function(value2, key2) {
            if ($scope.peopleList[i].personId == value2.personId) {
              $log.debug("this is for person "+ value2.personName);
              list1.splice(i, 1);
            }
          })
        }
        $scope.show = true;
        return $scope.peopleList = list1;
      };
      $scope.updateProjectParticipants =function(participants){
        var ee = [];
        angular.forEach(participants, function(value, key) {
          var txt = "{\"personId\":"+value.personId+",\"roleId\":"+value.roleId+"}";
          var txt2 = angular.fromJson(txt);
          this.push(txt2);
        },ee );
        $scope.ree = angular.bind(this,ee);
        //$scope.jsn = angular.fromJson($scope.ree);
        //$scope.wee = allocatedPeople;
        $http.put('/api/projects/updateProjectParticipants/'+currentProject.projectId,$scope.ree ).then(function(response) {
          $state.go("management.projects.projectsTable");
          var $string = "Successfully updated the project participants";
          var optionalDelay = 3000;
          alertFactory.addAuto('success', $string, optionalDelay);
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = {};
          if(response.data.message !=null){
            $string = response.data.message;
            if($string = "Forbidden. Please check the user has proper permissions"){
              $string = $string+". Perhaps, the project is closed!"
            }
          }else {
            $string = "Participants already added to the project";
          }
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
      };
      $scope.clearParticipants =function(ev, participants){
        var confirm = $mdDialog.confirm()
        .title('Would you like to clear all the project participants?')
        .textContent('This will also clear the project participants on Redmine')
        .ariaLabel('')
        .targetEvent(ev)
        .ok('Proceed!')
        .cancel('Cancel');
        $mdDialog.show(confirm).then(function() {
          angular.forEach(participants, function(value, key) {
            this.push(value);
          },$scope.peopleList);
          $scope.list2= [];
        });
      }
      $scope.manageProject = function(project) {
        $state.go("management.projects.editProject",project );
      };
    });
    app.controller('editProject', function($scope, $state, $stateParams, $filter, $location, $http, alertFactory, $base64, $q, dataService, alertFactory, $localStorage, $mdDialog, $log) {
      var currentProject = $stateParams;
      var editProject = null;
      var finalProject = null;
      $scope.formatProjects = function(projectsList, currentProject, indent){
        for(var i=0; i<currentProject.projectList.length; i++){
          var project = {};
          project.projectId = currentProject.projectList[i].projectId;
          project.projectName = " - ".repeat(indent)+currentProject.projectList[i].projectName;
          projectsList.push(project);
          var updatedProject = currentProject.projectList[i];
          if(updatedProject.projectList !=null && updatedProject.projectList.length !=0){
            indent++;
            $scope.formatProjects(projectsList, updatedProject, indent);
            indent--;
          }
        }
      };
      $http.get('/api/projects/getAllProjectsNested').then(function(response)
      {
        var projects = response.data;
        var projectsList = [];
        angular.forEach(projects, function(value, key) {
          var project = {};
          project.projectId = value.projectId;
          project.projectName = value.projectName;
          projectsList.push(project);
          if(value.projectList !=null && value.projectList.length !=0){
            var indent = 1;
            $scope.formatProjects(projectsList, value, indent);
          }
        });
        $scope.projectsList = projectsList;
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching list of projects";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
      $http.get('api/sprints/listOfSprintsInProject/'+currentProject.projectId).then(function(response) {
        $scope.sprintsInProject= response.data;
      })
      .catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching list of sprints in the project";
        alertFactory.addAuto('danger', $string, optionalDelay);
      });
      $scope.manageSprint = function(sprint) {
        $state.go("management.sprints.editSprint.existingSprint",sprint );
      };
      $http.get('/api/people/getAllPeople').then(function(response)
      {
        $scope.peopleList = response.data;
      }).catch(function(response, status) {
        var optionalDelay = 5000;
        var $string = "Error in fetching list of people";
        alertFactory.addAuto('danger', $string, optionalDelay);
      }).then(function(){
        $http.get('/api/projects/' +currentProject.projectId).then(function(response)
        {
          editProject = response.data;
          finalProject = editProject;
        })
        .catch(function(response, status) {
          var optionalDelay = 5000;
          var $string = "Error in fetching project details";
          alertFactory.addAuto('danger', $string, optionalDelay);
        }).then(function(){
          if(editProject.projectStartDate != null){
            finalProject.projectStartDate =  new Date(editProject.projectStartDate);
          }else{
            finalProject.projectStartDate =  null;
          }
          if(editProject.projectEndDate != null){
            finalProject.projectEndDate =  new Date(editProject.projectEndDate);
          }else{
            finalProject.projectEndDate =  null;
          }
          $scope.projectStartDate = projectStartDate;
          $scope.finalProject = finalProject;
          $scope.projectStartDate = new Date();
          $scope.minDate = new Date(
            $scope.projectStartDate.getFullYear(),
            $scope.projectStartDate.getMonth(),
            $scope.projectStartDate.getDate()
          );
          $scope.maxDate = new Date(
            $scope.projectStartDate.getFullYear(),
            $scope.projectStartDate.getMonth() + 3,
            $scope.projectStartDate.getDate()
          );
          $scope.onlyWeekdaysPredicate = function(date) {
            var day = date.getDay();
            return day === 1 || day === 2 || day  ==3 || day == 4 || day ==5;
          };
          $scope.minDate2 = $scope.projectStartDate;
        })
        $http.get('/api/projects/participants/' +currentProject.projectId).then(function(response)
        {
          var projectParticipants = response.data;
          $scope.projectParticipants = projectParticipants;
        })
        .catch(function(response, status) {
          //	$scope.loading = false;
          var optionalDelay = 5000;
          var $string = "Error in fetching project participants details";
          alertFactory.addAuto('danger', $string, optionalDelay);
        });
        $scope.clearProject =function(ev, project){
          var confirm = $mdDialog.confirm()
          .title('Would you like to clear all the project details?')
          .textContent('This will only clear the project details on this page. The project on Redmine remains unaffected.')
          .ariaLabel('')
          .targetEvent(ev)
          .ok('Proceed!')
          .cancel('Cancel');
          $mdDialog.show(confirm).then(function() {
            $scope.editform.$setPristine();
            $scope.finalProject=null;
            $scope.projectStartDate = null;
          });
        }
        $scope.manageProject = function(project) {
          $state.go("management.projects.editProject",project );
        }
        $scope.updateProject = function(project) {
          project.projectUpdatedBy = $localStorage.currentUser.userFirstName;
          project.projectId = currentProject.projectId;
          $http.put('/api/projects/updateProject',project).then(function(response) {
            $state.go("management.projects.projectsTable");
            var $string = "Successfully updated the project ";
            var optionalDelay = 3000;
            alertFactory.addAuto('success', $string, optionalDelay);
          })
          .catch(function(response, status) {
            var optionalDelay = 5000;
            var $string = {};
            if(response.data.message !=null){
              $string = response.data.message;
              if($string = "Forbidden. Please check the user has proper permissions"){
                $string = $string+". Perhaps, the project is closed!"
              }
            }else {
              $string = "Error in updating the project";
            }
            alertFactory.addAuto('danger', $string, optionalDelay);
          });
        };
        $scope.editProjectParticipants = function(){
          $state.go('management.projects.editProjectParticipants', currentProject);
        };
        $scope.createNewSprint = function(){
          $state.go('management.sprints.addSprint.newSprint');
        };
      });
    })
  })();
