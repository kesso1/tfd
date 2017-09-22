import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class tdf extends Application {
    @Override public void start(Stage stage) throws IOException {
        stage.setTitle("Word Frequency Hamlet");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> barChart =
                new BarChart<String, Number>(xAxis, yAxis);
        barChart.setTitle("Word Frequency Hamlet");
        xAxis.setLabel("Word");
        yAxis.setLabel("Count");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("WFH");
        Map<String, Long> wordFreq = genWordFreqMap("c://temp//hamlet.txt");
        wordFreq.forEach((k,v)-> series1.getData().add(new XYChart.Data(k, v)));

        Scene scene  = new Scene(barChart,800,600);
        for (XYChart.Series<String,Number> serie: barChart.getData()){
            for (XYChart.Data<String, Number> item: serie.getData()){
                item.getNode().setOnMousePressed((MouseEvent event) -> {
                    System.out.println(item.toString()+serie.toString());
                });
            }
        }
        barChart.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) throws IOException {
        launch(args);
        //genWordFreqMap("c://temp//hamlet.txt");
    }
    public static Map<String, Long> genWordFreqMap(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        Map<String, Long> wordFreq = null;
        try (Stream<String> stream = Stream.of(content.toLowerCase().split("\\W+")).parallel()) {
            wordFreq = stream
                    .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
            wordFreq = sortByValue(wordFreq);
            wordFreq.forEach((k,v)-> System.out.println(k + ":" + v));
            return wordFreq;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                return (e1.getValue()).compareTo(e2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
