var app = angular.module('igniteManage', ["ngTable"]);
app.controller('igniteManageController', function($scope, $http,$interval,$timeout,NgTableParams) {
	var data = {};
	$http({
		method : 'POST',
		url : 'rest/getDatagrid',
		data : data,
		headers : {
			"Content-Type" : "application/json"
		}
	}).then(function successCallback(response) {
		var str = response.data.resultValue;
		
    	str=str.replace(/\n/g,"\\n");
    	console.log(typeof str);
    	console.log(str);
        $scope.params= eval(str);
		
       // $scope.params=response.data.resultValue;
//		$scope.params = "[{cacheNodeSize:'bureau-number_sec_msisdn', cacheSize:'PARTITIONED', heapMemoryTotal:3, heapMemoryUsed:1024,cacheMemoryTotal:0,cacheKeysTotal:0, heapEntries:0}]";
//		console.log($scope.params);
//		$scope.params=$scope.params.replace(/\n/g,"\\n");
//		//$scope.params=  JSON.parse($scope.params);
//		$scope.params = JSON.parse(eval($scope.params));// 转成JSON格式
//        //$scope.params = $.parseJSON($scope.params);// 转成JSON对象
//		console.log("$scope.params"+$scope.params);
	}, function errorCallback(response) {
		$.AMUI.progress.done();
		$scope.alertInfo="failed!";
		var $modal = $('#my-modal');
	    $modal.modal();
	});

    $scope.getNodes= function() {
        var dataNode = {};
        $http({
            method : 'POST',
            url : 'rest/serverNodeList',
            data : dataNode,
            headers : {
                "Content-Type" : "application/json",
                "charset": "UTF-8"
            }
        }).then(function successCallback(response) {
            //$scope.dyna_table(response.data.resultValue,'nodeList');
            $scope.data = response.data.resultValue;
            console.log(response.data.resultValue);
            $scope.data=$scope.data.replace(/\n/g,"\\n");
            $scope.data= eval($scope.data);
            
            //$scope.data=angular.toJson($scope.data);
            //分页总数
            $scope.pageSize = 5;
            $scope.pages = Math.ceil($scope.data.length / $scope.pageSize); //分页数
            $scope.newPages = $scope.pages > 5 ? 5 : $scope.pages;
            $scope.pageList = [];
            $scope.selPage = 1;
            //设置表格数据源(分页)
            $scope.setData = function () {
            $scope.items = $scope.data.slice(($scope.pageSize * ($scope.selPage - 1)), ($scope.selPage * $scope.pageSize));//通过当前页数筛选出表格当前显示数据
            }
            $scope.items = $scope.data.slice(0, $scope.pageSize);
            console.log("$scope.items"+$scope.items);
            //分页要repeat的数组
            for (var i = 0; i < $scope.newPages; i++) {
            $scope.pageList.push(i + 1);
            }
            //打印当前选中页索引
            $scope.selectPage = function (page) {
            //不能小于1大于最大
            if (page < 1 || page > $scope.pages) return;
            //最多显示分页数5
            if (page > 2) {
            //因为只显示5个页数，大于2页开始分页转换
            var newpageList = [];
            for (var i = (page - 3) ; i < ((page + 2) > $scope.pages ? $scope.pages : (page + 2)) ; i++) {
            newpageList.push(i + 1);
            }
            $scope.pageList = newpageList;
            }
            $scope.selPage = page;
            $scope.setData();
            $scope.isActivePage(page);
            console.log("选择的页：" + page);
            };
            //设置当前选中页样式
            $scope.isActivePage = function (page) {
            return $scope.selPage == page;
            };
            //上一页
            $scope.Previous = function () {
            $scope.selectPage($scope.selPage - 1);
            }
            //下一页
            $scope.Next = function () {
            $scope.selectPage($scope.selPage + 1);
            };
            document.getElementById('divMain').style.display = 'block';
            document.getElementById('page').style.display = 'block';
        }, function errorCallback(response) {
            $.AMUI.progress.done();
            $scope.alertInfo="failed!";
            var $modal = $('#my-modal');
            $modal.modal();
        });
    };

    $scope.timeout=function(){
    	$timeout($scope.getNodes,30000)
    }
    var timeout_upd = $interval($scope.timeout,10000);
    $scope.$on('$destroy',function(){//跳转页面后清除定时器
        if (timeout_upd) {
        	$interval.cancel(timeout_upd);
        	timeout_upd = null;
        }
      });

    
    

});