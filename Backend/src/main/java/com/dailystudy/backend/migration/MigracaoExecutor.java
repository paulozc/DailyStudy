package com.dailystudy.backend.migration;

import com.dailystudy.backend.model.Usuario;
import com.dailystudy.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
            Optional<String> novaFoto = processarCampo(usuario.getImg_perfil(), aplicar, relatorio);
            Optional<String> novoBanner = processarCampo(usuario.getBanner_perfil(), aplicar, relatorio);

            novaFoto.ifPresent(usuario::setImg_perfil);
            novoBanner.ifPresent(usuario::setBanner_perfil);

            if (novaFoto.isPresent() || novoBanner.isPresent()){
                usuarioRepository.save(usuario);
            }
        }
        return relatorio;
    }

    private Optional<String> processarCampo(String valor, boolean aplicar, MigracaoRelatorio relatorio){
        if (!imagemService.precisaMigrar(valor)) {
            relatorio.registrarIgnorado();
            return Optional.empty();
        } if (!aplicar) {
            imagemService.validar(valor);
            relatorio.registrarMigrado();
            return Optional.empty();
        }

        String url = imagemService.migrar(valor).orElseThrow();
        relatorio.registrarMigrado();
        return Optional.of(url);
    }
}
