package com.dailystudy.backend.migration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "migracao.imagens", name = "habilitada", havingValue = "true")
class MigracaoImagensRunner implements ApplicationRunner {

    private final MigracaoExecutor usuarioExecutor;
    private final MigracaoPostExecutor postExecutor;
    private final boolean aplicar;

    MigracaoImagensRunner(MigracaoExecutor usuarioExecutor, MigracaoPostExecutor postExecutor, @Value("${migracao.imagens.aplicar:false}") boolean aplicar) {
        this.usuarioExecutor = usuarioExecutor;
        this.postExecutor = postExecutor;
        this.aplicar = aplicar;
    }

    @Override
    public void run(ApplicationArguments args) {
        usuarioExecutor.migrarUsuarios(aplicar);
        postExecutor.migrarPosts(aplicar);
    }
}
