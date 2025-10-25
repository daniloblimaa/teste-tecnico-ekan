package dev.danilobarreto.atendimento_api.repository;

import dev.danilobarreto.atendimento_api.model.Cid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CidRepository extends JpaRepository<Cid, String> {}
