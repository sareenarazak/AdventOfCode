import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayFifteen {
    public static void main(String[] args) {
        String line = FileUtils.readFileToString("resources/lenslibrary-hash.txt");
        System.out.println(hashCodePart1(line));
        System.out.println(performInstructions(Arrays.asList(line.split(","))));
    }

    private static int performInstructions(List<String> instructions) {
        Pattern pattern = Pattern.compile("([^=-]+)([=-]?)([0-9]*)");
        Map<Integer, LensBox> boxMap = new HashMap<>();

        instructions.stream()
                .map(pattern::matcher)
                .filter(Matcher::find)
                .forEach(m -> mapToLensBox(m, boxMap));

        return boxMap.values().stream()
                .mapToInt(box ->
                        (box.boxId + 1) * computeLensPower(box.labelToFocalLengthMap))
                .sum();
    }

    private static void mapToLensBox(Matcher m, Map<Integer, LensBox> lensBoxMap) {
        String label = m.group(1);
        String sign = m.group(2);

        int boxId = hashCode(label);

        if (sign.equals("=")) {
            int focalLength = Integer.parseInt(m.group(3));
            lensBoxMap.computeIfAbsent(boxId, LensBox::new)
                    .labelToFocalLengthMap.put(label, focalLength);

        } else if(sign.equals("-") && lensBoxMap.containsKey(boxId) ) {
            lensBoxMap.get(boxId).labelToFocalLengthMap.remove(label);
        }
    }

    private static int computeLensPower(Map<String, Integer> map) {
        AtomicInteger count  = new AtomicInteger(1);
        return map.values().stream()
                .mapToInt(n -> n * (count.getAndIncrement()))
                .reduce(0, Integer::sum);
    }

    private static int hashCode(String label) {
        return label.chars()
                .reduce(0,
                        (a, b) -> ((a + b) * 17) % 256);
    }
    private static int hashCodePart1(String instructions) {
        return Arrays.stream(instructions.split(","))
                .mapToInt( s -> s.chars()
                        .reduce(0,
                                (a,b) -> ((a + b) * 17 ) % 256))
                .sum();
    }

}
class LensBox {
    int boxId;
    Map<String, Integer> labelToFocalLengthMap;

    public LensBox(int boxId) {
        this.boxId = boxId;
        this.labelToFocalLengthMap = new LinkedHashMap<>();
    }
}