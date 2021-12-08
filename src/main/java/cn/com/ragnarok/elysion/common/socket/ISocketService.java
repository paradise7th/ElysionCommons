package cn.com.ragnarok.elysion.common.socket;

import java.nio.channels.SocketChannel;

public interface ISocketService {
  void initService();
  int acceptService(SocketChannel s);
  int receiveData(SocketChannel s);
  void stopService();

}
