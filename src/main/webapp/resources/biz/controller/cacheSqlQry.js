var app = angular.module('zkManage', []);
app.controller('zkManageController', function($scope, $http) {
	$scope.Info=[{"path":"查询"}]

	$.AMUI.progress.set(0.3);
	$.AMUI.progress.start();
	
	//$scope.params = params;
   // $scope.conf = [];
	$scope.conf = [];


	var data = {};
	$scope.slice="Slice";
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
	
	$scope.getCacheName= function(s) {
        $scope.cachename=s;
    };
    
	$scope.getDomain= function(s) {
		$scope.cachename=s;
        var name=$scope.cachename;
        if($scope.cachename.indexOf($scope.slice)>-1){
        	$scope.length=$scope.cachename.length-6;
        	$scope.cachename=$scope.cachename.substring(0,$scope.length);
        	console.log($scope.cachename);
        }
        var params={'cacheName':$scope.cachename};
        $scope.submitForm(name);
        $http({
            method : 'POST',
            url : 'rest/getDomain',
            data : params,
            headers : {
                "Content-Type" : "application/json",
                "charset": "UTF-8"
            }
        }).then(function successCallback(response) {
        	$scope.data=response.data.value;
        	$scope.data=$scope.data.replace(/[,]/g,"\",\"");
        	$scope.data=$scope.data.replace(/["]]/g,"]");
        	//$scope.data=str.replace(/\n/g,"\\n");
            $scope.params= eval($scope.data);
            document.getElementById('tabledata').style.display = 'block';
        }, function errorCallback(response) {
        	alert("fail");
            $.AMUI.progress.done();
            $scope.alertInfo="failed!";
            var $modal = $('#my-modal');
            $modal.modal();
        });
    };
    
    $scope.submitForm = function(s){
    	//console.log($scope.conf);
        //$modalInstance.close($scope.conf);
    	 //var params={'cacheName':$scope.params};
    	 var array1 = new Array();
    	 var array2 = new Array(); 
    	 if(s!=undefined){
    	 $scope.name=s;
    	 }
    	 //if($scope.name.indexOf($scope.slice)==-1){
    	//	 $scope.name=$scope.name+"-Slice";
         //}
    	 console.log($scope.name);
    	 arr1=$scope.params;
    	 arr2=$scope.conf;
    	 $scope.str="";
    	 for(var i=0;i<arr2.length;i++) {
    	 	if (i==arr2.length-1 && arr2[i]!="" && arr2[i]!=undefined){
    	 		$scope.str+=arr1[i]+"="+arr2[i];
    	     }else
    	     {
    	     	if (arr2[i]!="" && arr2[i]!=undefined){
    	     		$scope.str+=arr1[i]+"="+arr2[i]+" and ";
    	     	}
    	     }
    	 }
    	console.log($scope.str); 
    	var params={'cacheName':$scope.name,'sql':$scope.str};
		$http({
            method : 'POST',
            url : 'rest/submitForm',
            data : params,
            headers : {
                "Content-Type" : "application/json",
                "charset": "UTF-8"
            }
        }).then(function successCallback(response) {
            //$scope.dyna_table(response.data.value);
            
          //数据源
        	//alert("success!");
            //$scope.data = response.data.value;         
            $scope.data = response.data.value;
            $scope.data=$scope.data.replace(/\[/g,'{')
        	$scope.data=$scope.data.replace(/\]/g,"}");
        	$scope.data="["+$scope.data+"]";
            $scope.data=$scope.data.replace(/\n/g,"\\n");
            $scope.data= eval($scope.data);
            document.getElementById('sqlmain').style.display = 'block';
            //$scope.data=angular.toJson($scope.data);
            //分页总数
            $scope.pageSize = 5;
            $scope.pages = Math.ceil($scope.data.length / $scope.pageSize); //分页数
            $scope.newPages = $scope.pages > 5 ? 5 : $scope.pages;
            $scope.pageList = [];
            $scope.selPage = 1;
            //设置表格数据源(分页)
           // window.open('cacheSqlQry3.html');
            $scope.setData = function () {
            $scope.items = $scope.data.slice(($scope.pageSize * ($scope.selPage - 1)), ($scope.selPage * $scope.pageSize));//通过当前页数筛选出表格当前显示数据
            }
            $scope.items = $scope.data.slice(0, $scope.pageSize);
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
            //console.log("选择的页：" + page);
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
        }, function errorCallback(response) {
        	alert("fail");
            $.AMUI.progress.done();
            $scope.alertInfo="failed!";
            var $modal = $('#my-modal');
            $modal.modal();
        });
    	 
    };
});