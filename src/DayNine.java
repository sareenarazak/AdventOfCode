import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class DayNine {
    public static void main(String[] args) {

        List<String> lines = FileUtils.readInputFile("resources/sensorreadings.txt");

        List<List<Integer>> input = parseLinesToMeasurements(lines);

        System.out.println(getSumOfFutureValues(input));
        System.out.println(getSumOfEHistoryValues(input));

    }
    private static List<List<Integer>> parseLinesToMeasurements(List<String> lines) {
        return lines.stream()
                .map(l -> Arrays.stream(l.split(" "))
                        .map(Integer::valueOf).toList())
                .toList();
    }

    private static Integer getSumOfFutureValues(List<List<Integer>> measurements) {
        return measurements.stream()
                .mapToInt(DayNine::getPredictedValue)
                .sum();
    }

    private static Integer getSumOfEHistoryValues(List<List<Integer>> measurements) {
        // reverse the list : Can I do this differently ?
        return measurements.stream()
                .map(DayNine::reverseImmutableList)
                .mapToInt(DayNine::getPredictedValue)
                .sum();
    }

    private static Integer getPredictedValue(List<Integer> readings) {
        Integer sum = 0;

        while(hasNonZeroValues(readings)) {
            sum += readings.get(readings.size() - 1);
            readings = calculateDifferences(readings);
        }
        return sum;
    }

    private static boolean hasNonZeroValues(List<Integer> readings) {
        return readings.stream().anyMatch(d -> d != 0);
    }

    private static List<Integer> calculateDifferences(List<Integer> readings) {
        return IntStream.range(0, readings.size() - 1)
                .mapToObj(i -> readings.get(i + 1) - readings.get(i))
                .toList();
    }

    private static List<Integer> reverseImmutableList(List<Integer> immutableList) {
        List<Integer> copyOfImmutableList = new ArrayList<>(immutableList);
        Collections.reverse(copyOfImmutableList);

        return copyOfImmutableList;
    }
}