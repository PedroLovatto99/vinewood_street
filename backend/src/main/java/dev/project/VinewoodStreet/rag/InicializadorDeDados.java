package dev.project.VinewoodStreet.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Component
public class InicializadorDeDados implements CommandLineRunner {

    private final EmbeddingStore<TextSegment> store;
    private final EmbeddingModel embeddingModel;

    public InicializadorDeDados(EmbeddingStore<TextSegment> store, EmbeddingModel embeddingModel) {
        this.store = store;
        this.embeddingModel = embeddingModel;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Iniciando verificação do Banco Vetorial (ChromaDB)...");

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 50))
                .embeddingModel(embeddingModel)
                .embeddingStore(store)
                .build();

        Path caminhoDoPdf = Paths.get("src/main/resources/lore/empresas.pdf");

        try {
            System.out.println("Lendo o arquivo PDF da lore de Los Santos...");


            InputStream pdfStream = getClass().getResourceAsStream("/lore/empresas.pdf");

            if (pdfStream == null) {
                throw new RuntimeException("Arquivo empresas.pdf não foi encontrado dentro de resources/lore!");
            }

            Document documentoLore = new ApachePdfBoxDocumentParser().parse(pdfStream);

            System.out.println("Traduzindo e salvando no ChromaDB (Isso pode demorar alguns segundos)...");
            ingestor.ingest(documentoLore);

            System.out.println("✅ Arquivo empresas.pdf processado e salvo na memória da IA com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro ao ler o PDF: " + e.getMessage());
        }
    }
}