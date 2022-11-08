package team.caltech.olmago.order.service.message.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.caltech.olmago.common.message.MessageEnvelope;
import team.caltech.olmago.common.message.MessageEnvelopeRepository;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageStoreImpl implements MessageStore {
  private final MessageEnvelopeRepository messageEnvelopeRepository;
  
  @Transactional
  @Override
  public void saveMessage(MessageEnvelope msg) {
    messageEnvelopeRepository.save(msg);
  }
  
  @Transactional
  @Override
  public void saveMessage(List<MessageEnvelope> msgs) {
    messageEnvelopeRepository.saveAll(msgs);
  }
}
