package com.sheledon.transport.netty.client;

import io.netty.channel.Channel;

/**
 * @author Sheledon
 * @date 2022/4/7
 */
public class HealthState {
    public enum State{
        //    死亡状态
        RED(10),
        //   亚健康状态
        YELLOW(5),
        //    健康状态
        GREEN(0);
        int number;
        State(int number) {
            this.number = number;
        }
    }

    private final Channel channel;

    private int channelState;

    public HealthState(Channel channel) {
        this.channel = channel;
        this.channelState = 0;
    }

    public int getChannelState(){
        return channelState;
    }

    public void changeState(int channelState){
        this.channelState = channelState;
    }

    public int incrementAndGetState(){
        this.channelState++;
        return this.channelState;
    }

    public Channel getChannel(){
        return channel;
    }

}
