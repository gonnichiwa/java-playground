import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
        List<String> list = new ArrayList<>();

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

    @Test
    public void sortT(){
        int[] intArr = {2,4,3,1,5};
        Arrays.sort(intArr);
        List<Integer> l = Arrays.stream(intArr).boxed().collect(Collectors.toList());
        assertThat(l.get(0), is(1));
    }

    @Test
    public void intStreamP(){
        int answer = 0;
        int n = 10;
        boolean isEven = (n % 2) == 0;
        if(isEven) {
            answer = IntStream.rangeClosed(1,n).filter(num -> num % 2 == 0).map(m -> m*m).sum();
        } else { // 홀수
            answer = IntStream.rangeClosed(1,n).filter(num -> num % 2 == 1).sum();
        }
        System.out.println(answer);
    }

    @Test
    public void solutionT() {
        int[] arr = {0, 1, 2, 4, 3};
        int[][] queries = {{0, 4, 2}, {0, 3, 2}, {0, 2, 2}};
        /*
        * 첫 번째 쿼리의 범위에는 0, 1, 2, 4, 3이 있으며 이 중 2보다 크면서 가장 작은 값은 3입니다.
          두 번째 쿼리의 범위에는 0, 1, 2, 4가 있으며 이 중 2보다 크면서 가장 작은 값은 4입니다.
          세 번째 쿼리의 범위에는 0, 1, 2가 있으며 여기에는 2보다 큰 값이 없습니다.
            따라서 [3, 4, -1]을 return 합니다.
        * */
        int[] result = solution2(arr, queries);
    }
    /*문제 설명
    모든 자연수 x에 대해서 현재 값이 x이면 x가 짝수일 때는 2로 나누고, x가 홀수일 때는 3 * x + 1로 바꾸는 계산을 계속해서
    반복하면 언젠가는 반드시 x가 1이 되는지 묻는 문제를 콜라츠 문제라고 부릅니다.
    그리고 위 과정에서 거쳐간 모든 수를 기록한 수열을 콜라츠 수열이라고 부릅니다.
    계산 결과 1,000 보다 작거나 같은 수에 대해서는 전부 언젠가 1에 도달한다는 것이 알려져 있습니다.
    임의의 1,000 보다 작거나 같은 양의 정수 n이 주어질 때 초기값이 n인 콜라츠 수열을 return 하는 solution 함수를 완성해 주세요.
    */
    @Test
    public void solution181919() {
        int[] result = solution181919(10);
        for(int i: result){
            System.out.print(i + " ");
        }
    }
    public int[] solution181919(int n) {
        List<Integer> l = new ArrayList<>();
        int x = n;
        while(x != 1) {
            l.add(x);
            if(x % 2 == 0){
                x = x / 2;
            } else {
                x = 3 * x + 1;
            }
        }
        return l.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    public void test181918(){
        int[] given = new int[]{1,4,2,5,3};
        int[] result = solution181918_stack(given);
        for(int i : result){
            System.out.print(i + " ");
        }
    }


    public int[] solution2(int[] arr, int[][] queries) {
        int[] answer = {};
        return IntStream.range(0, queries.length)
                .map(i -> IntStream.rangeClosed(queries[i][0], queries[i][1])
                                    .map(a -> arr[a])
                                    .filter(a -> a > queries[i][2])
                                    .min().orElse(-1)
                ).toArray();
    }

    @Test
    public void solution11_Test(){
        /*문제 설명
        https://school.programmers.co.kr/learn/courses/30/lessons/181921

            정수 l과 r이 주어졌을 때, l 이상 r이하의 정수 중에서 숫자 "0"과 "5"로만 이루어진 모든 정수를 오름차순으로 저장한 배열을 return 하는 solution 함수를 완성해 주세요.

            만약 그러한 정수가 없다면, -1이 담긴 배열을 return 합니다.

            제한사항
            1 ≤ l ≤ r ≤ 1,000,000
            입출력 예
            l	r	result
            5	555	[5, 50, 55, 500, 505, 550, 555]
            10	20	[-1]
            입출력 예 설명
            입출력 예 #1

            5 이상 555 이하의 0과 5로만 이루어진 정수는 작은 수부터 5, 50, 55, 500, 505, 550, 555가 있습니다. 따라서 [5, 50, 55, 500, 505, 550, 555]를 return 합니다.
            입출력 예 #2

            10 이상 20 이하이면서 0과 5로만 이루어진 정수는 없습니다. 따라서 [-1]을 return 합니다. */
        int[] result = solution11_stream(5,555);
        for(int i : result){
            System.out.print(i + " ");
        }
    }
    public int[] solution20(int start_num, int end_num) {
        int[] answer = {};
        answer = IntStream.rangeClosed(start_num,end_num).toArray();
        return answer;
    }

    public int[] solution11(int l, int r) {
        List<Integer> answer = new ArrayList<>();
        for(int i=l; i <=r; i++){
            String a = String.valueOf(i);
            boolean ok = false;
            for(char n : a.toCharArray()){
                ok = n == '5' || n == '0';
                if (!ok) break;
            }
            if(ok) answer.add(i);
        }
        if(answer.size() == 0) {
            return new int[]{-1};
        } else {
            return answer.stream().mapToInt(a -> a).sorted().toArray();
        }
    }

    public int[] solution11_stream(int l, int r) {
        List<Integer> filtered = IntStream.rangeClosed(l, r)
                .filter(num -> String.valueOf(num).chars().allMatch(ch -> ch == '0' || ch == '5'))
                .boxed()
                .collect(Collectors.toList());
        return filtered.isEmpty() ? new int[] {-1} : filtered.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    public void solution10Test() {
        /*문제 설명
        https://school.programmers.co.kr/learn/courses/30/lessons/181922

            정수 배열 arr와 2차원 정수 배열 queries이 주어집니다. queries의 원소는 각각 하나의 query를 나타내며, [s, e, k] 꼴입니다.

            각 query마다 순서대로 s ≤ i ≤ e인 모든 i에 대해 i가 k의 배수이면 arr[i]에 1을 더합니다.

            위 규칙에 따라 queries를 처리한 이후의 arr를 return 하는 solution 함수를 완성해 주세요.

            제한사항
            1 ≤ arr의 길이 ≤ 1,000
            0 ≤ arr의 원소 ≤ 1,000,000
            1 ≤ queries의 길이 ≤ 1,000
            0 ≤ s ≤ e < arr의 길이
            0 ≤ k ≤ 5
            입출력 예
            arr	queries	result
            [0, 1, 2, 4, 3]	[[0, 4, 1],[0, 3, 2],[0, 3, 3]]	[3, 2, 4, 6, 4]
            입출력 예 설명
            입출력 예 #1

            각 쿼리에 따라 arr가 다음과 같이 변합니다.
            arr
            [0, 1, 2, 4, 3]
            [1, 2, 3, 5, 4]
            [2, 2, 4, 5, 4]
            [3, 2, 4, 6, 4]
            따라서 [3, 2, 4, 6, 4]를 return 합니다.
          */
            int[] arr = new int[]{0,1,2,4,3};
            int[][] queries = new int[][]{{0,4,1},{0,3,2},{0,3,3}};
            int[] result = solution10(arr, queries);
            for(int a : result){
                System.out.print(a +" ");
            }
    }


    public int[] solution10(int[] arr, int[][] queries) {
        // init
        int[] answer = Arrays.copyOf(arr, arr.length);
        for(int q = 0; q < queries.length; q++){
            int s = queries[q][0];
            int e = queries[q][1];
            int k = queries[q][2];
            for(int i = s; i <= e; i++){
                answer[i] = i % k == 0 ? answer[i]+1 : answer[i];
            }
        }
        return answer;
    }

    public int[] solution(int[] arr, int[][] queries) {
        // init
        int[] answer = new int[queries.length];
        Arrays.fill(answer, Integer.MAX_VALUE);
        // set
        for(int i = 0; i < queries.length; i++) {
            int s = queries[i][0];
            int e = queries[i][1];
            int k = queries[i][2];
            // cal
            for(int j = s; j <= e; j++){
                if(arr[j] > k) {
                    answer[i] = Math.min(answer[i], arr[j]);
                }
            }
            if(answer[i] == Integer.MAX_VALUE) answer[i] = -1;
        }
        return answer;
    }

    @Test
    public void forTest(){
        int[][] arr = new int[][]{{1,2,3},{4,5,6},{7,8,9}};
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[i].length; j++) {
                boolean ok = arr[i][j] % 2 == 0;
                if(ok) break;
            }
            System.out.println("kk");
        }
        System.out.println("ok");
    }

    @Test
    public void collectionTest(){
        int[] arr = {4,3,5,2,3,6};
        List<Integer> nums = Arrays.stream(arr).boxed().collect(Collectors.toList());
        Collections.swap(nums, 1,2);
        System.out.print(nums); // [4, 5, 3, 2, 3, 6]
    }

    @Test
    public void st(){
        String ineq = ">";
        String eq = "!";
        int a = 10;
        int b = 20;
        String exp = ineq + (eq.equals("!")? "" : eq);
        System.out.println(a + exp + b);

        char i = 'a';
        String abc = "abc";
        String answer = abc + i;
        System.out.println(answer);
    }

    @Test
    public void mapTest(){
        Map<Integer, Integer> m = new HashMap<>();
        m.put(1,1);
        m.put(2,3);
        m.put(3,1);
    }

    @Test
    public void stringUpperLower(){
        String ab = "adsgsg";

        char a = 'a';
        int result = (int) a;

        if(result >= 97 && result <= 122) {
            // lowercase
            System.out.println("lowercase");
        } else {
            // uppercase
            System.out.println("uppercase");
        }

    }

    @Test
    public void streamArrayTest(){
        int[] num_list = {2,1,6};
        int n = 3;
        // result : [6]
        int[] result = Arrays.stream(num_list).filter(num -> num >= n).toArray();

    }

    /*
0 65  A     26  97  a
1 66  B     27  98  b
2 67  C         99  c
3 68  D        100  d
4 69  E        101  e
  70  F        102  f
  71  G        103  g
  72  H        104  h
  73  I        105  i
74  J        106  j
75  K        107  k
76  L        108  l
77  M        109  m
78  N        110  n
79  O        111  o
80  P        112  p
81  Q        113  q
82  R        114  r
83  S        115  s
84  T        116  t
85  U        117  u
86  V        118  v
87  W        119  w
88  X        120  x
89  Y        121  y
90  Z        122  z
    * */

    public int[] solution181918_stack(int[] arr){
        Stack<Integer> s = new Stack<>();
        int i = 0;
        while(i < arr.length){
            if(s.isEmpty()){
                s.add(arr[i]);
                i += 1;
            } else if(!s.isEmpty() && s.peek() < arr[i]){
                s.add(arr[i]);
                i += 1;
            } else if(!s.isEmpty() && s.peek() >= arr[i]){
                s.pop();
            }
        }
        return s.stream().mapToInt(Integer::intValue).toArray();
    }

    public int[] solution181918(int[] arr) {
        List<Integer> l = new ArrayList<>();
        int i = 0;
        while(i < arr.length){
            if(l.size() == 0){
                l.add(arr[i]);
                i += 1;
            }
            else if(l.size() > 0 && l.get(l.size()-1) < arr[i]) {
                l.add(arr[i]);
                i += 1;
            }
            else if(l.size() > 0 && l.get(l.size()-1) >= arr[i]){
                l.remove(l.size()-1);
            }
        }
        return l.stream().mapToInt(Integer::intValue).toArray();
    }

