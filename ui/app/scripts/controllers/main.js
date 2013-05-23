'use strict';

angular.module('uiApp')
  .controller('MainCtrl', function ($scope, $http) {
    $scope.followers = [];
    $scope.cursor = -1;
    $scope.disable = false;
    $scope.value = "Show Followers!";
    
    $scope.getFollowers = function() {
      $scope.disable = true;
      $scope.value = "Loading...";

      $http.get(
        jsRoutes.controllers.Application.getFollowers($scope.cursor).url
      ).success(function(data) {
      data.users.forEach(function(user) {
        console.log(user);
          $scope.followers.push(user);
      });
      console.log(data.next_cursor);
      
      $scope.cursor = data.next_cursor;
      if ($scope.cursor != 0) {
          $scope.disable = false;
          $scope.value = "Show More Followers!";
      } else {
        $scope.value = "No More Followers...";
      }
      });
    };
  });
