var app = angular.module('zkManage', ["ngTable"]);
app.controller('zkManageController', function($scope, $http, NgTableParams) {
	
	var self = this;
	// 表头显示
	$scope.tableName = null;
	$scope.inputId = null;
	
	$scope.Info=[{"path":"查询"}]
//	$.AMUI.progress.set(0.3);
//	$.AMUI.progress.start();
	$("#dataTable").attr("disabled",true);

	var data = {};
	$http({
		method : 'POST',
		url : 'rest/getCatheList',
		data : data,
		headers : {
			"Content-Type" : "application/json"
		}
	}).then(function successCallback(response) {
        $scope.cacheNames=response.data.cacheNames;
	}, function errorCallback(response) {
		$.AMUI.progress.done();
		$scope.alertInfo="failed!";
		var $modal = $('#my-modal');
	    $modal.modal();
	});

	$scope.change = function(){
		var s = $scope.center;
		$scope.cacheName=s;
		var params={'centerName':$scope.cacheName};
		$http({
			method : 'POST',
			url : 'rest/Centerqry',
			data : params,
			headers : {
				"Content-Type" : "application/text",
				"charset": "UTF-8"
			}
		}).then(function successCallback(response) {
			var str1 = response.data.Acenter;
			var str2 = response.data.Bcenter;
			if(s == 1){
				$scope.centerip = str1;
			}else{
				$scope.centerip = str2;
			}
			console.log($scope.centerip);
			//document.getElementById('pk').style.display = 'block';
		}, function errorCallback(response) {
			$.AMUI.progress.done();
			$scope.alertInfo="查无相关字段!";
			var $modal = $('#my-modal');
			$modal.modal();
		});
	};

	$scope.setConnet= function() {
		var params={'ip':$scope.addres};
		$http({
			method : 'POST',
			url : 'rest/GetIpcon',
			data : params,
			headers : {
				"Content-Type" : "text/plain",//"application/json",
				"charset": "UTF-8"
			}
		}).then(function successCallback(response) {
			if(response != "" && response !=null){
				alert("连接成功");
				window.location.reload();
			}
		}, function errorCallback(response) {
			alert("连接失败");
			$.AMUI.progress.done();
			$scope.alertInfo="设置失败";
			var $modal = $('#my-modal');
			$modal.modal();
		});
	};
	
	$scope.getCacheinfo= function(s) {
        var params={'cacheName':s};
        $http({
            method : 'POST',
            url : 'rest/getCacheinfo',
            data : params,
            headers : {
                "Content-Type" : "application/json",
                "charset": "UTF-8"
            }
        }).then(function successCallback(response) {

        	var str = response.data.value;

        	console.log(typeof str);
        	console.log(str);
            $scope.data= eval(str);

        }, function errorCallback(response) {
        	alert("fail");
            $.AMUI.progress.done();
            $scope.alertInfo="failed!";
            var $modal = $('#my-modal');
            $modal.modal();
        });
    };

	

});