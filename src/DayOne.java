import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2023/day/1
 */

public class DayOne {

    public static final Map<String, String> numMap = Map.of(
            "one", "1",
            "two", "2",
            "three", "3",
            "four", "4",
            "five", "5",
            "six", "6",
            "seven", "7",
            "eight", "8",
            "nine", "9",
            "zero", "0");

    public static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]|one|two|three|four|five|six|seven|eight|nine|zero");
    public static void  main(String[] args) {
        List<String> lines = FileUtils.readInputFile("resources/calibrationsfull.txt");
        System.out.println("Sum of calibration values is " + sumOfCalibrationValues(lines));
        System.out.println("Sum of calibration values is " + sumOfCalibrationValues2(lines));
    }



    private static int sumOfCalibrationValues(List<String> lines) {
        List<Integer> values = recoverCalibrationValues(lines);
        return values.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private static List<Integer> recoverCalibrationValues(List<String> lines) {
        if(lines == null || lines.isEmpty()) return new ArrayList<>();

        List<Integer> calibrationValues = new ArrayList<>();
        for(String line : lines) {
            if(line == null || line.isEmpty()) continue;
            List<Character> digits = line.chars()
                    .mapToObj(c -> (char) c)
                    .filter(Character::isDigit)
                    .toList();

            Integer value = ((digits.get(0) -'0') * 10 )+ digits.get(digits.size()-1) -'0';
            calibrationValues.add(value);

        }
        return calibrationValues;
    }

    //part 2
    private static int sumOfCalibrationValues2(List<String> lines) {
        List<Integer> values = recoverCalibrationValues2(lines);
        return values.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
    private static List<Integer> recoverCalibrationValues2(List<String> lines) {
        List<Integer> calibrationValues = new ArrayList<>();
        // Try region modification, does the matcher start at the beginning each time ?
        for(String line : lines) {
            Matcher matcher = NUMBER_PATTERN.matcher(line);
            String first = null;
            String last = null;
            while (matcher.find()) {
                if (first == null) {
                    first = matcher.group();
                }
                last = matcher.group();

                // without this the matcher will search the whole line
                // the result is diff without this -> test / to learn
                matcher.region(matcher.start() + 1, line.length());

            }
            String number = numMap.getOrDefault(first, first) + numMap.getOrDefault(last, last);
            calibrationValues.add(Integer.valueOf(number));

        }
        return calibrationValues;
    }
}