import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class FileAppender {
    private static final String FULL_FILE_SUFFIX = "full";

    private File path;
    private int maxFileSizeMb;
    private int maxNumberOfFiles;
    private String prefix;

    FileAppender(String path, int maxFileSizeMb, int maxNumberOfFiles, String prefix) {
        this.maxFileSizeMb = maxFileSizeMb;
        this.maxNumberOfFiles = maxNumberOfFiles;
        this.prefix = prefix;

        boolean pathIsFine = true; // TODO: No, fix it

        if (pathIsFine) {
            this.path = new File(path);
        }
    }

    public void write(String stringToWrite) {
        File fileToWriteTo = getFileToWriteTo();

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWriteTo, true));
        writer.append(stringToWrite);
        writer.close();

        cleanup();
    }

    private File getFileToWriteTo() {
        File lastFile = getNewerFile();

        // TODO: If not full then return, otherwise create new and return new

    }

    private File getNewerFile() {
        File[] files = path.listFiles(); // TODO: Add filter instead of check in cycle

        FileTime newerFileTime = null;
        File newerFile = null;

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile() && file.getName().startsWith(prefix)) {
                BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                int compResult;
                if (newerFile != null) {
                    compResult = attr.creationTime().compareTo(newerFileTime);
                    if (compResult > 0) {
                        newerFile = file;
                        newerFileTime = attr.creationTime();
                    }
                } else {
                    newerFile = file;
                    newerFileTime = attr.creationTime();
                }
            }
        }

        return newerFile;
    }

    private boolean cleanup() {
        // TODO: Remove all files that have prefix and older and beyond file amount limit

    }
}
