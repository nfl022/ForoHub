package forohub.api.domain.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository <User, Long>{
    UserDetails findByEmail(String username);
}
