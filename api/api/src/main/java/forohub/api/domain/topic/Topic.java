package forohub.api.domain.topic;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import forohub.api.domain.Course;
import forohub.api.domain.message.Message;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topic")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Message> messages = new ArrayList<>();

    private LocalDateTime fecha;
    private String status;
    private String autor;

    @Enumerated(EnumType.STRING)
    private Course course;

    public Topic(TopicRegisterData topicRegisterData) {
        this.titulo = topicRegisterData.titulo();
        this.fecha = LocalDateTime.now();
        this.status = "ABIERTO";
        this.autor = topicRegisterData.autor();
        this.course = topicRegisterData.course();
        this.messages = new ArrayList<>();
        Message message = new Message(topicRegisterData.mensaje(), this.autor);
        this.agregarMensaje(message);
    }

    public void agregarMensaje(Message message) {
        messages.add(message);
        message.setTopic(this);
    }

    public void actualizarTopico(TopicUpdateData topicUpdateData) {
        if (topicUpdateData.mensaje() != null) {
            Message message = new Message(topicUpdateData.mensaje(), topicUpdateData.autor());
            this.agregarMensaje(message);
        }
        this.fecha = LocalDateTime.now();
        this.status = "ACTUALIZADO";
    }

    public void cerrarTopico (){
        this.status = "CERRADO";
    }

}
