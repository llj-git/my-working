var app = angular.module('zkManage', ["ngSanitize", "ngCsv","ngTable"]);
app.controller('zkManageController', function($scope, $http,$interval,$timeout,NgTableParams) {
	$scope.Info=[{"path":"查询"}]

	var count=6;
	var exportdate = "";
	var data = {};
//	$.AMUI.progress.set(0.3);
//	$.AMUI.progress.start();
	$http({
		method : 'POST',
		url : 'rest/getCatheList',
		data : data,
		headers : {
			"Content-Type" : "application/json"
		}
	}).then(function successCallback(response) {

        $scope.cacheNames=response.data.cacheNames;
        console.log($scope.cacheNames);
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
	

	$scope.queryPk= function() {
		var params={'cacheName':$scope.cacheName,'key':$scope.key};
		count--;
		$http({
            method : 'POST',
            url : 'rest/queryPk',
            data : params,
            headers : {
                "Content-Type" : "application/json",
                "charset": "UTF-8"
            }
        }).then(function successCallback(response) {
            //$scope.dyna_table(response.data.value);        	
            $scope.data = response.data.value;
            $scope.data=$scope.data.replace(/\n/g,"\\n");
            console.log("$scope.data:"+$scope.data+"!!!!!!!!");
            $scope.data= eval($scope.data);

            $scope.items=$scope.data;


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



            //$scope.textPort($scope.data);
            //$scope.interTime(count);
            //$scope.data=angular.toJson($scope.data);
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
            // console.log(typeof $scope.items);
            // //分页要repeat的数组
            // for (var i = 0; i < $scope.newPages; i++) {
            // $scope.pageList.push(i + 1);
            // }
            //打印当前选中页索引
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
            //设置当前选中页样式
            // $scope.isActivePage = function (page) {
            // return $scope.selPage == page;
            // };
            //上一页
            // $scope.Previous = function () {
            // $scope.selectPage($scope.selPage - 1);
            // }
            //下一页
            // $scope.Next = function () {
            // $scope.selectPage($scope.selPage + 1);
            // };
            // if($scope.data !=""){
            // document.getElementById('dataTable').style.display = 'block';
            // document.getElementById('pageAndexport').style.display = 'block';
            // }
        }, function errorCallback(response) {
            $.AMUI.progress.done();
            $scope.alertInfo="failed!";
            var $modal = $('#my-modal');
            $modal.modal();
        });
	};
	
   $scope.getCacheName= function(s) {
      $scope.cacheName=s;
      var params={'cacheName':$scope.cacheName};
		$http({
          method : 'POST',
          url : 'rest/CachePk',
          data : params,
          headers : {
              "Content-Type" : "application/text",
              "charset": "UTF-8"
          }
      }).then(function successCallback(response) {
    	  $scope.cacheCommit = response.data.cacheCommit;
    	  document.getElementById('pk').style.display = 'block';
      }, function errorCallback(response) {
          $.AMUI.progress.done();
          $scope.alertInfo="查无相关字段!";
          var $modal = $('#my-modal');
          $modal.modal();
      });
    };
    
    /*定时刷新器
    $scope.interTime=function(x){
    	console.log(x);
    	if(x>0){
    		var timeout_upd = $timeout($scope.queryPk,10000);
    	}else{
    		$timeout.cancel(timeout_upd);
    			}
    	}
    	*/
    /*
    $scope.textPort=function(k){
    	$scope.getArray = k;
    	$scope.filename = "test";
    	$scope.charset = gb2312;
    	$scope.getHeader=function(){
       	 $scope.getHeader = function () {return []};
       }
    }
    */
    $scope.textPort=function(){
    	var k = exportdate;
    	var arrData = typeof k != 'object' ? JSON.parse(k) : k;
    	var cache = "cache";

        // #region 拼接数据

        var excel = '<table>';

        //设置表头
        var row = "<tr>";

        for (var name in arrData[0]) {
            //每个单元格都可以指定样式. eg color:red   生成出来的就是 红色的字体了.
            row += "<td style='color:red;text-align:center;'>" + name + '</td>';
        }

        //列头结束
        excel += row + "</tr>";

        //设置数据
        for (var i = 0; i < arrData.length; i++) {

            var row = "<tr>";
            for (var index in arrData[i]) {

                var value = arrData[i][index] === "." ? "" : arrData[i][index];

                row += '<td style="text-align:center;">' + value + '</td>';//将值放入td
            }
            //将td 放入tr,将tr放入table
            excel += row + "</tr>";
        }
        //table结束
        excel += "</table>";

        // #endregion


        // #region 拼接html

        var excelFile = "<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>";
        excelFile += '<meta http-equiv="content-type" content="application/vnd.ms-excel; charset=UTF-8">';
        excelFile += '<meta http-equiv="content-type" content="application/vnd.ms-excel';
        excelFile += '; charset=UTF-8">';
        excelFile += "<head>";
        excelFile += "<!--[if gte mso 9]>";
        excelFile += "<xml>";
        excelFile += "<x:ExcelWorkbook>";
        excelFile += "<x:ExcelWorksheets>";
        excelFile += "<x:ExcelWorksheet>";
        excelFile += "<x:Name>";
        excelFile += "{worksheet}";
        excelFile += "</x:Name>";
        excelFile += "<x:WorksheetOptions>";
        excelFile += "<x:DisplayGridlines/>";
        excelFile += "</x:WorksheetOptions>";
        excelFile += "</x:ExcelWorksheet>";
        excelFile += "</x:ExcelWorksheets>";
        excelFile += "</x:ExcelWorkbook>";
        excelFile += "</xml>";
        excelFile += "<![endif]-->";
        excelFile += "</head>";
        excelFile += "<body>";
        excelFile += excel;//将table 拼接
        excelFile += "</body>";
        excelFile += "</html>";

        // #endregion

        var uri = 'data:application/vnd.ms-excel;charset=utf-8,' + encodeURIComponent(excelFile);

        //创建a标签
        var link = document.createElement("a");
        //指定url
        link.href = uri;
        //设置为隐藏
        link.style = "visibility:hidden";
        //指定文件名和文件后缀格式
        link.download = cache + ".xls";
        //追加a标签
        document.body.appendChild(link);
        //触发点击事件
        link.click();
        //移除a标签
        document.body.removeChild(link);
    }
    ///var timeout_upd = $interval($scope.timeout,35000);
    //$scope.$on('$destroy',function(){
    //    if (timeout_upd) {
    //    	$interval.cancel(timeout_upd);
    //    	timeout_upd = null;
    //    }
   //   });
});