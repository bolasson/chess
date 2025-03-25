package client;

import java.util.Arrays;
import java.util.List;

public class MiniREPL {
    private final String prompt;
    private final ValidateInput validateInput;
    private final List<String> exitValues = Arrays.asList("quit", "exit", "stop", "close", "q", "e", "s","c");

    public MiniREPL(String prompt, ValidateInput validateInput) {
        this.prompt = prompt;
        this.validateInput = validateInput;
    }

    public String run(java.util.Scanner scanner) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        while (true) {
            try {
                if (listContainsValue(exitValues, input)) {
                    return "quit";
                }
                if (validateInput.isValid(input).equals("valid")) {
                    break;
                }
            } catch (Exception e) {
                System.out.print(Client.ErrorMessage(e.getMessage()) + "\n" + prompt);
                input = scanner.nextLine();
            }
        }
        return input;
    }

    public static boolean listContainsValue(List<String> list, String value) {
        return list.stream().anyMatch(s -> s.equalsIgnoreCase(value));
    }
}