package de.kaufland.ksilence.service;

import de.kaufland.ksilence.model.Message;
import de.kaufland.ksilence.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    MessageRepository messageRepository;

    public Iterable<Message> readAll() {
        log.debug("Read all MESSAGES");
        return messageRepository.findAll();
    }
}
