package forohub.api.domain.topic;


import forohub.api.controller.TopicController;
import forohub.api.domain.message.ListMessageData;
import forohub.api.domain.message.NewMessageData;
import forohub.api.domain.Course;
import forohub.api.domain.message.Message;
import forohub.api.domain.message.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private MensajeRepository mensajeRepository;



    public Topic registrarTopico(TopicRegisterData topicRegisterData) {

        if (topicoRepository.existsByTituloAndMensajes_contenido(topicRegisterData.titulo(), topicRegisterData.mensaje())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tópico ya existe.");
        }
        Topic nuevoTopic = new Topic(topicRegisterData);
        return topicoRepository.save(nuevoTopic);
    }



    public Page<ListTopicData> listarTopicos(Pageable paginacion) {
        return topicoRepository.findAllActive(paginacion).map(ListTopicData::new);
    }



    public PagedModel<EntityModel<ListTopicData>> convertirAPagedModel(Page<ListTopicData> topicosPage,
                                                                       PagedResourcesAssembler<ListTopicData> pagedResourcesAssembler,
                                                                       Pageable paginacion) {
        return pagedResourcesAssembler.toModel(topicosPage,
                topico -> EntityModel.of(topico,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TopicController.class)
                                .listadoTopicos(paginacion)).withSelfRel()));
    }



    public Page<ListTopicData> buscarTopicosPorCurso(String nombreCurso, Pageable paginacion) {
        Course course;
        try {

            course = Course.valueOf(nombreCurso.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course inválido");
        }
        return topicoRepository.findByCursoAndStatusNotClosed(course, paginacion).map(ListTopicData::new);
    }



    public Optional<Topic> buscarTopicoPorId(Long id) {

        return topicoRepository.findById(id);
    }



    @Transactional
    public void actualizarTopico(Long id, TopicUpdateData topicUpdateData) {

        Topic topic = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));
        topic.actualizarTopico(topicUpdateData);
        topicoRepository.save(topic);
    }


    public ListMessageData obtenerUltimoMensaje(Long id) {

        Topic topic = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));
        List<Message> messages = topic.getMessages();
        if (!messages.isEmpty()) {
            Message ultimoMessage = messages.get(messages.size() - 1);
            return new ListMessageData(
                    ultimoMessage.getId(),
                    ultimoMessage.getContenido(),
                    ultimoMessage.getFecha(),
                    ultimoMessage.getAutor()
            );
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay messages en el tópico");
        }
    }



    @Transactional
    public ListMessageData agregarMensaje(Long id, NewMessageData newMessageData) {

        Topic topic = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));
        Message nuevoMessage = new Message(newMessageData);
        topic.agregarMensaje(nuevoMessage);
        topicoRepository.save(topic);
        return new ListMessageData(nuevoMessage);
    }



    @Transactional
    public void cerrarTopico(Long id) {
        Topic topic = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));
        topic.cerrarTopico();
        topicoRepository.save(topic);
    }



    @Transactional
    public void eliminarMensaje(Long idTopico, Long idMensaje) {

        Topic topic = topicoRepository.findById(idTopico)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));
        Message message = topic.getMessages().stream()
                .filter(m -> m.getId().equals(idMensaje))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message no encontrado"));
        topic.getMessages().remove(message);
        topicoRepository.save(topic);
        mensajeRepository.deleteById(idMensaje);
    }
}
