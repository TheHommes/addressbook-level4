package seedu.address.logic.commands;

public class IncorrectCommand extends Command {
        public final String resolvedText;
        public final String errorFeedback;

        public IncorrectCommand(String resolvedText, String errorFeedback){
            this.resolvedText = resolvedText;
            this.errorFeedback = errorFeedback;
        }

        @Override
        public CommandResult execute() {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult("Input: " + resolvedText + "\n" + errorFeedback);
        }
}
