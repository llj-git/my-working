var app = angular.module('zkManage', ["ngSanitize", "ngCsv" , "ngTable"]);
app.controller('zkManageController', function($scope, $http , NgTableParams) {
	$scope.Info=[{"path":"查询"}]

//	$.AMUI.progress.set(0.3);
//	$.AMUI.progress.start();


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
        console.log(typeof $scope.cacheNames);
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
	
	$scope.getCachecfg= function(s) {
        var params={'cacheName':s};
        $http({
            method : 'POST',
            url : 'rest/getCachecfg',
            data : params,
            headers : {
                "Content-Type" : "application/json",
                "charset": "UTF-8"
            }
        }).then(function successCallback(response) {
        	var str=JSON.parse(response.data.value);
        	console.log(typeof str);
            $scope.data= str;
            //var object=$scope.data[0];
//            $scope.textPort($scope.data,object);
//            document.getElementById('right').style.display = 'block';
        }, function errorCallback(response) {
        	alert("fail");
            $.AMUI.progress.done();
            $scope.alertInfo="failed!";
            var $modal = $('#my-modal');
            $modal.modal();
        });
    };

    $scope.textPort=function(x,k){
    	$scope.getArray = x;
        var arrayObj = new Array();
        for(var i in k){
        	arrayObj=i;
        	}
    	console.log($scope.getArray);
    	$scope.filename = "test";
    	$scope.getHeader=function(){
       	 $scope.getHeader = function () {return [arrayObj]};
       }
    }
	

});