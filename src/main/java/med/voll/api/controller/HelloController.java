package med.voll.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping//Para mapear la ruta del RequestMapping
    public static String helloWorld(){
        return "Te amo Karlita preciosa!!!";
    }
}