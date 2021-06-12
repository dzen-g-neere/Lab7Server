package commands;

import utility.CollectionManager;

/**
 * This is command 'info'. Prints information about collection.
 */
public class InfoCommand extends AbstractCommand implements Command{
    CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", " - вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов)");
        this.collectionManager = collectionManager;
    }
    /**
     * Execute of 'add' command.
     */
    @Override
    public String execute(String argument) {
        return collectionManager.showInfo();
    }
}
