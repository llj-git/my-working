package com.newland.boss.cloud.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.*;
import com.newland.boss.life.LifeCycleController;

public class JSchUtil {
     
private static final String ENCODING = "UTF-8";

private static Log LOGGER = LogFactory.getLog(JSchUtil.class);
	
	public static Session getJSchSession(String userName,String host,String password,int timeOut) throws JSchException {
		JSch jsch = new JSch();
		  
		Session session = jsch.getSession(userName, host);
		session.setPassword(password);
		session.setConfig("StrictHostKeyChecking", "no");//第一次访问服务器不用输入yes
		session.setTimeout(timeOut);
		session.connect();
		return session;
	}
	
	
	public static String execCommandByShell(Session session,String oper,String appname,String instancename,String appDeployPath)throws IOException,JSchException{
		
		long timeBegin = System.currentTimeMillis(); //记录开始时间
		
		String result = "";
		StringBuffer  finalResult =new StringBuffer();
		
		String exec1="./DeployAgentExt "+oper;
		
		String exec="";
		if(!instancename.equals("")){
			exec="./DeployAgentExt "+oper+" -inst_id="+instancename;
		}
		else if(!appname.equals("")&&instancename.equals(""))
		{
			exec="./DeployAgentExt "+oper+" "+appname;
		}
		else if(appname.equals("")&&instancename.equals(""))
		{
			exec="./DeployAgentExt "+oper;
		}
		
		String strExec="cd "+appDeployPath+"\n"+exec+"\nexit\n";
		
         //2.尝试解决 远程ssh只能执行一句命令的情况
		ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
		InputStream inputStream = channelShell.getInputStream();//从远端到达的数据  都能从这个流读取到
		channelShell.setPty(true);
		channelShell.connect();
		
		OutputStream outputStream = channelShell.getOutputStream();//写入该流的数据  都将发送到远程端
		
		outputStream.write(strExec.getBytes());
		outputStream.flush();
		
		/**
		shell管道本身就是交互模式的。要想停止，有两种方式： 
		一、人为的发送一个exit命令，告诉程序本次交互结束
		二、使用字节流中的available方法，来获取数据的总大小，然后循环去读。
		为了避免阻塞
		*/
		byte[] tmp = new byte[1024];
		//long a=System.currentTimeMillis();
		while(true){
			
			
			while(inputStream.available() > 0){
				int i = inputStream.read(tmp, 0, 1024);
				if(i < 0) break;
			    result = new String(tmp, 0, i,"gb2312");
//				if(s.indexOf("--More--") >= 0){
//					outputStream.write((" ").getBytes());
//					outputStream.flush();
//				}
			    //LOGGER.info(result);
				finalResult.append(result);
			
	
			 //这里也可能达到指定等待时间,结果还没返回
			  long timeEnd = System.currentTimeMillis(); //记录可能结束时间
			  long durationTime = timeEnd - timeBegin;
			  if(durationTime > DBPoolConnection.sessionTimeOut) {
				  return "sessionTimeOut";
			  }
			
			}
			
			 //主要是在这里判断主机卡住的可能
			 long timeEnd = System.currentTimeMillis(); //记录可能结束时间
			  long durationTime = timeEnd - timeBegin;
			  if(durationTime > DBPoolConnection.sessionTimeOut) {
				  return "sessionTimeOut";
			  }
			
			
			if(channelShell.isClosed()){
				if(inputStream.available()>0) { //有可能回到while循环
					continue;
				}
					
				LOGGER.info("exit-status:"+channelShell.getExitStatus());
				break;
			}
			try{
				//Thread.sleep(1000);
				
			}catch(Exception e){}
			
		}
		outputStream.close();
		inputStream.close();
		channelShell.disconnect();
		//session.disconnect();
		LOGGER.info("DONE");
	
		return finalResult.toString();
	}
	
	  public static String execCommandByJSch(Session session, String command) throws IOException,JSchException{
			
		          //1.默认方式，执行单句命令		
				ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
				InputStream in = channelExec.getInputStream();
				channelExec.setCommand(command);
				channelExec.setErrStream(System.err);
				channelExec.connect();
				
				byte[] b = new byte[1024];
				StringBuffer buffer = new StringBuffer();

			        while (in.read(b) > 0) {
			            buffer.append(new String(b));
			     }
				
				String result = buffer.toString();
				
				channelExec.disconnect();
				
				return result;
				

		}
	  
	  public static void  closeSession(Session session) throws JSchException {
			 session.disconnect();
		}
	 
	  
	  public static String execCommandByJSchNoResult(Session session, String command) throws IOException,JSchException{
			
          //1.默认方式，执行单句命令		
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		InputStream in = channelExec.getInputStream();
		channelExec.setCommand(command);
		channelExec.setErrStream(System.err);
		channelExec.connect();
			
		channelExec.disconnect();
		
		return "";
		

     }
	  public static String execCommandByShellExt(Session session,String oper,String appname,String instancename,String appDeployPath)throws IOException,JSchException{
			
			String result = "";
			StringBuffer  finalResult =new StringBuffer();
			
			String exec1="./DeployAgentExt "+oper;
			
			String exec="";
			if(!instancename.equals("")){
				exec="./DeployAgentExt "+oper+" -inst_id="+instancename;
			}
			else if(!appname.equals("")&&instancename.equals(""))
			{
				exec="./DeployAgentExt "+oper+" "+appname;
			}
			else if(appname.equals("")&&instancename.equals(""))
			{
				exec="./DeployAgentExt "+oper;
			}
			
			
	       //2.尝试解决 远程ssh只能执行一句命令的情况
			ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
			InputStream inputStream = channelShell.getInputStream();//从远端到达的数据  都能从这个流读取到
			channelShell.setPty(true);
			channelShell.connect();
			
			OutputStream outputStream = channelShell.getOutputStream();//写入该流的数据  都将发送到远程端
			//使用PrintWriter 就是为了使用println 这个方法
			//好处就是不需要每次手动给字符加\n
			PrintWriter printWriter = new PrintWriter(outputStream);
			printWriter.println("cd "+appDeployPath);
			printWriter.println(exec);
			printWriter.println("exit");//为了结束本次交互
			printWriter.flush();//把缓冲区的数据强行输出
			
			
			
			
			/**
			shell管道本身就是交互模式的。要想停止，有两种方式： 
			一、人为的发送一个exit命令，告诉程序本次交互结束
			二、使用字节流中的available方法，来获取数据的总大小，然后循环去读。
			为了避免阻塞
			*/
			byte[] tmp = new byte[1024];
			long a=System.currentTimeMillis();
			while(true){
				
				
//				while(inputStream.available() > 0){
//					int i = inputStream.read(tmp, 0, 1024);
//					if(i < 0) break;
//				    result = new String(tmp, 0, i,"gb2312");
////					if(s.indexOf("--More--") >= 0){
////						outputStream.write((" ").getBytes());
////						outputStream.flush();
////					}
//					//System.out.println(result);
//					finalResult.append(result);
//				}
				
				
				
				if(channelShell.isClosed()){
					long b=System.currentTimeMillis();
					System.out.println("wait="+(b-a));
					System.out.println("exit-status:"+channelShell.getExitStatus());
					break;
				}
				try{
					//Thread.sleep(1000);
					
				}catch(Exception e){}
				
			}
			outputStream.close();
			inputStream.close();
			channelShell.disconnect();
			//session.disconnect();
			System.out.println("DONE");
		
			return finalResult.toString();
		}





}
