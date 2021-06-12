package run;

import commands.*;
import connection.Operator;
import connection.Server;
import connection.User;
import database.DBLabWork;
import database.DBUser;
import database.DatabaseManager;
import exceptions.WrongArgumentException;
import labwork.Coordinates;
import labwork.Difficulty;
import labwork.LabWork;
import utility.*;

import java.util.*;

/**
 * Main application class. Creates all instances and runs the program.
 *
 * @author Дмитрий Залевский P3112
 */
public class Main {

    public static void main(String[] args) {
        DBUser dbUser = DatabaseManager.getDBUser();
        //dbUser.create(new User());
        DBLabWork dbLabWork = DatabaseManager.getDBLabWork();
        dbLabWork.setUser(new User());
        /*dbLabWork.clear();
        try {
            dbLabWork.create(new LabWork(0,"ZZZZZZZZZ", new Coordinates(2,2.0f), new Date(), 5L, 5L, 8f, Difficulty.HARD, null));
            dbLabWork.create(new LabWork(0,"IDIDID", new Coordinates(2,2.0f), new Date(), 5L, 5L, 8f, Difficulty.HARD, null));
        } catch (WrongArgumentException e){
            System.out.println("Ошибка при создании");
        }

        HashMap<String, LabWork> map = dbLabWork.read();
        Collection<LabWork> set = map.values();
        for (LabWork i : set){
            System.out.println(i);
        }
        System.out.println();
        dbLabWork.removeGreaterKey("a");
        map = dbLabWork.read();
        set = map.values();
        for (LabWork i : set){
            System.out.println(i);
        }
        System.out.println(dbLabWork.clear());
        String path = System.getenv("envVariable");
        */
        String path = "backup";


        Scanner userScanner = new Scanner(System.in);
        final String envVariable = path;
        LabWorkAsker labWorkAsker = new LabWorkAsker(userScanner);
        FileManager fileManager = new FileManager(envVariable, labWorkAsker);
        CollectionManager collectionManager = new CollectionManager();
        collectionManager.loadCollection();
        CommandManager commandManager = new CommandManager(
                new InsertCommand(collectionManager, labWorkAsker),
                new ShowCommand(collectionManager),
                new ExitCommand(),
                new UpdateIDCommand(collectionManager, labWorkAsker),
                new InfoCommand(collectionManager),
                new ClearCommand(collectionManager),
                new ExecuteScriptCommand(),
                new FilterGreaterThanAveragePointCommand(collectionManager),
                new GroupCountingByCreationDateCommand(collectionManager),
                new HelpCommand(),
                new PrintDescendingCommand(collectionManager),
                new RemoveGreaterKey(collectionManager),
                new RemoveKeyCommand(collectionManager),
                new ReplaceIfGreaterCommand(collectionManager, labWorkAsker),
                new ReplaceIfLowerCommand(collectionManager, labWorkAsker),
                new SaveCommand(collectionManager)
        );
        ConsoleManager consoleManager = new ConsoleManager(userScanner, commandManager, labWorkAsker);
        ScriptExecutor scriptExecutor = new ScriptExecutor(commandManager, labWorkAsker);
        ServerConsole serverConsole = new ServerConsole(commandManager, userScanner);
//        ConsoleManager consoleManager = new ConsoleManager(userScanner, commandManager, labWorkAsker);
//        consoleManager.interectiveMode();
        Thread threadForReceiveFromTerminal = new Thread(serverConsole::interectiveMode);
        Thread startReceiveFromServerTerminal = new Thread(threadForReceiveFromTerminal);
        startReceiveFromServerTerminal.start();
        Operator operator = new Operator(userScanner, envVariable, collectionManager, fileManager, labWorkAsker, commandManager, scriptExecutor);
        Server server = new Server(operator);
        server.process();
/*
        try {
            byte[] bytes = new byte[10];
            SocketAddress address =
                    new InetSocketAddress(4888);
            DatagramChannel channel =
                    DatagramChannel.open();
            channel.bind(address);
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            address = channel.receive(buffer);
            for (int i = 0; i < 10; i++)
                bytes[i] *= 2;
            buffer.flip();
            channel.send(buffer, address);

        } catch (Exception e){
            e.printStackTrace();
        }
        */
    }

}
