package transsocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 用于转发数据
 *
 * @author Administrator
 */
public class TransPortData2 extends Thread {

  private transient static Log log = LogFactory.getLog(TranslatePort.class);

  Socket getDataSocket;
  Socket putDataSocket;

  String type;

  public TransPortData2(Socket getDataSocket, Socket putDataSocket, String type) {
    this.getDataSocket = getDataSocket;
    this.putDataSocket = putDataSocket;
    this.type = type;
  }

  public void run() {
    try {
      while (true) {

        InputStream in = getDataSocket.getInputStream();
        OutputStream out = putDataSocket.getOutputStream();
        //读入数据
        byte[] data = new byte[1024];
        int readlen = in.read(data);

        //如果没有数据，则暂停
        if (readlen <= 0) {
          Thread.sleep(300);
          continue;
        }

        for (byte b : data) {
//        Object result = JSONObject.parse(data);
          System.out.println("我要转发信息啦" + b);
        }
        out.write(data, 0, readlen);
        out.flush();
      }
    } catch (Exception e) {
      log.error("type:" + type, e);
    } finally {
      //关闭socket
      try {
        if (putDataSocket != null) {
          putDataSocket.close();
        }
      } catch (Exception exx) {
      }

      try {
        if (getDataSocket != null) {
          getDataSocket.close();
        }
      } catch (Exception exx) {
      }
    }
  }

  //将字节数组转换为16进制字符串
  public static String BinaryToHexString(byte[] bytes) {
    String hexStr = "0123456789ABCDEF";
    String result = "";
    String hex = "";
    for (byte b : bytes) {
      hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
      hex += String.valueOf(hexStr.charAt(b & 0x0F));
      result += hex + " ";
    }
    return result;
  }


}