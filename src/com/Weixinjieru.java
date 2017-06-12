package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import po.TextMessage;
import sun.misc.MessageUtils;
import util.CheckUtil;
import util.MessageUtil;
import util.TulingApiUtil;

/**
 * Servlet implementation class Weixinjieru
 */
public class Weixinjieru extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Weixinjieru() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse rep) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		PrintWriter out = rep.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		try {
			Map<String,String> map = MessageUtil.xmlToMap(request);
			String fromUserName = map.get("FromUserName");
			String ToUserName = map.get("ToUserName");
			//String CreateTime = map.get("CreateTime");
			String MsgType = map.get("MsgType");
			//System.out.println("信息类型是"+MsgType);
			String Content = map.get("Content");
			//String MsgId = map.get("MsgId");
			String message = null;
			int i=0;
			if(MessageUtil.MESSAGE_TEXT.equals(MsgType)){
				if("1".equals(Content)){
					message = MessageUtil.initText(ToUserName, fromUserName, MessageUtil.firstMenu());
					System.out.println(message);
					i = i+1;
				}
				else if("2".equals(Content)){
					message = MessageUtil.initNewsMessage(ToUserName, fromUserName);
					//message = MessageUtil.initText(ToUserName, fromUserName, MessageUtil.SecondeMenu());
					i = i+1;
				}
				else if("3".equals(Content)){
					message = MessageUtil.initImageMessage(ToUserName, fromUserName);
					//message = MessageUtil.initText(ToUserName, fromUserName, MessageUtil.SecondeMenu());
					i = i+1;
				}
				else if("4".equals(Content)){
					message = MessageUtil.initMusicMessage(ToUserName, fromUserName);			
					i = i+1;
				}
				else if("?".equals(Content) || "？".equals(Content)){
					message = MessageUtil.initText(ToUserName, fromUserName, MessageUtil.menuText());
					i = i+1;
				}
				else if("小白".equals(Content)){
					message = MessageUtil.initText(ToUserName, fromUserName, "我是小白，你叫我做什么啊");
					i = i+1;
				}
				else{
					//System.out.println(i);
					message = TulingApiUtil.getTulingResult(Content);
				}
			}
			else if(MessageUtil.MESSAGE_EVRNT.equals(MsgType)){
				String eventType = map.get("Event");
				//System.out.println(eventType);
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					message = MessageUtil.initText(ToUserName, fromUserName, MessageUtil.menuText());
				}
				else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					message = MessageUtil.initText(ToUserName, fromUserName, MessageUtil.menuText());
				}
				else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
					String url = map.get("EventKey");
					message = MessageUtil.initText(ToUserName, fromUserName, url);
				}
				else if(MessageUtil.MESSAGE_scancode.equals(eventType)){
					String key =  map.get("EventKey");
					message = MessageUtil.initText(ToUserName, fromUserName, key);
				}
			}
			else if(MessageUtil.MESSAGE_LOCATION.equals(MsgType)){
				String Label = map.get("Label");
				//System.out.println("地理位置是"+Label);
				message = MessageUtil.initText(ToUserName, fromUserName, Label);
			}
			
			out.print(message);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			out.close();
		}
	}
}
