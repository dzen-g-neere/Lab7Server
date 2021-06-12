package commands;

import exceptions.WrongArgumentException;
import utility.CollectionManager;

/**
 * This is command 'group_counting_by_creation_date'. Groups and print elements of collection by creationDate.
 */
public class GroupCountingByCreationDateCommand extends AbstractCommand implements Command {
    CollectionManager collectionManager;

    public GroupCountingByCreationDateCommand(CollectionManager collectionManager) {
        super("group_counting_by_creation_date", " - сгруппировать элементы коллекции по значению поля creationDate, вывести количество элементов в каждой группе)");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'group_counting_by_creation_date' command.
     */
    @Override
    public String execute(String argument) {
        try {
            if (!argument.isEmpty()) {
                throw new WrongArgumentException();
            }
            return collectionManager.groupCountingByCrDate();
        } catch (WrongArgumentException e) {
            return "Используйте: '" + getName() + "'";
        } catch (Exception e) {
            return "Ошибка. Повторите ввод.";
        }
    }
}
