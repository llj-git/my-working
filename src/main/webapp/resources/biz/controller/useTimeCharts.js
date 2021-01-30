var app = angular.module('useTimeChart', []);
app.controller('useTimeChartController', function($scope, $http) {
	$scope.dataList=[];
	$scope.data=null;
	/**
	 * 初始化下拉框加载页面
	 */
	$http({
		method : 'POST',
		url : 'rest/loadHostName',
		headers : {
			"Content-Type" : "application/json ; charset=UTF-8"
		}
	}).then(function successCallback(response) {
		$scope.hostDataList=response.data;
	}, function errorCallback(response) {
		layer.alert("初始化数据失败！", {
			shade : false
		});
	});
	
	
	
	$http({
		method : 'POST',
		url : 'rest/loadMetricsApp',
		headers : {
			"Content-Type" : "application/json ; charset=UTF-8"
		}
	}).then(function successCallback(response) {
		$scope.appList=response.data;
	}, function errorCallback(response) {
		layer.alert("初始化数据失败！", {
			shade : false
		});
	});
	
	/**
	 * 加载数据，默认只查询1次
	 */
	$http({
		method : 'POST',
		url : 'rest/getMetricsList',
		headers : {
			"Content-Type" : "application/json ; charset=UTF-8"
		}
	}).then(function successCallback(response) {
		$scope.metricsList=response.data;
	}, function errorCallback(response) {
		layer.alert("初始化数据失败！", {
			shade : false
		});
	});
	
	/**
	 * app下拉框联动变化
	 */
	$scope.appSelectChange=function(){
		$scope.dataList=[];
		$scope.hostNameList=[];
		var app=$scope.data.appInstanceName;
		console.log("选择app:"+app);
		
		//主机名框匹配
		for (var i = 0; i < $scope.hostDataList.length; i++) {
			if($scope.hostDataList[i].indexOf(app)!=-1){
				$scope.hostNameList.push($scope.hostDataList[i]);
			}
		}
		
		for (var i = 0; i < $scope.metricsList.length; i++) {
			if($scope.metricsList[i].app==app){
				if(isEmpty(($scope.metricsList[i].metricsName).trim())){
					$scope.metricsList[i].metricsName=$scope.metricsList[i].metrics;
				}else{
					$scope.metricsList[i].metricsName=$scope.metricsList[i].metrics+"("+$scope.metricsList[i].metricsName+")";
				}
				$scope.dataList.push($scope.metricsList[i]);
			}
		}
	}
	
	$(function () {
		$("input[name=allHost]").click(function () {
	        if (this.checked) {
	            $("#hostck :checkbox").attr("checked", true);
	        } else {
	            $("#hostck :checkbox").attr("checked", false);
	        }
	    });
		
		$("input[name=allFile]").click(function () {
			if (this.checked) {
				$("#fileck :checkbox").attr("checked", true);
			} else {
				$("#fileck :checkbox").attr("checked", false);
			}
		});
	});
	
	/**
	 * 重置
	 */
	$scope.resetBtn = function() {
		$scope.data.appInstanceName=null;
		$scope.data.beginDate=null;
		$scope.data.endDate=null;
		$scope.dataList=[];
		$scope.hostNameList=[];
		$("input[name=allHost]")[0].checked=false;
		$("input[name=allFile]")[0].checked=false;
	};

	
	$scope.queryBtn = function() {
		if($scope.hostNameList.length<1){
			$.AMUI.progress.done();
			$scope.alertTitle = "警告";
			$scope.alertInfo ="没有该主机数据,无法查询!";
			var $modal = $('#my-modal');
			$modal.modal();
			return;
		}
		
		var file="";
		var host="";
		var hostCount=0;
		var fileCount=0;
		$('input[name="file"]:checked').each(function(){
			if($(this).val()!=-1){
				file+="'"+$(this).val()+"',";
				fileCount++;
			}
		});
		
		$('input[name="host"]:checked').each(function(){
			if($(this).val()!=-1){
				host+="'"+$(this).val()+"',";
				hostCount++;
			}
		});
		
		file=file.substring(0,file.length-1);
		host=host.substring(0,host.length-1);
		console.info("file:"+file);
		console.info("hostName:"+host);

		if(isEmpty(file) || isEmpty(host)){//校验是否选中
			$.AMUI.progress.done();
			$scope.alertTitle = "警告";
			$scope.alertInfo ="请选择文件/主机,不能空白!";
			var $modal = $('#my-modal');
			$modal.modal();
			return;
		 }
		
		if(fileCount==$scope.dataList.length){
			file=null;
		}
		if(hostCount==$scope.hostNameList.length){
			host=null;
		}
		
		$scope.data.fileName=file;
		$scope.data.hostName=host;
		
		$http({
			method : 'POST',
			url : 'rest/getDetailData',
			data :$scope.data,
			headers : {
				"Content-Type" : "application/json ; charset=UTF-8"
			}
		}).then(function successCallback(response) {
			$scope.chartData=response.data;
			if($scope.chartData.length<1){
				//无数据不创建图表
				$.AMUI.progress.done();
				$scope.alertTitle = "提示";
				$scope.alertInfo ="查无数据！";
				var $modal = $('#my-modal');
				$modal.modal();
				return;
			}
			dealData();//处理数据
		}, function errorCallback(response) {
			layer.alert("无数据！", {
				shade : false
			});
		});

	}
	
	
	function dealData(){
		var series=[];
		var tArray = [];
		var appInstanceNameArray = [];
		
		for (var i = 0; i < $scope.chartData.length; i++){
			if(appInstanceNameArray.indexOf($scope.chartData[i].appInstanceName)==-1){
				appInstanceNameArray.push($scope.chartData[i].appInstanceName);
			}
			tArray.push($scope.chartData[i].time);
		}

		tArray=timeSort(tArray);
		for (var i = 0; i < appInstanceNameArray.length; i++) {
			var meanArray=[];
			for (var j = 0; j < $scope.chartData.length; j++) {
				if(appInstanceNameArray[i]==$scope.chartData[j].appInstanceName){
					meanArray.push([tArray.indexOf($scope.chartData[j].time),$scope.chartData[j].mean]);
				}
			}
			series.push({
				"name":appInstanceNameArray[i],
                "type": 'line',
                //"barWidth": '18%',
                "data": meanArray
			});
		}
		barGraph(appInstanceNameArray,tArray,series);//柱形图
	}

	
	/**
	 * 柱状图
	 */
	function barGraph(appInstanceNameArray,tArray,series){
        var myChart = echarts.init(document.getElementById('lineGraph'));
        var option = {
            title: {
                text: ''
            },
            tooltip:{
                show:true
            },
            toolbox: {
                show: true,
                feature: {
                    mark: { show: true },
                    magicType: { show: true, type: ['line', 'bar'] },
                    saveAsImage: { show: true }
                }
            },
            legend: {
            	data:[]
//            	data:appInstanceNameArray,
            },
            grid: {
		        bottom: '3%',
		        containLabel: true
		    },
            xAxis : {
				type : 'category',
		        boundaryGap: true,
				name:'时间',
				data : tArray,
			},
			yAxis : {
				name:'用时   ',
				min: 0,
				type: 'value',
				nameLocation:'end',
				
			},
			series:series
        };
        
        if (option && typeof option === "object") {
			myChart.setOption(option, true);
		}
	}
	
});

function timeSort(arr){
	return arr.sort(function(a,b){return a> b ? 1 : -1 })
}

function isEmpty(obj) {
	if (typeof obj == "undefined" || obj == null || obj == "") {
		return true;
	} else {
		return false;
	}
}