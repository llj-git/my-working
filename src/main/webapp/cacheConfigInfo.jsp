<%--
  Created by IntelliJ IDEA.
  User: asus
  Date: 2019/8/1
  Time: 19:08
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
    <script src="resources/biz/controller/cacheConfigInfo.js"></script>
</head>
<body ng-controller="zkManageController as dm1">
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
                                           ng-click="getCachecfg(x)">{{ x }}</a></dd>
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
                <div id="right" class="am-panel am-panel-default">


                    <table class="table table-condensed table-bordered table-striped am-table-hover " ng-repeat="item in data">
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>字段</b></font></td>
                            <td ><font size="3"><b>值</b></font></td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>name：</b></font></td>
                            <td>{{item.name}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>grpName：</b></font></td>
                            <td>{{item.grpName}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>memPlcName：</b></font></td>
                            <td>{{item.memPlcName}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>rebalanceTimeout：</b></font></td>
                            <td>{{item.rebalanceTimeout}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>storeConcurrentLoadAllThreshold：</b></font></td>
                            <td>{{item.storeConcurrentLoadAllThreshold}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>rebalancePoolSize：</b></font></td>
                            <td>{{item.rebalancePoolSize}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>evictPlc：</b></font></td>
                            <td>{{item.evictPlc}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>evictPlcFactory：</b></font></td>
                            <td>{{item.evictPlcFactory}}</td>

                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>onheapCache：</b></font></td>
                            <td>{{item.onheapCache}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>evictFilter：</b></font></td>
                            <td>{{item.evictFilter}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>sqlOnheapCache：</b></font></td>
                            <td>{{item.sqlOnheapCache}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>sqlOnheapCacheMaxSize：</b></font></td>
                            <td>{{item.sqlOnheapCacheMaxSize}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>eagerTtl：</b></font></td>
                            <td>{{item.eagerTtl}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>dfltLockTimeout：</b></font></td>
                            <td>{{item.dfltLockTimeout}}</td>


                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>nearCfg：</b></font></td>
                            <td>{{item.nearCfg}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>loadPrevVal：</b></font></td>
                            <td>{{item.loadPrevVal}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>storeFactory：</b></font></td>
                            <td>{{item.storeFactory}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>storeKeepBinary：</b></font></td>
                            <td>{{item.storeKeepBinary}}</td>

                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>cacheMode：</b></font></td>
                            <td>{{item.cacheMode}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>atomicityMode：</b></font></td>
                            <td>{{item.atomicityMode}}</td>

                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>backups：</b></font></td>
                            <td>{{item.backups}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>rebalanceMode：</b></font></td>
                            <td>{{item.rebalanceMode}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>invalidate：</b></font></td>
                            <td>{{item.invalidate}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>tmLookupClsName：</b></font></td>
                            <td>{{item.tmLookupClsName}}</td>

                        </tr>

                        <tr>
                            <td style="text-align:left;"><font size="3"><b>rebalanceOrder：</b></font></td>
                            <td>{{item.rebalanceOrder}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>rebalanceBatchSize：</b></font></td>
                            <td>{{item.rebalanceBatchSize}}</td>

                        </tr>

                        <tr>
                            <td style="text-align:left;"><font size="3"><b>rebalanceBatchesPrefetchCnt：</b></font></td>
                            <td>{{item.rebalanceBatchesPrefetchCnt}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>writeBehindEnabled：</b></font></td>
                            <td>{{item.writeBehindEnabled}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>maxConcurrentAsyncOps：</b></font></td>
                            <td>{{item.maxConcurrentAsyncOps}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>sqlIdxMaxInlineSize：</b></font></td>
                            <td>{{item.sqlIdxMaxInlineSize}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>writeBehindFlushSize：</b></font></td>
                            <td>{{item.writeBehindFlushSize}}</td>

                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>writeBehindFlushFreq：</b></font></td>
                            <td>{{item.writeBehindFlushFreq}}</td>


                        </tr>
                        <td style="text-align:left;"><font size="3"><b>writeBehindFlushThreadCnt：</b></font></td>
                        <td>{{item.writeBehindFlushThreadCnt}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>maxQryIterCnt：</b></font></td>
                            <td>{{item.maxQryIterCnt}}</td>
                        <tr></tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>writeBehindBatchSize：</b></font></td>
                            <td>{{item.writeBehindBatchSize}}</td>

                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>writeBehindCoalescing：</b></font></td>
                            <td>{{item.writeBehindCoalescing}}</td>


                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>maxQryIterCnt：</b></font></td>
                            <td>{{item.maxQryIterCnt}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>rebalanceThrottle：</b></font></td>
                            <td>{{item.rebalanceThrottle}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>affMapper：</b></font></td>
                            <td>{{item.affMapper}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>rebalanceDelay：</b></font></td>
                            <td>{{item.rebalanceDelay}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>interceptor：</b></font></td>
                            <td>{{item.interceptor}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>longQryWarnTimeout：</b></font></td>
                            <td>{{item.longQryWarnTimeout}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>qryDetailMetricsSz：</b></font></td>
                            <td>{{item.qryDetailMetricsSz}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>sqlEscapeAll：</b></font></td>
                            <td>{{item.sqlEscapeAll}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>readFromBackup：</b></font></td>
                            <td>{{item.readFromBackup}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>sqlSchema：</b></font></td>
                            <td>{{item.sqlSchema}}</td>


                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>cpOnRead：</b></font></td>
                            <td>{{item.cpOnRead}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>topValidator：</b></font></td>
                            <td>{{item.topValidator}}</td>


                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>partLossPlc：</b></font></td>
                            <td>{{item.partLossPlc}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>encryptionEnabled：</b></font></td>
                            <td>{{item.encryptionEnabled}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>qryParallelism：</b></font></td>
                            <td>{{item.qryParallelism}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>evtsDisabled：</b></font></td>
                            <td>{{item.evtsDisabled}}</td>


                        </tr>
                        <tr>
                            <td colspan="6" style="text-align:center;"><font size="3"><b>AFF:RendezvousAffinityFunction</b></font>
                            </td>
                        </tr>

                        <tr>
                            <td style="text-align:left;"><font size="3"><b>parts：</b></font></td>
                            <td>{{item.parts}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>mask：</b></font></td>
                            <td>{{item.mask}}</td>


                        </tr>

                        <tr>
                            <td style="text-align:left;"><font size="3"><b>exclNeighbors：</b></font></td>
                            <td>{{item.exclNeighbors}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>affinityBackupFilter：</b></font></td>
                            <td>{{item.affinityBackupFilter}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>exclNeighborsWarn：</b></font></td>
                            <td>{{item.exclNeighborsWarn}}</td>
                        </tr>
                        <tr>
                            <td style="text-align:left;"><font size="3"><b>backupFilter：</b></font></td>
                            <td>{{item.backupFilter}}</td>


                        </tr>
                        <tr>
                            <td colspan="6" style="text-align:center;"><font size="3"><b>NODEFILTER:IgniteAllNodesP#3F7CE7icate</b></font>
                            </td>
                        </tr>
                    </table>
                </div>
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
