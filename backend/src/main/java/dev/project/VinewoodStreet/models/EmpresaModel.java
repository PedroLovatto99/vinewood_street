package dev.project.VinewoodStreet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name= "empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sigla;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Double precoAtual;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<HistoricoPrecoModel> historico;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<EventoNoticiaModel> noticias;



}
