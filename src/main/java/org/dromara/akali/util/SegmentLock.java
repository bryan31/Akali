package org.dromara.akali.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 分段锁
 */
public class SegmentLock {
    private final int mask;
    private final Lock[] locks;
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    public SegmentLock(int concurrency) {

        int size = formatSize(concurrency);
        mask = size - 1;
        locks = new Lock[size];
        for (int i = 0; i < size; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    /**
     * 阻塞获取锁，可被打断
     *
     * @param lockStr 锁资源
     * @throws InterruptedException 线程被中断异常
     */
    public void lockInterruptible(String lockStr) throws InterruptedException {
        int lockId = lockStr.hashCode();
        Lock lock = locks[lockId & mask];
        lock.lockInterruptibly();
    }

    /**
     * 阻塞获取锁，可被打断
     *
     * @param lockStr 锁资源
     */
    public void lockInterruptibleSafe(String lockStr) {
        try {
            lockInterruptible(lockStr);
        } catch (InterruptedException ignore) {
            // 忽略
        }
    }

    /**
     * 释放锁
     *
     * @param lockStr 锁资源
     */
    public void unlock(String lockStr) {
        int lockId = lockStr.hashCode();
        Lock lock = locks[lockId & mask];
        lock.unlock();
    }

    /**
     * 将大小格式化为 2的N次
     *
     * @param cap 初始大小
     * @return 格式化后的大小，2的N次
     */
    private int formatSize(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
