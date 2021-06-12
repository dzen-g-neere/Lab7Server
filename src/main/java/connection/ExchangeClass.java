package connection;

import labwork.LabWork;

import java.io.Serializable;

public class ExchangeClass implements Serializable {
    private String commandName = "";
    private String argument = "";
    private LabWork labWork;
    private String answer = "";
    private User user = new User();

    public ExchangeClass(String commandName, String argument, LabWork labWork) {
        this.commandName = commandName;
        this.argument = argument;
        this.labWork = labWork;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public LabWork getLabWork() {
        return labWork;
    }

    public void setLabWork(LabWork labWork) {
        this.labWork = labWork;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}