app = angular.module('querySql2DBView', ["ngTable"]);
app.controller("altiBaseQueryController",["$scope","$http","NgTableParams", function($scope,$http,NgTableParams){
	var self = this;
	// 表头显示
    $scope.columnLists = {COLUMN_1:"COLUMN_1",COLUMN_2:"COLUMN_2",COLUMN_3:"COLUMN_3",COLUMN_4:"COLUMN_4"};
	$scope.tableName = null;
	$scope.inputId = null;
	
	//加载下拉框
	$http({
		method : 'POST',
		url : 'rest/loadSyncTable',
		headers : {
			"Content-Type" : "application/json"
		} 
	}).then(function successCallback(response) {
		$scope.selectList=response.data;
	}, function errorCallback(response) {
		resetTable("请求失败，请检查！！！");
	});
	
	$http({
		method : 'POST',
		url : 'rest/loadTableField',
		headers : {
			"Content-Type" : "application/json"
		} 
	}).then(function successCallback(response) {
		$scope.tableField=response.data;
	}, function errorCallback(response) {
		resetTable("请求失败，请检查！！！");
	});
	
	//change事件
	$("#changeSel").change(function() { 
		$("#userInput").val("");
		var mainKey="";
		var s=$("#changeSel").val();
		var data=$scope.selectList;
		for (var i = 0; i < data.length; i++) {
			if(data[i].tableId==s){
				$("#keyId").text(data[i].mainKey+":");
				mainKey=data[i].mainKey;
			}
		}
		var tableName = $("#changeSel").find("option:selected").text().trim();
		var fields=$scope.tableField;
		$scope.conditionList=[];
		for(var i = 0; i < fields.length; i++) {
			if(fields[i].tableName==tableName && fields[i].field!=mainKey){
				$scope.conditionList.push(fields[i].field);
			}
		}
		$scope.$apply();
	});
	
     $scope.queryFun = function() {
    	$scope.altibaseValue=null;
    	$scope.oracleValue=null;
    	var condition="";
    	var tableName = $("#changeSel").find("option:selected").text().trim();
		var mainKeyValue=$("#userInput").val().trim();

		if(isEmpty(tableName)){
			$.AMUI.progress.done();
  			$scope.alertTitle="警告";
  			$scope.alertInfo="请选择操作的数据库表名！";
  			var $modal = $('#my-modal');
  		    $modal.modal();
  		    return;
		}
		if(isEmpty(mainKeyValue)){
			$.AMUI.progress.done();
  			$scope.alertTitle="警告";
  			$scope.alertInfo="请输入ID！";
  			var $modal = $('#my-modal');
  		    $modal.modal();
  		    return;
		}
		if($("#conditionSel").val()!=0){
			var conditionVal=$("#condition").val().trim();
			if(isEmpty(conditionVal)){
				$.AMUI.progress.done();
	  			$scope.alertTitle="警告";
	  			$scope.alertInfo="请输入备选条件值！";
	  			var $modal = $('#my-modal');
	  		    $modal.modal();
	  		    return;
			}else{
				var condition_prefix = $("#conditionSel").find("option:selected").text().trim();
				condition=condition_prefix+"='"+conditionVal+"'";
			}
		}
		$scope.inputId=mainKeyValue;
		$("#querybtn").attr("disabled",true);
		var data={
				"tableName":tableName,
				"mainKeyValue":mainKeyValue,
				"condition":condition,
				"type":2
		}	
		
		$http({
			method : 'POST',
			url : 'rest/getUserInfo',
			data : JSON.stringify(data),
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$scope.tableDataList = JSON.parse(response.data.data);
			if($scope.tableDataList.length > 0){
				$scope.columnLists=$scope.tableDataList[0];
				$scope.oracleValue=$scope.tableDataList;
			}else{
				layer.alert("oracle查询不到数据！！！", {shade: false});
			}
		}, function errorCallback(response) {
			resetTable("请求失败,请检查！！！")
		});
		
		//queryAltiBase
		$http({
			method : 'POST',
			url : 'rest/queryAltiBase',
			data : JSON.stringify(data),
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			if(response.data.result=="true"){
				$scope.returnData=JSON.parse(response.data.data);
				if($scope.returnData.length > 0){
					$scope.altibaseValue = $scope.returnData;
					if(isEmpty($scope.tableDataList)){
						$scope.columnLists = $scope.returnData[0];
					}
				}else{
					layer.alert("altibase查询不到数据！！！", {shade: false});
				}
			}
		}, function errorCallback(response) {
			resetTable("请求失败,请检查！！！")
		});
		
		 setTimeout(function(){
	     		$("#querybtn").attr("disabled",false);
	     	},3000);
	}
    

     
    function resetTable(msg){
    	  $scope.columnLists = {COLUMN_1:"COLUMN_1",COLUMN_2:"COLUMN_2",COLUMN_3:"COLUMN_3",COLUMN_4:"COLUMN_4"};
    	  $scope.tableDataList = null;
    	  $scope.columnLists=null;
    	  $scope.tableName = null;
      	  $scope.inputId = null;
      	  $scope.altibaseValue=null;
      	  $scope.oracleValue=null;
    	  layer.alert(msg, {shade: false});
    }
    
    $scope.resetFun = function() {
    	$scope.tableName = null;
    	$scope.inputId = null;
    	$scope.tableDataList=null;
    	$scope.conditionList=null;
    	$scope.columnLists = {COLUMN_1:"COLUMN_1",COLUMN_2:"COLUMN_2",COLUMN_3:"COLUMN_3",COLUMN_4:"COLUMN_4"};
    	$("#userInput").val("");
    	$("#condition").val("");
    	$("#changeSel").val('0');
    	$("#conditionSel").val('0');
    	$scope.altibaseValue=null;
    	$scope.oracleValue=null;
    }
    
	
}]);


function isEmpty(obj) {
	if (typeof obj == "undefined" || obj == null || obj == "") {
		return true;
	} else {
		return false;
	}
}



