import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class DeletedFilePrinter {

    private static final String[] sizes;

    static {
        sizes = new String[] {"B", "KB", "MB", "GB"};
    }

    public static void printFileData(Path filePath, Path secondOccuringPath) throws IOException {
        BasicFileAttributes fileInfo = Files.readAttributes(filePath, BasicFileAttributes.class);

        String humanReadableSize = convertToHumanReadableSize(fileInfo.size());

        System.out.printf("Deleting: %s - Size: %s - Already occurs in %s/\n",
                filePath.getFileName(), humanReadableSize, secondOccuringPath);
    }

    private static String convertToHumanReadableSize(long fileSize) {
        double fileSizeReadable = fileSize;
        int index = 0;
        for (index = 0; index < sizes.length; index++) {
            if (fileSizeReadable < 1024) {
                break;
            }
            fileSizeReadable = fileSizeReadable / 1024;
        }
        return String.format("%.2f %s", fileSizeReadable, sizes[index]);
    }
}
