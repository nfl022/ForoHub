package forohub.api.domain.topic;


import forohub.api.domain.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicRegisterData(
        @NotBlank(message = "El título es obligatorio.")
        String titulo,
        @NotBlank(message = "El mensaje es obligatorio.")
        String mensaje,
        @NotBlank(message = "El autor es obligatorio.")
        String autor,
        @NotNull(message = "El course es obligatorio.")
        Course course) {
}
