package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

import menu.Button;
import menu.ClickButton;
import menu.Menu;
import menu.ViewButton;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import po.AccessToken;


public class weixinUtil {
	private static final String APPID = "wx9a4c677f044bd0f5";
	private static final String APPSECRETT = "690ecff9b2f4c63ad99790ba9225a65a";
	
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	
	public static JSONObject doGetStr(String url){
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				//System.out.println(entity.toString());
				String result = EntityUtils.toString(entity,"UTF-8");
				//System.out.println(result);
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * post请求
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static JSONObject doPostStr(String url,String outStr){
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			httpost.setEntity(new StringEntity(outStr,"UTF-8"));
			HttpResponse response = httpclient.execute(httpost);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 获取token_access
	 * @return
	 */
	public static AccessToken getAccessToken(){
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRETT);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject != null){
			token.setAccess_token(jsonObject.getString("access_token"));
			token.setExpires_in(jsonObject.getInt("expires_in"));
		}
		return token;
	}
	
	/**
	 * 上传文件
	 */
	public static String upload(String filePath,String accessToken,String type) throws Exception{
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()){
			throw new IOException("文件不存在");
		}
		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
		
		URL urlObj = new URL(url);
		
		//链接
		HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();
		
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(true);
		
		//设置请求头信息
		con.setRequestProperty("Connecttion", "Keepp-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		
		//设置边界
		String BOUNDARY = ".............."+System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("utf-8");
		
		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);
		
		//文件正文部分
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while((bytes = in.read(bufferOut)) != -1){
			out.write(bufferOut,0,bytes);
		}
		in.close();
		
		//结尾部分
		byte[] foot =("\r\n--"+BOUNDARY+"\r\n").getBytes("utf-8");
		
		out.write(foot);
		out.flush();
		out.close();
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			//定义BufferReader输入流来读取URL响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while((line = reader.readLine())!=null){
				buffer.append(line);
			}
			if(result == null){
				result = buffer.toString();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			if(reader != null){
				reader.close();
			}
		}
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject);
		String typename = "media_id";
		if(!"image".equals(type)){
			typename = type +"_media_id";
		}
		String mediaId = jsonObject.getString(typename);
		return mediaId;
	}
	
	
	
	public static Menu initMenu(){
		Menu menu = new Menu();
		
		ClickButton button11 = new ClickButton();
		button11.setName("click菜单");
		button11.setType("click");
		button11.setKey("11");
		
		ViewButton button21 = new ViewButton();
		button21.setName("view菜单");
		button21.setType("view");
		button21.setUrl("http://www.baidu.com");
		
		ClickButton button31 = new ClickButton();
		button31.setName("扫码事件");
		button31.setType("scancode_push");
		button31.setKey("31");
		
		ClickButton button32 = new ClickButton();
		button32.setName("获取地理位置");
		button32.setType("location_select");
		button32.setKey("32");
		
		Button button = new Button();
		button.setName("菜单");
		button.setSb_button(new Button[]{button31,button32});
		
		//获取对象的属性名字
		/*Field[] fields=button.getClass().getDeclaredFields();  
        String[] fieldNames=new String[fields.length];  
    for(int i=0;i<fields.length;i++){  
        
        fieldNames[i]=fields[i].getName();  
        System.out.println( fieldNames[i]);  
    } */
		
		
		menu.setButton(new Button[]{button,button11,button21});
		
		return menu;
	}
	
	public static int createMenu(String token,String menu) throws Exception{
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
			//System.out.println("错误原因"+jsonObject.getString("errmsg"));
		}
		return result;
	}
	//查询菜单
	public static JSONObject queryMenu(String token){
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doGetStr(url);
		return jsonObject;
	}
	//删除菜单
	public static int deleteMenu(String token){
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doGetStr(url);
		int result = 0;
		if(jsonObject != null){
			result = jsonObject.getInt("ercode");
		}
		return result;
	}
	
}
	