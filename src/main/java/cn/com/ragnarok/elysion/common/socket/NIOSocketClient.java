package cn.com.ragnarok.elysion.common.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NIOSocketClient {
  private final static org.apache.log4j.Logger log = org.apache.log4j.
      LogManager.getLogger(NIOSocketClient.class);
  private String host;
  private int port;
  private SocketChannel sc;
  private Charset charset;

  public NIOSocketClient(String host,int port) {
    this.host=host;
    this.port=port;
    this.charset=Charset.forName("UTF-8");
  }

  public void openConnection(){
    InetSocketAddress addr=new InetSocketAddress(host,port);
    try {
      sc = SocketChannel.open(addr);
    }
    catch (IOException ex) {
      log.error("无法连接服务器:"+host+":"+port,ex);
    }
  }

  public void service() throws IOException {
    CharBuffer cb=null;
    ByteBuffer buffer=ByteBuffer.allocate(1024);
    ByteBuffer buffer2=ByteBuffer.allocate(1024);
  if(sc.isConnected()){
    sc.read(buffer2);
    buffer2.flip();
    cb=charset.decode(buffer2);
    System.out.println(cb.toString());



    buffer=charset.encode("elysion\r\n");
    sc.write(buffer);

    sc.read(buffer2);
    buffer2.flip();
    cb=charset.decode(buffer2);
    System.out.println(cb.toString());


    buffer=charset.encode("233max\r\n");
    sc.write(buffer);



    sc.read(buffer2);
    buffer2.flip();
    cb=charset.decode(buffer2);
    System.out.println(cb.toString());
  }

  }

  public static void main(String[] args) throws IOException {
    NIOSocketClient client=new NIOSocketClient("127.0.0.1",23);
    client.openConnection();
    client.service();
  }








}
