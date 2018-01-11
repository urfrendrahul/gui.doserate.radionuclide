angular.module('doseRate', [])
.controller('doseRateController', function($scope, $http) {
	
	$http.get('http://localhost:8080/calTypes').
        then(function(response) {
            $scope.calculationType = response.data;
			
        });
		
	$http.get('http://localhost:8080/categories').
        then(function(response) {
            $scope.stablityCategories = response.data;
			
        });
		
	$http.get('http://localhost:8080/fileNames').
        then(function(response) {
            $scope.fileNames = response.data;
			
        });
		
	$scope.distances = [];
	$scope.selectedStablities = [];
	
	$scope.add = function(){
		if(!$scope.downwindDistance){
			alert("Please enter distance");
			return;
		}
		$scope.distances.push($scope.downwindDistance);
		$scope.downwindDistance="";
	}
	
	$scope.remove = function(index){
		$scope.distances.splice(index,1);
	}
	
	$scope.toggleStablitySelection = function(cat){
		var idx = $scope.selectedStablities.indexOf(cat);
		if (idx > -1) {
			$scope.selectedStablities.splice(idx, 1);
		}

		else {
			$scope.selectedStablities.push(cat);
		}
		console.log($scope.selectedStablities);
	}
	
	$scope.calculate = function(){
	if(!$scope.calType || $scope.selectedStablities.length == 0){
		alert("Enter all fields");
		return;
	}
	
    $http.get('http://localhost:8080/doseRates', {params: {calcType: $scope.calType, speed: $scope.windSpeed, 
	height:$scope.releaseHeight, categories:$scope.selectedStablities, distances:$scope.distances }}).
        then(function(response) {
            $scope.result = response.data;
        });
	}
});