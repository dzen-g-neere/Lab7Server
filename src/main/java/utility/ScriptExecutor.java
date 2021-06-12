package utility;

import exceptions.IncorrectScriptException;
import exceptions.NoFileAccessException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ScriptExecutor {
    private static int recurs = 0;
    private static int recurs_max = 50;
    private CommandManager commandManager;
    private LabWorkAsker labWorkAsker;
    private HashSet<String> scriptsInProcess = new HashSet<String>();


    public ScriptExecutor(CommandManager commandManager, LabWorkAsker labWorkAsker) {
        this.commandManager = commandManager;
        this.labWorkAsker = labWorkAsker;
    }

    /**
     * Mode for work with commands from a script.
     */
    public String scriptMode(String argument) {
        String[] command;
        StringBuilder result = new StringBuilder();
        boolean isReadable = true;
        try {
            File file = new File(argument);
            if (file.exists() && !file.canRead()) {
                isReadable = false;
                throw new NoFileAccessException();
            }
        } catch (NoFileAccessException e) {
            result.append("Расширьте права файла на чтение и запись, и попробуйте снова.\n");
        }
        if (isReadable) {
            try (Scanner scriptScanner = new Scanner(new File(argument))) {
                if (!scriptScanner.hasNext()) throw new NoSuchElementException();
                labWorkAsker.setUserScanner(scriptScanner);
                labWorkAsker.setScriptMode(true);
                do {
                    command = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                    command[1] = command[1].trim();
                    while (scriptScanner.hasNextLine() && command[0].isEmpty()) {
                        command = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                        command[1] = command[1].trim();
                    }
                    System.out.println(String.join(" ", command));
                    try {
                        if (command[0].equals("execute_script") && scriptsInProcess.contains(command[1])) {
                            recurs++;
                            //System.out.println(recurs);
                        } else if (command[0].equals("execute_script")) {
                            scriptsInProcess.add(command[1]);
                            recurs++;
                            //System.out.println(recurs);
                        }
                        if (command[0].equals("execute_script")) {
                            if (scriptsInProcess.contains(command[1]) && recurs >= recurs_max) {
                                result.append("В скрипте присутствует бесконечная рекурсия. Выполнение продолжится со следующей команды.\n");
                                continue;
                            }
                        }
                        result.append(String.join(" ", command)).append("\n");
                        result.append(runCommand(command));
                    } catch (Error e) {
                        result.append("В вашем скрипте присутствует бесконечная рекурсия. Скрипт продолжит выполняться со следующей команды.\n");
                    }
                } while (scriptScanner.hasNextLine());
            } catch (FileNotFoundException exception) {
                result.append("Файл со скриптом не найден!");
            } catch (NoSuchElementException exception) {
                result.append("Файл со скриптом пуст!");
            } catch (IncorrectScriptException e) {
                result.append("Ошибка в скрипте. Скрипт закрыт.");
            } catch (Exception e) {
                result.append("Ошибка. Перезапустите программу.");
            }
        }
        recurs = 0;
        return result.toString();
    }

    /**
     * Selects and start command execution.
     */
    public String runCommand(String[] userCommand) throws IncorrectScriptException {
        try {
            switch (userCommand[0]) {
                case "insert":
                    return commandManager.insertLWToCollection(userCommand[1]);
                case "update":
                    return commandManager.updateID(userCommand[1]);
                case "show":
                    return commandManager.showCollection(userCommand[1]);
                case "help":
                    return commandManager.help(userCommand[1]);
                case "info":
                    return commandManager.info(userCommand[1]);
                case "remove_key":
                    return commandManager.remove_key(userCommand[1]);
                case "clear":
                    return commandManager.clear(userCommand[1]);
                case "execute_script":
                    return scriptMode(userCommand[1]);
                case "replace_if_greater":
                    return commandManager.replace_if_greater(userCommand[1]);
                case "replace_if_lowe":
                    return commandManager.replace_if_lowe(userCommand[1]);
                case "remove_greater_key":
                    return commandManager.remove_greater_key(userCommand[1]);
                case "group_counting_by_creation_date":
                    return commandManager.group_counting_by_creation_date(userCommand[1]);
                case "filter_greater_than_average_point":
                    return commandManager.filter_greater_than_average_point(userCommand[1]);
                case "print_descending":
                    return commandManager.print_descending(userCommand[1]);
                default:
                    return "Не является внутренней командой. Повтороте ввод или напишите help для получения актуального списка команд.";
            }
        } catch (ExceptionInInitializerError e) {
            System.out.println("Непредвиденная ошибка");
            e.printStackTrace();
        } catch (IncorrectScriptException e) {
            throw new IncorrectScriptException();
        } catch (Error | Exception e){
            return "Ошибка в скрипте.";
        }
        return "";
    }
}
