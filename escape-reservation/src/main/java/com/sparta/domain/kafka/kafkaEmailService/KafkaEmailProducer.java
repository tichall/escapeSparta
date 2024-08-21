package com.sparta.domain.kafka.kafkaEmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEmailProducer {

  private final KafkaTemplate<String, String> kafkaEmailTemplate;

  public void sendCreateReservationEmail(String topic, String email) {
    kafkaEmailTemplate.send(topic, email);
  }

  public void sendDeleteReservationEmail(String topic, String email) {
    kafkaEmailTemplate.send(topic, email);
  }
}
