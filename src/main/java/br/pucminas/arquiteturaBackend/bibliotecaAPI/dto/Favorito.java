/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pucminas.arquiteturaBackend.bibliotecaAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Christopher
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorito {

    private String cpf;
    private String isbn;
    private String titulo;

}
