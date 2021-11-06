package com.github.th997.orderserver.client;

import com.github.th997.userserver.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user-server", contextId = "time-test")
public interface UserServiceClient extends UserService {
}