//    public int[] solution(int n) {
//        return IntStream.concat(
//                        IntStream.iterate(n, i -> i > 1, i -> i % 2 == 0 ? i / 2 : i * 3 + 1),
//                        IntStream.of(1))
//                .toArray();
//    }

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
        System.out.println(countUnder5); // 7
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

    @Test
    public void collectors(){
        String[] str = {"a","b","c"};
        String result = Arrays.stream(str).collect(Collectors.joining());
        String result2 = String.join("1", str);
        assertThat(result, is("abc"));
        assertThat(result2, is("a1b1c"));
    }

    @Test
    public void intToString(){
        Integer a = 9;
        Integer b = 91;

        System.out.println(a.toString() + b.toString());
    }

    @Test
    public void maxNum(){
        int[] numbers = new int[7]; // {0,0,1,1,2,0,0}
        // param (4,4,3,2)
        numbers[4] += 1;
        numbers[4] += 1;
        numbers[3] += 1;
        numbers[2] += 1;
        int maxnum = Arrays.stream(numbers).max().getAsInt();
        boolean contains = Arrays.stream(numbers).anyMatch(num -> num == 1);
        System.out.println(maxnum);
        System.out.println(contains);
    }

    @Test
    public void solution_181916() {
        /*
        * https://school.programmers.co.kr/learn/courses/30/lessons/181916
        * 문제 설명
        1부터 6까지 숫자가 적힌 주사위가 네 개 있습니다. 네 주사위를 굴렸을 때 나온 숫자에 따라 다음과 같은 점수를 얻습니다.

        네 주사위에서 나온 숫자가 모두 p로 같다면 1111 × p점을 얻습니다.
        세 주사위에서 나온 숫자가 p로 같고 나머지 다른 주사위에서 나온 숫자가 q(p ≠ q)라면 (10 × p + q)2 점을 얻습니다.
        주사위가 두 개씩 같은 값이 나오고, 나온 숫자를 각각 p, q(p ≠ q)라고 한다면 (p + q) × |p - q|점을 얻습니다.
        어느 두 주사위에서 나온 숫자가 p로 같고 나머지 두 주사위에서 나온 숫자가 각각 p와 다른 q, r(q ≠ r)이라면 q × r점을 얻습니다.
        네 주사위에 적힌 숫자가 모두 다르다면 나온 숫자 중 가장 작은 숫자 만큼의 점수를 얻습니다.
        네 주사위를 굴렸을 때 나온 숫자가 정수 매개변수 a, b, c, d로 주어질 때, 얻는 점수를 return 하는 solution 함수를 작성해 주세요.

        제한사항
        a, b, c, d는 1 이상 6 이하의 정수입니다.
        입출력 예
        a	b	c	d	result
        2	2	2	2	2222
        4	1	4	4	1681
        6	3	3	6	27
        2	5	2	6	30
        6	4	2	5	2
        입출력 예 설명
        입출력 예 #1

        예제 1번에서 네 주사위 숫자가 모두 2로 같으므로 1111 × 2 = 2222점을 얻습니다. 따라서 2222를 return 합니다.
        입출력 예 #2

        예제 2번에서 세 주사위에서 나온 숫자가 4로 같고 나머지 다른 주사위에서 나온 숫자가 1이므로 (10 × 4 + 1)2 = 412 = 1681점을 얻습니다. 따라서 1681을 return 합니다.
        입출력 예 #3

        예제 3번에서 a, d는 6으로, b, c는 3으로 각각 같으므로 (6 + 3) × |6 - 3| = 9 × 3 = 27점을 얻습니다. 따라서 27을 return 합니다.
        입출력 예 #4

        예제 4번에서 두 주사위에서 2가 나오고 나머지 다른 두 주사위에서 각각 5, 6이 나왔으므로 5 × 6 = 30점을 얻습니다. 따라서 30을 return 합니다.
        입출력 예 #5

        예제 5번에서 네 주사위 숫자가 모두 다르고 나온 숫자 중 가장 작은 숫자가 2이므로 2점을 얻습니다. 따라서 2를 return 합니다.
        * */
        assertThat(solution181916(2,2,2,2), is(2222));
        assertThat(solution181916(4,1,4,4), is(1681));
        assertThat(solution181916(6,3,3,6), is(27));
        assertThat(solution181916(2,5,2,6), is(30));
        assertThat(solution181916(6,4,2,5), is(2));
    }

    public int solution181916(int a, int b, int c, int d) {
        int[] counts = new int[7];
        counts[a] += 1;
        counts[b] += 1;
        counts[c] += 1;
        counts[d] += 1;
        int maxNum = Arrays.stream(counts).max().getAsInt();
        int answer = 0;
        if(maxNum == 4) { // 같은 주사위 숫자 4개일 때
            return 1111 * a;
        } else if (maxNum == 3) { // 같은 숫자 3개 일 때
            int p=0, q=0;
            for(int i = 0; i < counts.length; i++) {
                if(counts[i] == 3) { p = i; }
                if(counts[i] == 1) { q = i; }
            }
            return (int) Math.pow(10*p + q, 2);
        } else if (maxNum == 2) { // 같은 숫자 2개일 때
            int p = 0, q = 0;
            boolean twoOneOne = Arrays.stream(counts).anyMatch(num -> num == 1); // 같은 숫자 2개, 나머지 2개 다른경우
            if(twoOneOne) {
                answer = 1;
                for(int i = 0; i < counts.length; i++) {
                    if(counts[i] == 1) answer *= i;
                }
                return answer;
            } else {
                // 같은 숫자 2개씩 나온 경우
                for(int i = 0; i < counts.length; i++){
                    if(counts[i] == 2) {
                        if(p == 0) {
                            p = i;
                        } else {
                            q = i;
                        }
                    }
                }
                return (p + q) * Math.abs(p - q);
            }
        } else {
            // 각각 다 다른 숫자일 경우
            return Arrays.stream(new int[]{a,b,c,d}).min().getAsInt();
        }
    }

    @Test
    public void page(){
        String[] printPages = getPages(179,191);
        for(String p : printPages) {
            System.out.println(p);
        }
    }

    private String[] getPages(int start, int end) {
        StringJoiner front = new StringJoiner(",");
        StringJoiner back = new StringJoiner(",");
        IntStream.rangeClosed(start,end).forEach(i -> {
            if (i % 2 == 0) {
                front.add(""+i);
            } else {
                back.add(""+i);
            }
        });
        return new String[]{front.toString(), back.toString()};
    }

    @Test
    public void stringMulyiplyTest(){
        String[] pictures = {".xx...xx.", "x..x.x..x", "x...x...x", ".x.....x.", "..x...x..", "...x.x...", "....x...."};
        solution(pictures, 2);
    }

    public String[] solution(String[] picture, int k) {
        String[] result = new String[picture.length*k];
        StringBuilder picM = new StringBuilder();

        for(int i = 0; i < picture.length; i++) {
            String p = picture[i];
            picM.setLength(0);
            for(char a : p.toCharArray()){
                for(int j = 1; j <= k; j++){
                    picM.append(a);
                }
            }

            for(int m = i*k; m < (i*k)+k; m++) {
                result[m] = picM.toString();
            }
        }

        return result;
    }


}

class Counter {
    public static long counter = 0;
    public static void wasCalled() {
        counter++;
    }
}
