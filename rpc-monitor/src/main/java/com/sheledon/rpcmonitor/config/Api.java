package com.sheledon.rpcmonitor.config;

/**
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
public class Api {
    private static final String BASE_URL="/rpc";
    public static class RpcApplication{
        public static final String BASE_URL = Api.BASE_URL+ "/application";
        public static final String GET_APPLICATION = "/{name}";
    }
    public static class RpcService{
        public static final String BASE_URL = Api.BASE_URL+"/service";
        public static final String SERVICE_INVOKE_COU9NT = "/invokeCount";
        public static final String GET_SERVICE_BY_APPLICATION_NAME = "/{applicationName}";
        public static final String SERVICE_NUMBER="/number";
    }
    public static class RpcLog{
        public static final String BASE_URL = Api.BASE_URL+"/log";
    }
    public static class User{
        public static final String BASE_URL = Api.BASE_URL+"/user";
        public static final String USER_LIST = "/list";
        public static final String DELETE_USER = "/{name}";
    }
}
