package commands;

import exceptions.IncorrectScriptException;

public interface Command {
    /**
     * Interface for all commands.
     */

    String execute(String argument) throws IncorrectScriptException;

    String getName();
    String getDescription();
}
