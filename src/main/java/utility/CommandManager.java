package utility;

import commands.*;
import exceptions.IncorrectScriptException;

import java.util.ArrayList;

public class CommandManager {

    private ArrayList<Command> commandArrayList = new ArrayList<>();
    private InsertCommand insertCommand;
    private ShowCommand showCommand;
    private ExitCommand exitCommand;
    private UpdateIDCommand updateIDCommand;
    private InfoCommand infoCommand;
    private ClearCommand clearCommand;
    private ExecuteScriptCommand executeScriptCommand;
    private FilterGreaterThanAveragePointCommand filterGreaterThanAveragePointCommand;
    private GroupCountingByCreationDateCommand groupCountingByCreationDateCommand;
    private HelpCommand helpCommand;
    private PrintDescendingCommand printDescendingCommand;
    private RemoveGreaterKey removeGreaterKey;
    private RemoveKeyCommand removeKeyCommand;
    private ReplaceIfGreaterCommand replaceIfGreaterCommand;
    private ReplaceIfLowerCommand replaceIfLowerCommand;
    private SaveCommand saveCommand;


    /**
     * Operates with the commands.
     */
    public CommandManager(
            InsertCommand insertCommand,
            ShowCommand showCommand,
            ExitCommand exitCommand,
            UpdateIDCommand updateIDCommand,
            InfoCommand infoCommand,
            ClearCommand clearCommand,
            ExecuteScriptCommand executeScriptCommand,
            FilterGreaterThanAveragePointCommand filterGreaterThanAveragePointCommand,
            GroupCountingByCreationDateCommand groupCountingByCreationDateCommand,
            HelpCommand helpCommand,
            PrintDescendingCommand printDescendingCommand,
            RemoveGreaterKey removeGreaterKey,
            RemoveKeyCommand removeKeyCommand,
            ReplaceIfGreaterCommand replaceIfGreaterCommand,
            ReplaceIfLowerCommand replaceIfLowerCommand,
            SaveCommand saveCommand
    ) {
        this.insertCommand = insertCommand;
        this.showCommand = showCommand;
        this.exitCommand = exitCommand;
        this.updateIDCommand = updateIDCommand;
        this.infoCommand = infoCommand;
        this.clearCommand = clearCommand;
        this.executeScriptCommand = executeScriptCommand;
        this.filterGreaterThanAveragePointCommand = filterGreaterThanAveragePointCommand;
        this.groupCountingByCreationDateCommand = groupCountingByCreationDateCommand;
        this.helpCommand = helpCommand;
        this.printDescendingCommand = printDescendingCommand;
        this.removeGreaterKey = removeGreaterKey;
        this.removeKeyCommand = removeKeyCommand;
        this.replaceIfGreaterCommand = replaceIfGreaterCommand;
        this.replaceIfLowerCommand = replaceIfLowerCommand;
        this.saveCommand = saveCommand;

        commandArrayList.add(insertCommand);
        commandArrayList.add(showCommand);
        commandArrayList.add(exitCommand);
        commandArrayList.add(updateIDCommand);
        commandArrayList.add(infoCommand);
        commandArrayList.add(clearCommand);
        commandArrayList.add(executeScriptCommand);
        commandArrayList.add(filterGreaterThanAveragePointCommand);
        commandArrayList.add(groupCountingByCreationDateCommand);
        commandArrayList.add(helpCommand);
        commandArrayList.add(printDescendingCommand);
        commandArrayList.add(removeGreaterKey);
        commandArrayList.add(removeKeyCommand);
        commandArrayList.add(replaceIfGreaterCommand);
        commandArrayList.add(replaceIfLowerCommand);
        commandArrayList.add(saveCommand);
    }

    /**
     * Start execute of 'insert' command.
     */
    public String insertLWToCollection(String arg) throws IncorrectScriptException {
        try {
            return insertCommand.execute(arg);
        } catch (IncorrectScriptException e) {
            throw new IncorrectScriptException();
        }

    }

    /**
     * Start execute of 'help' command.
     */
    public String help(String arg) {

        StringBuilder s = new StringBuilder();
        helpCommand.execute(arg);
        for (Command command : commandArrayList) {
            s.append(command.getName()).append(command.getDescription()).append("\n");
        }
        return s.toString();
    }

    /**
     * Start execute of 'show' command.
     */
    public String showCollection(String arg) {
        return showCommand.execute(arg);
    }

    /**
     * Start execute of 'info' command.
     */
    public String info(String arg) {
        return infoCommand.execute(arg);
    }

    /**
     * Start execute of 'remove_key' command.
     */
    public String remove_key(String arg) {
        return removeKeyCommand.execute(arg);
    }

    /**
     * Start execute of 'clear' command.
     */
    public String clear(String arg) {
        return clearCommand.execute(arg);
    }

    /**
     * Start execute of 'save' command.
     */
    public String save(String arg) {
        return saveCommand.execute(arg);
    }

    /**
     * Start execute of 'execute_script' command.
     */
    public String execute_script(String arg) {
        return executeScriptCommand.execute(arg);
    }

    /**
     * Start execute of 'exit' command.
     */
    public String exit(String arg) {
        return exitCommand.execute(arg);
    }

    /**
     * Start execute of 'replace_if_greater' command.
     */
    public String replace_if_greater(String arg) throws IncorrectScriptException {
        try {
            return replaceIfGreaterCommand.execute(arg);
        } catch (IncorrectScriptException e) {
            throw new IncorrectScriptException();
        }
    }

    /**
     * Start execute of 'replace_if_lowe' command.
     */
    public String replace_if_lowe(String arg) throws IncorrectScriptException {
        try {
            return replaceIfLowerCommand.execute(arg);
        } catch (IncorrectScriptException e) {
            throw new IncorrectScriptException();
        }
    }

    /**
     * Start execute of 'remove_greater_key' command.
     */
    public String remove_greater_key(String arg) {
        return removeGreaterKey.execute(arg);
    }

    /**
     * Start execute of 'group_counting_by_creation_date' command.
     */
    public String group_counting_by_creation_date(String arg) {
        return groupCountingByCreationDateCommand.execute(arg);
    }

    /**
     * Start execute of 'filter_greater_than_average_point' command.
     */
    public String filter_greater_than_average_point(String arg) {
        return filterGreaterThanAveragePointCommand.execute(arg);
    }

    /**
     * Start execute of 'print_descending' command.
     */
    public String print_descending(String arg) {
        return printDescendingCommand.execute(arg);
    }

    /**
     * Start execute of 'update' command.
     */
    public String updateID(String arg) throws IncorrectScriptException {
        return updateIDCommand.execute(arg);
    }
}
