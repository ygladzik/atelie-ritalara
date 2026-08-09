package br.com.atelieritalara.backend.service;

import br.com.atelieritalara.backend.entity.Endereco;
import br.com.atelieritalara.backend.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public Endereco salvar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public List<Endereco> listar() {
        return enderecoRepository.findAll();
    }

    public Optional<Endereco> buscarPorId(Long id) {
        return enderecoRepository.findById(id);
    }

    public void excluir(Long id) {
        enderecoRepository.deleteById(id);
    }
}