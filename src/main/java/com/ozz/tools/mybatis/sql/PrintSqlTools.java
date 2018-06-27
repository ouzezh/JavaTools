package com.ozz.tools.mybatis.sql;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * 根据日志打印出带参数的SQL
 */
public class PrintSqlTools {
  private String sqlPrefix = "==>  Preparing:";
  private String paramPrefix = "==> Parameters:";
  private String sqlPattern = "\\?";
  // private String paramSplitPattern = "\\((String|Integer)\\)(, |$)";
  // private String paramSplitLeft = "→";
  // private String paramSplitRigth = "←";
  // private String paramSplit = paramSplitLeft + "$1" + paramSplitRigth;
  // private String paramPattern =
  // "([^"+paramSplitLeft+paramSplitRigth+"]*)"+paramSplitLeft+"([^"+paramSplitLeft+paramSplitRigth+"]+)"+paramSplitRigth;

  @Test
  public void test() throws IOException {
    try {
      printSql();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  public void printSql() throws IOException {
    String filePath = getClass().getResource("sql.log").getPath();
    List<String> lines = FileUtils.readLines(new File(filePath), StandardCharsets.UTF_8);

    Pattern sqlP = Pattern.compile(sqlPattern);
    for (int i = 0; i < lines.size() - 1; i++) {
      String sqlStr = lines.get(i);
      String paramStr = lines.get(i + 1);
      if (sqlStr.contains(sqlPrefix) && paramStr.contains(paramStr)) {
        sqlStr = sqlStr.replaceFirst("^.*" + sqlPrefix, "").trim();
        paramStr = paramStr.replaceFirst("^.*" + paramPrefix, "").trim();

        System.out.println(sqlStr);
        System.out.println(paramStr);
        System.out.println();

        List<String> paramList = parseParam(paramStr);

        Matcher sm = sqlP.matcher(sqlStr);
        Iterator<String> it = paramList.iterator();
        String sql = sqlStr;
        while (sm.find() && it.hasNext()) {
          sql = sql.replaceFirst("\\?", it.next());
        }

        System.out.println();
        System.out.println(sql);
        System.out.println("----\r\n\r\n");
      }
    }
  }

  private List<String> parseParam(String paramStr) {
    String[] arr = paramStr.split(", ");
    List<String> paramList = new ArrayList<>();
    String temp = null;
    String endRegex = "\\([A-Z][a-z]+\\)$";
    String nullRegex = "null";
    for (String item : arr) {
      // 修正参数中包含", "的场景
      if (temp == null) {
        temp = item;
      } else {
        temp = temp + item;
      }

      boolean flag = temp.endsWith("(String)") || temp.endsWith("(Timestamp)");
      if (flag || temp.matches(".*" + endRegex) || nullRegex.equals(temp)) {
        System.out.print(temp);
        temp = temp.replaceFirst(endRegex, "");
        if (flag) {
          temp = "'" + temp + "'";
        }
        System.out.println(" -> " + temp);
        paramList.add(temp);
        temp = null;
      }
    }
    return paramList;
  }

}
