import cache.CacheBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        CacheBean<String> cacheBean = new CacheBean<String>(20) {
            @Override
            public String getValue() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String date_str = sdf.format(new Date());
                return date_str;
            }
        };

        for (int i = 0; i < 30; ++i) {
//            Thread.sleep(1000);
            Logger.getAnonymousLogger().log(Level.INFO, cacheBean.getCache() + " " + cacheBean.getIndex());
            cacheBean.refresh();
        }
        System.currentTimeMillis();

    }
}
