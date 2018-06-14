package com.canary.finance.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnowflakeDistributedIdRepository {
	private final static Logger LOGGER = LoggerFactory.getLogger(SnowflakeDistributedIdRepository.class);
	private final static long TWEPOCH = 1288834974657L;
	private final static long DATA_CENTER_ID_BITS = 5L;
    private final static long WORKER_ID_BITS = 5L;
    private final static long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);
    private final static long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
    private final static long SEQUENCE_BITS = 12L;
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private final static long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    private final static long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);  
    private final long workerId;
    private final long dataCenterId;
    private long lastTimestamp = -1L; 
    private long sequence = 0L;
    
    public SnowflakeDistributedIdRepository(long workerId, long dataCenterId) {
    	if (workerId > MAX_WORKER_ID || workerId < 0) { 
            throw new IllegalArgumentException(String.format("%s must range from %d to %d", workerId, 0, MAX_WORKER_ID));  
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("%s must range from %d to %d", dataCenterId, 0, MAX_DATA_CENTER_ID));  
        }

        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }
    
    public synchronized long nextValue() throws Exception {
    	long timestamp = currentTimeMillis();
        if(timestamp < this.lastTimestamp) {
        	LOGGER.error(String.format("Clock moved backwards.Refusing to generate id for %d milliseconds", (this.lastTimestamp-timestamp)));
            throw new Exception(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", (this.lastTimestamp-timestamp)));
        }  

        if(this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence+1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        this.lastTimestamp = timestamp;
          
        // 64 Bit ID (42(Millis)+5(Data Center ID)+5(Machine ID)+12(Repeat Sequence Summation))
        long nextId = ((timestamp-TWEPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (this.dataCenterId << DATA_CENTER_ID_SHIFT)
                | (this.workerId << WORKER_ID_SHIFT) | this.sequence;

        return nextId;  
    }
 
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = this.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = this.currentTimeMillis();
        }
        return timestamp;
    }
 
    private long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
