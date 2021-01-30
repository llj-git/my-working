var app = angular.module('bosscloudView', []);
app.controller('bosscloudViewController', function($scope, $http) {
	$.AMUI.progress.set(0.3);
	$.AMUI.progress.start();
	$scope.nodeLength = 0;
	$scope.StopNodeSum = 0;
	$scope.normalNodeSum = 0;
	var data = {};
	$scope.appName=null;
	$scope.appNameNow;
	
	$http({
		method : 'POST',
		url : 'rest/getAppList',
		data : data,
		headers : {
			"Content-Type" : "application/json"
		}
	}).then(function successCallback(response) {
		$scope.grouping(response.data);
		var $tab = $('#bosscloud_app');
		$tab.tabs('refresh');
		$scope.changeZk();
		$.AMUI.progress.done();
	}, function errorCallback(response) {
		$.AMUI.progress.done();
		$scope.alertInfo = "failed!";
		var $modal = $('#my-modal');
		$modal.modal();
	});

	$scope.getNodeList = function(nodeName) {
		$scope.appName="";
		$('#infoList').removeAttr("style");
		$.AMUI.progress.set(0.3);
		$.AMUI.progress.start();
		$scope.appNodeList = [];
//		var $tab = $('#bosscloud_app');
//		$tab.tabs('refresh');
		var data = "{\"appName\":\"" + nodeName + "\"}";
		$http({
			method : 'POST',
			url : 'rest/getAppNodeList',
			data : data,
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$scope.resetCountNode();
			$scope.countNode(response.data);
			$scope.appNodeList = response.data;
			$.AMUI.progress.done();
			$scope.appName=nodeName;
			$scope.appNameNow=nodeName;
		}, function errorCallback(response) {
			$.AMUI.progress.done();
			$scope.alertInfo = "failed!";
			var $modal = $('#my-modal');
			$modal.modal();
		});
	};
	$scope.opentest = function() {
		$scope.alertInfo = "aaaaa";
		var $modal = $('#my-modal');
		$modal.modal();
	};
	
	$scope.resetCountNode = function() {
		$scope.StopNodeSum = 0;
		$scope.normalNodeSum = 0;
	};

	$scope.countNode = function(data) {
		$scope.nodeLength = data.length;
		for(var i = 0; i  < data.length ; i++){
			if(data[i].state == "stop"){
				$scope.StopNodeSum++;
			}else if(data[i].state == "normal"){
				$scope.normalNodeSum++;
			}else{
				continue;
			}
		}
	};

	//分组显示
	$scope.grouping = function(list) {
		var arr = [];
		var str = "";
		if(list.length < 8){
			arr = list;
		}else{
			for (var i = 0; i < list.length; i++) {
				if (list.length - i < 8) {
					arr.push(list[i]);
				} else {
					str += "<option value='" + list[i] + "' style='color:black;'>"
							+ list[i] + "</option>";
				}
			}
		}
		$scope.appNameList = arr;
		$("#nodeList").append(str);
	};

	$scope.changeZk = function() {
		var connectHost = $("#zkAddress").val();
		var data = connectHost == "" ? {} : JSON.stringify({
			"connectHost" : connectHost
		});
		$.AMUI.progress.set(0.3);
		$.AMUI.progress.start();
		$http({
			method : 'POST',
			url : 'rest/changeZKConn',
			data : data,
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$.AMUI.progress.done();
			$("#zkAddress").val(response.data[0].connectHost);
		}, function errorCallback(response) {
			$.AMUI.progress.done();
			$scope.alertInfo = "failed!";
			var $modal = $('#my-modal');
			$modal.modal();
		});
	};

	$("#nodeList").change(function() {
		var val = $('select option:selected').val();
		$scope.getNodeList(val);
	});
	
	//启动
	$scope.runBtn=function(){
		var flag=false;
		var array=[];
		var selectTab=$scope.appName;
		var appname = selectTab.substring(0, selectTab.indexOf("."));
		var exectype=$("input[name=radioCheck]:checked").val()
		if(isEmpty(exectype)){
			$scope.alertInfo = "请选择执行方式:串行/并行";
			var $modal = $('#my-modal');
			$modal.modal();
			return;
		}
		$('input:checkbox:checked').each(function (index, item) {
			 var object=JSON.parse($(this).val());
			 if(object.state!="stop"){
				 flag=true;
			 }
			 var node={
						"ip":object.ip,
						"appname":appname,
						"instancename":object.nodeName
					 }
			 array.push(node);
		});
		if(flag){
			$scope.alertInfo = "请勿选择已启动或者启动中再次启动!";
			var $modal = $('#my-modal');
			$modal.modal();
			return;
		}
		
		$.AMUI.progress.set(0.3);
		$.AMUI.progress.start();
		var data={
		   "exec_type":exectype,
		   "items":array
		};
		
		$http({
			method : 'POST',
			url : 'rest/appstart',
			data : JSON.stringify(data),
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$.AMUI.progress.done();
			var info="";
			if(isEmpty(response.data.data)){
				info="已启动,页面将自动刷新";
			}else{
				info=response.data.data;
			}
			$scope.alertInfo =info ;
			var $modal = $('#my-modal');
			$modal.modal();
		}, function errorCallback(response) {
			$.AMUI.progress.done();
			$scope.alertInfo = "failed!";
			var $modal = $('#my-modal');
			$modal.modal();
		});
	};
	
	//停止
	$scope.stopBtn=function(){
		var flag=false;
		var array=[];
		var selectTab=$scope.appName;
		var appname = selectTab.substring(0, selectTab.indexOf("."));
		var exectype=$("input[name=radioCheck]:checked").val()
		if(isEmpty(exectype)){
			$scope.alertInfo = "请选择执行方式:串行/并行";
			var $modal = $('#my-modal');
			$modal.modal();
			return;
		}
		$('input:checkbox:checked').each(function (index, item) {
			var object=JSON.parse($(this).val());
			 if(object.state=="stop"){
				 flag=true;
			 }
			var node={
					"ip":object.ip,
					"appname":appname,
					"instancename":object.nodeName
			}
			array.push(node);
		});
		if(flag){
			$scope.alertInfo = "请勿选择停止再次停止!";
			var $modal = $('#my-modal');
			$modal.modal();
			return;
		}
		
		$.AMUI.progress.set(0.3);
		$.AMUI.progress.start();
		var data={
				"exec_type":exectype,
				"items":array
		};
		
		$http({
			method : 'POST',
			url : 'rest/appstop',
			data : JSON.stringify(data),
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$.AMUI.progress.done();
			var info="";
			if(isEmpty(response.data.data)){
				info="已停止,页面将自动刷新";
			}else{
				info=response.data.data;
			}
			$scope.alertInfo = info;
			var $modal = $('#my-modal');
			$modal.modal();
		}, function errorCallback(response) {
			$.AMUI.progress.done();
			$scope.alertInfo = "failed!";
			var $modal = $('#my-modal');
			$modal.modal();
		});
	};
	
	//刷新
	$scope.refreshBtn=function(){
		console.info("refresh...");
		$scope.getNodeList($scope.appNameNow);
		
	}
	
});

function isEmpty(obj) {
	if (typeof obj == "undefined" || obj == null || obj == "") {
		return true;
	} else {
		return false;
	}
}
