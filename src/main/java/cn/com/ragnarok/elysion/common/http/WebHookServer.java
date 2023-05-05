package cn.com.ragnarok.elysion.common.http;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * 简单的HTTP WEBHOOK服务端,用于内部调用或者小工具开发
 */
public class WebHookServer {
    private final static org.apache.log4j.Logger log = org.apache.log4j.LogManager.getLogger(WebHookServer.class);

    private HttpServer server;
    private boolean showlog=true;
    private int port;
    private String encoding="utf-8";
    
    public WebHookServer(int port) throws IOException {
        this.port=port;
        server=HttpServer.create(new InetSocketAddress(port),0);
    }
    
    public HttpServer getHttpServer(){
        return server;
    }
    
    public void setShowlogs(boolean showlog){
        this.showlog=showlog;
    }

    public void setEncoding(String encoding){
        this.encoding=encoding;
    }
    
    public void start(){
        log.info("WEBSERV-START: start at port: "+port);
        server.start();
    }
    
    public void addContextHandler(String context,HttpHandler handler){
        log.info("WEBSERV-INIT: init context:"+context);
        server.createContext(context,handler);
    }

    public void addContextService(String context,WebHookService service){
        log.info("WEBSERV-INIT: init context:"+context);
        service.initService();
        server.createContext(context, new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                InputStream in = httpExchange.getRequestBody(); //获得输入流
                try {
                    if(showlog){
                        log.info("WEBSERV-"+httpExchange.getRequestMethod()+": "+httpExchange.getRequestURI());
                    }
                    if(service.acceptRequest(httpExchange)==false){
                        return;
                    }

                    
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,encoding));
                    String line = null;
                    String body="";
                    while((line = reader.readLine()) != null) {
                        body+=line;
                    }
                    if(showlog){
                        log.info("WEBSERV-BODY: "+body);
                    }
                    String response=service.receiveData(httpExchange,body);
                    if(response!=null){
                        byte[] resdata=response.getBytes(encoding);
                        httpExchange.sendResponseHeaders(200, resdata.length);
                        OutputStream out = httpExchange.getResponseBody();  //获得输出流
                        out.write(resdata);
                        out.flush();
                    }
                } catch (Exception e) {
                    log.error("WEBSERV-ERR: SERVICE ERROR",e);
                } finally {
                   if(in!=null){
                       in.close();
                   }
                   httpExchange.close();
                }

            }
        });
    }
    

    public static void main(String[] args) throws IOException {
        WebHookServer webhook=new WebHookServer(8080);
        webhook.addContextService("/test", new WebHookService() {
            @Override
            public void initService() {
                
            }

            @Override
            public boolean acceptRequest(HttpExchange exchange) {
                log.info("accept");
                return true;
            }

            @Override
            public String receiveData(HttpExchange exchange, String body) {
                return "hhahahah";
            }
        });
        webhook.start();

    }

}

