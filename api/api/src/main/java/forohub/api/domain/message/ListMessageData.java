package forohub.api.domain.message;



import java.time.LocalDateTime;

public record ListMessageData(Long id,
                              String contenido,
                              LocalDateTime fecha,
                              String autor) {

    public ListMessageData(Message message) {
        this(message.getId(), // Asigna el id directamente
             message.getContenido(),
             message.getFecha(),
             message.getAutor());
    }

    public ListMessageData(Long id, String contenido, LocalDateTime fecha, String autor) {
        this.id = id;
        this.contenido = contenido;
        this.fecha = fecha;
        this.autor = autor;
    }
}
