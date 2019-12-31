package transsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 转发客户端的请求到真实的服务端 转发真实服务端的请求到客户端
 */
public class TranslatePort {

  private transient static Log log = LogFactory.getLog(TranslatePort.class);

  public static void main(String[] args) {
    try {
      if (args == null || args.length < 3) {
        log.error("输出参数不能为空，分别是 本地监听端口、远程IP、远程端口");
        return;
      }

      //获取本地监听端口、远程IP和远程端口
      int localPort = Integer.parseInt(args[0].trim());
      String remoteIp = args[1].trim();
      int remotePort = Integer.parseInt(args[2].trim());

      //启动本地监听端口
      ServerSocket serverSocket = new ServerSocket(localPort);
      log.error("localPort=" + localPort + ";remoteIp=" + remoteIp +
          ";remotePort=" + remotePort + ";启动本地监听端口" + localPort + "成功！");

      while (true) {
        Socket clientSocket = null;
        Socket remoteServerSocket = null;
        try {
          //获取客户端连接
          clientSocket = serverSocket.accept();

          log.error("accept one client");
          //建立远程连接
//          if(remoteServerSocket == null ){
          remoteServerSocket = initSocket(remoteIp, remotePort);
//          }
          log.error("create remoteip and port success");
          //启动数据转换接口
          new Thread(
              new TransPortData(remoteServerSocket, clientSocket, "remoteServer --->>> client"))
              .start();
          new Thread(
              new TransPortData(clientSocket, remoteServerSocket, "client --->>> remoteServer"))
              .start();
        } catch (Exception ex) {
          log.error("", ex);
        }
        //建立连接远程
      }
    } catch (Exception e) {
      log.error("", e);
    }
  }

  public static Socket initSocket(String remoteIp, Integer remotePort) {
    Socket remoteServerSocket = null;
    try {
      remoteServerSocket = new Socket(remoteIp, remotePort);
    } catch (IOException e) {
      System.out.println("链接失败，准备重连");
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e1) {
      }
      remoteServerSocket = initSocket(remoteIp, remotePort);
    }
    return remoteServerSocket;
  }
}