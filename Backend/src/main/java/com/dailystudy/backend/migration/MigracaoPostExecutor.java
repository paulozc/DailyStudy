package com.dailystudy.backend.migration;

import com.dailystudy.backend.exception.InvalidImageException;
import com.dailystudy.backend.model.Post;
import com.dailystudy.backend.repository.PostRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MigracaoPostExecutor {

    private final PostRepository postRepository;
    private final ImagemMigracaoService imagemService;

    public MigracaoPostExecutor(PostRepository postRepository, ImagemMigracaoService imagemService) {
        this.postRepository = postRepository;
        this.imagemService = imagemService;
    }

    public MigracaoRelatorio migrarPosts(boolean aplicar) {
        MigracaoRelatorio relatorio = new MigracaoRelatorio();

        for (Post post : postRepository.findAll()) {
            Optional<String> novaUrl = processarCampo(
                    post.getMediaUrl(), aplicar, relatorio, "post " + post.getId() + " / mediaUrl");

            novaUrl.ifPresent(post::setMediaUrl);

            if (novaUrl.isPresent()) {
                postRepository.save(post);
            }
        }
        return relatorio;
    }

    private Optional<String> processarCampo(String valor, boolean aplicar,
                                            MigracaoRelatorio relatorio, String referencia) {
        if (!imagemService.precisaMigrar(valor)) {
            relatorio.registrarIgnorado();
            return Optional.empty();
        }
        try {
            if (!aplicar) {
                imagemService.validar(valor);
                relatorio.registrarMigrado();
                return Optional.empty();
            }
            String url = imagemService.migrar(valor).orElseThrow();
            relatorio.registrarMigrado();
            return Optional.of(url);
        } catch (IllegalArgumentException | InvalidImageException e) {
            relatorio.registrarFalha(referencia, e.getMessage());
            return Optional.empty();
        }
    }
}
