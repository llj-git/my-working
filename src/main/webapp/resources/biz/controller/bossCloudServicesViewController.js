var app = angular.module('servicesView', []);
app.controller('servicesController', function($scope, $http) {
	$scope.serviceType=null;
	$scope.serviceTypeList = ["bds","csn","ignite"];
	var $tab = $('#services_app');
	$tab.tabs('refresh');
	
//	$http({
//		method : 'POST',
//		url : 'rest/getServiceType',
//		data : data,
//		headers : {
//			"Content-Type" : "application/json"
//		}
//	}).then(function successCallback(response) {
//		$scope.serviceTypeList = response.data;
//		var $tab = $('#services_app');
//		$tab.tabs('refresh');
//	}, function errorCallback(response) {
//		$scope.alertInfo="failed!";
//		var $modal = $('#my-modal');
//	    $modal.modal();
//	});
	
	
	$scope.serviceNodes=function(serviceType){	
		$http({
			method : 'POST',
			url : 'rest/serviceChildNodes',
			data : serviceType,
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$scope.serviceChildNodes = response.data;
			$scope.serviceType=serviceType;
		}, function errorCallback(response) {
			$scope.alertInfo="failed!";
			var $modal = $('#my-modal');
		    $modal.modal();
		});
	}
	
	
	$scope.serviceDes=function(){
		var serviceName = $("#changeSel").find("option:selected").text().trim();
		var data={
			"serviceType":$scope.serviceType,
			"serviceName":serviceName
		}
		$http({
			method : 'POST',
			url : 'rest/getServiceDetail',
			data : JSON.stringify(data),
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$scope.serviceList = response.data;
		}, function errorCallback(response) {
			$scope.alertInfo="failed!";
			var $modal = $('#my-modal');
		    $modal.modal();
		});
	}
	
});