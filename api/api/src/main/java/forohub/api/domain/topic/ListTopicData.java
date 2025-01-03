package forohub.api.domain.topic;



import forohub.api.domain.Course;
import forohub.api.domain.message.ListMessageData;

import java.util.List;
import java.util.stream.Collectors;

public record ListTopicData(
        Long id,
        String titulo,
        List<ListMessageData> mensajes,
        String status,
        Course course) {

    public ListTopicData(Topic topic){
        this(topic.getId(),
                topic.getTitulo(),
                topic.getMessages().stream()
                        .map(ListMessageData::new)
                        .collect(Collectors.toList()),
                topic.getStatus(),
                topic.getCourse());
    }
}
