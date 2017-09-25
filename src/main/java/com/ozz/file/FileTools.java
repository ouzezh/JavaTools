package com.ozz.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozz.demo.date.DateFormatDemo;
import com.ozz.demo.encrypt.DigestDemo;

public class FileTools {
  private Logger log = LoggerFactory.getLogger(getClass());

  private DigestDemo digestDemo;

  public List<List<String>> findRepeatFileInFolder(String folderPath)
      throws FileNotFoundException, IOException {
    return findRepeatFileInFolder(Collections.singleton(folderPath));
  }

  public List<List<String>> findRepeatFileInFolder(Collection<String> folderPaths)
      throws FileNotFoundException, IOException {
    /*
     * valid
     */
    List<Path> roots = new ArrayList<Path>();
    for (String rootPath : folderPaths) {
      Path root = Paths.get(rootPath);
      if (Files.exists(root) && Files.isDirectory(root)) {
        roots.add(root);
      } else {
        throw new RuntimeException("Folder is not exists! path: " + rootPath);
      }
    }

    /*
     * Collect data
     */
    long startTime = System.currentTimeMillis();
    Map<String, List<String>> mapOfMd5 = new HashMap<String, List<String>>();
    for (Path root : roots) {
      Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
        private int count = 0;
        private DateFormatDemo dateFormatUtil = new DateFormatDemo();

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
          // System.out.println(path.toString());
          count++;
          String digest = digestDemo.digest(path);
          log.info("scan " + count
                                                       + " "
                                                       + dateFormatUtil.getTimeStringByMillis(System.currentTimeMillis()
                                                                                              - startTime)
                                                       + " "
                                                       + path.toString());
          if (!mapOfMd5.containsKey(digest)) {
            mapOfMd5.put(digest, new ArrayList<String>());
          }
          mapOfMd5.get(digest).add(path.toString());
          return FileVisitResult.CONTINUE;
        }
      });
    }

    /*
     * output result
     */
    List<List<String>> res = new ArrayList<List<String>>();
    StringBuffer sb = new StringBuffer("\nRepeat Files start");
    for (Entry<String, List<String>> entry : mapOfMd5.entrySet()) {
      if (entry.getValue().size() > 1) {
        res.add(entry.getValue());
        sb.append("\n----" + res.size());
        for (String path : entry.getValue()) {
          sb.append("\n").append(path);
        }
      }
    }
    log.info(sb.append("\n\nRepeat Files end").toString());

    System.out.println("--End--");
    return res;
  }

  @Test
  public void test() throws IOException {
    try {
      copyDifferentFiles("D:/Develop/tools/eclipse_copy", "D:/Develop/tools/eclipse", "C:/Users/ouzezhou/Desktop/test", "configuration");
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * 拷贝两个文件夹中不同的文件到指定文件夹
   * 
   * 作用：制作eclipse的links插件
   */
  public void copyDifferentFiles(String folder, String compareFolder, String toFolder, String... ignoreFiles)
      throws IOException {
    Path root = Paths.get(folder);
    Path compareRoot = Paths.get(compareFolder);
    Path toRoot = Paths.get(toFolder);
    List<Path> ignorePaths = new ArrayList<>(ignoreFiles==null?0:ignoreFiles.length);
    for(String ignoreFile : ignoreFiles) {
      ignorePaths.add(Paths.get(ignoreFile));
    }
    
    Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        Path relativeze = root.relativize(path);
        for(Path ignorePath : ignorePaths) {
          if(relativeze.startsWith(ignorePath) || path.startsWith(ignorePath)) {
            return FileVisitResult.CONTINUE;
          }
        }
        
        Path comparePath = compareRoot.resolve(relativeze);
        if(!Files.exists(comparePath)) {
          log.info("copy: " + relativeze.toString());
          Path toPath = toRoot.resolve(relativeze);
          Files.createDirectories(toPath.getParent());
          Files.copy(path, toPath);
        }
        return FileVisitResult.CONTINUE;
      }
    });
    System.out.println("--End--");
  }
}

