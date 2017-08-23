package com.ozz.commons.lang;

import java.io.UnsupportedEncodingException;

public class MessyCodeRecover {
  public static void main(String[] args) throws UnsupportedEncodingException {
    new MessyCodeRecover().recover("浣犲ソ");
  }

  /**
   * 通过对常见编码格式尝试，恢复乱码文字并分析乱码原因
   * 
   * 美国：ASCII
   * 西欧：ASCII -> ISO 8859-1(又称Latin-1) -> Windows-1252
   * 中国：ASCII -> GB2312 -> GBK -> GB18030
   * 台湾：ASCII -> Big5
   * 
   * Unicode二进制编码：UTF-8,UTF-16,UTF-32
   */
  public void recover(String str) throws UnsupportedEncodingException {
    String[] charsets = new String[] {"windows-1252","GB18030","Big5","UTF-8"};
    for(int i=0;i<charsets.length;i++) {
      for(int j=0;j<charsets.length;j++) {
        if(i != j) {
          String s = new String(str.getBytes(charsets[i]), charsets[j]);
          System.out.println("----原编码为" + charsets[j] + ",被误读为" + charsets[i]);
          System.out.println(s);
          System.out.println();
        }
      }
    }
  }
}
