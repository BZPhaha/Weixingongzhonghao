package util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import po.Image;
import po.ImageMessage;
import po.MusicMessage;
import po.News;
import po.NewsMessage;
import po.TextMessage;
import po.music;

import com.thoughtworks.xstream.XStream;

public class MessageUtil {
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_NEWS = "news";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_MUSIC = "music";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVRNT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final String MESSAGE_scancode = "scandcode_push";
	
	//消息的格式转换
	
	//将xml转为map
	public static Map<String,String> xmlToMap(HttpServletRequest request) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		for(Element e : list){
			//System.out.println("标签名"+e.getName()+"值是"+e.getText());
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}
	//将文本对象类型转为xml
	public static String textToXML(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
		
	}
	//将图文信息转为xml
	public static String newsToXML(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
		
	}
	/**
	 * 拼装图文信息
	 * @param ToUserName
	 * @param fromUserName
	 * @param Content
	 * @return
	 */
	public static String initNewsMessage(String toUserName,String fromUserName){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		
		News news = new News();
		news.setTitle("bzp哈哈");
		news.setDescription("bzp是一个哈哈哈哈哈哈");
		news.setPicUrl("http://694138c9.ngrok.io/Test_servlet/image/bzphaha.jpg");
		news.setUrl("www.baidu.com");
		newsList.add(news);
		
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticls(newsList);
		newsMessage.setArticleCount(newsList.size());
		
		message = newsToXML(newsMessage);
		//System.out.println(message);
		return message;
	}
	
	//将图片信息转为XML
		public static String imageMessageToXml(ImageMessage imageMessage){
			XStream xstream = new XStream();
			xstream.alias("xml", imageMessage.getClass());
			xstream.alias("item", new Image().getClass());
			return xstream.toXML(imageMessage);
		}
		
		//组装图片信息
		public static String initImageMessage(String ToUserName,String fromUserName){
			String message = null;
			Image image = new Image();
			image.setMediaId("vB7aG5HJmFhT-m2vjOUWlZnz6Nscv7zNc1-vgFLZdQ0WtbIuauvTy0YdaemO-16B");
			ImageMessage imageMessage = new ImageMessage();
			imageMessage.setFromUserName(ToUserName);
			imageMessage.setToUserName(fromUserName);
			imageMessage.setMsgType(MESSAGE_IMAGE);
			imageMessage.setCreateTime(new Date().getTime());
			imageMessage.setImage(image);
			message = imageMessageToXml(imageMessage);
			//System.out.println("组装的图片信息"+message);
			return message;
		}
	
	
	//将音乐信息转为xml
		public static String MusicMessageToXML(MusicMessage musicMessage){
			XStream xstream = new XStream();
			xstream.alias("xml", musicMessage.getClass());
			xstream.alias("Music", new music().getClass());
			return xstream.toXML(musicMessage);
			
		}
		//组装音乐信息
		public static String initMusicMessage(String ToUserName,String fromUserName){
			String message = null;
			music music=new music();
			music.setThumbMediaId("24CLIPt9920PvJfzUoJq-9GY2ikOhnDzIIw-HAj4Mv_cDrTlq5fWv3I6m4ymM1au");
			music.setDescription("这是一首很好玩儿的音乐");
			music.setHQMusicUrl("http://694138c9.ngrok.io/Test_servlet/image/oneOk.mp3");
			music.setMusicUrl("http://694138c9.ngrok.io/Test_servlet/image/oneOk.mp3");
			music.setTitle("推荐一首歌");
			MusicMessage musicMessage = new MusicMessage();
			

			musicMessage.setFromUserName(ToUserName);
			musicMessage.setToUserName(fromUserName);
			musicMessage.setMsgType(MESSAGE_MUSIC);
			musicMessage.setCreateTime(new Date().getTime());
			musicMessage.setMusic(music);
			message = MusicMessageToXML(musicMessage);
			//System.out.println("组装的图片信息"+message);
			return message;
		}	
	
	
		public static String initText(String ToUserName,String fromUserName,String Content){
			TextMessage text = new TextMessage();
			text.setFromUserName(ToUserName);
			text.setToUserName(fromUserName);
			text.setMsgType(MESSAGE_TEXT);
			text.setCreateTime(new Date().getTime());
			text.setContent(Content);
			return  MessageUtil.textToXML(text);
		}
	/**
	 * 主菜单
	 * @return
	 */
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎你的关注，请按照菜单提示操作:\n\n");
		sb.append("BZP的介绍\n");
		sb.append("想了解BZP\n");
		sb.append("回复？调出此菜单\n");
		return sb.toString();
	}
	
	public static String firstMenu()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("本套课程主要介绍微信公众号开发\n");
		sb.append("BZP的介绍\n");
		sb.append("想了解BZP\n");
		return sb.toString();
	}
	public static String SecondeMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("BZP是一个 啊     啊   啊啊啊啊啊啊啊啊啊啊啊:\n\n");
		return sb.toString();
	}
	
	
	
}
