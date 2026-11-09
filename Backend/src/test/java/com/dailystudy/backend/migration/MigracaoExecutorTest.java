package com.dailystudy.backend.migration;

import com.dailystudy.backend.model.Usuario;
import com.dailystudy.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MigracaoExecutorTest {

    private static final String PNG_1X1 =
            "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==";

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ImagemMigracaoService imagemService;

    @InjectMocks
    private MigracaoExecutor executor;

    @Test
    void migrarUsuarios_simulacao_validaMasNaoSobeNemGrava() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setImg_perfil(PNG_1X1);
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(imagemService.precisaMigrar(PNG_1X1)).thenReturn(true);
        when(imagemService.precisaMigrar(null)).thenReturn(false);

        MigracaoRelatorio relatorio = executor.migrarUsuarios(false);

        assertEquals(1, relatorio.getMigrados());

        verify(imagemService).validar(PNG_1X1);
        verify(imagemService, never()).migrar(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void migrarUsuarios_camposVaziosOuJaMigrados_saoContadosComoIgnorados() {
        Usuario usuario = new Usuario();
        usuario.setId(2L);

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(imagemService.precisaMigrar(null)).thenReturn(false);

        MigracaoRelatorio relatorio = executor.migrarUsuarios(false);

        assertEquals(0, relatorio.getMigrados());
        assertEquals(2, relatorio.getIgnorados());
    }
}
