package cc.rapidev.qqbot.common.utils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@SuppressWarnings("NullableProblems")
public final class DirectoryCleaner extends SimpleFileVisitor<Path> {

    public static void clear(Path path) throws IOException {
        Files.walkFileTree(path, new DirectoryCleaner());
    }

    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        Files.delete(path);
        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (exc != null) {
            throw exc;
        } else {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    }

}