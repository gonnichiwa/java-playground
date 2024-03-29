import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ThreadPoolTest {
    @Test
    public void blockingServerThreadPool() throws InterruptedException {
        final int numOfThreads = 18;
        ExecutorService service = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        BlockingServer blockingServer = new BlockingServer();
        iterateThread(numOfThreads, service, latch, blockingServer);

        assertThat(((ThreadPoolExecutor) service).getPoolSize(), is(5));
    }

    private void iterateThread(final int numOfThreads,
                               ExecutorService service,
                               CountDownLatch latch,
                               BlockingServer server) throws InterruptedException {
        for(int i = 0; i < numOfThreads; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    server.run();
                    latch.countDown();
                    throw new IllegalArgumentException();
                }
            });
        }
        latch.await();
        System.out.println("iterateThread() end");
    }

    @Test
    public void counter() {
        MyCounter counter = new MyCounter();
        counter.increment();
        assertThat(counter.getCount(), is(1));
    }

}

class BlockingServer implements Runnable {
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(8888);
            System.out.println("접속 대기중");
            while (true) {
                Socket sock = socket.accept();
                System.out.println("클라이언트 연결완료");

                OutputStream out = sock.getOutputStream();
                InputStream in = sock.getInputStream();

                while(true){
                    try {
                        int request = in.read();
                        out.write(request);
                    } catch (IOException e){
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

class MyCounter {
    private int count;

    public void increment() {
        try {
            int temp = count;
            count = temp + 1;
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    public int getCount() {
        return count;
    }
}