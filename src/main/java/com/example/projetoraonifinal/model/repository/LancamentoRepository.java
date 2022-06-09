package com.example.projetoraonifinal.model.repository;

import com.example.projetoraonifinal.model.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}
