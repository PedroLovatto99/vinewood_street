package dev.project.VinewoodStreet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_preco")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoricoPrecoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private EmpresaModel empresa;

    private Double preco;

    private LocalDateTime dataHora;

}
