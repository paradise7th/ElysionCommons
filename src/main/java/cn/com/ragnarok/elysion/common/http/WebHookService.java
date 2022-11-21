package cn.com.ragnarok.elysion.common.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface WebHookService  {

    void initService();
    boolean acceptRequest(HttpExchange exchange);
    String receiveData(HttpExchange exchange,String body);
    
}
