package com.beiming.framework.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

/**
 * package: com.beiming.framework.util describe: 基于主键ID生成对应短网址编码 create_user: xiet
 * create_date:2018/6/23 create_time: 上午11:25
 **/
public class ShortUrlUtils {

  private static final String DICT = "abcdefghijklmnopqrstuvwxyz"
      + "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  // 数字->字符映射
  private static final char[] CHARS = DICT.toCharArray();
  // 字符->数字映射
  private static final Map<Character, Integer> NUMBERS = new HashMap<Character, Integer>();

  static {
    int len = CHARS.length;
    for (int i = 0; i < len; i++) {
      NUMBERS.put(CHARS[i], i);
    }
  }

  /**
   * 根据从数据库中返回的记录ID生成对应的短网址编码
   *
   * @param id (1-56.8billion)
   */
  public static String encode(long id) {
    StringBuilder shortURL = new StringBuilder();
    while (id > 0) {
      int r = (int) (id % 62);
      shortURL.insert(0, CHARS[r]);
      id = id / 62;
    }
    int len = shortURL.length();
    while (len < 6) {
      shortURL.insert(0, CHARS[0]);
      len++;
    }
    return shortURL.toString();
  }

  /**
   * 根据获得的短网址编码解析出数据库中对应的记录ID
   *
   * @param key 短网址 eg. RwTji8, GijT7Y等等
   */
  public static long decode(String key) {
    char[] shorts = key.toCharArray();
    int len = shorts.length;
    long id = 0l;
    for (int i = 0; i < len; i++) {
      id = id + (long) (NUMBERS.get(shorts[i]) * Math.pow(62, len - i - 1));
    }
    return id;
  }

  public static void main(String[] args) {
    String host = "https://idenauthen.market.alicloudapi.com";
    String path = "/idenAuthentication";
    String method = "POST";
    String appcode = "0d4932aaf0e446d18f6faddfd8c47d72";
    Map<String, String> headers = new HashMap<String, String>();
    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
    headers.put("Authorization", "APPCODE " + appcode);
    //根据API的要求，定义相对应的Content-Type
    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    Map<String, String> querys = new HashMap<String, String>();
    Map<String, String> bodys = new HashMap<String, String>();
    bodys.put("idNo", "11010719831103122x");
    bodys.put("name", "孙晓美");

    try {
      /**
       * 重要提示如下:
       * HttpUtils请从
       * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
       * 下载
       *
       * 相应的依赖请参照
       * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
       */
      HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
      System.out.println(response.toString());
      //获取response的body
      System.out.println(EntityUtils.toString(response.getEntity()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
