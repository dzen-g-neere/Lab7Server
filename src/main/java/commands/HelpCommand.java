package commands;

import exceptions.WrongArgumentException;

/**
 * Command 'help'. Displays help for available commands.
 */
public class HelpCommand extends AbstractCommand implements Command {
    public HelpCommand() {
        super("help", " - вывести справку по доступным командам");
    }

    /**
     * Execute of 'help' command.
     */
    @Override
    public String execute(String argument) {
        try {
            if (!argument.isEmpty()) {
                throw new WrongArgumentException();
            }
        } catch (WrongArgumentException e) {
            return "Используйте: '" + getName() + "'";
        } catch (Exception e) {
            return "Ошибка. Повторите ввод.";
        }
        return "";
    }
}
