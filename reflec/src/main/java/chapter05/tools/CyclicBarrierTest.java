package chapter05.tools;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

	private final static CyclicBarrier CYCLIC_BARRIER = new CyclicBarrier(
			3, new Runnable() {
				public void run() {
					System.out.println("======>我是导游，本次点名结束，准备走下一个环节!");
				}
			});

	public static void main(String[] args) throws InterruptedException,
			BrokenBarrierException {
		for (int i = 0; i < 3; i++) {
			new Thread(String.valueOf(i)) {
				public void run() {
					try {
						System.out.println("我是线程：" + this.getName() + " 到达111111！");
						CYCLIC_BARRIER.await();
						System.out.println("我是线程：" + this.getName() + " 到达222222！");
						CYCLIC_BARRIER.await();
						System.out.println("我是线程：" + this.getName() + " 到达333333！");
						CYCLIC_BARRIER.await();
						System.out.println("我是线程：" + this.getName() + " 到达555555！");
						CYCLIC_BARRIER.await();
						System.out.println("我是线程：" + this.getName() + " 到达666666！");
						CYCLIC_BARRIER.await();
						System.out.println("我是线程：" + this.getName() + " 到达777777！");
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
}