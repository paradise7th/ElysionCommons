package cn.com.ragnarok.elysion.common.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOSocketServer implements Runnable {
  private final static org.apache.log4j.Logger log = org.apache.log4j.
      LogManager.getLogger(NIOSocketServer.class);
  public static final int ACCEPT=0;
  public static final int REJECT=-1;


  private int port;
  private ISocketService service;
  private Selector selector;
  private ServerSocketChannel ssc;
  private boolean running=false;

  public NIOSocketServer(int port,ISocketService service)  {
    this.port=port;
    this.service=service;
  }

  public void startServer() throws IOException {
    ssc=ServerSocketChannel.open();
    ssc.configureBlocking(false);
    InetSocketAddress addr=new InetSocketAddress(port);
    ssc.socket().bind(addr);
    selector=Selector.open();
    ssc.register(selector,SelectionKey.OP_ACCEPT);
    log.info("监听端口:"+port);

  }

  public void startService(){
    log.info("启动服务:"+service.getClass().toString());
    Thread t=new Thread(this);
    t.start();
  }

  private void _startService(){
    running=true;
    service.initService();
    while(running){

      try {
        int count = selector.select();
        if (count <= 0) {
          continue;
        }
        for (Iterator it = selector.selectedKeys().iterator(); it.hasNext(); ) {
          SelectionKey key = (SelectionKey) it.next();
          it.remove();
          if (key.isAcceptable()) {
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            int c = service.acceptService(sc);
            if (c == ACCEPT) {
              sc.register(selector, SelectionKey.OP_READ);
            }else{
              sc.socket().close();
            }
          }
          if (key.isReadable()) {
            int c=service.receiveData( (SocketChannel) key.channel());
            if(c==REJECT){
              ((SocketChannel)key.channel()).socket().close();
              key.cancel();
            }
          }

        }
      }
      catch (Exception ex) {
        log.error("服务出错",ex);
      }
    }
  }

  public void run(){
    _startService();
  }

  public void stopService(){
    running=false;
    log.info("延迟3秒后开始关闭服务...");
    try {
      Thread.sleep(3000);
    }
    catch (InterruptedException ex) {
    }
    if(service!=null){
      service.stopService();
    }
    log.info("服务关闭");
  }

  public void stopServer(){
    log.info("停止监听:"+port);
    try {
      ssc.close();
    }
    catch (IOException ex) {
    }
  }



}
