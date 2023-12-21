import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DayThirteen {
    public static void main(String[] args) {
        String mirrors = FileUtils.readFileToString("resources/mirrors.txt");
        List<List<String>> input = Arrays.stream(mirrors.split("\n\n")).map(para -> Arrays.asList(para.split("\n"))).toList();
        System.out.println("score is: " + getTotalScore(input));
    }
    private static int getTotalScore(List<List<String>> input) {
        return input.stream()
                .mapToInt(DayThirteen::getScore)
                .sum();
    }

    private static int getScore(List<String> lines) {
        // check for horizontal inflection
        int horizontalInflectionPoint = findInflectionRange(lines);

        // Add one to the index to account - since index starts at 0
        if(horizontalInflectionPoint != -1) return ( horizontalInflectionPoint + 1)  * 100;

        // if no horizontal inflection, check for vertical
        List<String > columns = getColumns(lines);
        int verticalInflectionPoint = findInflectionRange(columns);

        return verticalInflectionPoint != -1 ? (verticalInflectionPoint + 1)  : 0;
    }
    private static int findInflectionRange(List<String> grid){
        List<Integer> inflectionPoints = IntStream.range(0, grid.size() - 1)
                .boxed()
                .filter(row ->
                        grid.get(row).equals(grid.get(row + 1)))
                .toList();

        return  inflectionPoints.stream()
                .filter(p -> isMirrored(grid, p))
                .findFirst()
                .orElse(-1);
    }
    private static boolean isMirrored(List<String> grid, int point) {
        int left = point - 1;
        int right = point + 2;
        while(left >= 0 && right < grid.size()) {
            if (!grid.get(left--).equals(grid.get(right++))) return false;
        }
        return true;
    }

    private static List<String> getColumns(List<String> grid) {
        return IntStream.range(0, grid.get(0).length())
                .mapToObj(col ->
                        grid.stream()
                                .map(row -> String.valueOf(row.charAt(col)))
                                .collect(Collectors.joining()))
                .toList();
    }
}
