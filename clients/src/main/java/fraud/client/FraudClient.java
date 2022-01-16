package fraud.client;

import fraud.model.FraudCheckResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "fraud",
        url = "${clients.fraud.url}"
)
public interface FraudClient {
    @GetMapping(path = "/{customerId}")
    FraudCheckResponse isFraudster(
            @PathVariable("customerId") Integer customerId
    );
}
