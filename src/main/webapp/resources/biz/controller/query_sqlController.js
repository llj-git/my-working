app = angular.module('querySqlView', ["ngTable"]);
app.controller("userInfoController",["$scope","$http","NgTableParams", function($scope,$http,NgTableParams){
		
	var self = this;
	// 表头显示
    $scope.columnLists = {COLUMN_1:"COLUMN_1",COLUMN_2:"COLUMN_2",COLUMN_3:"COLUMN_3",COLUMN_4:"COLUMN_4"};
	$scope.tableName = null;
	$scope.inputId = null;
    $scope.dataChangeGroup=[];
    
    
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
		var s=$("#changeSel").val();
		var mainKey="";
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
	
	//加载页面表格数据
 	function loadTable(tableDataList){
 		self.checkboxes = {
	      checked: false,items: {}
 		};

 	    self.tableParams=new NgTableParams({ count: 10},{
 	    	counts: [10, 20,30],
 	    	dataset:tableDataList
 	    });
 	    
 	    self.selectedPageSizes=self.tableParams.settings().counts;
 	    self.availablePageSizes = [5, 10, 15, 20];
 	    self.changePage = changePage;

 	    function changePage(nextPage){
 	      self.tableParams.page(nextPage);
 	    }
 	    function changePageSize(newSize){
 	      self.tableParams.count(newSize);
 	    }
 	    
 	    function changePageSizes(newSizes){
 	      if (newSizes.indexOf(self.tableParams.count()) === -1) {
 	        newSizes.push(self.tableParams.count());
 	        newSizes.sort();
 	      }
 	      self.tableParams.settings({ counts: newSizes});
 	    }
     }
 	

 	
     $scope.queryFun = function() {
    	var condition="";
    	var mainKeyValue=$("#userInput").val().trim();
		var tableName = $("#changeSel").find("option:selected").text().trim();

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
			"type":1
		}
		$http({
			method : 'POST',
			url : 'rest/getUserInfo',
			data : JSON.stringify(data),
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			fillTable(response);
			$scope.dataChangeGroup=[];
		}, function errorCallback(response) {
			resetTable("请求失败,请检查！！！")
		});
		
	}
    

    function fillTable(response){
    	if(angular.equals(response.data.result,"false")){
			resetTable(response.msg);
		}else {
			$scope.tableName=response.data.tableName.trim();
			if($scope.tableName=="user_product"){
				$("#desc").removeAttr("style");
			}else{
				$("#desc").css('display','none'); 
			}
			
			$scope.columnLists = new Array();
			$scope.tableDataList = JSON.parse(response.data.data);
			console.log($scope.tableDataList.length);
			if($scope.tableDataList.length > 0){
				$scope.columnLists=$scope.tableDataList[0];
				$("#columnName").val($scope.columnLists);
				loadTable($scope.tableDataList);
			}else{
				resetTable("查询不到需要的数据！！！");
			}
		}
    	
    	setTimeout(function(){
    		$("#querybtn").attr("disabled",false);
    	},3000)
    	
    	
    }
     
    function resetTable(msg){
    	  $scope.columnLists = {COLUMN_1:"COLUMN_1",COLUMN_2:"COLUMN_2",COLUMN_3:"COLUMN_3",COLUMN_4:"COLUMN_4"};
    	  $scope.tableDataList = null;
    	  $scope.columnLists=null;
    	  $scope.conditionList=null;
    	  $scope.tableName = null;
      	  $scope.inputId = null;
    	  loadTable($scope.tableDataList);
    	  layer.alert(msg, {shade: false});
    	  $("#desc").css('display','none'); 
    	  $("#querybtn").attr("disabled",false);
    }
    
    $scope.resetFun = function() {
    	$scope.tableName = null;
    	$scope.inputId = null;
    	$scope.tableDataList=null;
    	$scope.conditionList=null;
    	$scope.columnLists = {COLUMN_1:"COLUMN_1",COLUMN_2:"COLUMN_2",COLUMN_3:"COLUMN_3",COLUMN_4:"COLUMN_4"};
    	loadTable($scope.tableDataList);
    	$("#userInput").val("");
    	$("#condition").val("");
    	$("#changeSel").val('0');
    	$("#conditionSel").val('0');
    	$scope.dataChangeGroup=[];
    	$("#desc").css('display','none'); 
    }
    
    
    /**
     * 鼠标点击单元格变编辑框
     *
     */
    $scope.dataEdit=function(e){
        var td = $(e.target);
        var txt = td.text().trim();
        //user_product NAME字段进行处理禁止修改
        var str=$("#dataTable").find("tr").eq(2);
        var header=str.find("th").eq(td[0].cellIndex).text().trim();
        console.info("header:"+header);
        if(header=="NAME"){
        	return;
        }
 
        var input = $("<input type='text' value='" + txt + "' style='width:100px;height:30px;'/>");
        td.html(input);
        input.click(function() { 
        	return false; 
        });
        //获取焦点
        input.trigger("focus");
        //文本框失去焦点后提交内容，重新变为文本
        input.blur(function() {
            var newtxt = $(this).val();
              //判断文本有没有修改
            if (newtxt != txt) {
                td.html(newtxt);
                var cellIndex = td[0].cellIndex;//对应的值
                var rowIndex = (td.parent())[0].rowIndex-1;
                var changeRow={
            		"cellIndex":cellIndex,
            		"rowIndex":rowIndex
                }
                $scope.dataChangeGroup.push(changeRow);//保存变化值的位置
                console.info($scope.dataChangeGroup);
            }else{
                td.html(txt);
            }
        });
    };

    
    /**
     * 添加页面弹窗
     */
    $scope.addFun=function(){
    	if($scope.tableDataList==null){
			$.AMUI.progress.done();
  			$scope.alertTitle="警告";
  			$scope.alertInfo="请先进行查询操作!";
  			var $modal = $('#my-modal');
  		    $modal.modal();
		}else{
			var key=$("#keyId").text();
			key=(key.substring(0,key.length-1)).toUpperCase();
			var value=$("#userInput").val().trim();
    	
    	var html='<div class="content"><form method="post" id="myform" enctype="multipart/form-data">';
    	angular.forEach($scope.columnLists,function(obj,index){
    	   //console.log(index);
    	    if(index!='ROWID' && index!='$$hashKey'){
    	    	if($scope.tableName.toUpperCase()=="USER_PRODUCT" && "NAME"==index){
    	    		//user_product关联表另外2个字段不做处理
    	    	}else{
	    	    	html+='<div class="form-group row mt-2">'
		    	 		+'<div class="left col-xs-3 text-right"><label>'+index+':</label></div>'
		    	 		+'<div class="right col-xs-8 text-left"><input type="text" class="form-control" '
		    	 		+'id="'+index+'" name="'+index+'"'
	    	    	
	    	    	if(index==key){
	    	    	 	html+='value="'+value+'" readonly="readonly"></div></div>'
	    	    	}else{
		    	    	 if(index.toLowerCase().indexOf("time")>-1){
		    	    		 html+='placeholder="时间格式:yyyy-MM-dd HH:mm:ss"></div></div>'
		    	    	 }else{
		    	    		 html+='placeholder=""></div></div>'
		    	    	 }
	    	    	}
    	    	}
    	    }
    	});
    	html+='<div class="form-group text-center submit"><button type="button" class="btn btn-primary" onclick="submitInsertData()">提交</button>'
    		+'&nbsp;&nbsp;<button type="button" class="btn am-btn-default" onclick="closeIframe()">取消</button></div></form></div>';
    	
    	layer.open({
            title:'添加数据',
            type: 1,
            area: ['700px', '530px'],
            fix: false, //不固定
            maxmin: true,
            content:html,
            success:function(layero,index){
            },
            end:function(){
            }
        });
		}
    }

 
    /**
     * 查询套餐详情
     */
   $scope.queryDescFun=function(){
	   if($scope.tableName!="user_product"){
		   $.AMUI.progress.done();
			$scope.alertTitle = "警告";
			$scope.alertInfo ="仅支持user_product表套餐查询!";
			var $modal = $('#my-modal');
			$modal.modal();
			return;
	   }
	   var rowId=$('input:checkbox:checked').val();
		if(isEmpty(rowId)){
			$.AMUI.progress.done();
			$scope.alertTitle = "警告";
			$scope.alertInfo ="请选择数据进行查询!";
			var $modal = $('#my-modal');
			$modal.modal();
			return;
		 }
		var productId="";
		 $('input:checkbox:checked').each(function (index,item) {
			var row=$(this).parent("td").parent("tr");
			productId=row.find("[name='PRODUCT_ID']").html().trim();
	      
		 });
		 var data={
      			"productId": productId,
      			"dbName":$scope.tableName,
      			"userId":$scope.inputId
      	 }
		 $http({
	  			method : 'POST',
	  			url : 'rest/getProductDesc',
	  			data : JSON.stringify(data),
	  			headers : {
	  				"Content-Type" : "application/json ; charset=UTF-8"
	  			}
	  		}).then(function successCallback(response) {
	  			$.AMUI.progress.done();
	  			$scope.alertTitle="套餐详情";
	  			$scope.alertInfo=response.data.data;
	  			var $modal = $('#my-modal');
	  		    $modal.modal();
	  		    $scope.dataChangeGroup=[];
	  		}, function errorCallback(response) {
	  			$.AMUI.progress.done();
	  			$scope.alertTitle = "警告";
	  			$scope.alertInfo = response.data.data;
	  			var $modal = $('#my-modal');
	  			$modal.modal();
	  		});
		 
   }
    
    $scope.updateFun=function(){
    	var rowId=$('input:checkbox:checked').val();
    	//校验是否选中
		if(isEmpty(rowId)){
			$.AMUI.progress.done();
			$scope.alertTitle = "警告";
			$scope.alertInfo ="请选择数据进行修改!";
			var $modal = $('#my-modal');
			$modal.modal();
			return;
		 }
		
		var data=[];
    	 $('input:checkbox:checked').each(function (index, item) {
    		var rowIndex=($(this).parent().parent())[0].rowIndex-1;
    		rowId=$(this).val();
    		
    		var firsttr=$("#dataTable").find("tr").eq(2);
            var tr = $(this).eq(0).parent().parent(); // 找到选中的复选框所在的行
            var colum="";
            
            $scope.dataChangeGroup=$scope.dataChangeGroup.unRepeat();
            var groupLength=$scope.dataChangeGroup.length;
            for (var i = 0; i <groupLength ; i++) {
				if($scope.dataChangeGroup[i].rowIndex==rowIndex){
					var num=$scope.dataChangeGroup[i].cellIndex;
					var header=firsttr.find("th").eq(num).text().trim();
					if($scope.tableName.toUpperCase()=="USER_PRODUCT" && 'NAME'==header){
						continue;
					}
					var text = tr.find("td").eq(num).text().trim();
	            	colum+=header+"="+verifyData(text)+",";
				}
			}
  
            if(!isEmpty(colum)){
            	 var obj={
             			"colum": colum,
             			"rowId": rowId,
             			"dbName":$scope.tableName,
             			"inputId":$scope.inputId
             	 }
             	data.push(obj);
            }
           
		 });
    	 $("#submitbtn").attr("disabled",true);
    	 
    	$http({
  			method : 'POST',
  			url : 'rest/submitData',
  			data : JSON.stringify(data),
  			headers : {
  				"Content-Type" : "application/json ; charset=UTF-8"
  			}
  		}).then(function successCallback(response) {
  			$.AMUI.progress.done();
  			$scope.alertTitle="提示";
  			$scope.alertInfo=response.data.data;
  			var $modal = $('#my-modal');
  		    $modal.modal();
  		    $scope.dataChangeGroup=[];
  		}, function errorCallback(response) {
  			$.AMUI.progress.done();
  			$scope.alertTitle = "警告";
  			$scope.alertInfo = response.data.data;
  			var $modal = $('#my-modal');
  			$modal.modal();
  		});
    	setTimeout(function(){
    		$("#submitbtn").attr("disabled",false);
    	},2000);
    }
	
}]);

