var app = angular.module('igniteManage', ["ngTable"]);
app.controller('igniteManageController', function($scope, $http ,NgTableParams) {

	var data = {};
	$http({
		method : 'POST',
		url : 'rest/getNodeIds',
		data : data,
		headers : {
			"Content-Type" : "application/json"
		}
	}).then(function successCallback(response) {
		console.log(response.data.cacheNames);
        $scope.nodeIds=response.data.cacheNames;
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
	

    $scope.getNodeDetails= function(s) {
        var params={'nodeId':s};
        $http({
            method : 'POST',
            url : 'rest/getNodeDetails',
            data : params,
            headers : {
                "Content-Type" : "application/json",
                "charset": "UTF-8"
            }
        }).then(function successCallback(response) {
        	$scope.data = response.data.value;

        	$scope.data=$scope.data.replace(/\n/g,"\\n");

        	console.log($scope.data);

            $scope.data= eval($scope.data);

            $scope.items= $scope.data

            setTimeout(function(){

                layui.use(['table'], function(){
                    var table = layui.table;
                    table.init('demo', {

                        page:true,
                        // toolbar: '#toolbarDemo'
                        // ,cols: [[
                        //     {type:'checkbox'}
                        // ]]

                    });

                });
            },20);

        }, function errorCallback(response) {

        });
    };


});