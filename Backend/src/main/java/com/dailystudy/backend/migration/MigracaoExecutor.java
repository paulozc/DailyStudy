package com.dailystudy.backend.migration;

import com.dailystudy.backend.model.Usuario;
import com.dailystudy.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

@Component
public class MigracaoExecutor {

    private final UsuarioRepository usuarioRepository;
    private final ImagemMigracaoService imagemService;

    public MigracaoExecutor(UsuarioRepository usuarioRepository, ImagemMigracaoService imagemService) {
        this.usuarioRepository = usuarioRepository;
        this.imagemService = imagemService;
    }

    public MigracaoRelatorio migrarUsuarios(boolean aplicar) {

        MigracaoRelatorio relatorio = new MigracaoRelatorio();

        for (Usuario usuario : usuarioRepository.findAll()) {
            processarCampo(usuario.getImg_perfil(), relatorio);
            processarCampo(usuario.getBanner_perfil(), relatorio);
        }
        return relatorio;
    }

    public void processarCampo(String valor, MigracaoRelatorio relatorio){
        if (imagemService.precisaMigrar(valor)) {
            imagemService.validar(valor);
            relatorio.registrarMigrado();
        } else {
            relatorio.registrarIgnorado();
        }
    }
}
