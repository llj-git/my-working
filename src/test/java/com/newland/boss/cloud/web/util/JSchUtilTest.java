package com.newland.boss.cloud.web.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.newland.boss.life.LifeCycleController;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.nio.channels.SeekableByteChannel;

/**
 * @author : 你自己的名字
 * @date :
 */
@PrepareForTest(JSchUtil.class)
public class JSchUtilTest extends PowerMockTestCase {
    //Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyByte()
//getJSchSession(String userName,String host,String password,int timeOut)
    @Test
    public void testGetJSchSession() throws Exception {
        PowerMockito.spy(JSchUtil.class);
        PowerMockito.doAnswer(new Answer<Session>() {
            @Override
            public Session answer(InvocationOnMock invocationOnMock) throws Throwable {
//                JSch jsch, String username, String host, int port
//                Session session = new Session(new JSch(), invocationOnMock.getArguments()[0], invocationOnMock.getArguments()[1], 22);
                Constructor<Session> declaredConstructor = Session.class.getDeclaredConstructor(JSch.class, String.class, String.class, int.class);
                declaredConstructor.setAccessible(true);
                Session session = declaredConstructor.newInstance(new JSch(), invocationOnMock.getArguments()[0], invocationOnMock.getArguments()[1], 22);
                return session;
            }
        }).when(JSchUtil.class, "getJSchSession", Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), (int) Mockito.anyByte());
        Session jSchSession = JSchUtil.getJSchSession("11", "11", "11", 1);
        System.out.println(jSchSession);
//        String json2 = "{\n" +
//                "\t'exec_type': '1',\n" +
//                "\t'group_type': '0',\n" +
//                "\t'items': [{\n" +
//                "\t\t'ip': '10.46.208.201',\n" +
//                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
//                "\t\t'instancename': '30052005'\n" +
//                "\t},{\n" +
//                "\t\t'ip': '10.46.208.201',\n" +
//                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
//                "\t\t'instancename': '30052006'\n" +
//                "\t},{\n" +
//                "\t\t'ip': '10.46.208.201',\n" +
//                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
//                "\t\t'instancename': '30166550'\n" +
//                "\t}, {\n" +
//                "\t\t'ip': '10.46.208.201',\n" +
//                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
//                "\t\t'instancename': '30166551'\n" +
//                "\t},{\n" +
//                "\t\t'ip': '10.46.208.241',\n" +
//                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
//                "\t\t'instancename': '30202076'\n" +
//                "\t}, {\n" +
//                "\t\t'ip': '10.46.208.241',\n" +
//                "\t\t'appname': 'DA_IB_ConDelRepeat',\n" +
//                "\t\t'instancename': '30202077'\n" +
//                "\t}]\n" +
//                "}";
//        String appstatus = new LifeCycleController().appstatus(json2);
//        System.out.println(appstatus);
    }
}