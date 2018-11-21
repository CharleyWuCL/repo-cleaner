package charley.wu.repo.cleaner;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地仓库清理助手
 *
 * @author Charley
 * @since 1.0
 */
public class Cleaner {

  private static final String HOME = System.getenv("HOME");
  private static final String DEFAULT_MAVEN_PATH = HOME + "/.m2/repository/";
  private static final String DEFAULT_GRADLE_PATH = HOME + "/.gradle/caches/modules-2/files-2.1/";
  private static final String REGEX = ".*(unknown|lastUpdated|error).*";

  private List<String> basePaths = new ArrayList<>();

  /**
   * 构造函数
   * @param mavenPath   Maven仓库目录
   * @param gradlePath  Gradle仓库目录
   */
  public Cleaner(String mavenPath, String gradlePath) {
    basePaths.add(mavenPath != null ? mavenPath : DEFAULT_MAVEN_PATH);
    basePaths.add(gradlePath != null ? gradlePath : DEFAULT_GRADLE_PATH);
    System.out.println("Maven path is: "+mavenPath+"; Gradle path is: "+gradlePath);
  }

  /**
   * 主函数
   */
  public void clean() {
    for (String path : basePaths) {
      Path dir = Paths.get(path);
      if (null == dir) {
        System.err.println("Dir "+path+" not exits.");
        continue;
      }
      clean(dir);
    }
  }

  /**
   * Clean 内部方法
   * @param dir 目录
   */
  private void clean(Path dir) {
    if (checkBad(dir)) {
      deleteBad(dir);
      return;
    }
    if (Files.isDirectory(dir)) {
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
        for (Path subPath : stream) {
          clean(subPath);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 检测无用文件及文件夹
   * 空文件夹; unknown; lastUpdate; error
   * @param file 文件
   * @return  True or False
   */
  private boolean checkBad(Path file) {
    String path = file.toFile().getAbsolutePath();
    return path.matches(REGEX);
  }

  /**
   * 删除无用文件
   *
   * @param path 文件路径
   */
  private void deleteBad(Path path) {
    System.out.println(path.toString());
    try{
      Files.deleteIfExists(path);
    }catch (IOException e){
      e.printStackTrace();
    }
  }

  /**
   * 执行入口
   *
   * @param args 参数
   */
  public static void main(String[] args) {
    Cleaner cleaner = new Cleaner(null, null);
    cleaner.clean();
  }
}
