package usac.api.services;

@org.springframework.stereotype.Service
public class B64Service extends Service{
    

    //En esta funcion validamos que la cadena de string tenga el inicio
    //data:image/png;base64, para que el navegador pueda interpretar la imagen
    public boolean hasExtension(String base64) {
        return base64.startsWith("data:image");
    }

    //En esta funcion agregamos el inicio data:image/png;base64, a la cadena de string
    public String addExtension(String base64) {
        return "data:image/png;base64," + base64;
    }
}
