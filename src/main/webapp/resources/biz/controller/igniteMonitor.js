var app = angular.module('igniteManage', ["ngTable"]);
app.controller('igniteManageController', function($scope, $http ,NgTableParams) {
	var data = {};
	var count=6;
	$http({
		method : 'POST',
		url : 'rest/getIgniteStateACenter',
		data : data,
		headers : {
			"Content-Type" : "application/json"
		}
	}).then(function successCallback(response) {
        //$scope.dyna_table(response.data.resultValue,'igniteRunning');
        
        if (response.data.resultValue!=null){

            $scope.params=response.data.resultValue;

            $scope.params=$scope.params.replace(/\n/g,"\\n");
            $scope.params= eval($scope.params);

            $scope.items= $scope.params

            setTimeout(function(){

                layui.use(['table','element'], function(){
                    var table = layui.table;
                    table.init('demo', {

                        page:true,
                        title:'集群在线监控看板'
                        // toolbar: '#toolbarDemo'
                        // ,cols: [[
                        //     {type:'checkbox'}
                        // ]]

                    });

                });
            },20);


        }
        


	}, function errorCallback(response) {
		$.AMUI.progress.done();
		$scope.alertInfo="failed!";
		var $modal = $('#my-modal');
	    $modal.modal();
	});

    $http({
        method : 'POST',
        url : 'rest/getIgniteStateBCenter',
        data : data,
        headers : {
            "Content-Type" : "application/json"
        }
    }).then(function successCallback(response) {
        //$scope.dyna_table(response.data.resultValue,'igniteRunning');

        if (response.data.resultValue!=null){


        }


        $scope.bCenterParams=response.data.resultValue;

        $scope.bCenterParams=$scope.bCenterParams.replace(/\n/g,"\\n");
        $scope.bCenterParams= eval($scope.bCenterParams);
        $scope.bCenteritems= $scope.bCenterParams

        setTimeout(function(){

            layui.use(['table','element'], function(){
                var table = layui.table;
                table.init('bCenter', {

                    page:true,
                    title:'集群在线监控看板'
                    // toolbar: '#toolbarDemo'
                    // ,cols: [[
                    //     {type:'checkbox'}
                    // ]]

                });

            });
        },20);

    }, function errorCallback(response) {
        $.AMUI.progress.done();
        $scope.alertInfo="failed!";
        var $modal = $('#my-modal');
        $modal.modal();
    });

    $http({
        method : 'POST',
        url : 'rest/getDataGridACenter',
        data : data,
        headers : {
            "Content-Type" : "application/json"
        }
    }).then(function successCallback(response) {
        var str = response.data.resultValue;

        str=str.replace(/\n/g,"\\n");
        $scope.paramACenter= eval(str);
        $scope.dataGridACenter=$scope.paramACenter
        setTimeout(function(){

            layui.use(['table'], function(){
                var table = layui.table;
                table.init('dataGridACenter', {

                    page:true,
                    title:'数据网格在线监控看板'
                });

            });
        },20);

    }, function errorCallback(response) {
        $.AMUI.progress.done();
        $scope.alertInfo="failed!";
        var $modal = $('#my-modal');
        $modal.modal();
    });

    $http({
        method : 'POST',
        url : 'rest/getDataGridBCenter',
        data : data,
        headers : {
            "Content-Type" : "application/json"
        }
    }).then(function successCallback(response) {
        var str = response.data.resultValue;
        str=str.replace(/\n/g,"\\n");
        $scope.paramBCenter = eval(str);
        $scope.dataGridACenter = $scope.paramBCenter;

        setTimeout(function(){

            layui.use(['table'], function(){
                var table = layui.table;
                table.init('dataGridBCenter', {

                    page:true,
                    title:'数据网格在线监控看板'
                });

            });
        },20);

    }, function errorCallback(response) {
        $.AMUI.progress.done();
        $scope.alertInfo="failed!";
        var $modal = $('#my-modal');
        $modal.modal();
    });


    $http({
        method : 'POST',
        url : 'rest/serverNodeList',
        data : data,
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

        $scope.item = $scope.data


        setTimeout(function(){

            layui.use(['table'], function(){
                var table = layui.table;
                table.init('dataTable', {

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


    $scope.getNodes= function() {
        var dataNode = {};
        count--;
        $http({
            method : 'POST',
            url : 'rest/serverNodeList',
            data : dataNode,
            headers : {
                "Content-Type" : "application/json",
                "charset": "UTF-8"
            }
        }).then(function successCallback(response) {
           // $scope.dyna_table(response.data.resultValue,'nodeList');
          //数据源
            $scope.data = response.data.resultValue;
            $scope.data=$scope.data.replace(/\n/g,"\\n");

            $scope.data= eval($scope.data);
           // $scope.interTime(count);
            //$scope.data=angular.toJson($scope.data);

            $scope.items=$scope.data;


            setTimeout(function(){

                layui.use(['table'], function(){
                    var table = layui.table;
                    table.init('node', {

                        page:true,
                        // toolbar: '#toolbarDemo'
                        // ,cols: [[
                        //     {type:'checkbox'}
                        // ]]

                    });

                });
            },20);
            //分页总数
            // $scope.pageSize = 5;
            // $scope.pages = Math.ceil($scope.data.length / $scope.pageSize); //分页数
            // $scope.newPages = $scope.pages > 5 ? 5 : $scope.pages;
            // $scope.pageList = [];
            // $scope.selPage = 1;
            // //设置表格数据源(分页)
            // $scope.setData = function () {
            // $scope.items = $scope.data.slice(($scope.pageSize * ($scope.selPage - 1)), ($scope.selPage * $scope.pageSize));//通过当前页数筛选出表格当前显示数据
            // }
            // $scope.items = $scope.data.slice(0, $scope.pageSize);
            // //分页要repeat的数组
            // for (var i = 0; i < $scope.newPages; i++) {
            // $scope.pageList.push(i + 1);
            // }
            // //打印当前选中页索引
            // $scope.selectPage = function (page) {
            // //不能小于1大于最大
            // if (page < 1 || page > $scope.pages) return;
            // //最多显示分页数5
            // if (page > 2) {
            // //因为只显示5个页数，大于2页开始分页转换
            // var newpageList = [];
            // for (var i = (page - 3) ; i < ((page + 2) > $scope.pages ? $scope.pages : (page + 2)) ; i++) {
            // newpageList.push(i + 1);
            // }
            // $scope.pageList = newpageList;
            // }
            // $scope.selPage = page;
            // $scope.setData();
            // $scope.isActivePage(page);
            // console.log("选择的页：" + page);
            // };
            // //设置当前选中页样式
            // $scope.isActivePage = function (page) {
            // return $scope.selPage == page;
            // };
            // //上一页
            // $scope.Previous = function () {
            // $scope.selectPage($scope.selPage - 1);
            // }
            // //下一页
            // $scope.Next = function () {
            // $scope.selectPage($scope.selPage + 1);
            // };
            //  document.getElementById('divMain').style.display = 'block';
            // document.getElementById('page').style.display = 'block';
        }, function errorCallback(response) {
            $.AMUI.progress.done();
            $scope.alertInfo="failed!";
            var $modal = $('#my-modal');
            $modal.modal();
        });
    };
    
//    $scope.interTime=function(x){
//    	console.log(x);
//    	if(x>0){
//    		var timeout_upd = $timeout($scope.getNodes,10000);
//    	}else{
//    		$timeout.cancel(timeout_upd);
//    			}
//    	}


    $scope.getData= function() {
        var dataNode = {};
        $http({
            method : 'POST',
            url : 'rest/serverDataList',
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

            $scope.item = $scope.data


            setTimeout(function(){

                layui.use(['table'], function(){
                    var table = layui.table;
                    table.init('dataTable', {

                        page:true,
                        // toolbar: '#toolbarDemo'
                        // ,cols: [[
                        //     {type:'checkbox'}
                        // ]]

                    });

                });
            },20);

            // //$scope.data=angular.toJson($scope.data);
            // //分页总数
            // $scope.pageSize = 5;
            // $scope.pages = Math.ceil($scope.data.length / $scope.pageSize); //分页数
            // $scope.newPages = $scope.pages > 5 ? 5 : $scope.pages;
            // $scope.pageList = [];
            // $scope.selPage = 1;
            // //设置表格数据源(分页)
            // $scope.setData = function () {
            //     $scope.item = $scope.data.slice(($scope.pageSize * ($scope.selPage - 1)), ($scope.selPage * $scope.pageSize));//通过当前页数筛选出表格当前显示数据
            // }
            // $scope.item = $scope.data.slice(0, $scope.pageSize);
            // console.log("$scope.items"+$scope.items);
            // //分页要repeat的数组
            // for (var i = 0; i < $scope.newPages; i++) {
            //     $scope.pageList.push(i + 1);
            // }
            // //打印当前选中页索引
            // $scope.selectPage = function (page) {
            //     //不能小于1大于最大
            //     if (page < 1 || page > $scope.pages) return;
            //     //最多显示分页数5
            //     if (page > 2) {
            //         //因为只显示5个页数，大于2页开始分页转换
            //         var newpageList = [];
            //         for (var i = (page - 3) ; i < ((page + 2) > $scope.pages ? $scope.pages : (page + 2)) ; i++) {
            //             newpageList.push(i + 1);
            //         }
            //         $scope.pageList = newpageList;
            //     }
            //     $scope.selPage = page;
            //     $scope.setData();
            //     $scope.isActivePage(page);
            //     console.log("选择的页：" + page);
            // };
            // //设置当前选中页样式
            // $scope.isActivePage = function (page) {
            //     return $scope.selPage == page;
            // };
            // //上一页
            // $scope.Previous = function () {
            //     $scope.selectPage($scope.selPage - 1);
            // }
            // //下一页
            // $scope.Next = function () {
            //     $scope.selectPage($scope.selPage + 1);
            // };
            // document.getElementById('divMain').style.display = 'block';
            // document.getElementById('page').style.display = 'block';
        }, function errorCallback(response) {
            $.AMUI.progress.done();
            $scope.alertInfo="failed!";
            var $modal = $('#my-modal');
            $modal.modal();
        });
    };








});