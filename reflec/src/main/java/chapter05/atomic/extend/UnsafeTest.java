package chapter05.atomic.extend;

import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;

import sun.misc.Unsafe;

/**
 * Unsafe类提供了硬件级别的原子操作，Java无法直接访问到操作系统底层（如系统硬件等)，
 * 为此Java使用native方法来扩展Java程序的功能。
 * 具体实现使用c++,详见文件sun.misc.natUnsafe.cc();
 */
public class UnsafeTest {
	
	long longV = 2;
	
	int intV1;
	
	int intV2;
	
	String field1 = "123";
	
	String field2;
	
	static String STATIC_FIELD;
	
	static String STATIC_FIELD2;

	public static void main(String []args) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

		Unsafe unsafe = getUnsafe();
		System.out.println("JVM地址宽度：" + unsafe.addressSize());//输出JVM地址宽度
		
		Field field1 = UnsafeTest.class.getDeclaredField("longV");
		Field field2 = UnsafeTest.class.getDeclaredField("intV1");
		Field field3 = UnsafeTest.class.getDeclaredField("intV2");
		Field field4 = UnsafeTest.class.getDeclaredField("field1");
		Field field5 = UnsafeTest.class.getDeclaredField("field2");
		Field staticField = UnsafeTest.class.getDeclaredField("STATIC_FIELD");
		Field staticField2 = UnsafeTest.class.getDeclaredField("STATIC_FIELD2");
		
		System.out.println(unsafe.objectFieldOffset(field1));//内存地址偏移量
		System.out.println(unsafe.objectFieldOffset(field2));
		System.out.println(unsafe.objectFieldOffset(field3));
		System.out.println(unsafe.objectFieldOffset(field4));
		System.out.println(unsafe.objectFieldOffset(field5));

		System.out.println(unsafe.staticFieldOffset(staticField));
		System.out.println(unsafe.staticFieldOffset(staticField2));
		System.out.println(unsafe.getInt(50));


	}

	private static Unsafe getUnsafe() throws IllegalAccessException {
		Field filed = Unsafe.class.getDeclaredFields()[0];
		filed.setAccessible(true);
		Unsafe unsafe = (Unsafe)filed.get(null);
		return unsafe;
	}
}
