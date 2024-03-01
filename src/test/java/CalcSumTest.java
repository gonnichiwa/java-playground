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
}

interface BufferedReaderCallback {
    Integer doSomethingWithReader(BufferedReader br) throws IOException;
}
interface LineCallback {
    Integer doSomethingWithLine(String line, int initval);
}
class Calculator {
    protected Integer lineReadTemplate(BufferedReader br, LineCallback lc, int initVal) throws IOException {
        int sum = initVal;
        String line = null;
        while((line = br.readLine()) != null){
            sum = lc.doSomethingWithLine(line, sum);
        }
        return sum;
    };

    protected Integer fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException {
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
        return this.fileReadTemplate(filePath, new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                return lineReadTemplate(br, new LineCallback() {
                    @Override
                    public Integer doSomethingWithLine(String line, int initval) {
                        return initval + Integer.parseInt(line);
                    }
                }, 0);
            }
        });
    }

    protected Integer calcMultiply(String filePath) throws IOException {
        return this.fileReadTemplate(filePath, new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                return lineReadTemplate(br, new LineCallback() {
                    @Override
                    public Integer doSomethingWithLine(String line, int initval) {
                        return initval * Integer.parseInt(line);
                    }
                }, 1);
            }
        });
    }
}
