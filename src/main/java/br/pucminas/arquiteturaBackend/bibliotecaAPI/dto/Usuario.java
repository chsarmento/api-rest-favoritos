package br.pucminas.arquiteturaBackend.bibliotecaAPI.dto;

import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private String cpf;
    private String nome;

    private List<Livro> listaFavorito = new LinkedList<>();
    
    private void addLivroNaLista(Livro livro) {
        listaFavorito.add(livro);
    }

}
