var app = angular.module('bds_switch', []);
app.controller('bdsSwitchController', function($scope, $http) {
	$scope.getSwitchBdsList = function() {
		$.AMUI.progress.set(0.3);
		$.AMUI.progress.start();
		var data = {};
		$http({
			method : 'POST',
			url : 'rest/getSwitchBdsList',
			data : data,
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$scope.bdsList = response.data;
			$.AMUI.progress.done();
		}, function errorCallback(response) {
			$.AMUI.progress.done();
			$scope.alertTitle="警告";
			$scope.alertInfo="failed!";
			var $modal = $('#my-modal');
		    $modal.modal();
		});
	};
	$scope.bdsList = [];
	$scope.doBdsSwitch = function() {
		$.AMUI.progress.set(0.3);
		$.AMUI.progress.start();
		var dbSource = "";
		var currServiceType = "";
		var branchDB0Selected = false;
		var branchDB1Selected = false;
		var branchDB2Selected = false;
		var reqJson = "";
		for(var i=0; i <$scope.bdsList.length; i++ ){
			dbSource = $scope.bdsList[i].dbSource;
			currServiceType = $scope.bdsList[i].currServiceType;
			if (currServiceType.indexOf(":") != -1) {
				currServiceType=currServiceType.substring(0, currServiceType.indexOf(":"));
			}
			branchDB0Selected = $scope.bdsList[i].branchDB0Selected;
			branchDB1Selected = $scope.bdsList[i].branchDB1Selected;
			branchDB2Selected = $scope.bdsList[i].branchDB2Selected;
//			alert(dbSource+" "+currServiceType+" "+branchDB0Selected+"  "+branchDB1Selected+"  "+branchDB2Selected);
			switch (currServiceType) {
			case "0":
				if( !$scope.bdsSwitchCheck(dbSource, branchDB0Selected, branchDB1Selected, branchDB2Selected) ){
					return;
				} else {
					reqJson = $scope.buildSwitchReqJson(reqJson, dbSource, branchDB0Selected, "0", branchDB1Selected, "1", branchDB2Selected, "2");
				}
				break;
			case "1":
				if( !$scope.bdsSwitchCheck(dbSource, branchDB1Selected, branchDB0Selected, branchDB2Selected) ) {
					return;
				} else {
					reqJson = $scope.buildSwitchReqJson(reqJson, dbSource, branchDB1Selected, "1", branchDB0Selected, "0", branchDB2Selected, "2");
				}
				break;
			case "2":
				if( !$scope.bdsSwitchCheck(dbSource, branchDB2Selected, branchDB0Selected, branchDB1Selected) ) {
					return;
				} else {
					reqJson = $scope.buildSwitchReqJson(reqJson, dbSource, branchDB2Selected, "2", branchDB0Selected, "0", branchDB1Selected, "1");
				}
				break;
			} 
		}
		if( reqJson != "" ) {
			reqJson = "["+reqJson+"]";
		} else {
			//都没选
			return;
		}
		
		$http({
			method : 'POST',
			url : 'rest/doBdsSwitch',
			data : reqJson,
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$.AMUI.progress.done();
			$scope.alertTitle="提示";
			$scope.alertInfo=response.data;
			var $modal = $('#my-modal');
		    $modal.modal();
		}, function errorCallback(response) {
			$.AMUI.progress.done();
			$scope.alertTitle="警告";
			$scope.alertInfo="failed!";
			var $modal = $('#my-modal');
		    $modal.modal();
		});
	};
	$scope.refreshBds = function() {
		$scope.getSwitchBdsList();
	};
	//主备切换校验
	$scope.bdsSwitchCheck = function(dbSource, bMainSelected, bBak1Selected, bBak2Selected) {
		//其中一个复选框打勾,则进行校验
		if( bMainSelected == true || bBak1Selected == true || bBak2Selected == true) {
			if( bMainSelected == false ) {
				$.AMUI.progress.done();
				$scope.alertTitle="警告";
				$scope.alertInfo="主备切换"+dbSource+"主库节点必须打勾.";
				var $modal = $('#my-modal');
			    $modal.modal();
				return false;
			} else if( bBak1Selected == bBak2Selected) {
				$.AMUI.progress.done();
				$scope.alertTitle="警告";
				$scope.alertInfo="主备切换"+dbSource+"备库节点必须(或者只能)打勾选择一个.";
				var $modal = $('#my-modal');
			    $modal.modal();
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	};
	//构造主备切换请求json串
	$scope.buildSwitchReqJson = function(reqJson, dbSource, bMainSelected, mainSerType, bBak1Selected, bak1SerType, bBak2Selected, bak2SerType) {
		var tmpReqJson = "";
		var dest = "";
		if( bMainSelected ) {
			if( bBak1Selected ) {
				dest = bak1SerType;
			}else if(bBak2Selected) {
				dest = bak2SerType;
			}
			tmpReqJson = "{\"source\":\"" + dbSource + "\",\"from\":\""+mainSerType+"\",\"dest\":\""+dest+"\"}";
		} else {
			//全没选,不进行主备切换
		}
		
		if( tmpReqJson != "" ) {
			if( reqJson != "" ) {
				reqJson = reqJson+","+tmpReqJson;
			} else {
				reqJson = tmpReqJson;
			}
		}
		
		return reqJson;
	};
	$scope.getSwitchBdsList();
});