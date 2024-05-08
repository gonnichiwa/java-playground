import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.*;

public class StreamTest {
    // gonnichiwa.github.io/posts/java-stream
    String[] arr;
    HashMap<Integer, String> data;
    List<String> names;

    @Before
    public void given(){
        arr = new String[]{"Java", "Scala", "Groovy", "Python", "Go", "Swift"};

        data = new HashMap<>();
        data.put(1, "Java");
        data.put(2, "Scala");
        data.put(3, "Groovy");
        data.put(4, "Python");
        data.put(5, "Go");
        data.put(6, "Swift");

        names = Arrays.asList("Eric", "Elena", "Tom");
    }

    @Test
    public void arrayStream(){
        Stream<String> stream = Arrays.stream(arr);
        // 가공
        stream.map(String::toUpperCase)
                .forEach(x -> System.out.print(x + " ")); // JAVA SCALA GROOVY PYTHON GO SWIFT SCALA GROOVY

        Arrays.stream(arr, 1, 3)
                .map(String::toUpperCase)
                .forEach(x -> System.out.print(x + " ")); // SCALA[1] GROOVY[2]
    }

    /*
    * 스트림 연결하기
    * */
    @Test
    public void 스트림_연결하기() {
        Stream<String> stream1 = Stream.of("Java", "Scala", "Groovy");
        Stream<String> stream2 = Stream.of("Python", "Go", "Swift");
        Stream<String> concat = Stream.concat(stream1, stream2);
        // print
        concat.forEach(System.out::println); // [Java, Scala, Groovy, Python, Go, Swift]
    }

    /*
      가공하기
     */
    @Test
    public void filteringList(){
        Stream<String> stream = names.stream().filter(name -> name.contains("a"));
        // print
        stream.forEach(System.out::println); // Elena
    }

    @Test
    public void 스트림_값추가(){
        Stream<String> builderStream =
                Stream.<String>builder()
                        .add("Eric").add("Elena").add("Java")
                        .build();
        // print
        builderStream.forEach(System.out::println); // [Eric, Elena, Java]
    }

    @Test
    public void limit으로_stream_생성(){
        Stream<String> generatedStream =
                Stream.generate(() -> "gen").limit(5);

        // print
        generatedStream.forEach(System.out::println); // [gen, gen, gen, gen, gen]
    }

    @Test
    public void iterate_stream(){
        Stream<Integer> iteratedStream =
                Stream.iterate(30, n -> n + 2).limit(5);
        // print
        iteratedStream.forEach(System.out::println); // [30, 32, 34, 36, 38]
    }

    @Test
    public void primitiveWrapperStream(){
        IntStream intStream = IntStream.range(1,5);
        LongStream longStream = LongStream.rangeClosed(1L,5L);
        DoubleStream doubleStream = new Random().doubles(3);
        // print
        intStream.forEach(System.out::println); // 1,2,3,4
        longStream.forEach(System.out::println); // 1L,2L,3L,4L,5L
        doubleStream.forEach(System.out::println);

//        Stream<Integer> integStream = intStream.boxed(); // java.lang.IllegalStateException: stream has already been operated upon or closed
        Stream<Integer> integerStream = IntStream.range(1,5).boxed();
        integerStream.forEach(System.out::println);
    }

    @Test
    public void stream_재사용(){
        // List로 데이터 가지고 있다가
        List<String> list = Stream.of("Eric", "Elena", "Java")
                .filter(name -> name.contains("a"))
                .collect(Collectors.toList());
        // stream 생성해서 바로 씀.
        Optional<String> firstElement = list.stream().findFirst();
        Optional<String> anyElement = list.stream().findAny();

        firstElement.ifPresent(name -> System.out.printf("firstElement=%s \n", name)); // firstElement=Elena
        anyElement.ifPresent(name -> System.out.printf("anyElement=%s", name)); // anyElement=Elena
    }

    @Test
    public void 정규식_문자열_split_and_stream(){
        Stream<String> strStream = Pattern.compile(", ").splitAsStream("Eric, Elena, Java");
        // print
        strStream.forEach(System.out::println); // [Eric, Elena, Java]
    }

    @Test
    public void streamOf(){
        long countUnder5 = Stream.of(1,2,3,4,5,6,7).map(x -> x < 5).count();
        System.out.println(countUnder5);
    }

    @Test
    public void reduceTestOne(){
        int sum = Stream.of(1, 2, 3, 4, 5)
                .reduce((a, b) -> a + b + 1)
                .get();
        /*
        * 4 ==  (a=1  + b=2) + 1
        * 8 ==  (a=4  + b=3) + 1
        * 13 == (a=8  + b=4) + 1
        * 19 == (a=13 + b=5) + 1
        * */
        System.out.println(sum); // 19 (4->8->13->19)
    }

    @Test
    public void reduceTestTwo(){
        int sum = Stream.of(1, 2, 3, 4, 5)
                .reduce(10, (a, b) -> a + b + 1);
        /*
        * 12 == (a=10 + b=1) + 1
        * 15 == (a=12 + b=2) + 1
        * 19 == (a=15 + b=3) + 1
        * 24 == (a=19 + b=4) + 1
        * 30 == (a=24 + b=5) + 1
        * */
        System.out.println(sum); // 30
    }

    @Test
    public void reduceTestThree(){
        int sum = Stream.of(1, 2, 3, 4, 5)
                .parallel()
                .reduce(0
                        , (a, b) -> a + b
                        , (a, b) -> {
                            System.out.printf("combiner %d, %d was called: %d %n", a, b, (a+b));
                            return a + b;
                        });
        // 초기값 0 : 15 (0+1) 1, (1+2) 3, (3+3) 6, (6+4) 10, (10+5) 15
        System.out.println(sum);
    }

    @Test
    public void lazyInvocationTest() {
        List<String> list = Arrays.asList("Eric", "Elena", "Java");
        // 스트림 정의만 했을 때.
        Stream<String> stream = list.stream()
                .filter(el -> {
                    Counter.wasCalled();
                    return el.contains("a");
                });
        // .filter() 수행없음
        System.out.println(Counter.counter); // 0

        // collect()로 정의한 stream 동작
        List<String> data = stream.collect(Collectors.toList());
        // .filter() 동작확인
        System.out.println(Counter.counter); // 3
    }
}

class Counter {
    public static long counter = 0;
    public static void wasCalled() {
        counter++;
    }
}
