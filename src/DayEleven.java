import java.util.List;
import java.util.stream.IntStream;

public class DayEleven {
    private static final char GALAXY = '#';
    private static final char BLANK_SPACE = '.';

    private static final long PART_TWO_FACTOR = 1000000;
    private static final long PART_ONE_FACTOR = 2;

    public static void main(String[] args) {
        List<String> lines = FileUtils.readInputFile("resources/galaxy.txt");

        //part 1
        System.out.println(getTotalDistance(lines, PART_ONE_FACTOR));

        //part 2
        System.out.println(getTotalDistance(lines, PART_TWO_FACTOR));
    }

    private static long getTotalDistance(List<String> lines, long factor) {
        List<Galaxy> galaxies = getGalaxies(lines);
        List<Integer> emptyRows = getEmptyRows(lines);
        List<Integer> emptyCols = getEmptyColumns(lines);

        return galaxies.stream()
                .flatMap(g1 -> galaxies.stream()
                        .filter(g2 -> galaxies.indexOf(g1) < galaxies.indexOf(g2))
                        .map(g2 -> getDistanceBetween(g1, g2, emptyRows, emptyCols, factor)))
                .mapToLong(Long::longValue)
                .sum();

//      Not sure if this is better than the code above

//        long sum = 0;
//        for(int i = 0 ; i < galaxies.size(); i++) {
//            for(int j = i+1; j <  galaxies.size(); j++) {
//                sum += getDistanceBetween(galaxies.get(i), galaxies.get(j), emptyRows, emptyCols, factor);
//            }
//        }
//      return sum;

    }
    private static long getDistanceBetween(Galaxy g1, Galaxy g2, List<Integer> emptyRows, List<Integer> emptyCols, long factor) {
        int distanceWithoutExpanding = Math.abs(g1.x - g2.x) + Math.abs(g1.y - g2.y);

        long rowExpansion = getEmptyBetween(g1.x, g2.x, emptyRows);
        long colExpansion = getEmptyBetween(g1.y, g2.y, emptyCols);
        long totalExpansion = (rowExpansion + colExpansion) * (factor - 1);

        return distanceWithoutExpanding + totalExpansion;
    }

    private static long getEmptyBetween(int coordinate1, int coordinate2, List<Integer> emptyCoordinates) {
        int minCoordinate = Math.min(coordinate1, coordinate2);
        int maxCoordinate = Math.max(coordinate1, coordinate2);

        return emptyCoordinates.stream()
                .filter(coordinate -> coordinate > minCoordinate && coordinate < maxCoordinate)
                .count();
    }
    private static List<Galaxy> getGalaxies(List<String> lines) {
        return IntStream.range(0,lines.size())
                .boxed()
                .flatMap(row ->
                        IntStream.range(0,lines.get(row).length())
                                .filter(col -> lines.get(row).charAt(col) == GALAXY)
                                .mapToObj(col -> new Galaxy(row, col)))
                .toList();
    }

    private static List<Integer> getEmptyRows(List<String> lines) {
        return  IntStream.range(0, lines.size())
                .filter(row -> lines.get(row).chars()
                        .allMatch(c -> c == BLANK_SPACE))
                .boxed()
                .toList();
    }

    private static List<Integer> getEmptyColumns(List<String> lines) {
        int colLength = lines.get(0).length();
        return  IntStream.range(0, colLength)
                .filter(col ->
                        lines.stream().
                                allMatch(l -> l.charAt(col) == BLANK_SPACE))
                .boxed()
                .toList();
    }
}
class Galaxy {
    int x;
    int y;
    public Galaxy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}
