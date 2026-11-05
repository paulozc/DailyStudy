package com.dailystudy.backend.migration;

import com.dailystudy.backend.service.OciStorageService;
import com.dailystudy.backend.util.ImageValidator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@Service
public class ImagemMigracaoService {

    private final OciStorageService storage;

    public ImagemMigracaoService(OciStorageService storage) {
        this.storage = storage;
    }

    public Optional<String> migrar(String valorAtual) {
        if (valorAtual == null || valorAtual.isBlank() || valorAtual.startsWith("https://")) {
            return Optional.empty();
        }
        byte[] bytes = DataUriDecoder.decodificar(valorAtual);
        String contentType = ImageValidator.validar(bytes);
        String url = storage.upload(new ByteArrayInputStream(bytes), bytes.length, contentType);
        return Optional.of(url);
    }
}
