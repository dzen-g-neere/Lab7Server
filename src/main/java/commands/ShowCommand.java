package commands;

import exceptions.WrongArgumentException;
import utility.CollectionManager;


/**
 * This is command 'show'. Prints all elements of collection.
 */
public class ShowCommand extends AbstractCommand implements Command {
    private CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", " - вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'show' command.
     */
    @Override
    public String execute(String argument) {
        try {
            if (!argument.isEmpty()) {
                throw new WrongArgumentException();
            }
            return collectionManager.showCollection();
        } catch (WrongArgumentException e) {
            return "Используйте: '" + getName() + "'";
        } catch (Exception e) {
            return "Ошибка. Повторите ввод.";
        }
    }
}
