package dev.project.VinewoodStreet.models;

import dev.project.VinewoodStreet.enums.TipoImpacto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "evento_noticia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoNoticiaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private EmpresaModel empresa;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String conteudoGerado;

    @Enumerated(EnumType.STRING)
    private TipoImpacto impacto;

    private Double percentualVariacao;

    private LocalDateTime dataPublicacao;

}
