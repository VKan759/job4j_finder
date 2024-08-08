import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.FileVisitResult.*;

public class Searcher implements FileVisitor<Path> {
    private Pattern pattern;
    private List<Path> paths = new LinkedList<>();
    public List<Path> getPaths() {
        return paths;
    }

    private String fileName;
    private String searchType;

    public Searcher(String fileName, String searchType) {
        this.fileName = fileName;
        this.searchType = searchType;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (searchType.equals("name") && file.toFile().getName().equals(fileName)) {
            paths.add(file);
        }
        if (searchType.equals("regex")) {
            pattern = Pattern.compile(fileName);
            Matcher matcher = pattern.matcher(file.toString());
            if (matcher.matches()) {
                paths.add(file);
            }
        }
        if (searchType.equals("mask")) {
            String regex = fileName.replace("*.?", ".*\\.[a-z]");
            pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(file.getFileName().toString());
            if (matcher.matches()) {
                paths.add(file);
            }
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return CONTINUE;
    }
}
