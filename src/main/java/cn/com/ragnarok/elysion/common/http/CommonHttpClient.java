package cn.com.ragnarok.elysion.common.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonHttpClient {
	 private static Logger log = Logger.getLogger(CommonHttpClient.class);  
     private String url="";
     private Map<String, Object> postParams=new HashMap<String, Object>();
     
     boolean keepCookies=false;
     String cookies="";
     String ua=null;
     
     public CommonHttpClient(){
    	 
     }
     
     public CommonHttpClient(String url){
    	 this.url=url;
     }
     
     public CommonHttpClient(String url,Map<String,Object> postParams){
    	 this.url=url;
    	 this.postParams=postParams;
     }
     
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setKeepCookies(boolean b){
		this.keepCookies=b;
	}
	
	public String getLastCookies(){
		return cookies;
	}
	
	public void setCookies(String str){
		this.cookies=str;
	}
	
	public void setUserAgent(String ua){
		this.ua=ua;
	}
	
	public Map<String, Object> getPostParams() {
		return postParams;
	}
	
	public void setPostParams(Map<String, Object> postParams) {
		this.postParams = postParams;
	}
	
	public void addPostParams(String key,Object value){
		this.postParams.put(key, value);
	}
	
	public void clearPostParams(){
		this.postParams.clear();
	}
	
	public String get(){
		String data=null;
		CloseableHttpClient client=HttpClients.createDefault();
		HttpGet get=new HttpGet(url);
		CloseableHttpResponse res=null;
		if(keepCookies){
			get.addHeader("Cookie", cookies);
		}
		if(ua!=null){
			get.setHeader("User-Agent", ua);
		}
		
		try {
			res=client.execute(get);
			int code=res.getStatusLine().getStatusCode();
		    log.debug("get: "+url+" code: "+code );
		    if(code==200){
		    		data="";
		    		
		    		if(keepCookies){
		    			Header[] headers=res.getHeaders("Set-Cookie");
		    			if(headers!=null){
		    				cookies="";
		    				for (int i = 0; i < headers.length; i++) {
		    					cookies+= headers[i].getValue()+";";
							}
		    						    				
		    			}
		    		}
		    		
		    	    HttpEntity entity = res.getEntity();
				    BufferedReader br= new BufferedReader( new InputStreamReader(entity.getContent(),"utf-8"));
				    String line=null;
				    while( (line=br.readLine())!=null){
				    	data+=line+"\n";
				    }
				    EntityUtils.consume(entity);
		    }
		 
		}catch(Exception e){
			log.error("get error:"+url,e);
		}finally {	
		    try {
				res.close();
			} catch (Exception e) {
			}
		    try {
				client.close();
			} catch (Exception e) {
			}
		}
		return data;
	}
	
	public String post(){
		String data=null;
		CloseableHttpClient client=HttpClients.createDefault();
		HttpPost post=new HttpPost(url);
		
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		for (String key : postParams.keySet()) {
			nvps.add(new BasicNameValuePair(key, postParams.get(key).toString()));
		}
		
		CloseableHttpResponse res=null;
		
		if(keepCookies){
			post.addHeader("Cookie", cookies);
		}
		if(ua!=null){
			post.setHeader("User-Agent", ua);
		}
		
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps));
			res=client.execute(post);
			int code=res.getStatusLine().getStatusCode();
		    log.debug("post: "+url+" code: "+code );
		    if(code==200){
		    		data="";
		    		
		    		if(keepCookies){
		    			Header[] headers=res.getHeaders("Set-Cookie");
		    			if(headers!=null){
		    				cookies="";
		    				for (int i = 0; i < headers.length; i++) {
		    					cookies+= headers[i].getValue()+";";
							}
		    						    				
		    			}
		    		}
		    		
		    	    HttpEntity entity = res.getEntity();
		    	    
				    BufferedReader br= new BufferedReader( new InputStreamReader(entity.getContent(),"utf-8"));
				    String line=null;
				    while( (line=br.readLine())!=null){
				    	data+=line+"\n";
				    }
				    EntityUtils.consume(entity);
		    }
		    
		    if(code==301 || code==302){
		    	
		    	if(keepCookies){
	    			Header[] headers=res.getHeaders("Set-Cookie");
	    			if(headers!=null){
	    				cookies="";
	    				for (int i = 0; i < headers.length; i++) {
	    					cookies+= headers[i].getValue()+";";
						}
	    						    				
	    			}
	    		}
		    	
		    	String newurl=res.getFirstHeader("location").getValue();
		    	if(newurl==null){
		    		newurl="/";
		    	}
		    	setUrl(newurl);
		    	return get();
		    }
		 
		}catch(Exception e){
			log.error("post error:"+url,e);
		}finally {	
		    try {
				res.close();
			} catch (Exception e) {
			}
		    try {
				client.close();
			} catch (Exception e) {
			}
		}
		return data;
	}
	
	
	public BufferedImage getImage(){
		BufferedImage img=null;
		CloseableHttpClient client=HttpClients.createDefault();
		HttpGet get=new HttpGet(url);
		
		
		
		CloseableHttpResponse res=null;
		if(keepCookies){
			get.addHeader("Cookie", cookies);
		}
		if(ua!=null){
			get.setHeader("User-Agent", ua);
		}
		
		try {
			res=client.execute(get);
			int code=res.getStatusLine().getStatusCode();
		    log.debug("get: "+url+" code: "+code );
		    if(code==200){
		    		
			      if(keepCookies){
		    			Header[] headers=res.getHeaders("Set-Cookie");
		    			if(headers!=null){
		    				cookies="";
		    				for (int i = 0; i < headers.length; i++) {
		    					cookies+= headers[i].getValue()+";";
							}
		    						    				
		    			}
		    		}
		    		
		    	    HttpEntity entity = res.getEntity();
		    	    img=ImageIO.read(entity.getContent());
				   
				    EntityUtils.consume(entity);
		    }
		 
		}catch(Exception e){
			log.error("post error:"+url,e);
		}finally {	
		    try {
				res.close();
			} catch (Exception e) {
			}
		    try {
				client.close();
			} catch (Exception e) {
			}
		}
		return img;
	}
	
	
	public static void main(String[] args) throws IOException {
//		CommonHttpClient client=new CommonHttpClient("http://www.cmvideo.cn/register_Account.msp");
//		CommonHttpClient client2=new CommonHttpClient("http://www.cmvideo.cn//isExist_Account.msp");
//		CommonHttpClient client3=new CommonHttpClient("http://www.cmvideo.cn/sendDynPwd.msp");
//		client.addPostParams("msisdn", "18970059183");
//		client.addPostParams("password", "18970059183");
//		client.addPostParams("dynCode", "18970059183");
//		String data=client.post();
//		if(data.indexOf(",\"result\":false")>0){
//			
//		}
////		data=new String(data.getBytes("iso8859-1"), "UTF-8");
//		log.info("data:" +data);
		
		CommonHttpClient client=new CommonHttpClient("http://wap.cmvideo.cn/captcha.jpg?type="+Math.random());
		client.setKeepCookies(true);
		
		
		client.setUrl("http://wap.cmvideo.cn/sendResetPasswdMessageCode.msp");
		client.addPostParams("mobile", "13564966691");
		client.addPostParams("captchaCode", "22");
		client.addPostParams("dispatchSuccUrl", "/wap2/mh/p/sy/share/login/yfzc/index.jsp");
		client.addPostParams("diapatchFailUrl", "/wap2/mh/p/sy/share/login/yfzc/index.jsp");
		System.out.println(client.post());
		System.out.println(client.getLastCookies());
		
		client.setUrl("http://wap.cmvideo.cn/captcha.jpg?type="+Math.random());
		client.get();
		client.getLastCookies();
		
//		ImageIO.write(client.getImage(),"png",new File("f:/output.png"));
//		System.out.println(client.getLastCookies());
				
	}
	
	 
     
}
