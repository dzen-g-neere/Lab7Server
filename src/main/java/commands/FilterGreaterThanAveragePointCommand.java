package commands;

import utility.CollectionManager;

/**
 * This is command 'filter_greater_than_average_point'. Prints elements which 'averagePoint' is more than given.
 */
public class FilterGreaterThanAveragePointCommand extends AbstractCommand implements Command {
    CollectionManager collectionManager;

    public FilterGreaterThanAveragePointCommand(CollectionManager collectionManager) {
        super("filter_greater_than_average_point", " averagePoint - вывести элементы, значение поля averagePoint которых больше заданного");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'filter_greater_than_average_point' command.
     */
    @Override
    public String execute(String argument) {
        float averagePoint;
        try {
            averagePoint = Float.parseFloat(argument);
            if (averagePoint <= 0) {
                throw new NumberFormatException();
            }
            return collectionManager.filterGreaterThanAveragePoint(averagePoint);
        } catch (NullPointerException e) {
            return "Ошибка. Аргумент не может быть пустой строкой.";
        } catch (NumberFormatException e) {
            return "Ошибка. Аргумент должен быть числом больше 0.";
        }

    }
}
