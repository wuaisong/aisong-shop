package demo.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider")
public interface ProviderFeignClient {

	@RequestMapping("/api/provider/hello")
	public String hello(@RequestParam("name") String name);

}
