package flashcards;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        CardList list = new CardList();
        Scanner scanner = new Scanner(System.in);
        String choice;
        String exportFilename = null;
        Logger logger = Logger.getInstance();

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-import")) {
                list.importValues(args[i + 1]);
            } else if (args[i].equals("-export")) {
                exportFilename = args[i + 1];
            }
        }

        mainLoop:
        while (true) {
            logger.logMessageWithNL("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):"); // Test 4 fails because it expects input and not Input
            choice = scanner.nextLine();
            logger.logInput(choice);
            switch (choice) {
                case "add":
                    list.addNewCard();
                    break;
                case "remove":
                    list.deleteCard();
                    break;
                case "import":
                    list.importValues();
                    break;
                case "export":
                    list.exportValues();
                    break;
                case "ask":
                    list.checkCardKnowledge();
                    break;
                case "log":
                    logger.saveLogsToFile();
                    break;
                case "hardest card":
                    list.getHardestCards();
                    break;
                case "reset stats":
                    list.resetMistakeStatistics();
                    break;
                case "exit":
                    break mainLoop;
            }

            logger.logMessage("\n");
        }

        logger.logMessageWithNL("Bye bye!");
        if (exportFilename != null) {
            list.exportValues(exportFilename);
        }
    }
}
