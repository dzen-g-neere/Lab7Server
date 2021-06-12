package connection;

import database.DBLabWork;
import database.DBUser;
import database.DatabaseManager;
import exceptions.WrongArgumentException;
import labwork.LabWork;
import utility.*;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Operator {
    private static Scanner userScanner;
    private static String myenv;
    private static FileManager fileManager;
    private static CollectionManager collectionManager;
    private static LabWorkAsker asker;
    private static CommandManager commandManager;
    private static ScriptExecutor scriptExecutor;

    public Operator(Scanner userScanner, String myenv, CollectionManager collectionManager, FileManager fileManager, LabWorkAsker asker, CommandManager commandManager, ScriptExecutor scriptExecutor) {
        Operator.userScanner = userScanner;
        Operator.myenv = myenv;
        Operator.fileManager = fileManager;
        Operator.collectionManager = collectionManager;
        Operator.asker = asker;
        Operator.commandManager = commandManager;
        Operator.scriptExecutor = scriptExecutor;
    }

    public static ExchangeClass startCommand(ExchangeClass exchangeClass) {
        try {
            String arg = exchangeClass.getArgument();
            LabWork labWork;
            User user;
            DBUser dbUser = DatabaseManager.getDBUser();
            DatabaseManager.getDBLabWork().setUser(exchangeClass.getUser());
            //System.out.println(dbUser.login(exchangeClass.getUser()) + "  " + exchangeClass.getUser().getLogin() + " |" + exchangeClass.getCommandName() + "|");
            if (dbUser.login(exchangeClass.getUser()).equals(exchangeClass.getUser().getLogin())||exchangeClass.getCommandName().equals("register")||exchangeClass.getCommandName().equals("login")) {
                switch (exchangeClass.getCommandName()) {
                    case "register": {
                        user = exchangeClass.getUser();
                        exchangeClass.setAnswer(dbUser.create(user));
                    }
                    break;
                    case "login": {
                        user = exchangeClass.getUser();
                        if(dbUser.login(user).equals(user.getLogin()))
                        exchangeClass.setAnswer("Пользователь " + user.getLogin() + " успешно авторизован.");
                        else exchangeClass.setAnswer("Неверные данные для входа.");
                    }
                    break;
                    case "insert": {
                        DBLabWork dbLabWork = DatabaseManager.getDBLabWork();
                        labWork = exchangeClass.getLabWork();
                        labWork.setId(asker.askID());
                        labWork.setCreator_id(dbLabWork.getUserId(exchangeClass.getUser().getLogin()));
                        collectionManager.addLabWorkToCollection(labWork.getName(), labWork);
                        exchangeClass.setAnswer("Элемент добавлен в коллекцию.\n");
                    }
                    break;
                    case "update": {
                        labWork = exchangeClass.getLabWork();
                        LabWork labWork1 = null;
                        try {
                            labWork1 = collectionManager.getByKey(labWork.getName());
                        } catch (WrongArgumentException e) {
                            exchangeClass.setAnswer("Элемента с данным ключом не обнаружено.\n");
                        }
                        if (labWork1 != null) {
                            labWork.setId(labWork1.getId());
                            collectionManager.updateLabWork(labWork);
                            exchangeClass.setAnswer("Элемент обновлён.\n");
                        }
                    }
                    break;
                    case "show":
                        exchangeClass.setAnswer(commandManager.showCollection(arg));
                        break;
                    case "help":
                        exchangeClass.setAnswer(commandManager.help(arg));
                        break;
                    case "info":
                        exchangeClass.setAnswer(commandManager.info(arg));
                        break;
                    case "remove_key":
                        exchangeClass.setAnswer(commandManager.remove_key(arg));
                        break;
                    case "clear":
                        exchangeClass.setAnswer(commandManager.clear(arg));
                        break;
                    case "execute_script":
                        exchangeClass.setAnswer(exchangeClass.getCommandName() + " " + exchangeClass.getArgument() + "\n" + scriptExecutor.scriptMode(arg));
                        break;
                    case "replace_if_greater": {
                        LabWork labWorkOld;
                        try {
                            String argument = exchangeClass.getArgument();
                            if (argument.isEmpty()) throw new WrongArgumentException();
                            labWorkOld = collectionManager.getByKey(argument);
                            LabWork labWorkNew = exchangeClass.getLabWork();
                            labWorkNew.setId(labWorkOld.getId());
                            if (labWorkNew.compareTo(labWorkOld) > 0) {
                                collectionManager.removeKey(argument);
                                collectionManager.updateLabWork(labWorkNew);
                                exchangeClass.setAnswer("Замена успешна\n");
                            } else
                                exchangeClass.setAnswer("Заменяемый элемент больше нового. Замена не произведена\n");
                        } catch (WrongArgumentException e) {
                            exchangeClass.setAnswer("Аргумент некорректен\n");
                        }
                    }
                    break;
                    case "replace_if_lowe": {
                        LabWork labWorkOld;
                        try {
                            String argument = exchangeClass.getArgument();
                            if (argument.isEmpty()) throw new WrongArgumentException();
                            labWorkOld = collectionManager.getByKey(argument);
                            LabWork labWorkNew = exchangeClass.getLabWork();
                            labWorkNew.setId(labWorkOld.getId());
                            if (labWorkNew.compareTo(labWorkOld) < 0) {
                                collectionManager.removeKey(argument);
                                collectionManager.updateLabWork(labWorkNew);
                                exchangeClass.setAnswer("Замена успешна\n");
                            } else
                                exchangeClass.setAnswer("Заменяемый элемент меньше нового. Замена не произведена\n");
                        } catch (WrongArgumentException e) {
                            exchangeClass.setAnswer("Аргумент некорректен\n");
                        }
                    }
                    break;
                    case "remove_greater_key":
                        exchangeClass.setAnswer(commandManager.remove_greater_key(arg));
                        break;
                    case "group_counting_by_creation_date":
                        exchangeClass.setAnswer(commandManager.group_counting_by_creation_date(arg));
                        break;
                    case "filter_greater_than_average_point":
                        exchangeClass.setAnswer(commandManager.filter_greater_than_average_point(arg));
                        break;
                    case "print_descending":
                        exchangeClass.setAnswer(commandManager.print_descending(arg));
                        break;
                    default:
                        System.out.println("Не является внутренней командой. Повтороте ввод или напишите help для получения актуального списка команд.");
                }
            } else exchangeClass.setAnswer("Невозможно выполнить команду без авторизации.");
        } catch (Exception e) {
            exchangeClass.setAnswer("Что-то пошло не так");
            return exchangeClass;
        }
        return exchangeClass;
    }
}
