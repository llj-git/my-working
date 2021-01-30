package com.newland.boss.life;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : 你自己的名字
 * @date :
 */
public class LifeCycleControllerTest2 extends PowerMockTestCase {
@Test
    public void testAppstatus2() throws Exception {
        String json = "{\n" +
                "\t'exec_type': '1',\n" +
                "\t'group_type': '0',\n" +
                "\t'items': [{\n" +
                "\t\t'ip': '10.46.208.201',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30052005'\n" +
                "\t}, {\n" +
                "\t\t'ip': '10.46.208.201',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30052006'\n" +
                "\t}]\n" +
                "}";
        String json2 = "{\n" +
                "\t'exec_type': '1',\n" +
                "\t'group_type': '0',\n" +
                "\t'items': [{\n" +
                "\t\t'ip': '10.46.208.201',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30052005'\n" +
                "\t},{\n" +
                "\t\t'ip': '10.46.208.201',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30052006'\n" +
                "\t},{\n" +
                "\t\t'ip': '10.46.208.201',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30166550'\n" +
                "\t}, {\n" +
                "\t\t'ip': '10.46.208.201',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30166551'\n" +
                "\t},{\n" +
                "\t\t'ip': '10.46.208.241',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30202076'\n" +
                "\t}, {\n" +
                "\t\t'ip': '10.46.208.241',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30202077'\n" +
                "\t}]\n" +
                "}";
        String appstatus = new LifeCycleController().appstatus(json);
        System.out.println(appstatus);
    }

    @Test
    public void testStartOrStop() throws Exception {
        Map <String,String> map = new HashMap<>();
        map.put("ip","10.46.208.201");
        map.put("appname","DA_IB_ConDelRepeat");
        map.put("instancename","30052005");
        map.put("loginName","admin");
        map.put("passwd","admin");
        map.put("deployPath","123");
        map.put("hostname","myhost");

//        String loginName=map.get("loginName").toString();
//        String passwd=map.get("passwd").toString();
//        String appDeployPath = map.get("deployPath").toString();
//        String hostName=map.get("hostname").toString();
//        HashMap<String, Object> stringObjectHashMap = new LifeCycleController().startOrStop((HashMap) map, "-psg", "",);
//        System.out.println(stringObjectHashMap);b
    }
    @Test
    public void testGetSession(){
//        try {
//            //记得吧getsession改回成private
//           Session session4 = LifeCycleController.getSession("127.0.0.1", "admin", "admin");
//            System.out.println(session4);
//            Session session1 = LifeCycleController.getSession("10.46.208.201", "admin", "admin");
//            System.out.println(session1);
//            Session session2 = LifeCycleController.getSession("10.46.216.70", "admin", "admin");
//            System.out.println(session2);
//            Session session3 = LifeCycleController.getSession("10.48.141.181", "admin", "admin");
//            System.out.println(session3);
//        } catch (JSchException e) {
//            e.printStackTrace();
//        }
    }
    @Test
    public void test2() throws Exception {
        LifeCycleController spy = PowerMockito.spy(new LifeCycleController());
        PowerMockito.when(spy.appstatus( "{\n" +
                "\t'exec_type': '1',\n" +
                "\t'group_type': '0',\n" +
                "\t'items': [{\n" +
                "\t\t'ip': '10.46.208.201',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30052005'\n" +
                "\t}, {\n" +
                "\t\t'ip': '10.46.208.201',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30052006'\n" +
                "\t}]\n" +
                "}")).thenReturn("10");
        String appstatus = spy.appstatus("{\n" +
                "\t'exec_type': '1',\n" +
                "\t'group_type': '0',\n" +
                "\t'items': [{\n" +
                "\t\t'ip': '10.46.208.201',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30052005'\n" +
                "\t}, {\n" +
                "\t\t'ip': '10.46.208.201',\n" +
                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
                "\t\t'instancename': '30052006'\n" +
                "\t}]\n" +
                "}");
        System.out.println(appstatus);

    }
}