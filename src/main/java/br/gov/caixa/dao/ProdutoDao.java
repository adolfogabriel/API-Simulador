package br.gov.caixa.dao;


import br.gov.caixa.entity.ProdutoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ProdutoDao {


    @PersistenceContext(unitName = "sqlserverPU")
    private EntityManager entityManager;

    @Cacheable("obterProduto")
    public ProdutoEntity obterProduto(Double valor, Integer prazo) {
        String sql = "";
        try {
            sql = "SELECT * " +
                    "FROM PRODUTO " +
                    "WHERE NU_MINIMO_MESES <= :prazo " +
                    "AND (NU_MAXIMO_MESES >= :prazo OR NU_MAXIMO_MESES IS NULL)\n" +
                    "AND VR_MINIMO <= :valor " +
                    "AND (VR_MAXIMO >= :valor OR VR_MAXIMO IS NULL)";

        } catch (Exception e) {
            log.error("Erro ao consultar produto:" + e);
        }
        return (ProdutoEntity) entityManager.createNativeQuery(sql, ProdutoEntity.class)
                .setParameter("valor", valor)
                .setParameter("prazo", prazo)
                .getResultList().stream().findFirst().orElse(null);
    }
}
