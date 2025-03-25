package client;

import java.util.List;

public class MiniREPL {
    private final String prompt;
    private final String invalidPrompt;
    private final ValidateInput validateInput;
    private final List<String> exitValues;
    private final java.util.Scanner scanner;

    public MiniREPL(String prompt, String invalidPrompt, ValidateInput validateInput, List<String> exitValues, java.util.Scanner scanner) {
        this.prompt = prompt;
        this.invalidPrompt = invalidPrompt;
        this.validateInput = validateInput;
        this.exitValues = exitValues;
        this.scanner = scanner;
    }

    public String run() {
        System.out.println(prompt);
        String input = scanner.nextLine();;
        while (true) {
            try {
                String testInput = input;
                if (exitValues.stream().anyMatch(s -> s.equalsIgnoreCase(testInput))) {
                    return "";
                }
                if (validateInput.validate(input)) {
                    break;
                }
            } catch (Exception e) {
                System.out.println(invalidPrompt + "\n" + prompt);
                input = scanner.nextLine();
            }
        }
        return input;
    }
}