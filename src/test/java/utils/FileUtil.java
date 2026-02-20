package utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

  private FileUtil() {
    // prevent object creation
  }

  public static Path getFilePath(String relativePath) {
    return Paths.get("src", "test", "resources", relativePath)
            .toAbsolutePath();
  }
}
