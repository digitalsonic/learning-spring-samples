package learning.spring.binarytea.support;

import com.alibaba.druid.filter.AutoLoad;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
@AutoLoad
public class ConnectionConnectFilter extends FilterEventAdapter {
    @Override
    public void connection_connectBefore(FilterChain chain, Properties info) {
        log.info("Trying to create a new Connection.");
        super.connection_connectBefore(chain, info);
    }

    @Override
    public void connection_connectAfter(ConnectionProxy connection) {
        super.connection_connectAfter(connection);
        log.info("We have a new connected Connection.");
    }
}