function submitInsertData(){
	console.log("submit Data...");
	var form = $("#myform").serializeArray();
	var tableName = $("#changeSel").find("option:selected").text().trim();
	console.log(tableName);

	var table = {
		"name" : "tableName",
		"value" : tableName
	}
	form.push(table);
		
	$.ajax({
	    type: "POST",
	    url: "rest/addUserInfo",
	    data: JSON.stringify(form),
	    headers : {
			"Content-Type" : "application/json"
		},
	    success: function(data){
			var rs = JSON.parse(data);
	    	if(rs.result=='success'){
	    		layer.msg('添加成功！',{
	                icon: 1,
	                time: 2000 //2秒关闭
	            },function(){
	            	closeIframe();
	            });
	    	}else{
	    		layer.msg('添加失败！',{
	                icon: 2,
	                time: 2000
	            },function(){
	            	closeIframe();
	            });
	    	}
	    }
	});
	
}

function closeIframe() {
	layer.closeAll();
}


/**
 * 校验数据格式
 * @param text
 * @returns
 */
function verifyData(text) {
	if (isEmpty(text)) {
		return null;
	}

	if((text.indexOf("-")!= -1) && (text.indexOf(":")!=-1)){
		//时间格式
		if(text.length>20){
			text=text.substring(0,19);
		}
		text = text.replaceAll(':', '');
		text = text.replaceAll('-', '');
		text = text.replace(/\s/g, "");
		return "to_date('"+text+"','yyyymmddhh24miss')";
	}
	return "'" + text + "'";
}


String.prototype.replaceAll=function(f,e){
    var reg=new RegExp(f,"g"); 
    return this.replace(reg,e); 
}

Array.prototype.unRepeat = function(){
	var emptyObj = {};
	var result = [];
	for(var i = 0;i<this.length;i++){
		this[i] = JSON.stringify(this[i]);
		if(!emptyObj[this[i]]){
			emptyObj[this[i]] = 1;
			result.push(JSON.parse(this[i]));
		}
	}
	return result;// 返回结果
}


function isEmpty(obj) {
	if (typeof obj == "undefined" || obj == null || obj == "") {
		return true;
	} else {
		return false;
	}
}




