import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class FileUtils {
    public static List<String> readInputFile(String filePath) {
        Path currentWorkingDir = Paths.get(System.getProperty("user.dir"));

        Path path = currentWorkingDir.resolve(filePath);
        try {
            return Files.readAllLines(path);
        } catch (IOException ex) {
            throw new RuntimeException("Could not read text file at " + path);
        }
    }
    public static String readFileToString(String filePath)  {
        Path currentWorkingDir = Paths.get(System.getProperty("user.dir"));

        Path path = currentWorkingDir.resolve(filePath);
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException ex) {
            throw new RuntimeException("Could not read text file at " + path);
        }
    }
}
