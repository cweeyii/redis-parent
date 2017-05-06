package com.cweeyii.cache.framework.iterator;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.Iterator;
import java.util.List;


public class ScanIterator implements Iterator {
    protected final static Log log = LogFactory.getLog(ScanIterator.class);
    private ScanResult<String> scanResult;
    private Jedis jedis;
    private ScanParams scanParams;

    public ScanIterator(Jedis jedis, String pattern, int segmentSize) {
        this.jedis = jedis;
        scanParams = new ScanParams();
        scanParams.match(pattern);
        scanParams.count(segmentSize);
        this.scanResult = jedis.scan("0", scanParams);
    }

    @Override
    public boolean hasNext() {
        return scanResult != null && !scanResult.getStringCursor().equals("0")
                && !CollectionUtils.isEmpty(scanResult.getResult());
    }

    @Override
    public Object next() {
        List<String> list = null;
        if (scanResult != null) {
            list = scanResult.getResult();
            scanResult = jedis.scan(scanResult.getStringCursor(), scanParams);
        }
        return list;
    }
}