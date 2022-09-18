package com.sheledon.transport.netty.handle;

import com.sheledon.common.netty.constants.RpcMessageType;
import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.transport.netty.RpcSession;
import com.sheledon.transport.netty.client.HealthManager;
import com.sheledon.transport.netty.client.HealthState;
import io.netty.channel.Channel;


/**
 * @author Sheledon
 * @date 2021/11/12
 * @Version 1.0
 */
public class RpcHeartBeatResponseStrategy implements MessageStrategy{
    private HealthManager healthManager;

    public RpcHeartBeatResponseStrategy(HealthManager healthManager) {
        this.healthManager = healthManager;
    }

    @Override
    public RpcMessage handle(RpcSession session) {
        Channel channel = session.getChannelContext().channel();
        if (!healthManager.isGreen(channel)){
            healthManager.changeChannelState(channel, HealthState.State.GREEN);
        }
        return null;
    }

}
