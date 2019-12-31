import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class TestHttp {

  public static void main(String[] args) {
    String url = "http://115.159.58.23:8082/basicGateway/file/previewImg/c2pzb2RyX2RlMzM5MmIxODE1YTRkMzY5MjNiNmFjOTdiNTM5Yjg3.jpg";
    http(url);
  }

  /**
   * 发送http下载文件
   */
  public static void http(String url) {
    URL u = null;
    HttpURLConnection con = null;
    FileOutputStream os = null;
    InputStream inputStream = null;
    // 尝试发送请求
    try {
      // aHVhaWFub2RyXzQ3OTUxMGE5YzMzMjRhZDdhODEyMjZhZTcyZWFhM2Yz.jpg
      u = new URL(url);
      con = (HttpURLConnection) u.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("cType", "PC");
      con.setRequestProperty("appName", "sjsodr");
      con.setDoOutput(true);
      con.setDoInput(true);
      con.setUseCaches(false);
      os = new FileOutputStream("/Users/xiet/test_file/sssss.jpg");
//      con.setRequestProperty("Content-Type", "binary/octet-stream");
      inputStream = con.getInputStream();
      Map<String, List<String>> headerFields = con.getHeaderFields();
      List<String> list = headerFields.get("Content-Disposition");
      String string = list.get(0);
      System.out.println(string); // fileName在这个字符串中
//      System.out.println(JSON.toJSON(headerFields));
      byte[] b = new byte[inputStream.available()];
      inputStream.read(b);
      os.write(b);
      os.flush();
      inputStream.close();
      os.close();

      // 读取返回内容
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (con != null) {
        con.disconnect();
      }
    }
  }
}
