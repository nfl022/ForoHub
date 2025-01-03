package forohub.api.controller;



import forohub.api.domain.user.UserAuthenticationData;
import forohub.api.infra.security.JWTTokenData;
import forohub.api.infra.security.TokenService;
import forohub.api.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Authentication", description = "Endpoints para autenticación de usuarios ya registrados y gestión de tokens JWT.")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;



    @PostMapping()
    @Operation(summary = "Autenticar usuario",
            description = "Autentica a un usuario con sus credenciales y genera un token JWT.")
    public ResponseEntity<?> autenticarUsuario(
            @Parameter(description = "Datos de autenticación del usuario", required = true)
            @RequestBody @Valid UserAuthenticationData userAuthenticationData) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userAuthenticationData.email(),
                    userAuthenticationData.clave()
            );
            var usuarioAutenticado = authenticationManager.authenticate(authenticationToken);
            var JWTtoken = tokenService.generarToken((User) usuarioAutenticado.getPrincipal());
            return ResponseEntity.ok(new JWTTokenData(JWTtoken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }
}
