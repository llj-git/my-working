package com.newland.boss.life;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.newland.boss.cloud.web.util.DBPoolConnection;
import com.newland.boss.life.dao.GetDataDao;

public class NextChange {

    private static Log LOGGER = LogFactory.getLog(NextChange.class);
    //时间间隔(一天)
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;


    public void init() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22); //晚上10点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime(); //第一次执行定时任务的时间
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }
        Timer timer = new Timer();
        Task task = new Task();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.schedule(task, date, PERIOD_DAY);
    }

    // 增加或减少天数
    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    public class Task extends TimerTask {
        @Override
        public void run() {
            try {
                LOGGER.info("change day reload。。。。");
                DBPoolConnection.appDeployEntityList = new GetDataDao().getAppDeployInfoList();
                DBPoolConnection.appDeployMutualAidRelationList = new GetDataDao().getAppDeployMutualAidRelationList();
                DBPoolConnection.checkWaitingDurationList = new GetDataDao().getCheckWaitingDurationList();
            } catch (SQLException e) {
                LOGGER.info("换天重载异常");
            }

        }
    }


}
