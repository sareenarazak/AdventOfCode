import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/1
 */

public class DayOne {

    public static final Map<String, Integer> numMap = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9,
            "zero", 0);
    public static void  main(String[] args) {
        List<String> lines = FileUtils.readInputFile("resources/calibrationsfull.txt");
        int sum = sumOfCalibrationValues(lines);
        System.out.println("Sum of calibration values is " + sum);
    }



    private static int sumOfCalibrationValues(List<String> lines) {
        List<Integer> values = recoverCalibrationValues(lines);
        return values.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private static List<Integer> recoverCalibrationValues(List<String> lines) {
        if(lines == null || lines.isEmpty()) return new ArrayList<>();

        List<Integer> caliberationValues = new ArrayList<>();
        for(String line : lines) {
            if(line == null || line.isEmpty()) continue;
            List<Character> digits = line.chars()
                    .mapToObj(c -> (char) c)
                    .filter(Character::isDigit)
                    .toList();

            Integer value = ((digits.get(0) -'0') * 10 )+ digits.get(digits.size()-1) -'0';
            caliberationValues.add(value);

        }
        return caliberationValues;
    }
}