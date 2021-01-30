	var app = angular.module('metLineApp', []);
	app.controller('metLineController', function($scope, $http) {

		/**
		 * 初始化加载页面
		 */
		$http({
			method : 'POST',
			url : '../rest/queryAllMetricsDetail',
			data : {"appInstanceName":null, "beginDate":null, "endDate":null},
			headers : {
				"Content-Type" : "application/json ; charset=UTF-8"
			}
		}).then(function successCallback(response) {
			var tArray = new Array();
			var maxArray = new Array();
			var minArray = new Array();
			var meanArray = new Array();
			for (var i = 0; i < response.data.length; i++){
				tArray.push(response.data[i].time);
				maxArray.push(response.data[i].max);
				minArray.push(response.data[i].min);
				meanArray.push(response.data[i].mean);
			}
			drawGraph(maxArray, meanArray, minArray, tArray);
		}, function errorCallback(response) {
		});
		
		/**
		 * 查找应用实例
		 */
		$http({
			method : 'POST',
			url : '../rest/queryAllAppNames',
			headers : {
				"Content-Type" : "application/json ; charset=UTF-8"
			}
		}).then(function successCallback(response) {
			$scope.appInstanceName = response.data;
		}, function errorCallback(response) {
			
		});
		
		/**
		 * 查找文件名
		 */
		$http({
			method : 'POST',
			url : '../rest/queryAllFileNames',
			headers : {
				"Content-Type" : "application/json ; charset=UTF-8"
			}
		}).then(function successCallback(response) {
			$scope.fileName = response.data;
		}, function errorCallback(response) {
			
		});
		
		/**
		 * 查找主机名
		 */
		$http({
			method : 'POST',
			url : '../rest/queryAllHostNames',
			headers : {
				"Content-Type" : "application/json ; charset=UTF-8"
			}
		}).then(function successCallback(response) {
			$scope.hostName = response.data;
		}, function errorCallback(response) {
			
		});
		
		
		
		/**
		 * 重置
		 */
		$scope.resetBtn = function() {
			$scope.data.hostName = null;
			$scope.data.appInstanceName = null;
			$scope.data.fileName = null;
			$scope.data.beginDate = null;
			$scope.data.endDate = null;
		};
		
		/**
		 * 查找MetricsDetil
		 */
		$scope.queryBtn = function(){
			
			$http({
				method : 'POST',
				url : '../rest/queryAllMetricsDetail',
				data : $scope.data,
				headers : {
					"Content-Type" : "application/json ; charset=UTF-8"
				}
			}).then(function successCallback(response) {
				var tArray = new Array();
				var maxArray = new Array();
				var minArray = new Array();
				var meanArray = new Array();
				for (var i = 0; i < response.data.length; i++){
					tArray.push(response.data[i].time);
					maxArray.push(response.data[i].max);
					minArray.push(response.data[i].min);
					meanArray.push(response.data[i].mean);
				}
				drawGraph(maxArray, meanArray, minArray, tArray);
			}, function errorCallback(response) {
			});
			
		};
		
		
		
		/**
		 * 绘图
		 */
		function drawGraph(maxArray, meanArray, minArray, tArray){
			
			var dom = document.getElementById("lineGraph");
			var myChart = echarts.init(dom);
			var app = {};
			option = null;
			option = {
			    title: {
			        text: ''
			    },
			    tooltip: {
			        trigger: 'axis'
			    },
			    legend: {
			        data:['最大时间','平均时间','最小时间']
			    },
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },
			    toolbox: {
			        feature: {
			            saveAsImage: {}
			        }
			    },
			    xAxis: {
			        type: 'category',
			        boundaryGap: false,
			        data: tArray
			    },
			    yAxis: {
			        type: 'value'
			    },
			    series: [
			        {
			            name:'最大时间',
			            type:'line',
			            stack: '总量',
			            data:maxArray
			        },
			        {
			            name:'平均时间',
			            type:'line',
			            stack: '总量',
			            data:meanArray
			        },
			        {
			            name:'最小时间',
			            type:'line',
			            stack: '总量',
			            data:minArray
			        }
			    ]
			};
			
			if (option && typeof option === "object") {
			    myChart.setOption(option, true);
			}
		}
		
			
	});