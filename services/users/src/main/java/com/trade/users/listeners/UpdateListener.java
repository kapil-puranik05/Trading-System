package com.trade.users.listeners;

import com.trade.users.dtos.WalletUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class UpdateListener {
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "wallet-updates", groupId = "${KAFKA_GROUP_ID}")
    public void listen(ConsumerRecord<String, String> record) {
        WalletUpdateDTO walletUpdate = objectMapper.readValue(record.value(), WalletUpdateDTO.class);

    }
}
