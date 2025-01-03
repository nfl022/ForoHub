package forohub.api.controller;


import forohub.api.domain.message.ListMessageData;
import forohub.api.domain.message.NewMessageData;
import forohub.api.domain.topic.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Tópicos", description = "Operaciones relacionadas con los tópicos")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private PagedResourcesAssembler<ListTopicData> pagedResourcesAssembler;



    @PostMapping
    @Operation(summary = "Crea un nuevo tópico", description = "Permite la creación de un nuevo tópico")
    public ResponseEntity<TopicRegisterData> registrarTopico(
            @Valid @RequestBody TopicRegisterData topicRegisterData,
            UriComponentsBuilder uriComponentsBuilder) {

        Topic topic = topicService.registrarTopico(topicRegisterData);

        URI uri = uriComponentsBuilder.path("/topicos/{id}")
                .buildAndExpand(topic.getId())
                .toUri();

        return ResponseEntity.created(uri).body(topicRegisterData);
    }



    @GetMapping
    @Operation(summary = "Obtiene la lista de tópicos", description = "Devuelve una lista de todos los tópicos existentes")
    public ResponseEntity<PagedModel<EntityModel<ListTopicData>>> listadoTopicos(
            @PageableDefault(size = 10, sort = "fecha", direction = Sort.Direction.ASC) Pageable paginacion) {

        // Obtener la página de DTO ListTopicData desde el servicio
        Page<ListTopicData> topicosPage = topicService.listarTopicos(paginacion);

        // Convertir la página de DTO ListTopicData a un modelo paginado de recursos usando el servicio
        PagedModel<EntityModel<ListTopicData>> pagedModel = topicService.convertirAPagedModel(topicosPage,
                pagedResourcesAssembler, paginacion);

        return ResponseEntity.ok(pagedModel);
    }



    @GetMapping("/buscar")
    @Operation(summary = "Buscar tópicos por course",
            description = "Busca los tópicos existentes referentes a un course en específico.")
    public ResponseEntity<PagedModel<EntityModel<ListTopicData>>> buscarTopicosPorCurso(
            @Parameter(description = "Nombre del course", required = true)
            @RequestParam(name = "course") String nombreCurso,
            @Parameter(description = "Información de paginación y ordenamiento")
            @PageableDefault(size = 10, sort = "fecha", direction = Sort.Direction.ASC) Pageable paginacion) {

        // Mapear la página de tópicos a una página de datos listados de tópicos
        Page<ListTopicData> datosListadoTopicoPage = topicService.buscarTopicosPorCurso(nombreCurso, paginacion);

        // Convertir la página de DTO ListTopicData a un modelo paginado de recursos usando el servicio
        PagedModel<EntityModel<ListTopicData>> pagedModel = topicService.convertirAPagedModel(datosListadoTopicoPage,
                pagedResourcesAssembler, paginacion);

        // Convertir la página de datos listados de tópicos a un modelo paginado de EntityModel
        return ResponseEntity.ok(pagedModel);
    }



    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un tópico por ID", description = "Devuelve un tópico específico basado en su ID")
    public ResponseEntity<EntityModel<Topic>> buscarDetalleTopicoPorId(
            @Parameter(description = "ID del tópico a obtener", required = true) @PathVariable Long id) {
        Optional<Topic> optionalTopico = topicService.buscarTopicoPorId(id);

        // Si el Optional de Topic tiene un valor presente, obtén el objeto Topic y devuelve una respuesta OK con sus datos encapsulados en EntityModel.
        if (optionalTopico.isPresent()) {
            Topic topic = optionalTopico.get();
            return ResponseEntity.ok(EntityModel.of(topic));
        } else {
            // Si no se encuentra un Topic con el ID proporcionado, devuelve una respuesta HTTP 404 (Not Found).
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un tópico existente", description = "Permite la actualización de un tópico basado en su ID")
    public ResponseEntity <ListMessageData>actualizarTopico(
            @Parameter(description = "ID del tópico a actualizar", required = true) @PathVariable Long id,
            @Valid @RequestBody TopicUpdateData topicUpdateData){

        // Llamar al servicio para actualizar el tópico
        topicService.actualizarTopico(id, topicUpdateData);

        // Obtener los datos del último mensaje
        ListMessageData datosUltimoMensaje = topicService.obtenerUltimoMensaje(id);

        // Devolver respuesta con los datos del último mensaje
        return ResponseEntity.ok(datosUltimoMensaje);
    }



    @PostMapping("/{id}/mensajes")
    @Operation(summary = "Agregar un nuevo mensaje a un tópico",
            description = "Agrega un nuevo mensaje a un tópico existente.")
    public ResponseEntity<ListMessageData> agregarMensaje(
            @Parameter(description = "Identificador del tópico al cual se añadirá el mensaje", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos del nuevo mensaje a agregar", required = true)
            @Valid @RequestBody NewMessageData newMessageData) {
        ListMessageData nuevoMensaje = topicService.agregarMensaje(id, newMessageData);
        return ResponseEntity.ok().body(nuevoMensaje);
    }



    @DeleteMapping("/{id}")
    @Operation(summary = "Cerrar un tópico", description = "Permite la eliminación lógica de un tópico basado en su ID")
    @Transactional
    public ResponseEntity<String> cerrarTopico(
            @Parameter(description = "ID del tópico a eliminar", required = true) @PathVariable Long id) {
        topicService.cerrarTopico(id);
        return ResponseEntity.ok("Tópico cerrado exitosamente");
    }



    @DeleteMapping("/{idTopico}/mensajes/{idMensaje}")
    @Operation(summary = "Elimina un mensaje de un tópico",
            description = "Elimina definitivamente un mensaje de un tópico basado en los identificadores del tópico y del mensaje.")
    public ResponseEntity<String> eliminarMensaje(
            @Parameter(description = "Identificador del tópico que contiene el mensaje", required = true)
            @PathVariable Long idTopico,
            @Parameter(description = "Identificador del mensaje a eliminar", required = true)
            @PathVariable Long idMensaje) {
        topicService.eliminarMensaje(idTopico, idMensaje);
        return ResponseEntity.ok("Message eliminado exitosamente");
    }
}
