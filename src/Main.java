import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final int DEPTH_OF_ONE = 1;

    public static void main(String[] args) {
        String home = System.getProperty("user.home");
        Path homePath = Paths.get(URI.create("file://" + home));
        Path downloadsPath = Paths.get(URI.create("file://" + home + "/Downloads"));

        try {
            checkIfPathExists(homePath);
            checkIfPathExists(downloadsPath);

            Stream<Path> downloadsContents = Files.find(downloadsPath, DEPTH_OF_ONE, (p,attr) -> true);
            Map<Path, Path> downloadPathsByFileName =
                    downloadsContents.collect(Collectors.toMap(path -> path.getFileName(), path -> path));

            FileVisitor<Path> homeDirectoryVisitor = new HomeDirectoryFileVisitor(downloadPathsByFileName);
            Files.walkFileTree(homePath, homeDirectoryVisitor);
        } catch(FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage() + "\nShutting down...");
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Couldn't access Downloads directory contents\nShutting down...");
        }
    }

    public static void checkIfPathExists(Path directory) throws FileNotFoundException {
        if (!Files.exists(directory)) {
            throw new FileNotFoundException("Directory " + directory + " not found on file system");
        }
    }
}
