package forohub.api.controller;


import forohub.api.domain.user.NewUseData;
import forohub.api.domain.user.User;
import forohub.api.domain.message.UsuerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Usuarios", description = "Endpoints para la gestión de usuarios, incluyendo registro y consulta.")
@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UsuerService usuerService;

    /**
     * Registra un nuevo usuario.
     *
     * @param newUseData Datos del nuevo usuario a registrar.
     * @return ResponseEntity<User> con los detalles del usuario registrado.
     */
    @PostMapping("/registro")
    @Operation(summary = "Registrar un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema.")
    public ResponseEntity<User> registrarUsuario(
            @Parameter(description = "Datos del nuevo usuario a registrar", required = true)
            @Valid @RequestBody NewUseData newUseData) {
        User userCreado = usuerService.registrarNuevoUsuario(newUseData);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreado);
    }
}
