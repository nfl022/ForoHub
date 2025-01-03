package forohub.api.domain.message;


import forohub.api.domain.user.NewUseData;
import forohub.api.domain.user.User;
import forohub.api.domain.user.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuerService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registrarNuevoUsuario(NewUseData newUseData) {

        User nuevoUser = new User();
        nuevoUser.setNombre(newUseData.nombre());
        nuevoUser.setEmail(newUseData.email());
        nuevoUser.setClave(passwordEncoder.encode(newUseData.clave()));
        return usuarioRepository.save(nuevoUser);
    }
}
