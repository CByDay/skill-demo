//package cn.example.seckilldemo.utils;
//
///**
// * @Description: 令牌通学习02
// * @Author:
// * @Date: 2023-05-31
// */
//public class TokenBucket2 {
//
//    private final static Object MUTEX = new Object();
//    public double acquire() {
//        return acquire(1);
//    }
//
//    public double acquire(int permits) {
//        checkPermits(permits);  //检查参数是否合法（是否大于0）
//        long microsToWait;
//        synchronized (MUTEX) { //应对并发情况需要同步
//            microsToWait = reserveNextTicket(permits, readSafeMicros()); //获得需要等待的时间
//        }
//        ticker.sleepMicrosUninterruptibly(microsToWait); //等待，当未达到限制时，microsToWait为0
//        return 1.0 * microsToWait / TimeUnit.SECONDS.toMicros(1L);
//    }
//
//    private long reserveNextTicket(double requiredPermits, long nowMicros) {
//        resync(nowMicros); //补充令牌
//        long microsToNextFreeTicket = nextFreeTicketMicros - nowMicros;
//        double storedPermitsToSpend = Math.min(requiredPermits, this.storedPermits); //获取这次请求消耗的令牌数目
//        double freshPermits = requiredPermits - storedPermitsToSpend;
//
//        long waitMicros = storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend)
//                + (long) (freshPermits * stableIntervalMicros);
//
//        this.nextFreeTicketMicros = nextFreeTicketMicros + waitMicros;
//        this.storedPermits -= storedPermitsToSpend; // 减去消耗的令牌
//        return microsToNextFreeTicket;
//    }
//
//    private void resync(long nowMicros) {
//        // if nextFreeTicket is in the past, resync to now
//        if (nowMicros > nextFreeTicketMicros) {
//            storedPermits = Math.min(maxPermits,
//                    storedPermits + (nowMicros - nextFreeTicketMicros) / stableIntervalMicros);
//            nextFreeTicketMicros = nowMicros;
//        }
//    }
//}
