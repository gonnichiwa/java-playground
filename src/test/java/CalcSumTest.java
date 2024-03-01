import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CalcSumTest {

    Calculator calculator;
    String numFilePath;

    @Before
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilePath = getClass().getResource("numbers.txt").getPath();
    }
    @Test
    public void sumOfNumbers() throws IOException {
        int sum = this.calculator.calcSum(this.numFilePath);
        assertThat(sum, is(10));
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        assertThat(this.calculator.calcMultiply(this.numFilePath), is(24));
    }

    @Test
    public void concatenateString() throws IOException {
        assertThat(this.calculator.concatenate(this.numFilePath), is("1234"));
    }
}

interface BufferedReaderCallback<T> {
    T doSomethingWithReader(BufferedReader br) throws IOException;
}
interface LineCallback<T> {
    T doSomethingWithLine(String line, T initval);
}
class Calculator {
    protected <T> T lineReadTemplate(BufferedReader br, LineCallback<T> lc, T initVal) throws IOException {
        T sum = initVal;
        String line = null;
        while((line = br.readLine()) != null){
            sum = lc.doSomethingWithLine(line, sum);
        }
        return sum;
    }

    protected <T> T fileReadTemplate(String filePath, BufferedReaderCallback<T> callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            return callback.doSomethingWithReader(br);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    protected Integer calcSum(String filePath) throws IOException {
        return this.fileReadTemplate(filePath, new BufferedReaderCallback<Integer>() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                return lineReadTemplate(br, (line, initval) -> initval + Integer.parseInt(line), 0);
            }
        });
    }

    protected Integer calcMultiply(String filePath) throws IOException {
        return this.fileReadTemplate(filePath,
                br -> lineReadTemplate(br, (line, initval) -> initval * Integer.parseInt(line), 1)
        );
    }

    protected String concatenate(String filePath) throws IOException {
        return this.fileReadTemplate(filePath, new BufferedReaderCallback<String>() {
            @Override
            public String doSomethingWithReader(BufferedReader br) throws IOException {
                return lineReadTemplate(br, new LineCallback<String>() {
                    @Override
                    public String doSomethingWithLine(String line, String initval) {
                        return initval + line;
                    }
                }, "");
            }
        });
    }
}
