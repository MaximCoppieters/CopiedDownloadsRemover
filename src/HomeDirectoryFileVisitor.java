import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HomeDirectoryFileVisitor implements FileVisitor<Path> {

    private Map<Path, Path> downloadPathsByFileName;
    private DeletedFilePrinter fileInfoLogger;
    private Set<Path> filesDeleted;

    public HomeDirectoryFileVisitor(Map<Path, Path> downloadPathsByFileName) {
        this.downloadPathsByFileName = downloadPathsByFileName;
        fileInfoLogger = new DeletedFilePrinter();
        filesDeleted = new HashSet<>();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (!dir.toString().contains("Downloads")) { //Visit all directories besides Downloads
            if (downloadPathsByFileName.containsKey(dir.getFileName()) && !filesDeleted.contains(dir.getFileName())) {
                Path nameOfFileToDelete = dir.getFileName();
                Path downloadFilePathToDelete = downloadPathsByFileName.get(nameOfFileToDelete);

                fileInfoLogger.printFileData(downloadFilePathToDelete, dir.getParent());

                FileVisitor<Path> recursiveDeleter = new RecursiveDeletionFileVisitor();
                Files.walkFileTree(downloadFilePathToDelete, recursiveDeleter);

                try {
                    Files.delete(downloadFilePathToDelete);
                } catch (DirectoryNotEmptyException dne) {
                    System.out.printf("Couldn't completely delete %s due to limited access rights\n",
                            downloadFilePathToDelete.getFileName());
                }

                filesDeleted.add(nameOfFileToDelete);
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
