package com.investment.apigateway.dashboard;

import com.investment.apigateway.services.CompanyService;
import com.investment.apigateway.services.TechnicalAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;
import server.technicalanalysis.TechnicalAnalysisServerResponse;

import java.util.Optional;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class StockDashboardHandlers {

    private CompanyService companyService;
    private TechnicalAnalysisService technicalAnalysisService;

    @Autowired
    public StockDashboardHandlers(CompanyService companyService, TechnicalAnalysisService technicalAnalysisService) {
        this.companyService = companyService;
        this.technicalAnalysisService = technicalAnalysisService;
    }

    /*
        Multiple calls to microservices to obtain required data for the Stock Dashboard endpoint
     */
    public Mono<ServerResponse> stockDashboardHandler(ServerRequest request) {
        Optional<String> ticker = Optional.of("IBM");

        // Company Service
        Mono<String> company = companyService.getCompanyResult(ticker.get());

        // User Service
        Mono<String> user = Mono.just("user");

        // Stock Price Service
        Mono<String> stock = Mono.just("stockPrice");
        String stockPrice = "200.00";

        // Technical Analysis Service
        Mono<TechnicalAnalysisServerResponse> test = technicalAnalysisService.getSimpleMovingDayAverageResult(ticker.get(), stockPrice);

        // Multiple Mono to Single Response
        // stackoverflow.com/questions/59723927/spring-webflux-extract-value-from-mono
        // https://blog.knoldus.com/reactive-java-combining-mono/
        // https://medium.com/@knoldus/spring-boot-combining-mono-s-358b83b7485a

        return Mono.zip(company, user, stock, test)
                .flatMap(objects ->
                        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .body(fromObject(buildResult(objects))));
    }

    public Result buildResult(Tuple4<String, String, String, TechnicalAnalysisServerResponse> tuple4) {
        return new Result(tuple4.getT1(), tuple4.getT2(), tuple4.getT3(), tuple4.getT4());
    }

    public class Result {

        String company;
        String user;
        String stock;
        TechnicalAnalysisServerResponse technicalAnalysis;

        public Result() {
        }

        public Result(String company, String user, String stock, TechnicalAnalysisServerResponse  technicalAnalysis) {
            this.company = company;
            this.user = user;
            this.stock = stock;
            this.technicalAnalysis = technicalAnalysis;
        }

        public String getCompany() {
            return company;
        }

        public String getUser() {
            return user;
        }

        public String getStock() {
            return stock;
        }

        public TechnicalAnalysisServerResponse  getTechnicalAnalysis() {
            return technicalAnalysis;
        }

    }

}