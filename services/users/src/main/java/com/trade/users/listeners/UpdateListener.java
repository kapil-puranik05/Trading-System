package com.trade.users.listeners;

import com.trade.users.dtos.WalletUpdateDTO;
import com.trade.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class UpdateListener {
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @KafkaListener(topics = "wallet-updates", groupId = "${KAFKA_GROUP_ID}")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println(record.key());
        System.out.println(record.value());
        WalletUpdateDTO walletUpdate = objectMapper.readValue(record.value(), WalletUpdateDTO.class);
        userService.updateWallet(walletUpdate);
    }
}
