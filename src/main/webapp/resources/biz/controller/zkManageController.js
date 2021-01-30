var app = angular.module('zkManage', []);
app.controller('zkManageController', function($scope, $http) {
	$scope.reqNo = "";
	$scope.zkId = "";
	$scope.Info=[{"path":"Home","text":"Welcome"}]
	var rightNodePath;
	$scope.zkPropRes=[];
	var zTree, rMenu;
	var defaultZkPathIp;
	var zkPathIp;

	$http({
		method : 'POST',
		url : 'rest/getZkCfg',
		data : '{}',
		headers : {
			"Content-Type" : "application/json"
		}
	}).then(function successCallback(response) {
		$scope.zkCfg = response.data;
	}, function errorCallback(response) {
		$.AMUI.progress.done();
		$scope.alertInfo="failed!";
		var $modal = $('#my-modal');
	    $modal.modal();
	});
	
	//--------左侧菜单--------
	var setting = {
			view: {
				dblClickExpand: false
			},
			check: {
				enable: true,
				chkboxType: { "Y" : "", "N" : "" }//不级联父子节点
			},
			async: {
                 enable: true,
                 url: "",
                 autoParam: ["path"]
            },
			callback: {
				onRightClick: OnRightClick,
				onClick: onClick
			},
			data: {
                simpleData: {
                    enable: true,
                    idKey: "name", // id编号命名 默认  
                    pIdKey: "pName", // 父id编号命名 默认  
                    rootPId: "0"
                }
            }
		};
	
	function onClick(event, treeId, treeNode, clickFlag) {
		var jsonText=treeNode.text;
		$("#textArea").val(jsonText);

		if(!isEmpty(jsonText)){
			$scope.getZkProp(jsonText,treeNode.path);
		}
		if(isEmpty(treeNode.children) ){
			$scope.getChildNode(treeNode.path,treeNode.tId);
		}
	}
	
	/**
	 * 左侧菜单鼠标右键功能
	 */
	function OnRightClick(event, treeId, treeNode) {
		rightNodePath=treeNode.path;
		if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
			zTree.cancelSelectedNode();
			showRMenu("root", event.clientX, event.clientY);
		} else if (treeNode && !treeNode.noR) {
			zTree.selectNode(treeNode);
			showRMenu("node", event.clientX, event.clientY);
		}
	}
	function showRMenu(type, x, y) {
		$("#rMenu ul").show();
        y += document.body.scrollTop;
        x += document.body.scrollLeft;
        rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});
		$("body").bind("mousedown", onBodyMouseDown);
	}
	function hideRMenu() {
		if (rMenu) rMenu.css({"visibility": "hidden"});
		$("body").unbind("mousedown", onBodyMouseDown);
	}
	function onBodyMouseDown(event){
		if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
			rMenu.css({"visibility" : "hidden"});
		}
	}
	
	/**
	 * 初始化默认连接zk菜单树
	 */
	$http({
		method : 'POST',
		url : 'rest/getDir',
		data : {},
		headers : {
			"Content-Type" : "application/json"
		}
	}).then(function successCallback(response) {
		$.AMUI.progress.done();
		//初始化菜单和ip显示
		$scope.loadTreeNode(response.data);
		var result = JSON.stringify(response.data);
		if(result!="[]"){
			defaultZkPathIp=response.data[0].zkHost;
			zkPathIp=response.data[0].zkHost;
		}
	}, function errorCallback(response) {
		$.AMUI.progress.done();
		$scope.alertInfo="failed!";
		var $modal = $('#my-modal');
	    $modal.modal();
	});
	
	/**
	 * 点击获取子节点
	 */
	$scope.getChildNode= function(path,tId){
		$("#addressPath").text(path);
		var data={
			"path":	path,
			"connectHost":zkPathIp
		}
		$http({
			method : 'POST',
			url : 'rest/getDir',
			data : data,
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$.AMUI.progress.done();
			var result = JSON.stringify(response.data);
			if(result!="[]"){
				zTree = $.fn.zTree.getZTreeObj("treeDemo");
				var node = zTree.getNodeByTId(tId);
				zTree.addNodes(node,response.data[0].data);
				
			}
		}, function errorCallback(response) {
			$.AMUI.progress.done();
			$scope.alertInfo="failed!";
			var $modal = $('#my-modal');
		    $modal.modal();
		});
	}
	
	
	
	//--------左侧菜单--------
	
	
	//--------右侧菜单--------
	var RightSetting = {
			data: {
				key: {
					
				},
				simpleData: {
					enable: true
				}
			},
			view: {
				dblClickExpand: false
			},
			callback: {
				onRightClick: showZkPropDesc,
			}
		};
	function showZkPropDesc(event, treeId, treeNode) {
		var result=getZkPropDesc(treeNode.id,$scope.zkPropRes);
		if(typeof(result)=="object"){
			$("#modalTitle").text("字段描述信息");
			$("#modalInfo").text(result.desc);
			$('#my-modal1').modal();
		}
	}


	//--------右侧菜单--------
	
	
	 $scope.btnQueryZk= function() {
		var inputzkIpPath=$("#zkAddress").val().trim();
		if(defaultZkPathIp==inputzkIpPath){
			return;
		}
		zkPathIp=inputzkIpPath;
		$.AMUI.progress.set(0.3);
		$.AMUI.progress.start();

		var data={
			"path":	"",
			"connectHost":zkPathIp
		}
		$http({
			method : 'POST',
			url : 'rest/getDir',
			data : JSON.stringify(data), 			            
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$.AMUI.progress.done();
			$scope.loadTreeNode(response.data);
		}, function errorCallback(response) {
			$.AMUI.progress.done();
			$scope.alertInfo="failed!";
			var $modal = $('#my-modal2');
		    $modal.modal();
		});
	 };

	/**
	 * 初始树节点
	 */
	$scope.loadTreeNode=function(data){
		var result = JSON.stringify(data);
		if(result!="[]"){
			$(document).ready(function(){
				$("#zkAddress").val(data[0].zkHost);
				$.fn.zTree.init($("#treeDemo"), setting, data[0].data);
				zTree = $.fn.zTree.getZTreeObj("treeDemo");
				var nodes = zTree.getNodes();
				for (var i = 0; i < nodes.length; i++) { 
					zTree.expandNode(nodes[i], true, false, true);// 设置节点展开
	            }
				rMenu = $("#rMenu");
			});
		}else{
			$scope.alertInfo="Connection failed!";
			var $modal = $('#my-modal2');
		    $modal.modal();
		}
	}
	
		
	$scope.saveData= function() {
		$.AMUI.progress.set(0.3);
		$.AMUI.progress.start();
		var nodepath = $("#addressPath").text().trim();
		var nodedata = $("#textArea").val().trim();
		$http({
			method : 'POST',
			url : 'rest/saveData',
			data : JSON.stringify({
				                  "nodepath": nodepath,
				                  "nodedata": nodedata
				              }),
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$.AMUI.progress.done();
			if(response.data="保存成功"){
				zTree = $.fn.zTree.getZTreeObj("treeDemo");
				var selectedNodes = zTree.getSelectedNodes();
				selectedNodes[0].text=nodedata;
			}
			$scope.alertTitle="提示";
			$scope.alertInfo=response.data;
			var $modal = $('#my-modal1');
		    $modal.modal();
		}, function errorCallback(response) {
			$.AMUI.progress.done();
			$scope.alertTitle="提示";
			$scope.alertInfo="保存失败!";
			var $modal = $('#my-modal2');
		    $modal.modal();
		});
	};

	
	
	/**
	 * 隐藏json树
	 */
	$scope.hideJsonTree= function(){
		$("#right_tree").hide();
		$("#right").css("left","330px");
		$("#right").css("width","auto");
		$("#right").css("margin-left","10px");
		$("#textArea").attr("cols","105");
		$("#showTreebtn").css('display','block'); 
		$("#hideJsonDataBtn").css('display','none'); 
	};
	
	/**
	 * 显示Json树
	 */
	$scope.showJsonTree=function(){
		$("#right_tree").show();
		$("#textArea").attr("cols","63");
		$("#right_tree").css("background","#FFFFFF");
		$("#showTreebtn").css('display','none'); 
		$("#hideJsonDataBtn").css('display','block'); 
		$("#right").css('width','550px');
	}
	
	/**
	 * 隐藏json数据
	 */
	$scope.hideJsonData=function(){
		$("#right").hide();
		$("#right").removeAttr("style");
		$("#right_tree").css("left","330px");
		$("#right_tree").css("width","auto");
		$("#right_tree").css("background","#FFFFFF");
		$("#right_tree").css("margin-left","10px");
		$("#restBtn").css('display','block'); 
		$("#showJsonDataBanner").css('display','block'); 
		$("#hideJsonTreeBtn").css('display','none'); 
		$("#showJsonDataBanner").css('height','55px'); 
	}

	/**
	 * 显示json数据
	 */
	$scope.showJsonData=function(){
		$("#right").show();
		$("#right_tree").css("left","900px");
		$("#right_tree").css("width","auto");
		$("#showJsonDataBanner").css('display','none'); 
		$("#hideJsonTreeBtn").css('display','block'); 
	}
	
	
	$scope.addNode= function() {
		$scope.selectNodePath=rightNodePath;
	    $('#my-prompt').modal({
	      relatedTarget: this,
	      	onConfirm: function(e) {
		    	$.AMUI.progress.set(0.3);
		  		$.AMUI.progress.start();
		  		var nodetilte = e.data;
		  		var nodepath=$scope.selectNodePath;
		  		document.getElementById("inf").value="";
	  		$http({
	  			method : 'POST',
	  			url : 'rest/addNode',
	  			data : JSON.stringify({
	  				                  "nodepath": nodepath,
	  				                  "nodetitle": nodetilte
	  				              }),
	  			headers : {
	  				"Content-Type" : "application/json"
	  			}
	  		}).then(function successCallback(response) {
	  			$.AMUI.progress.done();
	  			if(response.data.result="success"){
	  				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		  			var selectedNode = treeObj.getSelectedNodes();
			  		var newNode = {pName: selectedNode[0].name, name: nodetilte, path: nodepath+"/"+nodetilte,text: ""};  
			  		if (selectedNode.length > 0) {
			  		    treeObj.addNodes(selectedNode[0], newNode);  
			  		}
	  			}
	  			$scope.alertTitle="提示";
	  			$scope.alertInfo=response.data.data;
	  			var $modal = $('#my-modal1');
	  		    $modal.modal();
	  			rMenu = $("#rMenu");
	  		}, function errorCallback(response) {
	  			$.AMUI.progress.done();
	  			$scope.alertTitle="提示";
	  			$scope.alertInfo="添加失败!";
	  			var $modal = $('#my-modal2');
	  		    $modal.modal();
	  		});
	      },
	      onCancel: function(e) {
	    	  document.getElementById("inf").value="";
	      }
	    });
	};
	
	$scope.deleteNode= function(path) {
		$scope.selectNodePath=rightNodePath;
	    $('#my-confirm').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	$.AMUI.progress.set(0.3);
	  		$.AMUI.progress.start();
	  		var nodepath = $scope.selectNodePath;
	  		$http({
	  			method : 'POST',
	  			url : 'rest/deleteNode',
	  			data : JSON.stringify({
	  				                  "nodepath": nodepath
	  				              }),
	  			headers : {
	  				"Content-Type" : "application/json"
	  			}
	  		}).then(function successCallback(response) {
	  			$.AMUI.progress.done();
	  			if(response.data.result="success"){
	  				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	  				var nodes = treeObj.getCheckedNodes(true);
	  				if(nodes.length<1){
	  					nodes = treeObj.getSelectedNodes();
	  				}
	  				$(nodes).each(function(i,v){
			  			treeObj.removeNode(nodes[i]);  
			  		});
	  			}
	  		
	  			$scope.alertTitle="提示";
	  			$scope.alertInfo=response.data.data;
	  			var $modal = $('#my-modal1');
	  		    $modal.modal();
	  		    rMenu = $("#rMenu");
	  		}, function errorCallback(response) {
	  			$.AMUI.progress.done();
	  			$scope.alertTitle="提示";
	  			$scope.alertInfo="删除失败!";
	  			var $modal = $('#my-modal2');
	  		    $modal.modal();
	  		});
	      },
	      onCancel: function(e) {
	      }
	    });
   };
   $scope.copyNode= function(path) {
	   $scope.selectNodePath=rightNodePath;
	    $('#my-promptcp').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	$.AMUI.progress.set(0.3);
	  		$.AMUI.progress.start();
	  		var nodepath = $scope.selectNodePath;
	  		var nodetilte = e.data;
	  		document.getElementById("ins").value="";
	  		$http({
	  			method : 'POST',
	  			url : 'rest/copyNode',
	  			data : JSON.stringify({
	  				                  "nodepath": nodepath,
	  				                  "nodetitle": nodetilte
	  				              }),
	  			headers : {
	  				"Content-Type" : "application/json"
	  			}
	  		}).then(function successCallback(response) {
	  			$.AMUI.progress.done();
	  			if(response.data.result="success"){
	  				var copyNodes=[];
	  				copyNodes=response.data.node;
	  				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	  				var pName=treeObj.getSelectedNodes()[0].pName;
	  				var nodes = zTree.getNodesByParam("name", pName, null);
		  			var parentPath=nodepath.substr(0,nodepath.lastIndexOf("/"));
			  		var newNode = {pName: pName, name: nodetilte, path: parentPath+"/"+nodetilte,text: ""};  
			  		copyNodes.push(newNode);
			  		treeObj.addNodes(nodes[0],copyNodes);
	  			}
	  			$scope.alertTitle="提示";
	  			$scope.alertInfo=response.data.data;
	  			var $modal = $('#my-modal1');
	  		    $modal.modal();
				rMenu = $("#rMenu");
	  		}, function errorCallback(response) {
	  			$.AMUI.progress.done();
	  			$scope.alertTitle="提示";
	  			$scope.alertInfo="复制失败!";
	  			var $modal = $('#my-modal2');
	  		    $modal.modal();
	  		});
	      },
	      onCancel: function(e) {
	    	  document.getElementById("ins").value="";
	      }
	    });
   };
   
   $scope.exportData2Sql= function() {
	   var nodes = zTree.getCheckedNodes(true);
	   if( nodes.length == 0 ){
		   return;
	   }
	   var paths = [];
	   for(var i = 0; i < nodes.length; i++) {
		   paths.push( nodes[i].path);
       }
	      
	   $('#inputReqNo').modal({
		   onConfirm: function(e) {
			   var zkId = document.getElementById("zkId").value;
			   if( $scope.reqNo == "" || zkId == "" ) {
				   alert("需求编号或者zookeeper标识不能为空！");
				   return;
			   }
			   $http({
					method : 'POST',
					url: 'rest/exportData2Sql',
					data:{"reqNo":$scope.reqNo,"zkId":zkId,"paths":paths},
					responseType: "blob" 
				}).then(function successCallback(response) {
						// 这里会弹出一个下载框，增强用户体验
					    var blob = new Blob([response.data], {type: "multipart/form-data"});  
					    var fileName = $scope.reqNo+"_zk.sql";
					    
					    if (!!window.ActiveXObject || "ActiveXObject" in window){
					    	//IE
					    	window.navigator.msSaveBlob(blob, fileName);
					    } else {
					    	//Not IE
					    	var objectUrl = URL.createObjectURL(blob);  
				            //  创建a标签模拟下载  
				            var aForTxt = $("<a><span class='forTxt'>下载</span></a>").attr("href",objectUrl);  
				            aForTxt.attr("download",fileName);
				            $("body").append(aForTxt);  
				            $(".forTxt").click();  
				            aForTxt.remove(); 
					    }
			            $scope.reqNo="";
				}, function errorCallback(response) {
					 alert(response.data);
				});
		   },
		   onCancel: function(e) {
			   $scope.reqNo="";
		   }
	   });
   }
   
   $scope.getZkProp=function(jsonText,path){
		$http({
			method : 'POST',
			url : 'rest/getZkProp',
			data : JSON.stringify({
				                  "zkPath": path
				              }),
			headers : {
				"Content-Type" : "application/json"
			}
		}).then(function successCallback(response) {
			$.AMUI.progress.done();
			$scope.change2Tree(jsonText,response.data);
			$scope.zkPropRes=response.data;
		}, function errorCallback(response) {
			
		});
	};

	var treeNodes=[];
	$scope.change2Tree=function(jsonText,zkPropRes){
		$("#treeTextDemo").html("");//清空上回数据
		var zTreeRight;
		treeNodes=[];
		if (jsonText != null && jsonText != "" && jsonText.indexOf("{")!=-1) {
			var obj = eval("("+jsonText+")");
			//获取配置字段描述信息
			for (var p in obj) {
				var child1="";
				var child2="";
				var root="";
				var res=getZkPropDesc(p,zkPropRes);
				
				if(typeof(obj[p])!="object"){
					//key-value形式Joson串
					root={
							"id" : p,
							"pId" : 0,
							"name" : (typeof(res)=="object")?(res.name):p
						};
					child1={
							"id" : p+"."+obj[p],
							"pId" : p,
							"name" : obj[p]
						};
					 add2Group(treeNodes,root,child1,child2); 
				}
			
				if(typeof(obj[p])=="object"){
					//key-对象形式Joson串
					root={
							"id" : p,
							"pId" : "0",
							"name" : (typeof(res)=="object")?(res.name):p
					};
					if(typeof(obj[p].type)!="undefined"){
						child1={
								"id" : "type",
								"pId" : p,
								"name" : "type:"+obj[p].type
						 };
					}
					add2Group(treeNodes,root,child1,child2); 
					
					var obj1=obj[p].value;
					if(typeof(obj1)=="undefined" && typeof(obj[p].type)=="undefined"){
						//特殊情况
						for(var key in obj[p]){
							child1={
									"id" : (obj[p])[key]+key,
									"pId" : p,
									"name" :key+":"+(obj[p])[key]
							};
							add2Group(treeNodes,root,child1,child2); 
						}
							
					}
					
					if(typeof(obj1)!="undefined" && typeof(obj1)!="object"){
						child1={
								"id" : p+"."+obj[p].value,
								"pId" : p,
								"name" : "value:"+obj[p].value
						};
						child2={
								"id" : "type",
								"pId" : p,
								"name" : "type:"+obj[p].type
						};
						 add2Group(treeNodes,root,child1,child2); 
					}
	
					 var randomNum1=Math.floor(Math.random() * 99) + 1;
					if(typeof(obj1)=="object"){
						for (var s in obj1) {
							 root={
									"id" : p+"."+s,
									"pId" : p,
									"name" : s
								};
							 
							 add2Group(treeNodes,root,child1,child2);
							 if(typeof(obj1[s].value)!="object"){
								 child1={
											"id" : "value:"+randomNum1,
											"pId" : p+"."+s,
											"name" : "value:"+obj1[s].value
										};
								child2={
											"id" : "type:"+randomNum1,
											"pId" : p+"."+s,
											"name" : "type:"+obj1[s].type
								};
								 add2Group(treeNodes,root,child1,child2); 
							 }
							 var randomNum=Math.floor(Math.random() * 99) + 1;
							 if(typeof(obj1[s].value)=="object"){
								   var obj2=obj1[s].value;

									for (var x in obj2) {
										var result=getZkPropDesc((p+"."+s+"."+x),zkPropRes);
										 root={
													"id" : p+"."+s+"."+x,
													"pId" : p+"."+s,
													"name" : (typeof(result)=="object")?(result.name):x
											};
										 add2Group(treeNodes,root,child1,child2);
										if(typeof(obj2[x].value)=="object"){
											var obj3=obj2[x].value;
											
											for (var y in obj3) {
												var result1=getZkPropDesc((p+"."+s+"."+x+"."+y),zkPropRes);
												root={
														"id" : p+"."+s+"."+x+"."+y,
														"pId" : p+"."+s+"."+x,
														"name" : (typeof(result1)=="object")?(result1.name):y
												};
												child1={
														"id" : "value"+randomNum,
														"pId" : p+"."+s+"."+x+"."+y,
														"name" : "value:"+obj3[y].value
													};
												child2={
														"id" : "type",
														"pId" : p+"."+s+"."+x+"."+y,
														"name" : "type:"+obj3[y].type
												};
												add2Group(treeNodes,root,child1,child2);
											}

										}else{
											child1={
													"id" : "value",
													"pId" : p+"."+s+"."+x,
													"name" : "value:"+obj2[x].value
												};
											child2={
													"id" : "type",
													"pId" : p+"."+s+"."+x,
													"name" : "type:"+obj2[x].type
											};
											add2Group(treeNodes,root,child1,child2);
										}
									}
									add2Group(treeNodes,root,child1,child2);
							 }
						}
					}
					
				}

			}
			$.fn.zTree.init($("#treeTextDemo"),RightSetting,treeNodes);
			zTreeRight = $.fn.zTree.getZTreeObj("treeTextDemo");
			var nodes = zTreeRight.getNodes();
			for (var i = 0; i < nodes.length; i++) { // 设置节点展开
				zTreeRight.expandNode(nodes[i], true, false, true);
            }
		}
		
	};
   
   function showTree(nodepath){
	    zTree = $.fn.zTree.getZTreeObj("treeDemo");
		// 去掉最后一个子节点
		var parentPath = nodepath.substr(0,nodepath.lastIndexOf("/"));
		// 获取选中节点的父节点
		var parentNode = parentPath.substr(parentPath.lastIndexOf("/"));
		// 去掉/
		parentNode = parentNode.substring(1);
		// 判断是否为空，为空则为一二级节点
		parentNode = parentNode==""? zkPath : parentNode;
		var nodes = zTree.getNodesByParam("name", parentNode, null);
		// console.log(nodes.children.length>0);
		if(typeof(nodes[0]) != "undefined"  && nodes[0].children==null){
			parentPath = parentPath.substr(0,parentPath.lastIndexOf("/"));
			parentPath = parentPath.substr(parentPath.lastIndexOf("/"));
			parentPath = parentPath.substring(1);
			// 判断是否为空，为空则为一二级节点
			parentPath = parentPath==""? zkPath : parentPath;
			console.log(parentPath);
			nodes = zTree.getNodesByParam("name", parentPath, null);
		}
		for (var i = 0; i < nodes.length; i++) { // 设置节点展开
			zTree.expandNode(nodes[i], true, false, true);
        }
   }

	function sortZtree(data){
		if(typeof(data.length) != "undefined"){
			//console.log("刷新列表");
			var len = data.length;
			for(var i = 0;i < len;i++){
				return (function fn() {
					//arguments[0]指的是函数的第一个参数，此处为data[0],即/这一父节点的json格式数据
					var jsonArray = typeof(arguments[0]) == "undefined" ? data[0]:arguments[0];
					for(var k in jsonArray){
						//排序的条件:存在子节点且长度大于1
						if(k.indexOf('children')!=-1&&jsonArray[k]!=null){
					    	if(jsonArray[k].length>1){
					        	jsonArray[k] = jsonArray[k].sort(function(a,b){
					            	return a.name > b.name? 1 : -1;
					        	});
					    	}
							//递归的条件长度大于0
							if(jsonArray[k].length>0){
					        	for(var j = 0;j<jsonArray[k].length;j++){             
									//递归调用匿名函数
									fn(jsonArray[k][j]);
					            } 
					        }
					    }  
					}
					return jsonArray;      
				})()
			}
		}else{
			//console.log("增复删");
			return (function fn() {
				//arguments[0]指的是函数的第一个参数，此处为data[0],即/这一父节点的json格式数据
				var jsonArray = typeof(arguments[0]) == "undefined" ? data:arguments[0];
				for(var k in jsonArray){
					//排序的条件:①存在子节点②子节点的个数大于1
					if(k.indexOf('children')!=-1&&jsonArray[k]!=null){
				    	if(jsonArray[k].length>1){
				        	jsonArray[k] = jsonArray[k].sort(function(a,b){
				            	return a.name > b.name? 1 : -1;
				        	});
				    	}
						//递归的条件①有子节点②父节点的类型为1
						if(jsonArray[k].length>0){
				        	for(var j = 0;j<jsonArray[k].length;j++){             
								//递归调用匿名函数
								fn(jsonArray[k][j]);
				            } 
				        }
				    }  
				}
				return jsonArray;      
			})()
		}
	}
	
	function add2Group(treeNodes,root,child1,child2){
		if(root!="" && JSON.stringify(treeNodes).indexOf(JSON.stringify(root))==-1){
			treeNodes.push(root);
		}
		if(child1!="" && JSON.stringify(treeNodes).indexOf(JSON.stringify(child1))==-1){
			treeNodes.push(child1);
		}
		if(child2!="" && JSON.stringify(treeNodes).indexOf(JSON.stringify(child2))==-1){
			treeNodes.push(child2);
		}
	}
	
	
	function getZkPropDesc(param,zkPropRes){
		for (var i = 0; i < zkPropRes.length; i++) {
			if(zkPropRes[i].zkProp==param){
				if((zkPropRes[i].zkProp).indexOf(".")!=-1){
					var index=param.lastIndexOf(".");
					param=param.substring(index + 1, param.length);
				}
				var result={
						name:param+"("+zkPropRes[i].zkPropName+")",
						desc:zkPropRes[i].zkPropDesc
				}
				return result;
			}
		}
	}
	
	function isEmpty(obj) {
		if (typeof obj == "undefined" || obj == null || obj == "") {
			return true;
		} else {
			return false;
		}
	}

	
});