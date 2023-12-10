import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DayEight {
    public static void main(String[] args) {
        String data = FileUtils.readFileToString("resources/network.txt");
        System.out.println(getNumberOfStepsToDest(data));
    }

    private static int getNumberOfStepsToDest(String data) {
        String[] dataGroups = parseDirectionsAndNetwork(data);
        String directions = dataGroups[0];
        Map<String, List<String>> networkMap = parseInputIntoMap(dataGroups[1]);

        return numOfStepsFromSourceToDest(networkMap,"AAA", "ZZZ", directions);
    }
    private static String[] parseDirectionsAndNetwork(String data) {
        return Pattern.compile("\n\n").split(data);
    }
    private static Map<String, List<String>> parseInputIntoMap(String networkData) {
        List<String> nodeMappings = List.of(networkData.split("\\n"));

        Pattern pattern = Pattern.compile("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");

        return nodeMappings.stream()
                .map(pattern::matcher)
                .filter(Matcher::find)
                .collect(Collectors.toMap(
                        matcher -> matcher.group(1),
                        matcher -> List.of(matcher.group(2), matcher.group(3))));
    }
    
    private static int numOfStepsFromSourceToDest(Map<String,List<String>> network, 
                                                  String source, 
                                                  String destination, 
                                                  String directions) {
        int steps = directions.length();
        int count = 0;

        while(!destination.equals(source)) {
            count++;
            source  = getDestinationNode(network,source,directions);
        }

        return count * steps;
    }

        private static String getDestinationNode(Map<String,List<String>> network, String source, String directions) {

        for(char dir : directions.toCharArray()) {
            if(dir == 'L') {
                source = network.get(source).get(0);
            } else if(dir == 'R') {
                source = network.get(source).get(1);

            }
            else {
                throw new IllegalArgumentException("Direction not possible");
            }
        }
        return source;
    }
}
