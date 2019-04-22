package com.joaopaulofcc.gt4w.controller;

import com.joaopaulofcc.gt4w.model.Pessoa;
import com.joaopaulofcc.gt4w.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/api")
@CrossOrigin(origins = "*")
@Api(value="API REST Pessoas")
public class PessoaController
{
    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping("/pessoas")
    @ApiOperation(value="Retorna uma lista de pessoas")
    public Page<Pessoa> getPessoas(Pageable pageable)
    {
        return pessoaRepository.findAll(pageable);
    }

    @PostMapping("/pessoas")
    @ApiOperation(value="Insere uma nova pessoa")
    public Pessoa createPessoa(@Valid @RequestBody Pessoa pessoa)
    {
        return pessoaRepository.save(pessoa);
    }

    @PutMapping("/pessoas/{pessoaCpf}")
    @ApiOperation(value="Atualiza uma pessoa")
    public Pessoa updatePessoa(@PathVariable String pessoaCpf, @Valid @RequestBody Pessoa pessoaRequest)
    {
        return pessoaRepository.findById(pessoaCpf)
                .map(pessoa -> {
                    pessoa.setNome(pessoaRequest.getNome());
                    pessoa.setDataNasc(pessoaRequest.getDataNasc());
                    pessoa.setPeso(pessoaRequest.getPeso());
                    pessoa.setUf(pessoaRequest.getUf());
                    return pessoaRepository.save(pessoa);
                }).orElseThrow(() -> new RuntimeException("Pessoa não encontrada!"));
    }

    @DeleteMapping("/pessoas/{pessoaCpf}")
    @ApiOperation(value="Deleta uma pessoa")
    public ResponseEntity<?> deletePessoa(@PathVariable String pessoaCpf)
    {
        return pessoaRepository.findById(pessoaCpf)
                .map(pessoa -> {
                    pessoaRepository.delete(pessoa);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new RuntimeException("Pessoa não encontrada!"));
    }
}
