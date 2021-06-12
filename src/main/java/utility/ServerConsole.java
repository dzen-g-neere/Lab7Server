package utility;

import exceptions.IncorrectScriptException;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerConsole {
    private CommandManager commandManager;
    private Scanner scanner;


    public ServerConsole(CommandManager commandManager, Scanner scanner) {
        this.commandManager = commandManager;
        this.scanner = scanner;
    }

    /**
     * Mode for work with commands from user input.
     */
    public void interectiveMode() {
        String[] command;
        try {
            while (true) {
                System.out.println("Введите команду: ");
                command = (scanner.nextLine().trim() + " ").split(" ", 2);
                command[1] = command[1].trim();
                runCommand(command);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Введён конец файла! Завершение программы.");
            System.exit(0);
        } catch (IncorrectScriptException e) {
            System.out.println("Ошибка при считываниии команды пользователя!");
        }

    }

    /**
     * Selects and start command execution.
     */
    public void runCommand(String[] userCommand) throws IncorrectScriptException {
        try {
            switch (userCommand[0]) {
                case "save":
                    commandManager.save(userCommand[1]);
                    break;
                case "exit":
                    commandManager.save(userCommand[1]);
                    commandManager.exit(userCommand[1]);
                    break;
                default:
                    System.out.println("Не является внутренней командой. Введите 'save' для сохранения, либо 'exit' для сохранения и выхода из программы");
            }
        } catch (ExceptionInInitializerError e) {
            System.out.println("Непредвиденная ошибка");
            e.printStackTrace();
        }
    }
}
