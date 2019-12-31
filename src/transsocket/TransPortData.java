package transsocket;

import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 用于转发数据
 *
 * @author Administrator
 */
public class TransPortData implements Runnable {

  private transient static Log log = LogFactory.getLog(TranslatePort.class);

  Socket getDataSocket;
  Socket putDataSocket;
  String type;

  public TransPortData(Socket getDataSocket, Socket putDataSocket, String type) {
    this.getDataSocket = getDataSocket;
    this.putDataSocket = putDataSocket;
    this.type = type;
  }

  public void run() {
    try {
      switch (type) {
        case "1":
          System.out.println("客户端====>服务端");
          break;
        case "2":
          System.out.println("服务端====>客户端");
          break;
        default:
          break;
      }
      OutputStream out = putDataSocket.getOutputStream();
      InputStream in = getDataSocket.getInputStream();
      int r;
      List<Byte> bs = new LinkedList<Byte>();
      List<Byte> bsAll = new LinkedList<Byte>();
      while (true) {
        r = in.read();
        if (r == -1) {
//          sendAllMessage(out, bs, 3);
          System.out.println(type + "待转化数组为" + bs);
          System.out.println(type + " 连接断开");
          break;
        }

        bsAll.add((byte) r);
        if (r != 0) {
        } else {
          sendAllMessage(out, bsAll, -1);
          bsAll = new LinkedList<Byte>();
//          out.write(r);
//          bs = new LinkedList<>();
        }
//        } else {
//          sendMessage(out, bs, 0);
//        }

      }
    } catch (Exception e) {
      log.error("==============type:" + type, e);
    }
  }

  private void sendMessage(OutputStream out, List<Byte> bs, Integer typex) throws IOException {
    System.out.println(type + " 获取到结束标识符,类型为");
    if (bs.size() > 0) {
      System.out.println(type + "待转化数组为" + bs);
      byte[] bytes = Bytes.toArray(bs);
      String jsonObject = byteToAscii(bytes);
      System.out.println("我要转发信息啦" + jsonObject);
      bs = new LinkedList<Byte>();
    }
  }

  private void sendAllMessage(OutputStream out, List<Byte> bsAll, Integer type) throws IOException {
    System.out.println("获取到结束标识符,类型为" + type);
    if (bsAll.size() > 0) {
      System.out.println("待转化数组为" + bsAll);
      byte[] bytes = Bytes.toArray(bsAll);
      String jsonObject = byteToAscii(bytes);
      System.out.println("详细信息=========" + jsonObject);
      out.write(bytes, 0, bytes.length);
      out.flush();

    }
  }


  public static String byteToAscii(byte[] bytes) {
    StringBuffer sbu = new StringBuffer();
//    char[] chars = value.toCharArray();
    for (int i = 0; i < bytes.length; i++) {
      sbu.append((char) bytes[i]);
    }
    return sbu.toString();
  }
}