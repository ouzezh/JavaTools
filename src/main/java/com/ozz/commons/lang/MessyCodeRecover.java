package com.ozz.commons.lang;

import java.io.UnsupportedEncodingException;

public class MessyCodeRecover {
  public static void main(String[] args) throws UnsupportedEncodingException {
    new MessyCodeRecover().recover("浣犲ソ");
    // System.out.println(isEnglishOrChinese("中国人abcDEF（；(,。"));
  }

  /**
   * 通过对常见编码格式尝试，恢复乱码文字并分析乱码原因
   * 
   * 美国：ASCII
   * 
   * 西欧：ASCII -> ISO 8859-1(又称Latin-1) -> Windows-1252
   * 
   * 中国：ASCII -> GB2312 -> GBK -> GB18030
   * 
   * 台湾：ASCII -> Big5
   * 
   * Unicode二进制编码：UTF-8,UTF-16,UTF-32
   */
  public void recover(String str) throws UnsupportedEncodingException {
    String[] charsets = new String[] {"windows-1252", "GB18030", "Big5", "UTF-8"};
    for (int i = 0; i < charsets.length; i++) {
      for (int j = 0; j < charsets.length; j++) {
        if (i != j) {
          String s = new String(str.getBytes(charsets[i]), charsets[j]);

          // if (isEnglishOrChinese(s)) {
          System.out.println("----反编译编码" + charsets[i] + ",编译编码" + charsets[j]);
          System.out.println(s);
          System.out.println();
          // }
        }
      }
    }
  }

  private static boolean isEnglishOrChinese(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.BASIC_LATIN
        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean isEnglishOrChinese(String strName) throws UnsupportedEncodingException {
    char[] ch = strName.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (!isEnglishOrChinese(c)) {
        return false;
      }
    }
    return true;
  }
}
