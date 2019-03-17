package com.taotao.ordertimer.service.impl;

import com.taotao.ordertimer.service.OrderAlreadyEvaluationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderAlreadyEvaluationServiceImpl implements OrderAlreadyEvaluationService {
    @Value("${SERVER.PORT}")
    private Integer serverPort;
}
