package nl.gerimedica.assignment;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for recording hospital-related usage events.
 */
@Slf4j
public final class HospitalUtils {

    // Thread-safe counter for usage tracking (for demonstration)
    private static final AtomicInteger usageCounter = new AtomicInteger(0);

    // Private constructor prevents instantiation
    private HospitalUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Records a usage event with the given context and increments a usage counter.
     * 
     * @param context A description of where/how HospitalUtils is being used
     */
    public static void recordUsage(String context) {
        int currentCount = usageCounter.incrementAndGet();
        log.info("HospitalUtils used. Counter: {} | Context: {}", currentCount, context);
    }

    /**
     * For testing or monitoring: Get current usage count.
     */
    public static int getUsageCount() {
        return usageCounter.get();
    }

    /**
     * For testing: Reset the usage counter.
     */
    public static void resetUsageCount() {
        usageCounter.set(0);
    }
}
