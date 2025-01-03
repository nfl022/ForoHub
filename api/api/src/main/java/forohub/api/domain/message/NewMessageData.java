package forohub.api.domain.message;

import jakarta.validation.constraints.NotBlank;

public record NewMessageData(
        @NotBlank String contenido,
        @NotBlank String autor) {
}
