package com.joaopaulofcc.gt4w.repository;

import com.joaopaulofcc.gt4w.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, String>
{
}
