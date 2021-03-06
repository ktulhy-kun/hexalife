package ru.nsu.g13204.fit;

public class FileParserError extends Throwable {
    private final String msg;

    FileParserError(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
