package forohub.api.domain.topic;


import forohub.api.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicoRepository extends JpaRepository<Topic, Long> {


    boolean existsByTituloAndMensajes_contenido(String titulo, String mensaje);
    @Query("SELECT t FROM Topic t WHERE t.status <> 'CERRADO'")
    Page<Topic> findAllActive(Pageable pageable);
    @Query("SELECT t FROM Topic t WHERE t.course = :course AND t.status <> 'CERRADO'")
    Page<Topic> findByCursoAndStatusNotClosed(@Param("course") Course course, Pageable pageable);
}
