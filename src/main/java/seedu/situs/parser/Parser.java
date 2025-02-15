package seedu.situs.parser;

import seedu.situs.command.AddCommand;
import seedu.situs.command.AlertCommand;
import seedu.situs.command.AlertExpiringSoonCommand;
import seedu.situs.command.AlertLowStockCommand;
import seedu.situs.command.DateCommand;
import seedu.situs.command.DeleteCommand;
import seedu.situs.command.ExpireCommand;
import seedu.situs.command.HelpCommand;
import seedu.situs.command.ListCommand;
import seedu.situs.command.UpdateCommand;
import seedu.situs.command.FindCommand;
import seedu.situs.exceptions.DukeException;
import seedu.situs.ingredients.Ingredient;
import seedu.situs.ingredients.IngredientList;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class Parser {
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_UPDATE = "update";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_DATE = "date";
    private static final String COMMAND_EXPIRE = "expire";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_ALERTS = "alerts";
    private static final String COMMAND_SET_THRESHOLD = "set";

    private static final String INVALID_COMMAND_MESSAGE = "Invalid command!";
    private static final String DELETE_ERROR_MESSAGE = "Nothing to remove!";
    private static final String NUMBER_FORMAT_ERROR_MESSAGE = "Invalid number format!";
    private static final String NOT_FOUND_MESSAGE = "Ingredient not found!";
    private static final String INCORRECT_PARAMETERS_MESSAGE = "The number of parameters is wrong!";
    private static final String EXPIRY_FORMAT_ERROR_MESSAGE = "Invalid expiry date format!"
            + '\n' + "Please key in the expiry date in the format dd/mm/yyyy!";
    private static final String INVALID_ALERT_TYPE_MESSAGE = "Not an alert type!";

    private static final String SPACE_SEPARATOR = " ";
    private static final String EMPTY_STRING = "";
    private static final String DELIMITER = "n/|a/|e/";

    private static final int ADD_COMMAND_ARGUMENT_COUNT = 4;
    private static final int UPDATE_COMMAND_ARGUMENT_COUNT = 4;


    public static boolean isExit(String command) {
        return (command.equals(COMMAND_EXIT));
    }

    /**
     * Sends the input to the relevant command parser.
     *
     * @param command The user input String
     * @return the output message
     * @throws DukeException when there is an issue with the input
     */
    public static String parse(String command) throws DukeException {

        String[] words = command.split(SPACE_SEPARATOR, 2);

        switch (words[0]) {
        case COMMAND_LIST:
            return parseListCommand();
        case COMMAND_ADD:
            return parseAddCommand(command);
        case COMMAND_DELETE:
            return parseDeleteCommand(command);
        case COMMAND_UPDATE:
            return parseUpdateCommand(command);
        case COMMAND_DATE:
            return parseDateCommand(command);
        case COMMAND_HELP:
            return parseHelpCommand();
        case COMMAND_EXPIRE:
            return parseExpireCommand(command);
        case COMMAND_FIND:
            return parseFindCommand(command);
        case COMMAND_ALERTS:
            return parseAlertsCommand(command);
        case COMMAND_SET_THRESHOLD:
            return parseSetCommand(command);
        case COMMAND_EXIT:
            return "";
        default:
            return INVALID_COMMAND_MESSAGE;
        }
    }

    /**
     * Parses "find" command for keywords and executes find command.
     *
     * @param command The user input String
     * @return Search results for entered keywords
     * @throws DukeException If no keywords are entered
     */
    private static String parseFindCommand(String command) throws DukeException {
        String[] keywords = command.replace(COMMAND_FIND, "").trim().split(SPACE_SEPARATOR);

        assert (keywords != null);

        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i].isEmpty()) {
                throw new DukeException(INCORRECT_PARAMETERS_MESSAGE);
            }
            keywords[i] = keywords[i].trim();
        }

        String resultMsg = "";
        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];
            if (i > 0) {
                resultMsg += "\n";
            }
            resultMsg += new FindCommand(keyword).run();
        }
        return resultMsg;
    }

    /**
     * Calls and executes help command.
     *
     * @return a message summarising all possible commands recognised by SITUS
     */
    private static String parseHelpCommand() {
        return new HelpCommand().run();
    }

    /**
     * Parses update command and splits input into ingredient parameters.
     *
     * @param command 1 line of user input
     * @return Ingredient updated message
     */
    private static String parseUpdateCommand(String command) throws DukeException {
        String[] details = command.split(DELIMITER);

        if (details.length != UPDATE_COMMAND_ARGUMENT_COUNT) {
            throw new DukeException(INCORRECT_PARAMETERS_MESSAGE);
        }

        assert (details.length == UPDATE_COMMAND_ARGUMENT_COUNT);

        for (int i = 1; i < UPDATE_COMMAND_ARGUMENT_COUNT; i++) {
            details[i] = details[i].trim();
            if (details[i].equals(EMPTY_STRING)) {
                throw new DukeException(INCORRECT_PARAMETERS_MESSAGE);
            }
        }

        try {
            String ingredientName = details[1];
            double ingredientAmount = Double.parseDouble(details[2]);
            LocalDate ingredientExpiry = Ingredient.stringToDate(details[3]);

            Ingredient updatedIngredient =
                    new Ingredient(ingredientName, ingredientAmount,  ingredientExpiry);
            String resultMsg = new UpdateCommand(updatedIngredient).run();

            if (resultMsg.equals(EMPTY_STRING)) {
                resultMsg = NOT_FOUND_MESSAGE;
            }
            return resultMsg;
        } catch (NumberFormatException e) {
            throw new DukeException(NUMBER_FORMAT_ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            throw new DukeException(EXPIRY_FORMAT_ERROR_MESSAGE);
        }
    }

    /**
     * Parses add command and splits input into ingredient parameters,
     * then calls and executes the add command.
     *
     * @param command The user input String
     * @return Ingredient added message
     */
    private static String parseAddCommand(String command) throws DukeException {
        String[] details = command.split(DELIMITER);

        if (details.length != ADD_COMMAND_ARGUMENT_COUNT) {
            throw new DukeException(INCORRECT_PARAMETERS_MESSAGE);
        }

        assert (details.length == ADD_COMMAND_ARGUMENT_COUNT);

        for (int i = 1; i < ADD_COMMAND_ARGUMENT_COUNT; i++) {
            details[i] = details[i].trim();
            if (details[i].equals(EMPTY_STRING)) {
                throw new DukeException(INCORRECT_PARAMETERS_MESSAGE);
            }
        }

        try {
            String ingredientName = details[1];
            double ingredientAmount = Double.parseDouble(details[2]);
            LocalDate ingredientExpiry = Ingredient.stringToDate(details[3]);

            Ingredient newIngredient = new Ingredient(ingredientName, ingredientAmount,
                     ingredientExpiry);
            return new AddCommand(newIngredient).run();
        } catch (NumberFormatException e) {
            throw new DukeException(NUMBER_FORMAT_ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            throw new DukeException(EXPIRY_FORMAT_ERROR_MESSAGE);
        }
    }

    /**
     * Calls and Executes the List Command.
     *
     * @return List of ingredients
     * @throws DukeException if trying to access non-existing ingredients
     */
    private static String parseListCommand() throws DukeException {
        return new ListCommand().run();
    }

    /**
     * Calls and Executes the Delete Command.
     *
     * @param command The user input String
     * @return Ingredient Deleted Message
     * @throws DukeException if trying to access non-existing ingredients
     */
    private static String parseDeleteCommand(String command) throws DukeException {
        String detail = command.substring(COMMAND_DELETE.length()).trim();
        String resultMsg;

        if (detail.length() <= 0) {
            resultMsg = DELETE_ERROR_MESSAGE;
            return resultMsg;
        }

        try {
            int ingredientRemoveNumber = Integer.parseInt(detail);
            resultMsg = new DeleteCommand(ingredientRemoveNumber).run();
            return resultMsg;
        } catch (NumberFormatException e) {
            throw new DukeException(NUMBER_FORMAT_ERROR_MESSAGE);
        }
    }

    /**
     * Parses and executes the date command.
     *
     * @param command The user's input string
     * @return the result message
     * @throws DukeException if the date format is incorrect
     */
    private static String parseDateCommand(String command) throws DukeException {
        String detail = command.substring(COMMAND_DATE.length()).trim();

        return new DateCommand(detail).run();
    }

    /**
     * Parses and executes the expire command.
     */
    private static String parseExpireCommand(String command) throws DukeException {
        String detail = command.substring(COMMAND_EXPIRE.length()).trim();
        try {
            LocalDate expireBeforeDate = Ingredient.stringToDate(detail);
            return new ExpireCommand(expireBeforeDate).run();
        } catch (DateTimeParseException e) {
            throw new DukeException(EXPIRY_FORMAT_ERROR_MESSAGE);
        }
    }

    private static String parseAlertsCommand(String command) throws DukeException {
        String detail = command.substring(COMMAND_ALERTS.length()).trim();
        switch (detail) {
        case "all":
            return new AlertCommand().run();
        case "expiry":
            return new AlertExpiringSoonCommand().run();
        case "stock":
            return new AlertLowStockCommand().run();
        default:
            throw new DukeException(INVALID_ALERT_TYPE_MESSAGE);
        }
    }

    private static String parseSetCommand(String command) throws DukeException {
        String[] details = command.split(" ", 3);
        try {
            switch (details[1].trim()) {
            case "expiry":
                AlertExpiringSoonCommand.setExpiryThreshold(Long.parseLong(details[2].trim()));
                return "Successfully set expiry threshold to " + details[2].trim() + " days";
            case "stock":
                AlertLowStockCommand.setLowStockThreshold(Double.parseDouble(details[2].trim()));
                return "Successfully set low stock threshold to " + details[2].trim() + " kg";
            default:
                throw new DukeException("Invalid Input");
            }
        } catch (NumberFormatException e) {
            throw new DukeException(NUMBER_FORMAT_ERROR_MESSAGE);
        }
    }
}
