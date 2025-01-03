package forohub.api.domain.message;


import org.springframework.data.jpa.repository.JpaRepository;

public interface MensajeRepository extends JpaRepository<Message, Long> {
    void deleteById(Long id);
}
