package forohub.api.domain.message;

import com.fasterxml.jackson.annotation.JsonBackReference;

import forohub.api.domain.topic.Topic;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "mensajes")
@Entity(name = "Message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenido;
    private LocalDateTime fecha;
    private String autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    @JsonBackReference
    private Topic topic;

    public Message(String contenido, String autor) {
        this.contenido = contenido;
        this.fecha = LocalDateTime.now();
        this.autor = autor;
    }

    public Message(NewMessageData newMessageData) {
    }
}
