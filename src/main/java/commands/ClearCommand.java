package commands;

import exceptions.WrongArgumentException;
import utility.CollectionManager;

/**
 * This is command 'clear'. Delete all elements of collection.
 */
public class ClearCommand extends AbstractCommand implements Command{
    CollectionManager collectionManager;
    public ClearCommand(CollectionManager collectionManager) {
        super("clear", " - очистить коллекцию");
        this.collectionManager = collectionManager;
    }
    /**
     * Execute of 'clear' command.
     */
    @Override
    public String execute(String argument){
        try {
            if (!argument.isEmpty()) {
                throw new WrongArgumentException();
            }
            return collectionManager.clearCollection();
        } catch (WrongArgumentException e) {
            return "Некорректный аргумент. Используйте: '" + getName() + "'";
        } catch (Exception e) {
            return "Ошибка. Повторите ввод.";
        }
    }
}
