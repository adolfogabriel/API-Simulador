package br.gov.caixa.telemetria;

import br.gov.caixa.dao.TelemetriaDao;
import br.gov.caixa.h2.model.EndpointInfo;
import br.gov.caixa.h2.model.Telemetria;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class TelemetriaAspect {

    @Autowired
    TelemetriaDao telemetriaDao;

    @Around("execution(* br.gov.caixa.service.CalculoService.*(..))")
    public Object monitorar(ProceedingJoinPoint joinPoint) throws Throwable {
        long inicio = System.currentTimeMillis();
        Object resultado = joinPoint.proceed();
        long tempo = System.currentTimeMillis() - inicio;

        if (resultado instanceof ResponseEntity) {
            ResponseEntity<?> response = (ResponseEntity<?>) resultado;
            int statusCode = response.getStatusCode().value();


            Telemetria telemetria = new Telemetria();
            EndpointInfo endpointInfo = new EndpointInfo();
            endpointInfo.setHttpStatus(statusCode);
            endpointInfo.setTempo(tempo);
            endpointInfo.setMetodo(joinPoint.getSignature().getName());
            endpointInfo.setTelemetria(telemetria);

            telemetria.setListaEndpoints(List.of(endpointInfo));
            telemetriaDao.saveSolicitacao(telemetria);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(response.getBody());
        }
        return resultado;
    }
}