package com.unic.lab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by fridolin.jackstadt on 06/04/2017.
 */
public class Chat {

    private final ChatRepliesRepository repository;
    private final Map<ChatParticipant, Sender> participants = new HashMap<>();
    private final Sender allSender = reply -> participants.values().forEach(reply::sendTo);

    public Chat(ChatRepliesRepository repository) {
        this.repository = repository;
    }

    void subscribe(ChatParticipant participant, Sender sender) {
        participants.put(participant, sender);
    }

    void sendReply(ChatReply reply) {
        reply.getTo().map(participants::get).orElse(allSender).send(reply);
        repository.save(reply);
    }

    List<ChatReply> getReplies(ChatParticipant participant) {
        return repository.getReplies().stream().filter(participant::isRecipient).collect(Collectors.toList());
    }

}
