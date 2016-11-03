package chapter05.tools;

import java.util.concurrent.Phaser;

public class PhaserTest_3 {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(3) {
            /**
             *  registeredParties:线程注册的数量
             *  phase:第几次等待,从0开始，
             * */
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("执行onAdvance方法.....;  phase:" + phase + "   registeredParties=" + registeredParties);
                return phase==6;
            }
        };
        for (int i = 0; i < 3; i++) {
            Task_03 task = new Task_03(phaser);
            Thread thread = new Thread(task, "task_" + i);
            thread.start();
        }


        System.out.println("主线程任务已经结束....");
    }

    static class Task_03 implements Runnable {
        private final Phaser phaser;

        public Task_03(Phaser phaser) {
            this.phaser = phaser;
        }

        @Override
        public void run() {
            do {
                System.out.println(Thread.currentThread().getName() + "开始执行11111任务...");
                phaser.arriveAndAwaitAdvance();

                System.out.println(Thread.currentThread().getName() + "开始执行2222222任务...");
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() +"ok走你");
            } while (!phaser.isTerminated());
        }
    }
}