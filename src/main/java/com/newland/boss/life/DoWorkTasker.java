package com.newland.boss.life;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class DoWorkTasker implements Callable<String> {

    private String workType;
    private String resMsg;
    private List<HashMap> items = null;
    List<HashMap> finalResultMapList;
    Map<String, Object> sessionMap;
    List<HashMap> myFinalResultMapListSub;

    public DoWorkTasker(List<HashMap> items, String workType, String resMsg,List<HashMap>finalResultMapList,
                        Map<String, Object> sessionMap,
                        List<HashMap> myFinalResultMapListSub) {
        this.workType = workType;
        this.resMsg = resMsg;
        this.items = items;
        this.finalResultMapList=finalResultMapList;
        this.sessionMap=sessionMap;
        this.myFinalResultMapListSub=myFinalResultMapListSub;
    }

    @Override
    public String call() throws Exception {
        LifeCycleController.doWork(items, workType, resMsg, finalResultMapList, sessionMap, myFinalResultMapListSub);
        return "";
    }

}
