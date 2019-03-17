package com.taotao.jedis;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.HostAndPort;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class HostAndPortSet extends HashSet<HostAndPort> {
    private String hostAndPorts;

    public void setHostAndPorts(String hostAndPorts) {
        this.hostAndPorts = hostAndPorts;
    }

    @PostConstruct
    public void addAllHostAndPort() {
        this.addAll(Arrays.stream(hostAndPorts.split(",")).map(hostAndPort -> {
            String[] split = hostAndPort.split(":");
            int port = 6379;
            if (StringUtils.isNotBlank(split[1])) {
                port = Integer.valueOf(split[1]);
            }
            return new HostAndPort(split[0], port);
        }).collect(Collectors.toSet()));
    }
}
