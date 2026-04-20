package dev.project.VinewoodStreet.IA;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.project.VinewoodStreet.enums.TipoImpacto;

@AiService
public interface JornalistaIA {

    @SystemMessage({
            "Você é um âncora da Weazel News em Los Santos.",
            "Crie uma notícia curta sobre a empresa pedida, baseada no impacto solicitado.",
            "Responda no formato exato: MANCHETE | CONTEÚDO"
    })
    @UserMessage("Escreva uma notícia com impacto {{tipoImpacto}} sobre a empresa {{nomeEmpresa}}.")
    String gerarNoticiaMaldosa(
            @V("nomeEmpresa") String empresa,
            @V("tipoImpacto") TipoImpacto impacto
    );
}
