package flashcards;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Logger {
    private StringBuilder logs;
    private static final Logger logger = new Logger();
    private static final Scanner scanner = new Scanner(System.in);

    public static Logger getInstance() {
        return logger;
    }

    private Logger() {
        this.logs = new StringBuilder();
    }

    public void logMessage(String message) {
        logs.append(message);
        System.out.print(message);
    }

    public void logMessageWithNL(String message) {
        logs.append(message);
        logs.append("\n");
        System.out.println(message);
    }

    public void logInput(String input) {
        logs.append(input);
        logs.append("\n");
    }

    public void saveLogsToFile() {
        this.logMessageWithNL("File name:");
        String name = scanner.nextLine();
        this.logInput(name);
        try (FileWriter fileWriter = new FileWriter(name, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.print(this.logs);
            this.logMessageWithNL("The log has been saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
