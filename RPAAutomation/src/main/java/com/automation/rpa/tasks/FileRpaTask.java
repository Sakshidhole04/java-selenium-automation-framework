package com.automation.rpa.tasks;

import com.automation.rpa.core.RpaBaseTask;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * RPA task for file system operations.
 *
 * Supports: READ, WRITE, COPY, MOVE, DELETE, CREATE_DIR.
 *
 * Built with the Builder pattern for clean, readable task definitions:
 * <pre>
 *   FileRpaTask task = new FileRpaTask.Builder()
 *       .operation(FileOperation.WRITE)
 *       .source("output/report.txt")
 *       .content("Hello RPA")
 *       .build();
 * </pre>
 */
public class FileRpaTask extends RpaBaseTask {

    public enum FileOperation {
        READ, WRITE, COPY, MOVE, DELETE, CREATE_DIR
    }

    private final FileOperation operation;
    private final String        sourcePath;
    private final String        targetPath;
    private final String        content;

    private FileRpaTask(Builder builder) {
        super("FileTask[" + builder.operation + "] → " + builder.sourcePath);
        this.operation  = builder.operation;
        this.sourcePath = builder.sourcePath;
        this.targetPath = builder.targetPath;
        this.content    = builder.content;
    }

    @Override
    protected void execute() throws Exception {
        switch (operation) {
            case READ       -> readFile();
            case WRITE      -> writeFile();
            case COPY       -> copyFile();
            case MOVE       -> moveFile();
            case DELETE     -> deleteFile();
            case CREATE_DIR -> createDirectory();
        }
    }

    private void readFile() throws IOException {
        List<String> lines = Files.readAllLines(Path.of(sourcePath));
        log.Info("Read " + lines.size() + " lines from: " + sourcePath);
        lines.forEach(line -> log.Info("  >> " + line));
    }

    private void writeFile() throws IOException {
        ensureParentDirs(sourcePath);
        Files.writeString(Path.of(sourcePath), content);
        log.Info("File written: " + sourcePath);
    }

    private void copyFile() throws IOException {
        ensureParentDirs(targetPath);
        Files.copy(Path.of(sourcePath), Path.of(targetPath), StandardCopyOption.REPLACE_EXISTING);
        log.Info("Copied: " + sourcePath + " → " + targetPath);
    }

    private void moveFile() throws IOException {
        ensureParentDirs(targetPath);
        Files.move(Path.of(sourcePath), Path.of(targetPath), StandardCopyOption.REPLACE_EXISTING);
        log.Info("Moved: " + sourcePath + " → " + targetPath);
    }

    private void deleteFile() throws IOException {
        boolean deleted = Files.deleteIfExists(Path.of(sourcePath));
        log.Info(deleted ? "Deleted: " + sourcePath : "File not found (skip): " + sourcePath);
    }

    private void createDirectory() throws IOException {
        Files.createDirectories(Path.of(sourcePath));
        log.Info("Directory created: " + sourcePath);
    }

    private void ensureParentDirs(String path) throws IOException {
        Path parent = Path.of(path).getParent();
        if (parent != null) Files.createDirectories(parent);
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static class Builder {
        private FileOperation operation;
        private String sourcePath;
        private String targetPath;
        private String content = "";

        public Builder operation(FileOperation op)  { this.operation  = op;   return this; }
        public Builder source(String path)          { this.sourcePath = path; return this; }
        public Builder target(String path)          { this.targetPath = path; return this; }
        public Builder content(String text)         { this.content    = text; return this; }

        public FileRpaTask build() {
            if (operation == null || sourcePath == null) {
                throw new IllegalStateException("FileRpaTask requires 'operation' and 'source'.");
            }
            return new FileRpaTask(this);
        }
    }
}
