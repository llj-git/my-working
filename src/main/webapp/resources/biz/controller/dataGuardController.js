var app = angular.module('dataguard_switch', []);
app.controller('dataGuardController', function($scope, $http) {
	$scope.checkedDB=[];
	$scope.getDataGuardList = function() {
		$.AMUI.progress.set(0.3);
		$.AMUI.progress.start();
		var data = {};
	
		$http({
			method : 'POST',
			url : 'rest/getDateBaseList',
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

	
	/**
	 * 切换主备库
	 */
	$scope.dataGuardSwitch = function() {
		$.AMUI.progress.set(0.3);
		$.AMUI.progress.start();
		$scope.checkedDB=[];
		$('input:checkbox:checked').each(function (index, item) {
			 var object=JSON.parse($(this).val());
			 console.info(object.dbName);
			 $scope.checkedDB.push(object);
		});
		//校验用户选择数据是否合法
		var verifyResult=verifyInfo($scope.checkedDB);
		if (verifyResult != "success") {
			$.AMUI.progress.done();
			$scope.alertTitle = "警告";
			$scope.alertInfo = verifyResult;
			var $modal = $('#my-modal');
			$modal.modal();
			return;
		}

		$http({
			method : 'POST',
			url : 'rest/confirmInfo',
			data : JSON.stringify($scope.checkedDB),
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			if(response.data.result=="success"){
				$scope.confirmInfo=response.data.data;
				$scope.comfirmWindow();
			}else{
				$.AMUI.progress.done();
				$scope.alertTitle="警告";
				$scope.alertInfo=response.data.data;
				var $modal = $('#my-modal');
			    $modal.modal();
			}
			
		}, function errorCallback(response) {
			$.AMUI.progress.done();
			$scope.alertTitle="警告";
			$scope.alertInfo=response.data;
			var $modal = $('#my-modal');
		    $modal.modal();
		});
	};
	
	

	$scope.comfirmWindow=function(){
		 $('#my-confirm').modal({
			relatedTarget: this,
			closeOnConfirm:true,
			closeViaDimmer:false,
			closeOnCancel:true,
			onConfirm : function(e) {
				//确认框显示时，开始禁用切换按钮，避免用户重复进行下一次操作
				$("#switchBtn").attr("disabled",true);
				//确认无误则更新
				$http({
					method : 'POST',
					url : 'rest/switchDateBase',
					data : JSON.stringify($scope.checkedDB),
					headers : {
						"Content-Type" : "application/json"
					}
				}).then(function successCallback(response) {
					var result= response.data.data;
					$scope.getDataGuardList();
					$.AMUI.progress.done();
					$(".am-modal-hd").attr("background-color","#1C86EE");
					$scope.alertTitle = "提示";
					$scope.alertInfo = result;
					var $modal = $('#my-modal');
					$modal.modal();
					$scope.checkedDB=[];
					$("#switchBtn").attr("disabled", false);
				}, function errorCallback(response) {
					$.AMUI.progress.done();
					$scope.alertTitle = "警告";
					$scope.alertInfo = response.data;
					var $modal = $('#my-modal');
					$modal.modal();
				});
			},
		});
	}

	
	$scope.refreshDataGuard = function() {
		$scope.getDataGuardList();
	};

	$scope.getDataGuardList();
});

/**
 * 校验提交信息
 * @param hostNameGroup
 * @param dbGroup
 * @returns
 */
function verifyInfo(checkedDB) {
	if(checkedDB.length!=2){
		return "请选择1个主库和1个备库后重新操作";
	}
	var db1=checkedDB[0];
	var db2=checkedDB[1];
	if(db1.dbName!=db2.dbName){
		return "请选择同一个数据库名进行切换";
	}else if(db1.isWrite==db2.isWrite){
		return "切换主备库时必须有写库,请不要选择2个读库";
	}
	return "success";
}