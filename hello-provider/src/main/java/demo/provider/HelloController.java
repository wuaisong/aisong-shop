package demo.provider;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@RestController
public class HelloController {

    @RequestMapping("/provider/hello")
    public String hello(@RequestParam("name") String name) throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        return "from provider:" + name + address.getHostName();
    }

}
