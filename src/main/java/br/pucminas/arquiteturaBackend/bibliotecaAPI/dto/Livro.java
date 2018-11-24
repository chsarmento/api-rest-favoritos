package br.pucminas.arquiteturaBackend.bibliotecaAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

    private String isbn;
    private String titulo;
    private Double valor;
    private String descricao;

}
