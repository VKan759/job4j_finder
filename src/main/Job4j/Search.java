import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Search {
    public static void main(String[] args) throws IOException {
        validate(args);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите директорию:");
        String directory = args[0].substring(3).concat(scanner.nextLine());
        System.out.println("Введите тип поиска:");
        String searchType = scanner.nextLine();
        String fileName;
        if (searchType.equals("mask")) {
            fileName = args[1].substring(3);
        } else {
            System.out.println("Введите имя файла:");
            fileName = scanner.nextLine();
        }
        List<Path> result = search(Path.of(directory), fileName, searchType);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(Path.of(args[3].substring(3)))))) {
            writer.write(result.toString());
        }
    }

    private static void validate(String[] args) {
        if (args[0].substring(3).isEmpty()) {
            throw new IllegalArgumentException("Не указана директория");
        }
        if (args[1].substring(3).isEmpty()) {
            throw new IllegalArgumentException("Отсутствует имя файла/ маска/ регулярное выражение");
        }
        if (args[2].substring(3).isEmpty()) {
            throw new IllegalArgumentException("Не указан тип поиска");
        }
        if (args[3].substring(3).isEmpty() && !args[3].substring(3).contains(".")) {
            throw new IllegalArgumentException("Не указан файл для записи результата");
        }
    }

    public static List<Path> search(Path root, String fileName, String searchType) throws IOException {
        Searcher searcher = new Searcher(fileName, searchType);
        Files.walkFileTree(root, searcher);
        return searcher.getPaths();
    }
}
