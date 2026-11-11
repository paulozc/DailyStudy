package com.dailystudy.backend.migration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MigracaoImagensRunnerTest {

    @Mock
    MigracaoExecutor usuarioExecutor;
    @Mock
    MigracaoPostExecutor postExecutor;

    @Test
    void semAplicar_rodaSimulacaoNosDois() throws Exception {
        when(usuarioExecutor.migrarUsuarios(false)).thenReturn(new MigracaoRelatorio());
        when(postExecutor.migrarPosts(false)).thenReturn(new MigracaoRelatorio());

        new MigracaoImagensRunner(usuarioExecutor, postExecutor, false)
                .run(new DefaultApplicationArguments());

        verify(usuarioExecutor).migrarUsuarios(false);
        verify(postExecutor).migrarPosts(false);
        verify(usuarioExecutor, never()).migrarUsuarios(true);
        verify(postExecutor, never()).migrarPosts(true);
    }

    @Test
    void comAplicar_gravaNosDois() throws Exception {
        when(usuarioExecutor.migrarUsuarios(true)).thenReturn(new MigracaoRelatorio());
        when(postExecutor.migrarPosts(true)).thenReturn(new MigracaoRelatorio());

        new MigracaoImagensRunner(usuarioExecutor, postExecutor, true)
                .run(new DefaultApplicationArguments());

        verify(usuarioExecutor).migrarUsuarios(true);
        verify(postExecutor).migrarPosts(true);
    }

    @Test
    void falhaNosUsuarios_propagaENaoComecaOsPosts() {
        when(usuarioExecutor.migrarUsuarios(true))
                .thenThrow(new IllegalStateException("bucket indisponível"));

        MigracaoImagensRunner runner = new MigracaoImagensRunner(usuarioExecutor, postExecutor, true);

        assertThrows(IllegalStateException.class,
                () -> runner.run(new DefaultApplicationArguments()));
        verifyNoInteractions(postExecutor);
    }
}
