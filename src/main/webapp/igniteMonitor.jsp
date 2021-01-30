<%--
  Created by IntelliJ IDEA.
  User: asus
  Date: 2019/8/1
  Time: 10:45
  Author: Wuchenxin
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="igniteManage">
<head>
    <title>Title</title>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="X-UA-Compatible" content="IE=8"/>
    <!--[if lt IE 9]>
    <script src="resources/skeleton/js/modernizr.js "></script>
    <script src="resources/skeleton/js/amazeui.ie8polyfill.min.js"></script>
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
    <link rel="stylesheet" href="resources/layui/css/layui.css" media="all"/>
    <link rel="stylesheet" type="text/css"
          href="resources/skeleton/css/bootstrap.min.css"/>
    <script type="text/javascript"
            src="resources/skeleton/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript"
            src="resources/skeleton/plugins/ngtable/js/ng-table.min.js"></script>
    <script type="text/javascript"
            src="resources/skeleton/plugins/layer/1.9.3/layer.js"></script>
    <script src="resources/includeCss_3rd.js"></script>
    <script src="resources/layui/layui.js"></script>
    <script src="resources/biz/controller/igniteMonitor.js"></script>
    <%--<script src="resources/biz/controller/serviceIp.js"></script>--%>

</head>
<body ng-controller="igniteManageController as dm1">

<div class="layui-card">
    <div class="layui-card-header">
        <div class="layui-form-item">

            <div class="layui-inline" style="margin-top: 10px">
                <label class="layui-form-label" style="width: auto">集群在线监控看板(A中心)</label>
            </div>

        </div>
    </div>
    <div class="layui-card-body">
        <table id="igniteRunning" lay-filter="demo" >
            <thead>
            <tr >
                <th ng-repeat="(column,columnValue) in params[0]" lay-data="{field:'{{column}}', width:100}">{{column}}</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="params in items">
                <td ng-repeat="(column,columnValue) in params" >{{columnValue}}</td>
            </tr>
            </tbody>


        </table>
    </div>
</div>

<div class="layui-card">
    <div class="layui-card-header">
        <div class="layui-inline" style="margin-top: 10px">
            <label class="layui-form-label" style="width: auto">集群在线监控看板(B中心)</label>
        </div>
    </div>
    <div class="layui-card-body">
        <table id="" lay-filter="bCenter" >
            <thead>
            <tr>
                <th ng-repeat="(column,columnValue) in bCenterParams[0]" lay-data="{field:'{{column}}', width:100}">{{column}}</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="bCenterParams in bCenteritems">
                <td ng-repeat="(column,columnValue) in bCenterParams" >{{columnValue}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<div class="layui-card">
    <div class="layui-card-header">
        <div class="layui-form-item">

            <div class="layui-inline" style="margin-top: 10px">
                <label class="layui-form-label" style="width: auto">数据网格在线监控看板(A中心)</label>
            </div>

        </div>
    </div>
    <div class="layui-card-body">
        <table id="datagrid" lay-filter="dataGridACenter">
            <thead>
            <tr>
                <th ng-repeat="(column,columnValue) in paramACenter[0]" lay-data="{field:'{{column}}', width:100}">{{column}}</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="dataGridACenter in paramACenter">
                <td ng-repeat="(column,columnValue) in dataGridACenter">{{columnValue}}</td>
            </tr>
            </tbody>

        </table>
    </div>
</div>

<div class="layui-card">
    <div class="layui-card-header">
        <div class="layui-form-item">

            <div class="layui-inline" style="margin-top: 10px">
                <label class="layui-form-label" style="width: auto">数据网格在线监控看板(B中心)</label>
            </div>

        </div>
    </div>
    <div class="layui-card-body">
        <table   lay-filter="dataGridBCenter">
            <thead>
            <tr>
                <th ng-repeat="(column,columnValue) in dataGridACenter[0]" lay-data="{field:'{{column}}', width:100}">{{column}}
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="dataGridACenter in paramBCenter">
                <td ng-repeat="(column,columnValue) in dataGridACenter">{{columnValue}}</td>
            </tr>
            </tbody>

        </table>
    </div>
</div>

<div class="layui-card">
    <div class="layui-card-header">
        <div class="layui-form-item">

            <div class="layui-inline" style="margin-top: 10px">
                <label class="layui-form-label" style="width: auto">数据节点列表查看</label>
            </div>


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
                    <i class="layui-icon layui-icon-search " style="margin: auto"></i>
                </button>
            </div>




        </div>
    </div>
    <div class="layui-card-body">
        <table ng-table="dml" id="dataTable" lay-filter="dataTable">
            <thead>
                <tr>
                    <th ng-repeat="(column,columnValue) in item[0]" lay-data="{field:'{{column}}', width:100}">{{column}}</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="data in item">
                    <td ng-repeat="(column,columnValue) in data">{{columnValue}}</td>
                </tr>
            </tbody>

        </table>
    </div>
</div>


</body>
</html>
