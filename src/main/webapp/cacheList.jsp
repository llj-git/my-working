<%--
  Created by IntelliJ IDEA.
  User: asus
  Date: 2019/7/18
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html ng-app="zkManage">
<head>
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
            src="resources/skeleton/js/ng-csv.min.js"></script>
    <script type="text/javascript"
            src="resources/skeleton/js/angular-sanitize.min.js"></script>
    <link rel="stylesheet" href="resources/layui/css/layui.css" media="all"/>
    <link rel="stylesheet" type="text/css"
          href="resources/skeleton/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="resources/layui/css/layui.css">
    <script type="text/javascript"
            src="resources/skeleton/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript"
            src="resources/skeleton/plugins/ngtable/js/ng-table.min.js"></script>
    <script type="text/javascript"
            src="resources/skeleton/plugins/layer/1.9.3/layer.js"></script>
    <script src="resources/includeCss_3rd.js"></script>
    <script src="resources/layui/layui.js"></script>
    <script src="resources/biz/controller/catheList.js"></script>

</head>
<body class="layui-layout-body" ng-controller="zkManageController as dml">
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo">缓存列表</div>
        <!-- 头部区域（可配合layui已有的水平导航） -->
        <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item">
                <label class="layui-form-label" style="margin-top: 10px; width: auto">Cachename:</label>
                <div class="layui-input-inline">
                    <input type="text" name="title" style="background:#23262E;" ng-model="cacheName"
                           placeholder="请选择左侧Cachename" class="layui-input" disabled>
                </div>
            </li>

            <li class="layui-nav-item">
                <label class="layui-form-label" style="margin-top: 10px; width: auto" ng-repeat="x in cacheCommit">{{x}}:</label>
                <div class="layui-input-inline">
                    <input type="text" name="title" style="background:#23262E;" ng-model="$parent.key"
                           placeholder="请输入该字段的值" class="layui-input">
                </div>
            </li>

            <li class="layui-nav-item">
                <button class="layui-btn layui-btn-primary lay layui-bg-black" type="button" ng-click="queryPk(x);"><i
                        class="layui-icon layui-icon-search"></i></button>
            </li>
        </ul>
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
                                           ng-click="getCacheName(x)">{{ x }}</a></dd>
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


            <%--<div class="layui-card-body">--%>
                <%--<script type="text/html" id="toolbarDemo">--%>
                    <%--<div class="layui-btn-container">--%>
                        <%--<button class="layui-btn layui-btn-sm" lay-event="getCheckData">选中行导出为cvs</button>--%>
                        <%--<button class="layui-btn layui-btn-sm" lay-event="getCheckLength">全部导出为cvs</button>--%>
                        <%--<button class="layui-btn layui-btn-sm" lay-event="isAll">验证是否全选</button>--%>
                    <%--</div>--%>
                <%--</script>--%>

                <table ng-table="dml" id="dataTable" lay-filter="demo">
                    <thead>
                    <tr>
                        <th lay-data="{type:'checkbox'}"></th>
                        <th ng-repeat="(column,columnValue) in items[0]" style="background: #3F7CE7;color:#FFF;"
                            lay-data="{field:'{{column}}', width:100}">{{column}}</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="data in items">
                        <td></td>
                        <td ng-repeat="(column,columnValue) in data" style="text-align: center">{{columnValue}}</td>
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


    // var tip_index = 0;
    // $(document).on('mouseenter', '#mytest', function(){
    //     tip_index = layer.tips('#mytest.text', '#mytest', {time: 0});
    // }).on('mouseleave', '#mytest', function(){
    //     layer.close(tip_index);
    // })

    layui.use(['table', 'element','form'], function () {
        var element = layui.element;
        var table = layui.table;
        var form = layui.form ;

        form.on('select(test)', function(data){
            alert(2)
            $('select').trigger('change');
        });


        //头工具栏事件
        table.on('toolbar(demo)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            switch (obj.event) {


                case 'getCheckData':
                    var data = checkStatus.data;

                    var field = $('th').text()

                    console.log(data.valueOf());

                    $.ajax({
                        type: "post",
                        async: true, //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
                        url: "rest/exporCsv",
                        data: {
                            data: JSON.stringify(data)
                        },
                        dataType: "json",        //返回数据形式为json
                        success: function (result) {


                        },
                        error: function (errorMsg) {

                        }
                    });


                    break;
                case 'getCheckLength':
                    var data = checkStatus.data;
                    layer.msg('选中了：' + data.length + ' 个');
                    break;
                case 'isAll':
                    layer.msg(checkStatus.isAll ? '全选' : '未全选');
                    break;
            }
            ;
        });


    });


</script>
</body>
</html>
