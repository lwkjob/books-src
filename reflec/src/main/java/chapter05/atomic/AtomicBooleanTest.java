package chapter05.atomic;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 运行结果说明：仅有一个输出：我成功了，说明只有一个线程尝试将boolean值修改成功，其他的都未成功，达到锁的效果
 * @author zhongyin.xy
 *
 */
public class AtomicBooleanTest {

	/**
	 * 主要方法：
	 * @see AtomicBoolean#compareAndSet(boolean, boolean)  第一个参数为原始值，第二个参数为要修改的新值，若修改成功则返回true，否则返回false
	 * @see AtomicBoolean#getAndSet(boolean)   尝试设置新的boolean值，直到成功为止，返回设置前的数据
	 */
	public final static AtomicBoolean TEST_BOOLEAN = new AtomicBoolean();
	
	public static void main(String []args) {
		for(int i = 0 ; i < 10 ; i++) {
			new Thread(() ->{
				randomSleep5s();
				if(TEST_BOOLEAN.compareAndSet(false, true)) {
					System.out.println(Thread.currentThread().getName()+"我成功了！");
				}
			}).start();
		}
	}

	/**
	 * 随机睡5秒
	 */
	private static void randomSleep5s() {
		try {
			Thread.sleep(new Random().nextInt(5000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
} 
