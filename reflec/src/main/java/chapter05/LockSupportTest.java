package chapter05;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liwenke on 16/4/21.
 */
public class LockSupportTest {

    Object o = new Object();

    public static void main(String[] args) throws Exception {


//        new LockSupportTest().testWait();//wait被打断
       // new LockSupportTest().testSleep();//sleep被打断
//        new LockSupportTest().testSupportPark();//park控制线程阻塞和唤醒
        new LockSupportTest().testmd();//生产者消费者通讯

    }

    ReentrantLock reentrantLock = new ReentrantLock();
    Condition condition = reentrantLock.newCondition();

    private void testmd() throws Exception {
        LockSupportTest lockSupportTest = new LockSupportTest();
        new Thread(() -> {
            try {
                lockSupportTest.testConditionProduct();
            } catch (Exception e) {
                System.out.println("生成者被打断");
            }
        }).start();

        new Thread(() -> {
            try {
                lockSupportTest.testConditionConsumer();
            } catch (Exception e) {
                System.out.println("消费者被打断");
            }
        }).start();
    }

    //测试wait的方法可以被interrupt
    public void testWait() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread t1 = new Thread(() -> {
            countDownLatch.countDown();
            System.out.println("我等待");
            try {
                System.out.println("要wait了");
                synchronized (o) {
                    o.wait();//wait方法只能在同步代码中使用
                }
            } catch (InterruptedException e) {
                System.out.println("wait被打断");
            }
            System.out.println("我被interrupt唤醒了开始工作....");
        });
        t1.start();
        System.out.println("主线程等待");
        countDownLatch.await();
        System.out.println("主线程开始工作");
        randomSleep5s(5);//主线程睡觉
        t1.interrupt();
    }

    public void testSleep() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread t1 = new Thread(() -> {

            countDownLatch.countDown();
            System.out.println("我等待");
            System.out.println("要睡觉了");
            randomSleep5s(1000000);
            System.out.println("我被interrupt唤醒了开始工作....");
        });
        t1.start();
        System.out.println("主线程等待");
        countDownLatch.await();
        System.out.println("主线程开始工作");
        randomSleep5s(5);
        t1.interrupt();

    }

    public void testSupportPark() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread t1 = new Thread(() -> {
            countDownLatch.countDown();
            System.out.println("我等待LockSupport.park");
            LockSupport.park();
            System.out.println("我被LockSupport.unpark唤醒了开始工作....");
        });
        t1.start();
        System.out.println("主线程等待");
        countDownLatch.await();
        System.out.println("主线程开始工作");
        randomSleep5s(5);
        LockSupport.unpark(t1);
    }

    private static void randomSleep5s(int n) {
        try {
            TimeUnit.SECONDS.sleep(n);//睡觉可以被interrupt唤醒,唤醒后会进入异常catch
        } catch (InterruptedException e) {
            System.out.println("睡觉报错了InterruptedException:" + e);
        }
    }

    private Queue<String> queue = new ArrayBlockingQueue<String>(100);
    private volatile int n = 0;

    public void testConditionProduct() throws Exception {
        while (true) {
            try {
                reentrantLock.lock(); //为什么2个线程可以同时lock 我那个擦
                System.out.println("生产上完锁了");
                if (queue.isEmpty()) {
                    System.out.println("继续放");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("a");
                    stringBuilder.append(n++);
                    randomSleep5s(2);
                    stringBuilder.append("b");
                    queue.add(stringBuilder.toString());
                    condition.signal();
                } else {
                    System.out.println("生产睡觉");
                    condition.await();
                }
            } catch (InterruptedException e) {
                System.out.println("我出异常了...." + e);
            } finally {
                System.out.println("生产释放");
                System.out.println();
                reentrantLock.unlock();
            }
        }
    }

    public void testConditionConsumer() throws Exception {
        while (true) {
            try {
                reentrantLock.lock();
                System.out.println("消费者上完锁");
                if (queue.isEmpty()) {
                    System.out.println("没有数据了");
                    condition.await();//通知用signal(信号),不是notify 我擦....
                } else {
                    System.out.println("取出数据" + queue.poll());
                    condition.signal();
                }

            } catch (Exception e) {
                System.out.println("消费者出异常了...." + e);
            } finally {
                System.out.println("消费者释放锁....");
                reentrantLock.unlock();
            }
        }
    }
}
