import com.at.userapi.dto.UsuarioDTOInput;
import com.at.userapi.service.UsuarioService;
import org.junit.Assert;
import org.junit.Test;

public final class ServiceTest {
    private final UsuarioService usuarioService = new UsuarioService();

    @Test
    public void testUsuarioService() {
        UsuarioDTOInput usuarioInput = new UsuarioDTOInput();
        usuarioInput.setId(1);
        usuarioInput.setNome("Teste");
        usuarioInput.setSenha("123");

        usuarioService.inserir(usuarioInput);
        Assert.assertEquals(1, usuarioService.listar().size());
    }
}