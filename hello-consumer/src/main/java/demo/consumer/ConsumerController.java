package demo.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
	@Autowired
	private ProviderFeignClient providerFeignClient;
    
	@RequestMapping(path="/consumer/hello")
	public String consumer(@RequestParam("name") String name){
		return "from consumer:"+name;
	}
	
	@RequestMapping("/consumer/provider")
	public String consumerProvider(@RequestParam("name") String name){
		String hello = providerFeignClient.hello(name);
		return "from consumer,"+hello;
	}
}
