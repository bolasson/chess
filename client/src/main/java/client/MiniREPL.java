package client;

import java.util.Arrays;
import java.util.List;

public class MiniREPL {
    private final String prompt;
    private final String invalidPrompt;
    private final ValidateInput validateInput;
    private final List<String> exitValues = Arrays.asList("quit", "exit", "stop", "close", "q", "e", "s","c");

    public MiniREPL(String prompt, String invalidPrompt, ValidateInput validateInput) {
        this.prompt = prompt;
        this.invalidPrompt = invalidPrompt;
        this.validateInput = validateInput;
    }

    public String run(java.util.Scanner scanner) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        while (true) {
            try {
                String testInput = input;
                if (exitValues.stream().anyMatch(s -> s.equalsIgnoreCase(testInput))) {
                    return "quit";
                }
                if (validateInput.isValid(input).equals("valid")) {
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage() + "\n" + prompt);
                input = scanner.nextLine();
            }
        }
        return input;
    }
}