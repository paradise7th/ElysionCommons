package cn.com.ragnarok.elysion.common.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public abstract class AbstractSimpleSocketService
    implements ISocketService {
  private ByteBuffer buffer;
  private Charset charset;
  public AbstractSimpleSocketService() {
    this(1024,"UTF-8");
  }

  public AbstractSimpleSocketService(int bufferSize,String charset){
    buffer=ByteBuffer.allocate(bufferSize);
    this.charset=Charset.forName(charset);
  }




  /**
   * receiveData
   *
   * @param s SocketChannel
   * @return int
   * @todo Implement this common.socket.ISocketService method
   */
  public int receiveData(SocketChannel s) {
    //buffer.clear();
    int r=NIOSocketServer.ACCEPT;
    int c = 0;
    try {
      c = s.read(buffer);
    }
    catch (IOException ex) {
    }
    if(c>0){
      buffer.flip();
      CharBuffer cb=charset.decode(buffer);
      if(cb!=null){
        r=receiveDecodeData(cb.toString(),s);
      }
    }else{
      r=NIOSocketServer.REJECT;
    }
    buffer.clear();
    return r;
  }


  /**
   * @param s
   * @param sock
   * @return int
   */
  public abstract int receiveDecodeData(String s,SocketChannel sock);

  public void sendMessage(String message,SocketChannel sc) throws IOException {
    if(sc!=null){
      sc.write(charset.encode(message));
    }
  }

}
