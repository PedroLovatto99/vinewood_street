package dev.project.VinewoodStreet.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "acoes_compradas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UserModel usuario;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaModel empresa;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private Double precoMedioCompra;
}