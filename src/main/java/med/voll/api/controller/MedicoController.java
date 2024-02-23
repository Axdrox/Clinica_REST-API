package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    //RequestBody: para indicar que se están recibiendo datos en el body
    public void registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico) {
        medicoRepository.save(new Medico(datosRegistroMedico));
    }

    /**
     * @return: findAll(): Regresa un listado, porque se extiende de la clase de JpaRepository
     * Se tiene que cumplir el tipo de retorno de DatosListadoMedico, pero como regresa
     * una entidad "Medico", se utiliza el método stream() y que cree un Médico utilizando
     * los datos de DatosListadoMedico.
     *
     * findByActivoTrue: La búsqueda en el repositorio la realiza por el parámetro activo.
     * es una nomenclatura de SpringData (SpringJPA), para crear queries dinámicas y hacer el
     * WHERE en el SELECT.
     *
     */
    @GetMapping
    public Page<DatosListadoMedico> listadoMedicos(@PageableDefault(size = 2, sort = "nombre") Pageable paginacion) {
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
        return medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new);
    }

    /**
     * Se deben actualizar únicamente los datos:
     * >> Nombre
     * >> Documento
     * >> Direccion
     * <p>
     * getReferenceById(): porque el cliente está enviando el id, entonces realiza
     * la búsqueda en la base de datos. Este método ya lo ofrece JpaRepository
     *
     * @Transactional: Abre una transacción en la BD, JPA mapea lo que está trayendo
     * de la BD (el id en este caso) y se guarda en la variable "medico" y cuando se
     * actualizan los datos de ese médico, debido a que está dentro de la transacción
     * una vez que el método termina, la transacción también termina: hace commit
     * en la base de datos y los cambios son guardados.
     * +++ Beneficio: Se puede realizar un rollback si es que al realizar la
     * actualización existe una inconsistencia, porque no se ejecuta el commit a la
     * base de datos.
     */
    @PutMapping
    @Transactional //JPA va a mapear que cuando se termine este método, la transacción se va a liberar
    public void actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico) {
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
    }

    /**
     * DELETE LÓGICO
     * @param id
     */
    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarMedico(@PathVariable Long id) {
        // No es necesario un DTO, se le pasa el parámetro indicado en la url (id).
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
    }

/*                  DADO A QUE SE REALIZARÁ UNA ELIMINACIÓN LÓGICA
                    SE UTILIZARÁ EL MÉTODO UPDATE EN VEZ DE DELETE
 */
    /**
     * @param id: Se le está indicando que el id que obtenga en la url
     *            sea el parámetro que está esperando para poder trabajar
     *            con dicha variable, esto gracias a la anotación @PathVariable.
     * "/{id}": Para que sea dinámico.

    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarMedico(@PathVariable Long id) {
        // No es necesario un DTO, se le pasa el parámetro indicado en la url (id).
        Medico medico = medicoRepository.getReferenceById(id);
        medicoRepository.delete(medico);
    }
*/
}
