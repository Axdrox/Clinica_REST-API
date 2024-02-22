package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.DatosListadoMedico;
import med.voll.api.medico.DatosRegistroMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
     *
     * @return: findAll(): Regresa un listado, porque se extiende de la clase de JpaRepository
     * Se tiene que cumplir el tipo de retorno de DatosListadoMedico, pero como regresa
     * una entidad "Medico", se utiliza el método stream() y que cree un Médico utilizando
     * los datos de DatosListadoMedico.
     */
    @GetMapping
    public List<DatosListadoMedico> listadoMedicos(){
        return medicoRepository.findAll().stream().map(DatosListadoMedico::new).toList();
    }
}
