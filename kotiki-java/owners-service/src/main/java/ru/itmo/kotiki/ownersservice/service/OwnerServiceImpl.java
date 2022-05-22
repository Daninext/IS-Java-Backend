package ru.itmo.kotiki.ownersservice.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.kotiki.servicedata.dao.OwnerDAO;
import ru.itmo.kotiki.servicedata.entity.Owner;
import ru.itmo.kotiki.servicedata.transfer.OwnerBuffer;
import ru.itmo.kotiki.servicedata.transfer.OwnerTransfer;

import java.util.ArrayList;
import java.util.List;

@EnableKafka
@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {

    private final OwnerDAO ownerRepository;

    @Autowired
    public OwnerServiceImpl(OwnerDAO ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @KafkaListener(topics="addOwner", groupId = "owner", containerFactory = "ownerKafkaListenerContainerFactory")
    public void add(ConsumerRecord<Integer, OwnerBuffer> record) {
        ownerRepository.save(new Owner(record.value().getOwners().get(0)));
    }

    @KafkaListener(topics="updateOwner", groupId = "owner", containerFactory = "ownerKafkaListenerContainerFactory")
    public boolean update(ConsumerRecord<Integer, OwnerBuffer> record) {
        if (ownerRepository.existsById(record.value().getOwners().get(0).getId())) {
            Owner oldOwner = ownerRepository.getById(record.value().getOwners().get(0).getId());
            oldOwner.copy(new Owner(record.value().getOwners().get(0)));
            ownerRepository.save(oldOwner);
            return true;
        }
        return false;
    }

    @KafkaListener(topics="getOwnerById", groupId = "owner", containerFactory = "ownerKafkaListenerContainerFactory")
    @SendTo("resultOwner")
    public OwnerBuffer getById(ConsumerRecord<String, OwnerBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation) {
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlation);
        System.out.println(record.value().getOwners().get(0).getId());
        return new OwnerBuffer(new OwnerTransfer(ownerRepository.getById(record.value().getOwners().get(0).getId())));
    }

    @KafkaListener(topics="getAllOwners", groupId = "owner", containerFactory = "ownerKafkaListenerContainerFactory")
    @SendTo("resultOwner")
    public OwnerBuffer getAll(ConsumerRecord<String, OwnerBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation) {
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlation);
        List<OwnerTransfer> temp = new ArrayList<>();
        ownerRepository.findAll().forEach(owner -> temp.add(new OwnerTransfer(owner)));
        return new OwnerBuffer(temp);
    }

    @KafkaListener(topics="removeOwner", groupId = "owner", containerFactory = "ownerKafkaListenerContainerFactory")
    public boolean remove(ConsumerRecord<String, OwnerBuffer> record) {
        int id = record.value().getOwners().get(0).getId();
        if (ownerRepository.existsById(id)) {
            ownerRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
