package com.dailystudy.backend.controller;

import com.dailystudy.backend.dto.HistoricoBuscaDTO;
import com.dailystudy.backend.dto.TermoBuscaDTO;
import com.dailystudy.backend.model.Usuario;
import com.dailystudy.backend.service.HistoricoBuscaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class BuscaController {

    private final HistoricoBuscaService historicoBuscaService;

    @PostMapping("/historico")
    public ResponseEntity<Void> registrarBusca(@Valid @RequestBody TermoBuscaDTO dto, @AuthenticationPrincipal Usuario usuarioLogado) {
        historicoBuscaService.registrarBusca(usuarioLogado.getId(), dto.termo());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/historico")
    public ResponseEntity<List<HistoricoBuscaDTO>> listarHistorico(@AuthenticationPrincipal Usuario usuarioLogado) {

        List<HistoricoBuscaDTO> historico = historicoBuscaService.listarRecentes(usuarioLogado.getId())
                .stream()
                .map(HistoricoBuscaDTO::new)
                .toList();

        return ResponseEntity.ok(historico);
    }
}
