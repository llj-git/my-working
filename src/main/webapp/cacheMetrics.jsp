<%--
  Created by IntelliJ IDEA.
  User: asus
  Date: 2019/8/1
  Time: 17:11
  Author: Wuchenxin
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="zkManage">
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
    <script src="resources/skeleton/js/jquery.placeholder.min.js"></script>
    <script type="text/javascript"
            src="resources/skeleton/js/angular-sanitize.min.js"></script>
    <link rel="stylesheet" href="resources/layui/css/layui.css" media="all"/>
    <link rel="stylesheet" type="text/css"
          href="resources/skeleton/css/bootstrap.min.css" />

    <script type="text/javascript"
            src="resources/skeleton/plugins/ngtable/js/ng-table.min.js"></script>
    <script type="text/javascript"
            src="resources/skeleton/plugins/layer/1.9.3/layer.js"></script>
    <script src="resources/includeCss_3rd.js"></script>
    <script src="resources/biz/controller/igniteMonitor.js"></script>
    <script src="resources/biz/controller/cacheMetrics.js"></script>
</head>
<body ng-controller="zkManageController as dml">

<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo">缓存列表</div>
        <!-- 头部区域（可配合layui已有的水平导航） -->
    </div>

    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree " lay-filter="test">
                <li class="layui-nav-item">
                    <input class="layui-input" type="text" ng-model="test" placeholder="这里输入Cachename可查询"
                           style="background: #23262E">
                </li>
                <li class="layui-nav-item">
                    <dl>
                        <dd id="mytest"><a href="javascript:;" onmouseleave="close_shopm()"
                                           onmousemove="show_shopm(this)" ; data-d="{{ x }}"
                                           ng-repeat="x in cacheNames | filter:test | orderBy:x"
                                           ng-click="getCacheinfo(x)">{{ x }}</a></dd>
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
                        <label class="layui-form-label" >center:</label>
                        <div class="layui-input-inline">
                            <select class="form-control"  ng-model="$parent.center" ng-change="change()">
                                <option value="">请选择中心</option>
                                <option value="1">A中心</option>
                                <option value="2" >B中心</option>
                            </select>

                        </div>
                    </div>

                    <div class="layui-inline" style="margin-top: 10px">
                        <label class="layui-form-label">IP:</label>
                        <div class="layui-input-inline">
                            <select class="form-control" name="label" ng-options="y for (x, y) in centerip" ng-model="$parent.addres">
                            </select>
                        </div>
                    </div>

                    <div class="layui-inline" >
                        <button class="btn btn-default " ng-click="setConnet(x);">
                            <i class="layui-icon layui-icon-search "></i>
                        </button>
                    </div>

                </div>
            </div>


            <div class="layui-card-body">
                <table class="table table-condensed table-bordered table-striped am-table-hover " ng-repeat="item in data">
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>字段</b></font></td>
                        <td><font size="3"><b>值</b></font></td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>cacheName：</b></font></td>
                        <td>{{item.cacheName}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>mode：</b></font></td>
                        <td>{{item.mode}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>nodeSizes：</b></font></td>
                        <td>{{item.nodeSizes}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>partitions：</b></font></td>
                        <td>{{item.partitions}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>offHeapAllocSize：</b></font></td>
                        <td>{{item.offHeapAllocSize}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>offEntries：</b></font></td>
                        <td>{{item.offEntries}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>offPrimaryEntries：</b></font></td>
                        <td>{{item.offPrimaryEntries}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>offBackupEntries：</b></font></td>
                        <td>{{item.offBackupEntries}}</td>

                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>hits：</b></font></td>
                        <td>{{item.hits}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>hitPerc：</b></font></td>
                        <td>{{item.hitPerc}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>misses：</b></font></td>
                        <td>{{item.misses}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>missPerc：</b></font></td>
                        <td>{{item.missPerc}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>gets：</b></font></td>
                        <td>{{item.gets}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>puts：</b></font></td>
                        <td>{{item.puts}}</td>


                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>rollbacks：</b></font></td>
                        <td>{{item.rollbacks}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>executions：</b></font></td>
                        <td>{{item.executions}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>qurFails：</b></font></td>
                        <td>{{item.qurFails}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>qurMinTime：</b></font></td>
                        <td>{{item.qurMinTime}}</td>

                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>qurMaxTime：</b></font></td>
                        <td>{{item.qurMaxTime}}</td>
                    </tr>
                    <tr>
                        <td style="text-align:left;"><font size="3"><b>qurAverageTime：</b></font></td>
                        <td>{{item.qurAverageTime}}</td>

                    </tr>

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
