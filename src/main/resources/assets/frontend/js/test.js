app.controller('editPeopleToSprintCtrl', function($scope, $state, $timeout, $filter, $http, $q, dataService, alertFactory, $localStorage, $stateParams, $log, $window, $mdDialog, $rootScope) {
  if($rootScope.alerts.length !=0){
    angular.forEach($rootScope.alerts, function(value, key) {
      var alert = {};
      alert = $rootScope.alerts[key];
      if(alert.type == 'info'){
        $rootScope.alerts.splice(key,1);
      }
    });
  };
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
                //$log.debug("person is"+ $scope.peopleList[i].personName);
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
                  //$log.debug("person is"+ $scope.peopleList[i].personName);
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

      $scope.RoleDisplay = true;
      $scope.filterPeople = function(selectedRole) {
        $scope.isAllSelected = false;
        $scope.selectedRole = selectedRole;
        $scope.RoleDisplay = false;
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
