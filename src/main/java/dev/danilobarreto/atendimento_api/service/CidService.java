package dev.danilobarreto.atendimento_api.service;


import dev.danilobarreto.atendimento_api.model.Cid;
import dev.danilobarreto.atendimento_api.model.dto.CidDTO;
import dev.danilobarreto.atendimento_api.repository.CidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CidService {

    private final CidRepository cidRepository;

    public CidDTO buscarPorId(String id) {
        Cid cid = cidRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CID não encontrado"));
        return toDto(cid);
    }

    private CidDTO toDto(Cid cid) {
        CidDTO dto = new CidDTO();
        dto.setId(cid.getId());
        dto.setNome(cid.getNome());
        return dto;
    }
}
