package com.sheledon.transport.netty.client;

import io.netty.channel.Channel;
import io.netty.util.internal.ConcurrentSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通道健康管理器
 * @author Sheledon
 * @date 2022/4/7
 */
@Slf4j
@Component
public class HealthManager {

    private final Map<Channel,HealthState> healthStateMap = new ConcurrentHashMap<>();
    private final Set<Channel> greenChannelSet = new ConcurrentSet<>();
    private final Set<Channel> yellowChannelSet = new ConcurrentSet<>();
    public void addChannel(Channel channel){
        healthStateMap.put(channel,new HealthState(channel));
        setGreen(channel);
    }
    public void incrementState(Channel channel){
        HealthState state = healthStateMap.computeIfAbsent(channel, HealthState::new);
        int stateNum = state.incrementAndGetState();
        System.out.println("state is: "+stateNum);
        if (stateNum == HealthState.State.GREEN.number){
            setGreen(channel);
        } else if (stateNum >= HealthState.State.RED.number){
            setRed(channel);
        }else {
            setYellow(channel);
        }
    }
    public void changeChannelState(Channel channel,HealthState.State state){
        HealthState healthState = healthStateMap.computeIfAbsent(channel, HealthState::new);
        healthState.changeState(state.number);

    }
    private void setYellow(Channel channel){
        log.warn("channel state become yellow : {}",channel.remoteAddress());
        greenChannelSet.remove(channel);
        yellowChannelSet.add(channel);
    }
    private void setRed(Channel channel){
        log.warn("channel state become red : {}",channel.remoteAddress());
        greenChannelSet.remove(channel);
        yellowChannelSet.remove(channel);
        healthStateMap.remove(channel);
        channel.close();
    }
    private void setGreen(Channel channel){
        log.info("channel state become green : {}",channel.remoteAddress());
        yellowChannelSet.remove(channel);
        greenChannelSet.add(channel);
    }
    public boolean isGreen(Channel channel){
        return greenChannelSet.contains(channel);
    }

}
