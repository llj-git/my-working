<%--
  Created by IntelliJ IDEA.
  User: asus
  Date: 2019/8/1
  Time: 15:57
  Author: Wuchenxin
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="C" %>
<html ng-app="igniteManage">

<head>
    <title>Title</title>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="X-UA-Compatible" content="IE=8"/>
    <!--[if lt IE 9]>
    <script src="resources/skeleton/js/modernizr.js "></script>
    <script src="resources/skeleton/js/angular.min-1.2.32.js"></script>
    <![endif]-->
    <!--[if gte IE 9]><!-->
    <script src="resources/skeleton/js/angular.min-1.6.5.js"></script>
    <!--<![endif]-->
    <script type="text/javascript"
            src="resources/skeleton/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript"
            src="resources/skeleton/js/ng-csv.min.js"></script>
    <script type="text/javascript"
            src="resources/skeleton/js/angular-sanitize.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="resources/skeleton/css/bootstrap.min.css"/>

    <link rel="stylesheet" type="text/css" href="resources/skeleton/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="resources/layui/css/layui.css">
    <script type="text/javascript"
            src="resources/skeleton/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript"
            src="resources/skeleton/plugins/ngtable/js/ng-table.min.js"></script>
    <script type="text/javascript"
            src="resources/skeleton/plugins/layer/1.9.3/layer.js"></script>
    <script src="resources/includeCss_3rd.js"></script>
    <script src="resources/layui/layui.js"></script>
    <script src="resources/biz/controller/nodeList.js"></script>

</head>
<body ng-controller="igniteManageController as dm1">

<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo">节点列表</div>
    </div>

    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree " lay-filter="test">
                <li class="layui-nav-item">
                    <input class="layui-input" type="text" ng-model="test" placeholder="输入过滤，查询"
                           style="background: #23262E">
                </li>
                <li class="layui-nav-item">
                    <dl>
                        <dd id="mytest"><a href="javascript:;" onmouseleave="close_shopm()"
                                           onmousemove="show_shopm(this)" ; data-d="{{ x }}"
                                           ng-repeat="x in nodeIds | filter:test | orderBy:x"
                                           ng-click="getNodeDetails(x)">{{ x }}</a></dd>
                    </dl>
                </li>

            </ul>
        </div>
    </div>

    <div class="layui-body">
        <!-- 内容主体区域 -->


        <div class="layui-card">

            <div class=" layui-card-header" style="height:60px">
                <div class="layui-form-item">

                    <div class="layui-inline" style="margin-top: 10px">
                        <label class="layui-form-label">center:</label>
                        <div class="layui-input-inline">
                            <select class="form-control" ng-model="$parent.center" ng-change="change()">
                                <option value="">请选择中心</option>
                                <option value="1">A中心</option>
                                <option value="2">B中心</option>
                            </select>

                        </div>
                    </div>

                    <div class="layui-inline" style="margin-top: 10px">
                        <label class="layui-form-label">IP:</label>
                        <div class="layui-input-inline">
                            <select class="form-control" name="label" ng-options="y for (x, y) in centerip"
                                    ng-model="$parent.addres">
                            </select>
                        </div>
                    </div>

                    <div class="layui-inline">
                        <button class="btn btn-default " ng-click="setConnet(x);">
                            <i class="layui-icon layui-icon-search "></i>
                        </button>
                    </div>

                </div>
            </div>


            <div class="layui-card-body">
                <table ng-table="dml" id="dataTable" lay-filter="demo">
                    <thead>
                        <tr>
                            <th ng-repeat="(column,columnValue) in data[0]" lay-data="{field:'{{column}}', width:175,sort:true}">{{column}}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr ng-repeat="data in items">
                            <td ng-repeat="(column,columnValue) in data" >{{columnValue}}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <%--<table id="demo" lay-filter="test"></table>--%>


    </div>


    <div class="layui-footer">
        <!-- 底部固定区域 -->
        © 2019 Newland BOSS 运维系统.
    </div>
</div>
<script>
    function show_shopm(t) {
        var row = $(t).attr('data-d'); //获取显示内容
        //小tips
        var index = layer.tips(row, t, {
            tips: [2, '#23262E'],

        })
    };


    function close_shopm() {
        layer.closeAll('tips'); //关闭所有的tips层
    }
</script>


</body>
</html>
