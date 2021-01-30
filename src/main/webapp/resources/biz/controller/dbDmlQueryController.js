app = angular.module('dbDmlQueryView', ["ngTable"]);
app.controller("dbDmlQueryCtlr",["$scope","$http","NgTableParams", function($scope,$http,NgTableParams){
		
	var self = this;
	// 表头显示
    $scope.columnLists = {DB_NAME:"DB_NAME",COLUMN_1:"COLUMN_1",COLUMN_2:"COLUMN_2",COLUMN_3:"COLUMN_3"};
    $scope.sql = null;
	$scope.homecity = null;
	$scope.userId = null;
	
	//加载页面表格数据
 	function loadTable(dataList){
     	self.tableParams = new NgTableParams({count: 10}, {
     		counts: [],
     		dataset: dataList
    	});
     }
 	
 	// 执行方法
     $scope.queryFun = function() {
		console.log($scope.sql);
		console.log($scope.homecity);
		console.log($scope.userId);
		
		if($scope.sql == null || $scope.sql.length < 10){
			resetTable("语句不能为空或语句错误，请检查！！！")
			return;
		}
		
		if($scope.sql.substring(0,6).toUpperCase()!="SELECT"){
			resetTable("只支持数据查询，请检查！！！")
			return;
		}
		
		if($scope.homecity == null || $scope.userId == null){
			sqlDMLFunction("getSQLDMLList",$scope.sql);
		}else{
			if(!/^\d+$/.test($scope.userId)){
				resetTable("userId必须为数字,请检查！！！")
				return;
			}
			var addData = {
				sql : $scope.sql,
				homeCity : $scope.homecity,
				userId : $scope.userId
			}
			console.log(addData);
			sqlDMLFunction("addSqlData",addData);
		}
	}
    
     // CRUD DML 操作
    function sqlDMLFunction(url,data){
    	var sqlConfirm = confirm("确定是否执行当前的语句?");
		// 询问是否执行
		if(sqlConfirm == true){
			$http({
				method : 'POST',
				url : 'rest/'+url,
				data : data,
				headers : {
					"Content-Type" : "application/json"
				}
			}).then(function successCallback(response) {
				fillTable(response);
			}, function errorCallback(response) {
				resetTable("请求失败，请检查！！！")
			});
		}
    }
    
    // 渲染表格
    function fillTable(response){
    	if(angular.equals(response.data.result,"false")){
			resetTable(response.data.msg);
		}else {
			console.log(response.data);
			$scope.columnLists = new Array();
			$scope.dataList = JSON.parse(JSON.parse(response.data.data));
			console.log($scope.dataList);
			console.log($scope.dataList.length);
			if($scope.dataList.length > 0){
                $scope.columnLists = $scope.dataList[0];
			    /*	console.log(columnList);*/
				/*for ( var key in columnList) {
					$scope.columnLists.push(key);
				}*/
				console.log($scope.columnLists);
				loadTable($scope.dataList);
			}else{
				resetTable("查询不到需要的数据！！！");
			}
		}
    }
    
    // 重置表格
    function resetTable(msg){
    	  $scope.columnLists = {DB_NAME:"DB_NAME",COLUMN_1:"COLUMN_1",COLUMN_2:"COLUMN_2",COLUMN_3:"COLUMN_3"};
    	  $scope.dataList = null;
    	  loadTable($scope.dataList);
    	  layer.alert(msg, {shade: false});
    }
    
    // 重置
    $scope.resetFun = function() {
    	$scope.sql = null;
		$scope.homecity = null;
		$scope.userId = null;
    }
    
}]);
