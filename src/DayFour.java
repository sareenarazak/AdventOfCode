import java.util.Arrays;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/4
 */
public class DayFour {
    public static void main(String[] args) {
        List<String> lines = FileUtils.readInputFile("resources/scratchcards.txt");
        System.out.println("total points from scratch card is : " + calculateTotalPoints(lines));
    }

    private static double calculateTotalPoints(List<String> lines) {
        return lines.stream()
                .map(DayFour::calculatePointForCard)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
    private static double calculatePointForCard(String line) {
        // Example line
        // Card   1: 24 12 26 39 19 98 74 16 82 77 | 80 11 51  1 74 60 77 68 42 35 39 78 21 12 29 19 25 98 65 91 33 17 59 24 31
        String allNumbers = line.split(":")[1].trim();

        String[] winningAndActualNumbers  = allNumbers.split("\\|");

        String winningNumbers = winningAndActualNumbers[0].trim();
        String actualNumbers = winningAndActualNumbers[1].trim();

        List<Integer> winningNums = parseNumbers(winningNumbers);
        List<Integer> actualNums = parseNumbers(actualNumbers);

        long count = winningNums.stream()
                .filter(actualNums::contains)
                .count();
        return count > 0 ? Math.pow(2, count - 1) : 0L;
    }
    private static List<Integer> parseNumbers(String numbers) {
        return Arrays.stream(numbers.split("\\s+"))
                .map(Integer::valueOf)
                .toList();
    }
}
