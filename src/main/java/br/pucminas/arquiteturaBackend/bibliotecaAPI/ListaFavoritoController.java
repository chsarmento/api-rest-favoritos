package br.pucminas.arquiteturaBackend.bibliotecaAPI;

import br.pucminas.arquiteturaBackend.bibliotecaAPI.dto.Favorito;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucminas.arquiteturaBackend.bibliotecaAPI.dto.Usuario;
import br.pucminas.arquiteturaBackend.bibliotecaAPI.dto.Livro;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/v1/public/")
public class ListaFavoritoController {

    private List<Usuario> usuarios = new ArrayList<>();
    private List<Livro> livros = new ArrayList<>();

    // Usuarios
    @ApiOperation(value = "Busca lista de usuários", response = Usuario.class)
    @GetMapping("usuarios")
    public List<Usuario> todosUsuarios() {
        return usuarios;
    }

    @ApiOperation(value = "Adiciona um usuário na lista", response = Usuario.class)
    @PostMapping("usuarios")
    Usuario adicionarUsuario(@RequestBody Usuario usuario) {
        usuarios.add(usuario);
        return usuario;
    }

    @ApiOperation(value = "Atualiza o nome do usuário", response = Usuario.class)
    @PutMapping("usuarios/{id}")
    Usuario replaceUsuario(@RequestBody Usuario usuario, @PathVariable String cpf) {

        usuarios.stream().filter(p -> p.getCpf().equals(cpf)).forEach(p -> {
            p.setNome(usuario.getNome());
        });

        return usuario;
    }

    @ApiOperation(value = "Remove um usuário da lista", response = Void.class)
    @DeleteMapping("usuarios/{id}")
    void deleteUsuario(@PathVariable String cpf) {
        usuarios.removeIf(p -> p.getCpf().equals(cpf));
    }

    // Livros
    @ApiOperation(value = "Adiciona um livro na lista de favorito", response = Livro.class)
    @PostMapping("favoritos")
    Livro adicionarFavorito(@RequestBody Favorito favorito) {

        Livro livro = null;

        // verifica se o usuario existe
        Usuario usuario = obterUusarioBy(favorito.getCpf());

        if (null != usuario) {

            // verifica se o livro existe
            livro = obterLivroBy(favorito.getIsbn(), favorito.getTitulo());

            if (livro == null) {
                // acionar a API de consulta de outras informações
                livro = obterInformacoesDoLivro(favorito.getIsbn());

                livros.add(livro);

                // adiciona livro na lista de favoritos do usuario
                usuario.getListaFavorito().add(livro);
            } else {
                // livro já existe na lista?
                if (!livroJaExisteNaListaFavorito(livro, usuario.getListaFavorito())) {
                    usuario.getListaFavorito().add(livro);
                }
            }
        }

        return livro;
    }

    @ApiOperation(value = "Remove um livro da lista de favorito de um usuário", response = Void.class)
    @DeleteMapping("favoritos/{cpf}/{isbn}")
    void removeFavorito(@PathVariable String cpf, @PathVariable String isbn) {

        Usuario usuario = obterUusarioBy(cpf);

        if (null != usuario) {
            usuario.getListaFavorito().removeIf(p -> p.getIsbn().equals(isbn));
        }
    }

    @ApiOperation(value = "Busca lista de favoritos de um usuário", response = List.class)
    @GetMapping("favoritos/{cpf}")
    public List<Livro> listaLivroPor(@PathVariable String cpf) {

        Usuario usuario = obterUusarioBy(cpf);

        return usuario != null ? usuario.getListaFavorito() : null;
    }

    private boolean livroJaExisteNaListaFavorito(Livro livro, List<Livro> listaLivros) {
        return listaLivros.stream()
                .filter(l -> l.getIsbn().equals(livro.getIsbn()))
                .count() == 0;
    }

    private Livro obterLivroBy(String isbn, String titulo) {
        return livros.stream()
                .filter(p -> p.getIsbn().equals(isbn) || p.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);
    }

    private Usuario obterUusarioBy(String cpf) {
        return usuarios.stream()
                .filter(p -> p.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }

    private Livro obterInformacoesDoLivro(String isbn) {

        Livro livro = new Livro();
        livro.setIsbn(isbn);

        RestTemplate restTemplate = new RestTemplate();

        String urlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        ResponseEntity<String> response = restTemplate.exchange(urlString, HttpMethod.GET, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {

            ObjectMapper mapper = new ObjectMapper();
            try {
                LinkedHashMap dados = mapper.readValue(response.getBody(), LinkedHashMap.class);

                if (null != dados) {

                    livro.setTitulo((String) ((LinkedHashMap) ((LinkedHashMap) ((ArrayList) dados.get("items")).get(0)).get("volumeInfo")).get("title"));

                    livro.setDescricao((String) ((LinkedHashMap) ((LinkedHashMap) ((ArrayList) dados.get("items")).get(0)).get("volumeInfo")).get("description"));
                }

            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        return livro;

    }

}
