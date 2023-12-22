import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DayThirteen {
    public static void main(String[] args) {
        String mirrors = FileUtils.readFileToString("resources/mirrors.txt");
        List<List<String>> input = Arrays.stream(mirrors.split("\n\n")).map(para -> Arrays.asList(para.split("\n"))).toList();
        System.out.println("score is part 1 : " + getTotalScore(input, 0));
        System.out.println("score is part 2 : " + getTotalScore(input, 1));

    }
    private static int getTotalScore(List<List<String>> input, int smudgeCount) {
        return input.stream()
                .mapToInt(lines -> getScore(lines, smudgeCount))
                .sum();
    }

    private static int getScore(List<String> lines, int smudgeCount) {
        // check for horizontal inflection
        int horizontalInflectionPoint = findInflectionRange(lines,smudgeCount);

        // Add one to the index to account - since index starts at 0
        if(horizontalInflectionPoint != -1) return ( horizontalInflectionPoint + 1)  * 100;

        // if no horizontal inflection, check for vertical
        List<String > columns = getColumns(lines);
        int verticalInflectionPoint = findInflectionRange(columns,smudgeCount);

        return verticalInflectionPoint != -1 ? (verticalInflectionPoint + 1)  : 0;
    }

    private static int findInflectionRange(List<String> grid, int smudge) {
        List<Integer> inflectionPoints = IntStream.range(0, grid.size() - 1)
                    .boxed()
                    .filter(row -> isDiffCountValid(grid.get(row), grid.get(row + 1), smudge))
                    .toList();

        return  inflectionPoints.stream()
                .filter(p -> isMirrored(grid, p, p + 1, smudge))
                .findFirst()
                .orElse(-1);
    }

    private static boolean isDiffCountValid(String first, String second , int smudge) {
        //consider rows that are equal or has same char diff count as smudge
        return  IntStream.range(0, first.length())
                .filter(i -> first.charAt(i) != second.charAt(i))
                .count() <= smudge;
    }

    private static boolean isMirrored(List<String> grid, int left, int right, int smudgeCount) {
        while(left >= 0 && right < grid.size()) {
            String first = grid.get(left--);
            String second = grid.get(right++);

            long diffCharCount = IntStream.range(0, first.length())
                    .filter(i -> first.charAt(i) != second.charAt(i))
                    .count();

            if (diffCharCount > smudgeCount) return false;
            smudgeCount -= diffCharCount;
        }

        return smudgeCount == 0;
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
