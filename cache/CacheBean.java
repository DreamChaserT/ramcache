package cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CacheBean<T> {
    List<T> cacheList = new ArrayList<>();

    private int index = 0;
    private int MAXSIZE = 2;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public CacheBean(int maxsize) {
        if (maxsize < 2) {
            maxsize = 2;
        }

        this.index = 0;
        this.MAXSIZE = maxsize;
        loadValue(0);
    }

    public abstract T getValue();

    public synchronized boolean loadValue() {
        final int nextIndex = (index + 1) % MAXSIZE;
        return loadValue(nextIndex);
    }

    private boolean loadValue(int index) {
        boolean res = false;
        try {
            int nextIndex = index;
            T data = getValue();

            if (cacheList.size() < MAXSIZE) {
                for (int i = cacheList.size(); i < MAXSIZE; ++i) {
                    cacheList.add(data);
                }
            }

            cacheList.set(nextIndex, data);
            res = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger("CacheBean loadValue").log(Level.WARNING, ex.getMessage());
        }
        return res;
    }

    public boolean switchIndex() {
        boolean res = false;
        lock.writeLock().lock();
        try {
            index = (index + 1) % MAXSIZE;
            res = true;
        } catch (Exception ex) {
            Logger.getLogger("CacheBean").log(Level.WARNING, ex.getMessage());
        } finally {
            lock.writeLock().lock();
        }
        return res;
    }

    public T getCache() {
        T res = null;
        lock.readLock().lock();
        try {
            res = cacheList.get(index);
        } finally {
            lock.readLock().unlock();
        }
        return res;
    }

    public boolean refresh() {
        boolean res = false;
        try {
            loadValue();
            switchIndex();
            res = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    //=============debug==============//

    public int getIndex() {
        return index;
    }

    //=============debug==============//

}
