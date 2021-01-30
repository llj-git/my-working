var app = angular.module('zkManage', ["ngSanitize", "ngCsv"]);
app.controller('zkManageController', function($scope, $http,$interval,$timeout) {
	$scope.Info=[{"path":"IP端口修改"}]

//	$.AMUI.progress.set(0.3);
//	$.AMUI.progress.start();
	var count=6;

	var data = {};
	$scope.setIp= function() {	
		var params={'ip':$scope.ip,'port':$scope.port};
		console.log(JSON.stringify(params));
		$http({
            method : 'POST',
            url : 'rest/setIp',
            data : params,
            headers : {
                "Content-Type" : "text/plain",//"application/json",
                "charset": "UTF-8"
            }
        }).then(function successCallback(response) {
        	if(response != "" && response !=null){
        		alert("连接成功");
        		}    
        }, function errorCallback(response) {
        	alert("连接失败");
            $.AMUI.progress.done();
            $scope.alertInfo="设置失败";
            var $modal = $('#my-modal');
            $modal.modal();
        });
	};
	
	    
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
	        		}    
	        }, function errorCallback(response) {
	        	alert("连接失败");
	            $.AMUI.progress.done();
	            $scope.alertInfo="设置失败";
	            var $modal = $('#my-modal');
	            $modal.modal();
	        });
		};
});