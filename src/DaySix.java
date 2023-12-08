import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class DaySix {
    public static void main(String[] args) {
        // Dynamic programming
        List<String> lines = FileUtils.readInputFile("resources/boatrace.txt");

        System.out.println(numOfWaysToWinAllRaces(parseRaceRecords1(lines.get(0), lines.get(1))));
        System.out.println(numOfWaysToWinAllRaces(parseRaceRecords2(lines.get(0), lines.get(1))));
    }

    private static Long numOfWaysToWinAllRaces(Map<Long, Long> currentRecords) {
        return currentRecords.entrySet().stream()
                .mapToLong(entry -> numOfWaysToWin(entry.getKey(), entry.getValue()))
                .reduce(1L, Math::multiplyExact);
    }

    private static Long numOfWaysToWin(Long time, Long currentWinningDistance) {
        return LongStream.range(1, time).filter(t -> t * (time -t ) > currentWinningDistance).count();
    }

    private static Map<Long, Long> parseRaceRecords1(String timeRecords, String winningDistanceRecords) {
        List<Long> timeValues = parseLineUsingRegex("\\d+", timeRecords);
        List<Long> distanceValues = parseLineUsingRegex("\\d+", winningDistanceRecords);

        return IntStream.range(0, timeValues.size())
                .boxed()
                .collect(
                        Collectors.toMap(timeValues::get, distanceValues::get));
    }

    // part 2
    // Ignore the space between numbers
    private static Map<Long, Long> parseRaceRecords2(String timeRecords, String winningDistanceRecords) {
        return parseRaceRecords1(
                timeRecords.replaceAll("\\s+", ""),
                winningDistanceRecords.replaceAll("\\s+", ""));
    }

    private static List<Long> parseLineUsingRegex(String regex, String line) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.results()
                .map(MatchResult::group)
                .map(Long::valueOf)
                .toList();
    }
}
