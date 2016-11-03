package chapter05.atomic.array;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongArray;

public class AtomicLongArrayTest {

    /**
     * 方法基本和AtomicIntegerArray类似，请参看AtomicIntegerArrayTest测试类的说明以及使用范例
     */
    public final static AtomicLongArray ATOMIC_LONG_ARRAY = new AtomicLongArray(new long[]{11l, 22l, 33l});

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[30];
        for (int i = 0; i < 30; i++) {
            Thread thread = createThread(i);
            threads[i]=thread;
        }
        joinThread(threads);
        System.out.println("其他线程完事儿了,开始主线程");
        printResult();
        System.out.println("主线程搞完了");
    }

    //创建30个线程
    private static Thread createThread(int i) {
        final int index = (i + 1) % 3;
        final long step = Long.valueOf(index) + 1;
        Thread thread = new Thread(() -> {
            randomSleep5s();//随机睡5秒
            long result = ATOMIC_LONG_ARRAY.getAndAdd(index, step);
            System.out.println("线程编号:" + Thread.currentThread().getName() + " 修改的下标:" + index + "增量:" + step + " 修改前的值:" + result + " 修改后的值:" + ATOMIC_LONG_ARRAY.get(index));
        }, "lwkthread-" + i);
        thread.start();
        return thread;
    }

    private static void joinThread(Thread[] threads) throws InterruptedException {
        for (int m = 0; m <threads.length ; m++) {
            threads[m].join();
        }
    }

    private static void printResult() {
        for (int j = 0; j < ATOMIC_LONG_ARRAY.length(); j++) {
            System.out.println("最后结果:" + ATOMIC_LONG_ARRAY.get(j));
        }
    }

    private static void randomSleep5s() {
        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt(5));
        } catch (Exception e) {
            System.out.println("睡觉报错了:" + e);
        }
    }
}
