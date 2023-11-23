package org.dromara.akali.test.util;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import org.dromara.akali.util.SegmentLock;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 分段锁测试
 */
public class SegmentLockTest {

    @Test
    public void testUnLock() {
        String lockId = "lockId";
        SegmentLock lock = new SegmentLock(4);
        StringBuffer strBuffer = new StringBuffer();
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(() -> {
            strBuffer.append("A");
            lock.lockInterruptibleSafe(lockId);
            ThreadUtil.safeSleep(200);
            strBuffer.append("AA");
            lock.unlock(lockId);
        });

        pool.execute(() -> {
            strBuffer.append("B");
            lock.lockInterruptibleSafe(lockId);
            strBuffer.append("BB");
        });

        ThreadUtil.safeSleep(1000);
        Assert.equals("ABAABB", strBuffer.toString());
    }
}
