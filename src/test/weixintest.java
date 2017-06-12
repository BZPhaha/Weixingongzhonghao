package test;

import net.sf.json.JSONObject;
import po.AccessToken;
import util.weixinUtil;

public class weixintest {
	public static void main(String[] args) {
		AccessToken token = weixinUtil.getAccessToken();
		System.out.println("票据"+token.getAccess_token());
		System.out.println("有效时间"+token.getExpires_in());
		
		
		String path="E:/13.png";
		try {
			//上传临时文件
			/*String  mediaId= weixinUtil.upload(path, token.getAccess_token(), "thumb");
			System.out.println("mediaId是"+mediaId);*/
			
			
			//创建菜单
			/*String menu = JSONObject.fromObject(weixinUtil.initMenu()).toString().replace("sb_button", "sub_button");
			System.out.println("生成的菜单是"+menu);
			int result = weixinUtil.createMenu(token.getAccess_token(), menu);
			if(result == 0){
				System.out.println("创建菜单成功");
			}else{
				System.out.println("错误码： "+result+"错误原因：");
			}*/
			
			//查询菜单
			/*JSONObject jsonObejct = weixinUtil.queryMenu(token.getAccess_token());
			System.out.println(jsonObejct);*/
			//删除菜单
			/*int result = weixinUtil.deleteMenu(token.getAccess_token());
			if(result == 0){
				System.out.println("删除成功");
			}
			else{
				System.out.println("删除失败，错误码是："+result);
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
